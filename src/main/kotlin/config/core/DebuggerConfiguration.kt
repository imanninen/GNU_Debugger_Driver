package org.gnudebugger.config.core

import java.io.BufferedReader

interface DebuggerConfiguration {
    val config: MutableList<DebugCommand>
    var targetIsSet: Boolean
    var breakPointsHandlers: MutableMap<DebugCommand, (BufferedReader) -> HandlerReturn>
    fun addConfiguration(configuration: DebugCommand)

    fun setBreakPointHandler(entry: Map.Entry<DebugCommand, (BufferedReader) -> HandlerReturn>)
    enum class HandlerReturn{
        RESUME,
    }
}