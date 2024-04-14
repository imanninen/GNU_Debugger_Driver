package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.BreakPointCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import java.io.BufferedReader

class LldbBreakPointCommand(
    override val file: String,
    override val line: Int) :
    BreakPointCommand, LldbDebugCommand {
    override val ciCommand: String
        get() = "breakpoint set --line $line --file $file\n"
    override fun handle(input: BufferedReader): String {
        var outputOfCommand = ""
        input.skip(ciCommand.length.toLong() + "(lldb) ".length.toLong())
        val line: String = input.readLine()
        outputOfCommand += line
        return outputOfCommand
    }
}