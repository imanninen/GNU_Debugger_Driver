package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand

interface PrintVarCommand : DebugCommand {
    val varName: String
}