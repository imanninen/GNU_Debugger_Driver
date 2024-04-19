package org.gnudebugger.debugger

import org.gnudebugger.config.core.DebugCommand
import org.gnudebugger.config.core.DebuggerConfiguration
import org.gnudebugger.config.core.commands.ContinueCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import java.io.BufferedReader
import java.io.OutputStream

/**
 * This interface describes abstract GNU debugger.
 * It uses [ProcessBuilder] and [Process] to
 * run debugger commands in the command line.
 * @property configuration which used to set up debugger workflow.
 */
interface Debugger {
    val configuration: DebuggerConfiguration

    /**
     * Method, which executes configured debugger.
     * @return [CommandResponse] to know status of execution.
     */
    fun run(): CommandResponse

    /**
     * Method, which used to handle [ContinueCommand]
     * @param command is [ContinueCommand] instance which should be handled.
     * @param input is input stream. It is necessary for executing command.
     * @param output is output stream. It is necessary for handling command.
     * @return specific value to mark that breakpoint handler is valid.
     */
    fun resume(
        command: ContinueCommand,
        input: BufferedReader,
        output: OutputStream
    ): DebuggerConfiguration.HandlerReturn

    /**
     * Method, which is used to execute and handle commands in handler scope.
     * @param command instance of [DebugCommand] to execute.
     * @param input is input stream. It is necessary for executing command.
     * @param output is output stream. It is necessary for handling command.
     * @return parsed command output
     * @throws IllegalArgumentException
     */
    fun executeInHandlerDebugCommand(command: DebugCommand, input: BufferedReader, output: OutputStream): String
}