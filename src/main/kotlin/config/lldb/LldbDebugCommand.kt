package org.gnudebugger.config.lldb

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import java.io.BufferedReader

interface LldbDebugCommand : DebugCommand {
    fun handle(input: BufferedReader): CommandResponse
}