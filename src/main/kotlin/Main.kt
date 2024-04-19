package org.gnudebugger

import org.gnudebugger.app.DebuggerDriver

internal fun main() {
    val debuggerDriver = DebuggerDriver("/usr/bin/lldb")

    debuggerDriver.loadDebugExecutable("debugee/target")
    debuggerDriver.setBreakPoint("main.c", 5)
    debuggerDriver.setBreakPoint("main.c", 12)
    debuggerDriver.setBreakPointHandler { input, output ->
        println(debuggerDriver.getBackTrace(input, output))
        println(debuggerDriver.getVarValueByName("i", input, output))
        debuggerDriver.resume(input, output)
    }
    debuggerDriver.setBreakPointHandler { input, output ->
        println(debuggerDriver.getBackTrace(input, output))
        debuggerDriver.resume(input, output)
    }
    debuggerDriver.run(listOf("aboba"))
}

