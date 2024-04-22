package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.BreakPointCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import java.io.BufferedReader

internal class LldbBreakPointCommand(
    override val file: String,
    override val line: Int) :
    BreakPointCommand, LldbDebugCommand {
    override val clCommand: String
        get() = "breakpoint set --line $line --file $file\n"
    override fun handle(input: BufferedReader): CommandResponse {
        var outputOfCommand = ""
        input.skip(clCommand.length.toLong() + "(lldb) ".length.toLong())
        val line: String = input.readLine()
        if (line.contains(Regex("Breakpoint [0-9]*: no locations \\(pending\\)."))){
            var error: String = "${input.readLine()}\n".replace(Regex("WARNING"), "ERROR")
            error += "(incorrect file name or line)"
            throw IllegalStateException(error)
        }
        outputOfCommand += line
        return SuccessCommandResponse(outputOfCommand)
    }
}