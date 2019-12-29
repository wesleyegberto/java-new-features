# Java 9

## Features

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

## JEPs

* [143](https://openjdk.java.net/jeps/143) - Improve contended locking
  * Improvement in monitor enter and exit, faster notifications
* [197](https://openjdk.java.net/jeps/197) - Segmented code cache
  * Improvement of code cache by separeting in three segments: Non-method, Profiled and Non-profiled.
  * Command line params to set the size in bytes:
    * -XX:NonMethodCodeHeapSize
    * -XX:ProfiledCodeHeapSize
    * -XX:NonProfiledCodeHeapSize
* [199](https://openjdk.java.net/jeps/199) - Smart Java compilation (phase two)

## Sample Projects

The shell script uses env var JAVA9_HOME to jdk-9 dir.

### Simple Deps

The calculator module uses math_libs module.
The math_api module export an interface:

* Operation: defines an operation;
  
The math_lib module export a class:

* MathIntegerOperations: expose the implementations of Operation;
* Showing dependencies

To show the deps we need to set modules path if a module use any other define

```sh
$JAVA9_HOME/bin/jdeps output/mlibs/mathlib-1.0.jar
$JAVA9_HOME/bin/jdeps --module-path output/mlibs/ output/mlibs/calculator.jar
```

### Services

The provider module exposes a service Calculator using the its implementation HitchhikerCalculator.
The consumer uses the exposed service by loooking up using ServiceLoader.

## Links

* [Virtual Hackday: Become an early Java 9 expert](https://www.youtube.com/watch?v=y868lMk6NtY)
* [Devoxx - Exploring Java 9 by Venkat Subramaniam](https://www.youtube.com/watch?v=8XmYT89fBKg)
* [jlink reference](https://docs.oracle.com/javase/9/tools/jlink.htm)
