# Debugger Driver
## Solution
***
### Main approach:
First of all, I come up to use lldb debugger because I have macOS on `m1` processor. 
(Unfortunately gdb is not support `m1` macs).
Then I thought a lot about how I should run debugger from program and come up to build the process using 
`ProcessBuilder`and then write commands to the input of the processor.
Also, I decided that my application will not compile user code from the provided sources. 
That is because of the primary goal of debugger it to debug and not compile, and also sometimes, compiling
some code is not a trivial task. 
(When a user uses some external libraries)

I tried to support linux x86, but this attempt unfortunately failed.
### Architecture:
- The package `org.gnudebugger.app` contains API class, that are provided to user.
- The package `org.gnudebugger.config.core` contains all abstract interfaces,
which are used to create lldb debugger configurations and commands.
- The package `org.gnudebugger.config.lldb` contains implementation of previous
package.
- The package `org.gnudebugger.debugger` contains abstract `Debugger` and its 
implementation `LldbDebugger`

### Tests:  
I wrote simple Unit tests to help me write code. See [tests](src/test/kotlin).
### Plans:
- If I have enough time, I want to support a gdb for linux x86.
- Have an idea how to create runtime interface and implement `step` function to go forward whe debugging.

## How to use a Debug driver:
***
If you want to use `lldb` GNU debugger, you must compile your program using `clang` or `gcc` with `-g` option. 
For now, it only supports C. First of all you have to create instance of `DebugDriver` class, which is basic 
class for sheath around GNU debugger. You can do it like this: 
```kotlin
val debuggerDriver = DebugDriver("/usr/bin/lldb")
```
### Debug driver as library:
Also, you may use `DebugDriver` as a library in your project. 
Here is a [.jar file](/build/libs/GNU_Debugger_Driver-1.0-SNAPSHOT.jar).

### Set target
To use my driver, you have to compile your code and provide executable file using function `loadDebugExecutable`.
It accepts a path as `String` and supports an absolute path or path relative to current directory.
#### Example:
```kotlin
debuggerDriver.loadDebugExecutable("debugee/target")
```
### Breakpoints
To set a breakpoint you should run `setBreakPoint` function, which accepts file name and line number where
you want to place point.
For each breakpoint, you should specify a breakpoint handler.
This approach is good, that you can specify different
handlers for different breakpoints, but if you want the same handler at all points, you have to specify it multiple 
times.
#### Example:
```kotlin
debuggerDriver.setBreakPoint("main.c", 5)
```
### Breakpoint handler
My driver has `setBreakPointHandler` which accepts function with `BufferedReader` and `OutputStream` as arguments.
They are needed to invoke some driver function in the scope. 
I didn't create something smarter than this, because I don't know how to not pass input and output from the process,
which is not running at the moment. Also don't know how to hide these parameters.
So, how does my breakpoint handler work? In the scope of the described function, you should write what you want to happen on
the break. But at the end you must invoke `driver.resume(input, output)` to finish handling point.

#### Commands that are used in breakpoint debugger scope:
- `driver.getBackTrace(input, output)` - return stack trace string representation. You may use it haw you want.
- `debugger.getVarValueByName("i", input, output)` - return a value in format `(type) value` or return 
`ERROR` message.
#### Example:
```kotlin
debuggerDriver.setBreakPointHandler {input, output ->
    println(debuggerDriver.getBackTrace(input, output))
    println(debuggerDriver.getVarValueByName("i", input, output))
    debuggerDriver.resume(input, output)
}
```
### Run
To run DebugDriver you should use `run()` method, which accepts a `List<String>` of program arguments.

