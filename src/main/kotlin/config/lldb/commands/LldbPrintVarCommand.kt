package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.PrintVarCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import java.io.BufferedReader

internal class LldbPrintVarCommand(override val varName: String) : PrintVarCommand, LldbDebugCommand {
    override val clCommand: String
        get() = "print $varName\n"
    override fun handle(input: BufferedReader): CommandResponse {
        var outputOfCommand = ""
        input.skip(clCommand.length.toLong() + "(lldb) ".length.toLong())
        var line: String = input.readLine()
        outputOfCommand += line
        return SuccessCommandResponse(outputOfCommand)
    }
}