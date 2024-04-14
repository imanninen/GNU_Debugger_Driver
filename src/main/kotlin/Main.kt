package org.gnudebugger

import org.gnudebugger.app.DebuggerDriver

fun main() {
    val debuggerDriver = DebuggerDriver("/usr/bin/lldb")
    var gg = ""
    debuggerDriver.loadDebugExecutable("debugee/target")
    debuggerDriver.setBreakPoint("main.c", 5)
    debuggerDriver.setBreakPoint("main.c", 12)
    debuggerDriver.setBreakPointHandler {input ->
        gg = input.toString()
        debuggerDriver.resume()
    }
    debuggerDriver.setBreakPointHandler { input ->
        debuggerDriver.resume()
    }
    debuggerDriver.run(listOf("abovba"))
    println(gg)
}

