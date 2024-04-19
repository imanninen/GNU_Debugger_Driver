package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.LoadTargetCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import java.io.BufferedReader
import java.io.File

internal class LldbLoadTargetCommand(
    override val pathToTarget: File
) : LoadTargetCommand, LldbDebugCommand {
    override val clCommand: String
        get() = "target create ${pathToTarget.path}\n"

    override fun handle(input: BufferedReader): CommandResponse {
        var outputOfCommand = ""
        input.skip(clCommand.length.toLong() + "(lldb) ".length.toLong())
        val line: String = input.readLine()
        if (! line.contains(Regex("Current executable set to"))) {
            throw IllegalStateException(line)
        }
        outputOfCommand += line
        return SuccessCommandResponse(outputOfCommand)
    }

}