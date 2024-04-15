package org.gnudebugger.config.core

import java.io.BufferedReader
import java.io.OutputStream

interface DebuggerConfiguration {
    val config: MutableList<DebugCommand>
    var targetIsSet: Boolean
    var breakPointsHandlers: MutableMap<DebugCommand, (BufferedReader, OutputStream) -> HandlerReturn>
    fun addConfiguration(configuration: DebugCommand)

    fun setBreakPointHandler(entry: Map.Entry<DebugCommand, (BufferedReader, OutputStream) -> HandlerReturn>)
    enum class HandlerReturn{
        RESUME,
    }
}