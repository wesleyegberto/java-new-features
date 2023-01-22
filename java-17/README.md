# Java 17

To run each example use: `java --enable-preview --source 17 <FileName.java>`

## JEPs

* [306](https://openjdk.java.net/jeps/306) - Restore Always-Strict Floating-Point Semantics
* [356](https://openjdk.java.net/jeps/356) - Enhanced Pseudo-Random Number Generators
* [382](https://openjdk.java.net/jeps/382) - New macOS Rendering Pipeline
* [391](https://openjdk.java.net/jeps/391) - macOS/AArch64 Port
* [398](https://openjdk.java.net/jeps/398) - Deprecate the Applet API for Removal
* [403](https://openjdk.java.net/jeps/403) - Strongly Encapsulate JDK Internals
* [406](https://openjdk.java.net/jeps/406) - Pattern Matching for switch (Preview)
* [407](https://openjdk.java.net/jeps/407) - Remove RMI Activation
* [409](https://openjdk.java.net/jeps/409) - Sealed Classes
* [410](https://openjdk.java.net/jeps/410) - Remove the Experimental AOT and JIT Compiler
* [411](https://openjdk.java.net/jeps/411) - Deprecate the Security Manager for Removal
* [412](https://openjdk.java.net/jeps/412) - Foreign Function & Memory API (Incubator)
* [414](https://openjdk.java.net/jeps/414) - Vector API (Second Incubator)
* [415](https://openjdk.java.net/jeps/415) - Context-Specific Deserialization Filters

## Features

### Language

* Sealed classes (standard)
* Pattern matching for switch (preview)
  * improved switch to support pattern matching for types (like `instanceof`)
  * support for `null` case
  * support for guards where we can use a boolean expression like `case String s && s.length > 10:`
* Pseudo-Random Number Generators (PRNG)
  * created new classes to generate number
  * two groups of PRNG algorithms:
    * splittable (LXM family):
      * allow spawn a new generator using existing one that produce statistically independent results
      * implemented by LXM algorithms (like `L32X64MixRandom`, `L32X64StarStarRandom`, `L64X128MixRandom`, `L64X128StarStarRandom` and others with different bitsizes)
    * jumpable:
      * allow the client to jump ahead a moderate number of draws
      * implemented by `Xoroshiro128PlusPlus` and `Xoshiro256PlusPlus`
  * we can use `RandomGeneratorFactory` to create the generators passing the algorithm name (like `L32X64MixRandom` or `Xoroshiro128PlusPlus`)
  * new types:
    * `SplittableGenerator`
    * `JumpableGenerator`
    * `LeapableGenerator`: allow the client to jump head a large number of draws;
    * `ArbitrarilyJumpableGenerator`: extends LeapableRandomGenerator and allow to jump arbitrary numbero of draws.
  * more detailed info in the [docs](https://download.java.net/java/early_access/jdk17/docs/api/java.base/java/util/random/package-summary.html)

### JVM

* Strongly encapsulate JDK internals
  * make illegal access internal API flag obsolate (was `--illegal-access=deny` in Java 16)
  * only `sun.misc` and `sun.reflect` will be allowed to be access through `jdk.unsupported` module

## Links

* [JDK 17 Jeps](https://openjdk.java.net/projects/jdk/17/)
