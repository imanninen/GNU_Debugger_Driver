package org.gnudebugger.debugger.lldb

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.core.commands.ContinueCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.LldbDebuggerConfiguration
import org.gnudebugger.config.lldb.responce.ErrorCommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import org.gnudebugger.debugger.Debugger
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream

/**
 * This is an implementation of [Debugger] interface and a lldb debugger.
 * @property executable file, that is provided as executable of lldb debugger
 * @property configuration configurations([DebuggerConfiguration]) before execution.
 */
internal class LldbDebugger(
    private val executable: File,
    override val configuration: LldbDebuggerConfiguration
) : Debugger {

    override fun resume(
        command: ContinueCommand,
        input: BufferedReader,
        output: OutputStream
    ): DebuggerConfiguration.HandlerReturn {
        executeInHandlerDebugCommand(command, input, output)
        return DebuggerConfiguration.HandlerReturn.RESUME
    }

    override fun executeInHandlerDebugCommand(
        command: DebugCommand,
        input: BufferedReader,
        output: OutputStream
    ): String {
        output.write(command.clCommand.toByteArray())
        output.flush()
        return when (val response = (command as LldbDebugCommand).handle(input)) {
            is SuccessCommandResponse -> response.output
            is ErrorCommandResponse -> throw IllegalStateException(response.errorMessage)
            else -> throw IllegalStateException("Unknown response format: ${response.javaClass}")
        }
    }

    override fun run() {
        require(configuration.targetIsSet) {"Target wasn't specified!"}
        require(configuration.breakpointsNumber == configuration.breakPointsHandlers.size)
            {"number of breakpoints don't match to breakpoints handlers!"}
        val pb = ProcessBuilder(executable.path)
        pb.redirectErrorStream(true)
        try {
            val lldbProcess = pb.start()
            val input = BufferedReader(InputStreamReader(lldbProcess.inputStream))
            for (command in configuration.config) {
                lldbProcess.outputStream.write(command.clCommand.toByteArray())
                lldbProcess.outputStream.flush()
                val availableErrorStreamBytes = lldbProcess.errorStream.available()
                if (availableErrorStreamBytes != 0) {
                    throw IllegalStateException(lldbProcess.errorStream.bufferedReader().readLine())
                }
                val response = (command as LldbDebugCommand).handle(input)
                when (response) {
                    is ErrorCommandResponse -> throw IllegalStateException(response.errorMessage)
                }
            }
            configuration.breakPointsHandlers.forEach { block ->
                block(input, lldbProcess.outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            println(e.message)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
}