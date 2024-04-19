package org.gnudebugger.config.core

import java.io.BufferedReader
import java.io.OutputStream

/** This it the interface for setting configuration for debugger.
 * @property config It is the list of commands ([DebugCommand]), which is used to set up debug running configurations
 * such as set breakpoint etc.
 * @property breakPointsHandlers It stores handlers for breakpoints.
 * Each handler corresponds to each breakpoint,
 * so the number of handlers has to be equal to the number of breakpoints
 */
interface DebuggerConfiguration {
    val config: MutableList<DebugCommand>
    var targetIsSet: Boolean
    val breakPointsHandlers: MutableList<(BufferedReader, OutputStream) -> HandlerReturn>

    /**
     * Method to add configuration at the end of [config].
     * @param configuration
     */
    fun addConfiguration(configuration: DebugCommand)

    /**
     * Method to add breakpoint handler to [breakPointsHandlers].
     * @param block runnable block, which it created by user to handle breakpoint.
     */
    fun setBreakPointHandler(block: (BufferedReader, OutputStream) -> HandlerReturn)

    /**
     * This class is used to set specific return value to [org.gnudebugger.core.Debugger.resume] method.
     */
    enum class HandlerReturn {
        RESUME,
    }
}