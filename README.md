# Debugger Driver
## Solution

## How to use a Debug driver:
If you want to use `lldb` GNU debugger, you must compile your program using `clang` or `gcc` with `-g` option. 
For now, it only supports C. First of all you have to create instance of `DebugDriver` class, which is basic 
class for sheath around GNU debugger. You can do it like this: 
```kotlin
val debuggerDriver = DebugDriver("/usr/bin/lldb")
```
Also, you may use `DebugDriver` as a library in your project. Here is a .jar [file](/build/libs/GNU_Debugger_Driver-1.0-SNAPSHOT.jar).

## Set target
To use my driver, you have to compile your code and provide executable file using function `loadDebugExecutable`.
It accepts a path as `String` and supports an absolute path or path relative to current directory.
#### Example:
```kotlin
debuggerDriver.loadDebugExecutable("debugee/target")
```
## Breakpoints
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
## Breakpoint handler
My driver has `setBreakPointHandler` which accepts function with `BufferedReader` and `OutputStream` as arguments.
They are needed to invoke some driver function in the scope. 
I didn't create something smarter than this, because I don't know how to not pass input and output from the process,
which is not running at the moment. Also don't know how to hide these parameters.
So, how does my breakpoint handler work? In the scope of the described function, you should write what you want to happen on
the break. But at the end you must invoke `driver.resume(input, output)` to finish handling point.

#### Example:
```kotlin
debuggerDriver.setBreakPointHandler {input, output ->
        println(debuggerDriver.getBackTrace(input, output))
        debuggerDriver.resume(input, output)
    }
```
## Run
To run DebugDriver you should use `run()` method, which accepts a `List<String>` of program arguments.

