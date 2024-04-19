package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand

/**
 * Abstract breakpoint command implements [DebugCommand] for abstract debugger.
 * @property file number of the line, where you want to place breakpoint.
 * @property line name of file in the target executable where you want to place breakpoint.
 */
interface BreakPointCommand: DebugCommand {
    val file: String
    val line: Int
}