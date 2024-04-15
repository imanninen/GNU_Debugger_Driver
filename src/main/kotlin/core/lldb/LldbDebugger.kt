package org.gnudebugger.core.lldb

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.LldbDebuggerConfiguration
import org.gnudebugger.core.Debugger
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream

internal class LldbDebugger(
    private val executable: File,
    override val configuration: LldbDebuggerConfiguration
) : Debugger {

    override fun resume(): DebuggerConfiguration.HandlerReturn {
        return DebuggerConfiguration.HandlerReturn.RESUME
    }

    override fun executeInHandlerDebugCommand(command: DebugCommand, input: BufferedReader, output: OutputStream): String {
        output.write(command.ciCommand.toByteArray())
        output.flush()
        return (command as LldbDebugCommand).handle(input)
    }

    override fun run() {
        require(configuration.targetIsSet)
        require(configuration.breakpointsNumber == configuration.breakPointsHandlers.size)
        val pb = ProcessBuilder(executable.path)
        pb.redirectErrorStream(true)
        try {
            val lldbProcess = pb.start()
            val input = BufferedReader(InputStreamReader(lldbProcess.inputStream))
            for (command in configuration.config) {
                lldbProcess.outputStream.write(command.ciCommand.toByteArray())
                lldbProcess.outputStream.flush()
                (command as LldbDebugCommand).handle(input)
            }
            configuration.breakPointsHandlers.forEach {(command, block) ->
                block(input, lldbProcess.outputStream)
                lldbProcess.outputStream.write(command.ciCommand.toByteArray())
                lldbProcess.outputStream.flush()
                (command as LldbDebugCommand).handle(input)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}