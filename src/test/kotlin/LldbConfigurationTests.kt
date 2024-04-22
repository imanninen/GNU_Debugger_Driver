import org.gnudebugger.config.lldb.LldbDebuggerConfiguration
import org.gnudebugger.config.lldb.commands.LldbBreakPointCommand
import org.gnudebugger.config.lldb.commands.LldbContinueCommand
import org.gnudebugger.config.lldb.commands.LldbLoadTargetCommand
import org.gnudebugger.config.lldb.commands.LldbRunCommand
import org.junit.jupiter.api.Assertions.assertFalse
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals


class LldbConfigurationTests {
    @Test
    fun configurationCreation() {
        val config = LldbDebuggerConfiguration()
        
        assert(config.config.isEmpty()) {"config should be empty!"}
        assertFalse(config.targetIsSet) {"targetIsSet should be false!"}
        assert(config.breakPointsHandlers.isEmpty()) {"breakPointHandlers should be empty!"}
        assertEquals(0, config.breakpointsNumber, "breakpoint should be 0!")
    }

    @Test
    fun configurationUpdate() {
        val configuration = LldbDebuggerConfiguration()
        val command1 = LldbContinueCommand()
        val command2 = LldbRunCommand("")
        val command3 = LldbBreakPointCommand("test.c", 5)
        val command4 = LldbLoadTargetCommand(File("test/path"))

        configuration.addConfiguration(command1)
        configuration.addConfiguration(command2)
        assertEquals(false, configuration.targetIsSet, "targetIsSet should be false!")
        assertEquals(2, configuration.config.size, "config should have size 2")
        assertEquals(command1, configuration.config[0], "$command1 is not in the config!")
        assertEquals(command2, configuration.config[1], "$command2 is not in the config!")
        assertEquals(0, configuration.breakpointsNumber, "No breakpoints were set!")

        configuration.addConfiguration(command3)
        assertEquals(command3, configuration.config[2], "$command3 is not in the config!")
        assertEquals(1, configuration.breakpointsNumber, "No breakpoints were set!")

        configuration.addConfiguration(command4)
        assertEquals(4, configuration.config.size, "Size of the config should be 4!")
        assertEquals(true, configuration.targetIsSet, "Target had been set!")
    }
}