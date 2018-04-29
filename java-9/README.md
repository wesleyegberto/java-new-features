# Java 9 New Features

A project to explore more about the new features of Java...

* Jigsaw
  * Creating modules;
  * Exports and requires;
  * Requires transitive;
  * Using non-exported class through its exported interface;
  * Running a module.
* jlink
  * Tool to assemble and optimized our modules and their deps into a custom runtime image;
  * The runtime image will contain the JVM, only the Java modules we need and our code;
  * The runtime image is generated for the platform we are using (current jdk platform);
  * We can set --launcher option to generate a script to run a module/class.


## Projects
The shell script uses env var JAVA9_HOME to jdk-9 dir.

### Simple Deps
The calculator module uses math_libs module.
The math_api module export an interface:
* Operation: defines an operation;
The math_lib module export a class:
* MathIntegerOperations: expose the implementations of Operation;
* Showing dependencies
To show the deps we need to set modules path if a module use any other define
```
$JAVA9_HOME/bin/jdeps output/mlibs/mathlib-1.0.jar
$JAVA9_HOME/bin/jdeps --module-path output/mlibs/ output/mlibs/calculator.jar
```

### Services


### Links
* [Virtual Hackday: Become an early Java 9 expert](https://www.youtube.com/watch?v=y868lMk6NtY)
* [Devoxx - Exploring Java 9 by Venkat Subramaniam](https://www.youtube.com/watch?v=8XmYT89fBKg)
* [jlink reference](https://docs.oracle.com/javase/9/tools/jlink.htm)
