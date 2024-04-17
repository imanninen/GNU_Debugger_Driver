package org.gnudebugger.config.lldb.commands

import org.gnudebugger.config.core.commands.ContinueCommand
import org.gnudebugger.config.lldb.LldbDebugCommand
import org.gnudebugger.config.lldb.responce.CommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import java.io.BufferedReader

class LldbContinueCommand : ContinueCommand, LldbDebugCommand {
    override val clCommand: String = "c\n"
    override fun handle(input: BufferedReader): CommandResponse {
        var outputOfCommand = ""
        input.skip(clCommand.length.toLong() + "(lldb) ".length.toLong())
        var line: String = input.readLine()
        require (line.contains(Regex("Process [0-9]* resuming"))) {"unexpected output: $line!"}

        while (line != "") {
            if (line.contains("(lldb)")) {
                line = input.readLine()
                continue
            }
            if (line.contains(Regex("Process [0-9]* exited with status = [0-9]")) ||
                line.contains(Regex("Target [0-9]*: \\([A-z]*\\) stopped\\."))) {
                outputOfCommand += "$line\n"
                break
            }
            outputOfCommand += "$line\n"
            line = input.readLine()
        }
        return SuccessCommandResponse(outputOfCommand)
    }
}