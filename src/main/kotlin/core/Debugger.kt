package org.gnudebugger.core

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.core.commands.ContinueCommand
import java.io.BufferedReader

interface Debugger {
    val configuration: DebuggerConfiguration
    fun run()

    fun resume(): DebuggerConfiguration.HandlerReturn
}