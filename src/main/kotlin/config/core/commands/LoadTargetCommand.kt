package org.gnudebugger.config.core.commands

import org.gnudebugger.config.core.DebugCommand
import java.io.File

/**
 * Abstract load target command implements [DebugCommand] for abstract debugger.
 * @property pathToTarget this is a path to target executable.
 */
interface LoadTargetCommand : DebugCommand {
    val pathToTarget: File
}