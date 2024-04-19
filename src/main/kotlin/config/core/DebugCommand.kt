package org.gnudebugger.config.core

/** Abstract interface fot all debugger commands.
 * @property clCommand represent command in understandable format for debugger.
 */
interface DebugCommand {
    val clCommand: String
}