package org.gnudebugger.app

import org.gnudebugger.config.CommandFactory
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.core.Debugger
import org.gnudebugger.core.DebuggerFactory
import java.io.BufferedReader
import java.io.File
import java.util.AbstractMap.SimpleEntry

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

    fun resume(): DebuggerConfiguration.HandlerReturn {
        return debugger.resume()
    }

    fun setBreakPointHandler(block: (BufferedReader) -> DebuggerConfiguration.HandlerReturn) {
        debugger.configuration.setBreakPointHandler(SimpleEntry(
            CommandFactory.createContinueCommand(debugger),
            block
        ))
    }

    fun run(programArgs: List<String> = emptyList()) {
        debugger.configuration.addConfiguration(
            CommandFactory.createRunCommand(debugger, programArgs)
        )
        debugger.run()
    }
}