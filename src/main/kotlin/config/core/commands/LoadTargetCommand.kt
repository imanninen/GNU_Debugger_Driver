package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand
import java.io.File

interface LoadTargetCommand: DebugCommand {
    val pathToTarget: File
}