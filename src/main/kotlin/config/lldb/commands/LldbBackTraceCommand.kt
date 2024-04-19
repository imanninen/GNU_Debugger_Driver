package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.BackTraceCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import java.io.BufferedReader

/**
 * Lldb stack trace debug command implementation.
 */
internal class LldbBackTraceCommand : BackTraceCommand, LldbDebugCommand {
    override fun handle(input: BufferedReader): CommandResponse {
        var outputOfCommand = ""
        input.skip(clCommand.length.toLong() + "(lldb) ".length.toLong())
        var line: String = input.readLine()
        line = input.readLine().trimIndent()
        while (line != "") {
            if (line.contains("(lldb)")) {
                line = input.readLine()
                continue
            }
            if (line.contains(Regex("start \\+ [0-9]*"))) {
                outputOfCommand += "$line\n"
                break
            }
            if (line.startsWith("* ")){
                line = line.replace("* ", "")
            }
            outputOfCommand += "$line\n"
            line = input.readLine().trimIndent()
        }
        return SuccessCommandResponse(outputOfCommand.trimIndent())
    }

    override val clCommand: String
        get() = "bt\n"
}