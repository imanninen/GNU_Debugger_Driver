package org.gnudebugger.app

class Sample {
    fun importantFunction() {
        val driver = DebuggerDriver("/usr/bin/lldb")
        driver.loadDebugExecutable("target")
        driver.setBreakPoint("main.c", 5)
        driver.setBreakPointHandler { input, output ->
            // do your stuff
            driver.resume(input, output)
        }
    }
}