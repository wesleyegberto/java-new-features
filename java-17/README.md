# Java 17

## Features

### Language

* Sealed classes (standard)
* Pseudo-Random Number Generators: created new classes to generate number:
  * New types:
    * `SplittableRandomGenerator`: allow spawn a new generator using existing one that produce statistically independent results;
    * `JumpableRandomGenerator`: allow the client to jump ahead a moderate number of draws;
    * `LeapableRandomGenerator`: allow the client to jump head a large number of draws;
    * `ArbitrarilyJumpableRandomGenerator`: extends LeapableRandomGenerator and allow to jump arbitrary numbero of draws.
  * We can use `RandomGeneratorFactory` to create the generators.

## JEPs

* [356](https://openjdk.java.net/jeps/356) - Enhanced Pseudo-Random Number Generators
* [382](https://openjdk.java.net/jeps/382) - New macOS Rendering Pipeline
* [391](https://openjdk.java.net/jeps/391) - macOS/AArch64 Port
* [398](https://openjdk.java.net/jeps/398) - Deprecate the Applet API for Removal
* [409](https://openjdk.java.net/jeps/409) - Sealed Classes
* [410](https://openjdk.java.net/jeps/410) - Remove the Experimental AOT and JIT Compiler
* [412](https://openjdk.java.net/jeps/412) - Foreign Function & Memory API (Incubator)
* [414](https://openjdk.java.net/jeps/414) - Vector API (Second Incubator)

## Links

* [JDK 17 Jeps](https://openjdk.java.net/projects/jdk/17/)
