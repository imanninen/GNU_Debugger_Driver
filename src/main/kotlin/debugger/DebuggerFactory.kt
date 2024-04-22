package org.gnudebugger.debugger

import org.gnudebugger.config.lldb.LldbDebuggerConfiguration
import org.gnudebugger.debugger.lldb.LldbDebugger
import java.io.File

/**
 * Class for creating instances of debuggers.
 */
internal class DebuggerFactory {
    companion object {
        /**
         * This method creates [Debugger] instance, based on a provided path.
         * @param pathToDebuggerExecutable [String] path to debugger binary file.
         * @return instance of [Debugger]
         * @throws IllegalArgumentException if file doesn't exist or it is incorrect.
         */
        @JvmStatic
        fun create(pathToDebuggerExecutable: String): Debugger {
            val executableFile = File(pathToDebuggerExecutable)
            require(executableFile.exists() && executableFile.isFile && executableFile.canExecute())
            { "Invalid file was given!" }
            return when {
                pathToDebuggerExecutable.endsWith("lldb") ->
                    LldbDebugger(executableFile, LldbDebuggerConfiguration())
                else -> {throw IllegalStateException("Unknown debugger: $pathToDebuggerExecutable")}
            }
        }
    }
}