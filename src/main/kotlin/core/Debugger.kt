package org.gnudebugger.core

import org.gnudebugger.config.core.DebuggerConfiguration

interface Debugger {
    val configuration: DebuggerConfiguration
    fun run()

    fun resume(): DebuggerConfiguration.HandlerReturn
}