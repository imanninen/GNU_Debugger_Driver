package org.gnudebugger.config.lldb

import org.gnudebugger.config.core.DebugCommand
import java.io.BufferedReader

interface LldbDebugCommand : DebugCommand {
    fun handle(input: BufferedReader): String
}