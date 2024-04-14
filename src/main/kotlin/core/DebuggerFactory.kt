package org.gnudebugger.core

import org.gnudebugger.config.lldb.LldbDebuggerConfiguration
import org.gnudebugger.core.lldb.LldbDebugger
import java.io.File

internal class DebuggerFactory {
    companion object {
        @JvmStatic
        fun create(pathToDebuggerExecutable: String): Debugger {
            val executableFile = File(pathToDebuggerExecutable)
            require(executableFile.exists() && executableFile.isFile && executableFile.canExecute())
            { "Invalid file was given!" }
            // TODO change creation (check for debugger support!)
            return LldbDebugger(executableFile, LldbDebuggerConfiguration())
        }
    }
}