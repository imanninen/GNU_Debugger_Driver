package org.gnudebugger.config.lldb

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import java.io.BufferedReader
/**
 * This interface is primary used to mark [DebugCommand] and show that command is applied to lldb debugger.
 */
interface LldbDebugCommand : DebugCommand {
    /**
     * This method handles command execution.
     * @param input is an input of [Process], which is used to parse command output.
     * @throws IllegalArgumentException if output is unexpected.
     * @return [CommandResponse]
     */
    fun handle(input: BufferedReader): CommandResponse
}