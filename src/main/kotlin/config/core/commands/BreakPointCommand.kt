package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand

interface BreakPointCommand: DebugCommand {
    val file: String
    val line: Int
}