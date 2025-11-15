# Java New Features

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/wesleyegberto/java-new-features)

A project to explore more about the new features from Java 8 through Java 21.

[JDK Release Notes](https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html)

[DeepWiki about this repo](https://deepwiki.com/wesleyegberto/java-new-features)

## OpenJDK Projects

* [Valhalla](./projects/valhalla/): enhance Java object model with value objects and primitive values.
* [Loom](./projects/loom/): project to bring user-mode threads.
* [Leyden](https://openjdk.org/projects/leyden/): improve the startup time, time to peak performance and footprint

## Resume by Version

`standard` shows the version in which a preview feature became stable and available for production use.

* [Java 26](java-26/)
  * Prepare to Make Final Mean Final
  * Remove the Applet API
  * Ahead-of-Time Object Caching with Any GC
  * HTTP/3 for the HTTP Client API
  * G1 GC: Improve Throughput by Reducing Synchronization
  * PEM Encodings of Cryptographic Objects (preview 2)
  * Structured Concurrency (preview 6)
  * Lazy Constants (Second Preview)
  * Vector API (Eleventh Incubator)
  * Primitive Types in Patterns, instanceof, and switch (preview 4)

* [Java 25](java-25/) (Sep, 2025)
  * Stable Values (preview)
  * Remove the 32-bit x86 Port
  * Structured Concurrency (preview 5)
  * Scoped Values (standard) :rocket:
  * Primitive Types in Patterns, instanceof, and switch (preview 3)
  * Key Derivation Function API (standard)
  * Module Import Declarations (standard)
  * Compact Source Files and Instance Main Methods (standard) :rocket:
  * Flexible Constructor Bodies (standard)
  * Ahead-of-Time Command-Line Ergonomics
  * Ahead-of-Time Method Profiling
  * Compact Object Headers (standard) :rocket:
  * Generational Shenandoah (standard)

* [Java 24](java-24/) (Mar, 2025)
  * Generational Shenandoah (experimental)
  * Compact Object Headers (experimental)
  * Prepare to Restrict the Use of JNI
  * Late Barrier Expansion for G1
  * Key Derivation Function API (preview)
  * Remove the Windows 32-bit x86 Port
  * Ahead-of-Time Class Loading & Linking
  * Class-File API (standard)
  * Steam Gatherers (standard)
  * Permanently Disable the Security Manager
  * Scoped Values (preview 4)
  * Primitive Types in Patterns, instanceof, and switch (preview 2)
  * Vector API (incubator)
  * ZGC Remove the Non-Generational Mode
  * Synchronize Virtual Threads without Pinning :rocket:
  * Flexible Constructor Bodies (preview 3)
  * Linking Run-Time Images without JMODs
  * Module Import Declarations (preview 2)
  * Simple Source Files and Instance Main Mathods (preview 4)
  * Warn Upon Use of Memory-Access Methods in sun.misc.Unsafe
  * Structured Concurrency (preview 4)
  * Deprecate the 32-bit x86 Port for Removal

* [Java 23](java-23/) (Sep, 2024)
  * Primitive Types in Patterns, `instanceof` and `switch` (preview)
  * Class-File API (preview 2)
  * Markdown Documentation Comments
  * Vector API (incubator)
  * Stream Gatherers (preview 2)
  * Deprecate the Memory-Access Methods in Unsafe for Removal
  * ZGC Generational Mode by Default
  * Module Import Declarations (preview)
  * Implicitly Declared Classes and Instance Main Methods (preview 3)
  * Structured Concurrency (preview 3)
  * Scoped Value (preview 3)
  * Flexible Constructor Bodies (preview 2)

* [Java 22](java-22/) (Mar, 2024)
  * Region Pinning for G1
  * Statements before `super` (preview)
  * FFM API (standard)
  * Unnamed Variable & Patterns (standard)
  * Class-File API (preview)
  * Launch Multi-File Source-Code Programs
  * String Templates (preview 2)
  * Vector API (incubator)
  * Stream Gatherers (preview)
  * Structured Concurrency (preview 2)
  * Scoped Value (preview 2)
  * Implicity Declared Classes and Instance Main Methods (preview 2)

* [Java 21](java-21/) (LTS; Sep, 2023)
  * String Templates (preview)
  * Sequenced Collections
  * Generational ZGC
  * Record Pattern (standard) :rocket:
  * Pattern Matching for `switch` (standard) :rocket:
  * Foreign Function & memory API (preview 3)
  * Unnamed Patterns and Variables (preview)
  * Virtual Threads (standard) :rocket:
  * Unnamed Classes and Instance Main Methods (preview)
  * Scoped Value (preview)
  * Vector API (incubator)
  * Deprecate the Windows 32-bit x86 Port for Removal
  * Prepare to Disallow the Dynamic Loading of Agents
  * KEM API
  * Structured Concurrency (preview)

* [Java 20](java-20/) (Mar, 2023)
  * Scoped values (incubator)
  * Record pattern (preview 2)
  * Pattern matching for `switch` (preview 4)
  * Foreign Function & memory API (preview 2)
  * Virtual Threads (preview 2)
  * Structured concurrency (second incubator)
  * Vector API (incubator)

* [Java 19](java-19/) (Sep, 2022)
  * Virtual Thread (preview) :rocket:
  * Pattern matching for `switch` (preview 3)
  * Vector API (fourth incubator)
  * Record pattern (preview)
  * Structured concurrency (incubator)

* [Java 18](java-18/) (Mar, 2022)
  * UTF-8 by Default
  * Simple Web Server
  * Code Snippets in Java API Documentation
  * Pattern matching for `switch` (preview 2)
  * Deprecated method `finalize`

* [Java 17](java-17/) (LTS; Sep, 2021)
  * Restore Always-Strict Floating-Point Semantics
  * Enhanced Pseudo-Random Number Generator
  * New macOS rendering for Java 2D API
  * macOS/AArch64 Port
  * Deprecate the Applet API for Removal
  * Strongly Encapsulate JDK Internals
  * Pattern matching for `switch` (preview)
  * Remove RMI Activation
  * Sealed Classes (standard) :rocket:
  * Remove the experimental AOT and JIT compiler
  * Deprecate the Security Manager for Removal
  * Foreign Function & Memory API (incubator)
  * Vector API (fourth incubator)
  * Context-Specific Deserialization Filters

* [Java 16](java-16/) (Mar, 2021)
  * Records (standard) :rocket:
  * Pattern matching for `instanceof` (standard)
  * Sealed classes (preview 2)
  * Unix-Domain Socket Channels
  * Warnings for Value-Based Classes
  * Foreign-Memory Access API (incubator)
  * Vector API (incubator)
  * Foreign Linker API (incubator)
  * Packaging Tool

* [Java 15](java-15/) (Sep, 2020)
  * Sealed classes (preview)
  * Hidden classes
  * DatagramSocket reimplementation
  * Pattern matching for `instanceof` (preview 2)
  * Records (preview 2)
  * Foreign-Memory Access API (incubator)
  * GCs ZGC and Shenandoah turned final

* [Java 14](java-14/) (Mar, 2020)
  * Switch expression (standard)
  * Pattern matching for `instanceof` (preview)
  * Text blocks improvements (preview 2)
  * Records (preview)
  * Helpful NullPointerExceptions
  * Packaging tool
  * JFR even streaming

* [Java 13](java-13/) (Sep, 2019)
  * Switch expression (preview 2)
  * Text blocks (preview)
  * String API updates
  * NIO updates
  * Socket and ServerSocket reimplementation (Project Loom)
  * Dynamic CDS Archives
  * ZGC: Uncommit Unused Memory

* [Java 12](java-12/) (Mar, 2019)
  * Switch expression (preview)
  * Compact Number Format
  * Collectors improvements
  * CompletableFuture improvements
  * CDS enable by default
  * New GC and improvements

* [Java 11](java-11/) (LTS; Sep, 2018)
  * Removal of Java EE Modules, JavaFX and deprecated API
  * Http Client (incubator)
  * var in lambda expressions
  * API improvements
  * Null I/O

* [Java 10](java-10/) (Mar, 2018)
  * Process API improvements
  * Collections improvements
  * Application Class-Data Sharing
  * `var` keyword to declare variables

* [Java 9](java-9/) (Sep, 2017)
  * Milling Project Coin
  * Process API
  * Platform Logging API and Service
  * Concurrency improvements
  * Collections improvements
  * Project Jigsaw
  * Segmented code cache
  * JShell
  * much more (see JEPs)

* [Java 8](java-8/) (Marc, 2014)
  * API
  * Stream API
  * Project Lambda
  * Repeating annotations

## Running

### Java and Javac

You will need to provide the flag to enable preview and the number of Java version:

```bash
java --enable-preview --source 21 SourceCode.java

javac --enable-preview --source 21 SourceCode.java
```

### JShell

To use JShell you can use the following flag:

```bash
jshell --enable-preview
```

### Maven

* Maven: 3.5.0
  * compiler plugin: 3.8.0
  * surefire and failsafe: 2.22.0
  * plugins using ASM (e.g. the shade plugin) will likely need to be updated as well

  To run the examples with Maven use:

```xml
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <release>${java.version}</release>
        <compilerArgs>--enable-preview</compilerArgs>
    </configuration>
</plugin>
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>--enable-preview</argLine>
    </configuration>
</plugin>
<plugin>
    <artifactId>maven-failsafe-plugin</artifactId>
    <configuration>
        <argLine>--enable-preview</argLine>
    </configuration>
</plugin>
```

## Migration Guide

Checking deprecated APIs from a JDK version:

```sh
jdeprscan --release <Jdk_Version> -l --for-removal
```

## Helpful Links

* [OpenJDK Projects](https://openjdk.java.net/projects/)
* [JDK Update Releases](https://openjdk.org/projects/jdk-updates/)
* [The Java Version Almanac](https://javaalmanac.io/)
* [Dev.java - tutorials, news and more](https://dev.java/)
  * [Java Platform Evolution](https://dev.java/evolution/)
  * [Learn Java](https://dev.java/learn/)
  * [Java Playground](https://dev.java/playground/)
* [Inside.java - Sip of Java](https://inside.java/2021/10/21/sip24/)
* [Learn.java](https://learn.java/)
* Download Java versions:
  * [The Role of Preview Features in Java and Beyond](https://blogs.oracle.com/javamagazine/the-role-of-previews-in-java-14-java-15-java-16-and-beyond)
  * [Place to get early releases from Oracle's JDK](https://jdk.java.net/)
  * [Oracle - Java Binaries Archive](https://www.oracle.com/java/technologies/downloads/archive/#JavaSE)
  * [Archived OpenJDK General-Availability Releases](https://jdk.java.net/archive/)
* [Considerations Bumping Java EE](https://vorozco.com/blog/2020/2020-08-21-considerations-bumping-javaee.html)
* Migration tools:
  * [Migrating From JDK 8 to Later JDK Releases](https://docs.oracle.com/en/java/javase/17/migrate/migrating-jdk-8-later-jdk-releases.html#GUID-7744EF96-5899-4FB2-B34E-86D49B2E89B6)
  * [Eclipse Migration Toolkit for Java (EMT4J) Simplifies Upgrading Java Applications](https://www.infoq.com/news/2022/12/eclipse-migration-toolkit-java/)
  * [Rewrite - Migrate to Java 17](https://docs.openrewrite.org/running-recipes/popular-recipe-guides/migrate-to-java-17)
* Java Projects:
  * [Project Leyden - Capturing Lightning in a Bottle](https://www.youtube.com/watch?v=lnth19Kf-x0&ab_channel=Java)
* Presentations:
  * [The Amazing Features of Modern Java - Venkat Subramaniam](https://youtu.be/nlZe-y2XvQY)
  * [JVM Language Submit 2025 - Growing the Java Language (Brian Goetz)](https://www.youtube.com/watch?v=Gz7Or9C0TpM)
* Blogs:
  * [JDK Security Enhancements - Sean Mullan](https://seanjmullan.org/blog/)

