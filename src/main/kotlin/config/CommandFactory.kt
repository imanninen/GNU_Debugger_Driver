package org.gnudebugger.config

import org.gnudebugger.config.core.commands.*
import org.gnudebugger.config.lldb.commands.*
import org.gnudebugger.core.Debugger
import org.gnudebugger.core.lldb.LldbDebugger
import java.io.File

internal class CommandFactory {
    companion object {
        private fun createLldbLoadTargetCommand(path: String): LldbLoadTargetCommand {
            val pathToTarget = File(path)
            require(pathToTarget.exists() && pathToTarget.isFile)
            { "File doesn't exist! Path: $path" }// TODO("should check lang.)
            return LldbLoadTargetCommand(pathToTarget)
        }

        private fun createLldbRunCommand(programArgs: List<String>): LldbRunCommand {
            val programArgsToStr = programArgs.joinToString(" ")
            return LldbRunCommand(programArgsToStr)
        }

        private fun createLldbBreakPointCommand(line: Int, fileName: String): LldbBreakPointCommand {
            return LldbBreakPointCommand(fileName, line)
        }

        private fun createLldbContinueCommand(): LldbContinueCommand {
            return LldbContinueCommand()
        }

        private fun createLldbBackTraceCommand(): LldbBackTraceCommand {
            return LldbBackTraceCommand()
        }

        fun createLoadTargetCommand(debugger: Debugger, path: String): LoadTargetCommand {
            return when (debugger) {
                is LldbDebugger -> createLldbLoadTargetCommand(path)

                else -> {
                    throw IllegalStateException("Unknown debugger was provided!")
                }
            }
        }

        fun createRunCommand(debugger: Debugger, programArgs: List<String>): RunCommand {
            return when (debugger) {
                is LldbDebugger -> createLldbRunCommand(programArgs)
                else -> {
                    throw IllegalStateException("Unknown debugger was provided!")
                }
            }
        }

        fun createBreakPointCommand(debugger: Debugger, line: Int, fileName: String): BreakPointCommand {
            return when (debugger) {
                is LldbDebugger -> createLldbBreakPointCommand(line, fileName)
                else -> {
                    throw IllegalStateException("Unknown debugger was provided!")
                }

            }
        }
        fun createContinueCommand(debugger: Debugger): ContinueCommand {
            return when (debugger) {
                is LldbDebugger -> createLldbContinueCommand()
                else -> {
                    throw IllegalStateException("Unknown debugger was provided!")
                }

            }
        }
        fun createBackTraceCommand(debugger: Debugger): BackTraceCommand {
            return when (debugger) {
                is LldbDebugger -> createLldbBackTraceCommand()
                else -> {
                    throw IllegalStateException("Unknown debugger was provided!")
                }

            }
        }

    }
}