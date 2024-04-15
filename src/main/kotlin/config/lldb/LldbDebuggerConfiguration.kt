package org.gnudebugger.config.lldb

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.lldb.commands.LldbBreakPointCommand
import org.gnudebugger.config.lldb.commands.LldbLoadTargetCommand
import java.io.BufferedReader
import java.io.OutputStream

class LldbDebuggerConfiguration : DebuggerConfiguration {
    override val config: MutableList<DebugCommand> = mutableListOf()
    override var targetIsSet = false
    override var breakPointsHandlers: MutableList<(BufferedReader, OutputStream) -> DebuggerConfiguration.HandlerReturn> =
        mutableListOf()
    var breakpointsNumber: Int = 0

    override fun addConfiguration(configuration: DebugCommand) {
        if (configuration is LldbLoadTargetCommand)
            targetIsSet = true
        else if (configuration is LldbBreakPointCommand)
            breakpointsNumber++
        config.add(configuration)
    }

    override fun setBreakPointHandler(block: (BufferedReader, OutputStream) -> DebuggerConfiguration.HandlerReturn) {
        breakPointsHandlers.add(block)
    }

}