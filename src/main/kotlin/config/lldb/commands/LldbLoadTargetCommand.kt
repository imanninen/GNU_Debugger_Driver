package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.LoadTargetCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import java.io.BufferedReader
import java.io.File

class LldbLoadTargetCommand(
    override val pathToTarget: File
) : LoadTargetCommand, LldbDebugCommand {
    override val ciCommand: String
        get() = "target create ${pathToTarget.path}\n"

    override fun handle(input: BufferedReader): String {
        var outputOfCommand = ""
        input.skip(ciCommand.length.toLong() + "(lldb) ".length.toLong())
        var line: String = input.readLine()
        outputOfCommand += line
        return outputOfCommand
    }

}