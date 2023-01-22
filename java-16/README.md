# Java 16

To run each example use: `java --enable-preview --source 16 <FileName.java>`

## JEPs

* [338](https://openjdk.java.net/jeps/338) - Vector API (Incubator)
* [347](https://openjdk.java.net/jeps/347) - Enable C++14 Language Features
* [357](https://openjdk.java.net/jeps/357) - Migrate from Mercurial to Git
* [369](https://openjdk.java.net/jeps/369) - Migrate to GitHub
* [376](https://openjdk.java.net/jeps/376) - ZGC: Concurrent Thread-Stack Processing
* [380](https://openjdk.java.net/jeps/380) - Unix-Domain Socket Channels
* [386](https://openjdk.java.net/jeps/386) - Alpine Linux Port
* [387](https://openjdk.java.net/jeps/387) - Elastic Metaspace
* [388](https://openjdk.java.net/jeps/388) - Windows/AArch64 Port
* [389](https://openjdk.java.net/jeps/389) - Foreign Linker API (Incubator)
* [390](https://openjdk.java.net/jeps/390) - Warnings for Value-Based Classes
* [392](https://openjdk.java.net/jeps/392) - Packaging Tool
* [393](https://openjdk.java.net/jeps/393) - Foreign-Memory Access API (Third Incubator)
* [394](https://openjdk.java.net/jeps/394) - Pattern Matching for instanceof
* [395](https://openjdk.java.net/jeps/395) - Records
* [396](https://openjdk.java.net/jeps/396) - Strongly Encapsulate JDK Internals by Default
* [397](https://openjdk.java.net/jeps/397) - Sealed Classes (Second Preview)

## Features

### Language

* Pattern matching for `instanceof` (standard)
* Record
  * now we can declare static class or a record in an inner class
* Warnings for [Value-Based Classes](https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/lang/doc-files/ValueBased.html)
  * starting the preparatives for Project Valhalla (can't wait for it to be fully implemented)
  * warning the using value-based classes in synchronization and flag to force an error (1) or log (2)
    * flag: `-XX:DiagnoseSyncOnValueBasedClasses=1|2`
  * starting with primitive wrappers (but soon might be expanded)
* Foreign-Memory Access API
  * change in the API
* Sealed classes (second preview)
  * `sealed`, `non-sealed` and `permits` became contextual keywords (we can use it as var name or method name)
* Unix-Domain Socket Channels

### JVM

* Elastic Metaspace - allow JVM to return unused memory to OS
  * flag to customize: `-XX:MetaspaceReclaimPolicy=balanced|aggressive|none`
* Packaging tool promoted to standard
  * changing only on flag: `--bind-services` to `--jlink-options`
* Changed default flag for JDK internals:
  * now it is `--illegal-access=deny`

## Links

* [JDK 16 Jeps](https://openjdk.java.net/projects/jdk/16/)
* [Valhalla Project](https://openjdk.java.net/projects/valhalla/)
