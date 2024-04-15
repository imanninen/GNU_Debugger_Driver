package org.gnudebugger

import org.gnudebugger.app.DebuggerDriver

fun main() {
    val debuggerDriver = DebuggerDriver("/usr/bin/lldb")
    var gg = ""
    debuggerDriver.loadDebugExecutable("debugee/target")
    debuggerDriver.setBreakPoint("main.c", 5)
    debuggerDriver.setBreakPoint("main.c", 12)
    debuggerDriver.setBreakPointHandler {input, output ->
        gg = input.toString()
        println(debuggerDriver.getBackTrace(input, output))
        debuggerDriver.resume()
    }
    debuggerDriver.setBreakPointHandler { input, output ->
        debuggerDriver.resume()
    }
    debuggerDriver.run(listOf("abovba"))
    println(gg)
}

