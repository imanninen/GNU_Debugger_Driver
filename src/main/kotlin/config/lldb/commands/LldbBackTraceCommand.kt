package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.BackTraceCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import java.io.BufferedReader

class LldbBackTraceCommand : BackTraceCommand, LldbDebugCommand {
    override fun handle(input: BufferedReader): String {
        var outputOfCommand = ""
        input.skip(ciCommand.length.toLong() + "(lldb) ".length.toLong())
        var line: String = input.readLine()
        line = input.readLine()
        while (line != "") {
            if (line.contains("(lldb)")) {
                line = input.readLine()
                continue
            }
            if (line.contains(Regex("start \\+ [0-9]*"))) {
                outputOfCommand += "$line\n"
                break
            }
            outputOfCommand += "$line\n"
            line = input.readLine()
        }
        return outputOfCommand
    }

    override val ciCommand: String
        get() = "bt\n"
}