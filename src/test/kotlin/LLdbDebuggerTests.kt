import org.gnudebugger.app.DebuggerDriver
import org.gnudebugger.config.lldb.responce.ErrorCommandResponse
import org.gnudebugger.config.lldb.responce.SuccessCommandResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class LLdbDebuggerTests {
    @Test
    fun debuggerCreationTest() {
        assertThrows<IllegalArgumentException> {
            DebuggerDriver("incorrect/path")
        }
        val debugger = DebuggerDriver("/usr/bin/lldb")
        assertThrows<IllegalArgumentException> { debugger.run() }
        assertThrows<IllegalArgumentException>("path should be a exists file!") { DebuggerDriver("/usr/bin") }
    }

    @Test
    fun loadTargetTest() {
        val debugger = DebuggerDriver("/usr/bin/lldb")

        assertThrows<IllegalArgumentException>("You should set target to debugger") { debugger.run() }
        assertThrows<IllegalArgumentException>("You should provide valid target. Current doesn't exists.")
        { debugger.loadDebugExecutable("not/valid/path") }
        assertThrows<IllegalArgumentException>(
            "You should provide valid target! Current is not an executable!"
        ) { debugger.loadDebugExecutable("debugee/main.c") }
        assertDoesNotThrow { debugger.loadDebugExecutable("debugee/target") }
        assertDoesNotThrow { debugger.run() }
    }

    @Test
    fun breakpointTest() {
        val debugger = DebuggerDriver("/usr/bin/lldb")
        debugger.loadDebugExecutable("debugee/target")
        debugger.setBreakPoint("main.c", 5)
        assertThrows<IllegalArgumentException>("For each breakpoint you should specify handler.")
        { debugger.run() }

        val debugger2 = DebuggerDriver("/usr/bin/lldb")
        debugger2.loadDebugExecutable("debugee/target")
        debugger2.setBreakPoint("main.c", 5)
        debugger2.setBreakPointHandler { input, output ->
            debugger2.resume(input, output)
        }
        val status = assertDoesNotThrow("Now number of breakpoint matches to number of handlers!")
        { debugger2.run() }
        assert(status is SuccessCommandResponse)

        val debugger3 = DebuggerDriver("/usr/bin/lldb")
        debugger3.loadDebugExecutable("debugee/target")
        debugger3.setBreakPoint("hello.c", 5)
        debugger3.setBreakPointHandler { input, output ->
            debugger3.resume(input, output)
        }
        val status3 = assertDoesNotThrow("Now number of breakpoint matches to number of handlers!")
        { debugger3.run() }
        assert(status3 is ErrorCommandResponse)
    }

    @Test
    fun backTraceTest() {
        val debugger = DebuggerDriver("/usr/bin/lldb")
        debugger.loadDebugExecutable("debugee/target")
        debugger.setBreakPoint("main.c", 5)
        var backtrace = ""
        debugger.setBreakPointHandler { input, output ->
            backtrace = debugger.getBackTrace(input, output)
            debugger.resume(input, output)
        }
        val status = assertDoesNotThrow("Now number of breakpoint matches to number of handlers!")
        { debugger.run() }
        assert(status is SuccessCommandResponse)
        assert(backtrace.contains("at main.c:5:2"))
        assert(backtrace.contains("dyld`start + 2360"))
    }

    @Test
    fun printVarTest() {
        val debugger = DebuggerDriver("/usr/bin/lldb")
        debugger.loadDebugExecutable("debugee/target")
        debugger.setBreakPoint("main.c", 5)
        var outputOfComm = ""
        debugger.setBreakPointHandler { input, output ->
            outputOfComm = debugger.getVarValueByName("argc", input, output)
            debugger.resume(input, output)
        }
        val status = assertDoesNotThrow("Now number of breakpoint matches to number of handlers!")
        { debugger.run() }
        assert(status is SuccessCommandResponse)
        val expectedBackTrace = "(int) 1"
        assertEquals(expectedBackTrace, outputOfComm)
    }
}