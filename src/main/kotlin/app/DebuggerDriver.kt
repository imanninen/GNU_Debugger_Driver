package org.gnudebugger.app

import org.gnudebugger.config.CommandFactory
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.debugger.Debugger
import org.gnudebugger.debugger.DebuggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.OutputStream

/**
 * Class, which is a wrapper around GNU debugger. It must be compatible with your OS and have to be downloaded.
 * @param pathToDebuggerExecutable [String] path to executable of the GNU debugger.
 */
class DebuggerDriver(
    pathToDebuggerExecutable: String
) {
    private val debugger: Debugger = DebuggerFactory.create(pathToDebuggerExecutable)

    /**
     * This method loaded target executable to debugger to work with it.
     * Under the hood it creates a [org.gnudebugger.config.core.DebugCommand] and add this command to
     * [org.gnudebugger.config.core.DebuggerConfiguration]
     * @param pathToFile [String] path to binary file.
     * @throws IllegalArgumentException if the file is incorrect or doesn't exist.
     */
    fun loadDebugExecutable(pathToFile: String) {
        val debugTarget = File(pathToFile)
        require(debugTarget.exists() && debugTarget.isFile && debugTarget.canExecute()) { "Invalid file was given!" }
        debugger.configuration.addConfiguration(
            CommandFactory.createLoadTargetCommand(debugger, pathToFile)
        )
    }

    /**
     * Set the breakpoint into a specific file on the specific line.
     * @param fileName [String] name of file in the provided executable.
     * @param line number of the line, where should be placed a breakpoint
     */
    fun setBreakPoint(fileName: String, line: Int) {
        debugger.configuration.addConfiguration(
            CommandFactory.createBreakPointCommand(debugger, line, fileName)
        )
    }

    /**
     * This method must be used in each breakpoint handler, to continue execution.
     * @param input input of command. You shouldn't touch it!
     * @param output output of the command. You shouldn't touch it!
     * @return [DebuggerConfiguration.HandlerReturn]]specific value to indicate, that handler is valid.
     */
    fun resume(input: BufferedReader, output: OutputStream): DebuggerConfiguration.HandlerReturn {
        return debugger.resume(CommandFactory.createContinueCommand(debugger), input, output)
    }

    /**
     * Method, that could be used only in breakpoint handlers.
     * It gets a back trace.
     * @param input input of command. You shouldn't touch it!
     * @param output output of the command. You shouldn't touch it!
     * @return [String] representation of stack trace.
     */
    fun getBackTrace(input: BufferedReader, output: OutputStream): String {
        return debugger.executeInHandlerDebugCommand(CommandFactory.createBackTraceCommand(debugger), input, output)
    }

    /**
     * Method, that could be used only in breakpoint handlers.
     * It gets a value of a variable by its name.
     * @param input input of command. You shouldn't touch it!
     * @param output output of the command. You shouldn't touch it!
     * @return [String] with type and value of the variable or an error message.
     */
    fun getVarValueByName(varName: String, input: BufferedReader, output: OutputStream): String {
        return debugger.executeInHandlerDebugCommand(
            CommandFactory.createPrintVarCommand(debugger, varName),
            input,
            output
        )
    }

    /**
     * Method sets a breakpoint handler ot the configuration of the debugger.
     * @param block lambda function that takes two arguments'
     * input with type [BufferedReader] and output with type [OutputStream].
     * You shouldn't touch them, otherwise correctness of working is not guaranteed.
     * This [block] should end with colling [DebuggerDriver.resume] method.
     * @sample [Samplee.importantFunction]
     */
    fun setBreakPointHandler(block: (BufferedReader, OutputStream) -> DebuggerConfiguration.HandlerReturn) {
        debugger.configuration.setBreakPointHandler(block)
    }

    /**
     * Method that start executing all configurations and breakpoint handlers.
     * @param programArgs arguments to pass them to target executable as program arguments
     * @return [CommandResponse] to know status of execution.
     * @throws IllegalArgumentException if number of breakpoints and breakpoint handlers are not equal
     */
    fun run(programArgs: List<String> = emptyList()): CommandResponse {
        debugger.configuration.addConfiguration(
            CommandFactory.createRunCommand(debugger, programArgs)
        )
        return debugger.run()
    }
}