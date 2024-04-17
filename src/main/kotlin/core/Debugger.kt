package org.gnudebugger.core

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.core.commands.ContinueCommand
import java.io.BufferedReader
import java.io.OutputStream

interface Debugger {
    val configuration: DebuggerConfiguration
    fun run()
    fun resume(command: ContinueCommand, input: BufferedReader, output: OutputStream): DebuggerConfiguration.HandlerReturn
    fun executeInHandlerDebugCommand(command: DebugCommand, input: BufferedReader, output: OutputStream): String
}