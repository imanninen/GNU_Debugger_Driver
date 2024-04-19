package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand

/**
 * Abstract print variable command implements [DebugCommand] for abstract debugger.
 * @property varName name of the variable to print.
 */
interface PrintVarCommand : DebugCommand {
    val varName: String
}