package org.gnudebugger.app

import org.gnudebugger.config.CommandFactory
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.core.Debugger
import org.gnudebugger.core.DebuggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.OutputStream

class DebuggerDriver(
    pathToDebuggerExecutable: String
) {
    private val debugger: Debugger = DebuggerFactory.create(pathToDebuggerExecutable)
    fun loadDebugExecutable(pathToFile: String) {
        val debugTarget = File(pathToFile)
        require(debugTarget.exists() && debugTarget.isFile) { "Invalid file was given!" }
        debugger.configuration.addConfiguration(
            CommandFactory.createLoadTargetCommand(debugger, pathToFile)
        )
    }

    fun setBreakPoint(fileName: String, line: Int) {
        debugger.configuration.addConfiguration(
            CommandFactory.createBreakPointCommand(debugger, line, fileName)
        )
    }

    fun resume(input: BufferedReader, output: OutputStream): DebuggerConfiguration.HandlerReturn {
        return debugger.resume(CommandFactory.createContinueCommand(debugger), input, output)
    }

    fun getBackTrace(input:BufferedReader, output: OutputStream): String {
        return debugger.executeInHandlerDebugCommand(CommandFactory.createBackTraceCommand(debugger), input, output)
    }

    fun setBreakPointHandler(block: (BufferedReader, OutputStream) -> DebuggerConfiguration.HandlerReturn) {
        debugger.configuration.setBreakPointHandler(block)
    }

    fun run(programArgs: List<String> = emptyList()) {
        debugger.configuration.addConfiguration(
            CommandFactory.createRunCommand(debugger, programArgs)
        )
        debugger.run()
    }
}