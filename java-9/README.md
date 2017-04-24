# Java 9 New Features

A project to explore more about the new features of Java...

* Modules
  * Creating modules
  * Exports and requires
  * Using non-exported class through its exported interface
  * Running a module
* JLink

## Project
The shell script uses env var JAVA9_HOME to jdk-9 dir.

The calculator module uses math_libs module.

The math_lib module export an interfaces and a class:
* Operation: defines an operation;
* MathIntegerOperations: expose the implementations of Operation;


### Links
* [Virtual Hackday: Become an early Java 9 expert](https://www.youtube.com/watch?v=y868lMk6NtY)
* [Devoxx - Exploring Java 9 by Venkat Subramaniam](https://www.youtube.com/watch?v=8XmYT89fBKg)