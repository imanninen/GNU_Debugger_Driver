package org.gnudebugger

import org.gnudebugger.app.DebuggerDriver

fun main() {
    val debuggerDriver = DebuggerDriver("/usr/bin/lldb")

    debuggerDriver.loadDebugExecutable("debugee/target")
    debuggerDriver.setBreakPoint("main.c", 5)
    debuggerDriver.setBreakPoint("main.c", 12)
    debuggerDriver.setBreakPointHandler {input, output ->
        println(debuggerDriver.getBackTrace(input, output))
        println(debuggerDriver.getVarValueByName("x", input, output))
        debuggerDriver.resume(input, output)
    }
    debuggerDriver.setBreakPointHandler { input, output ->
        println(debuggerDriver.getBackTrace(input, output))
        debuggerDriver.resume(input, output)
    }
    debuggerDriver.run(listOf("aboba"))
}

