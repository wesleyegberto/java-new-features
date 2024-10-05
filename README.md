# Java New Features

A project to explore more about the new features from Java 8 through Java 21.

[JDK Release Notes](https://www.oracle.com/java/technologies/javase/jdk-relnotes-index.html)

## OpenJDK Projects

* [Valhalla](./projects/valhalla/): enhance Java object model with value objects and primitive values.
* [Loom](./projects/loom/): project to bring user-mode threads.

## Resume by Version

* [Java 24](java-24/)
  * Prepare to Restrict the Use of JNI
  * Class-File API

* [Java 23](java-23/) (Sep, 2024)
  * Primitive Types in Patterns, `instanceof` and `switch` (preview)
  * Class-File API (preview 2)
  * Stream Gatherers (preview 2)
  * Markdown Documentation Comments
  * Module Import Declarations (preview)
  * Implicity Declared Classes and Instance Main Methods (preview 3)
  * Structured Concurrency (preview 3)
  * Scoped Value (preview 3)
  * Flexible Constructor Bodies (preview 2)

* [Java 22](java-22/) (Mar, 2024)
  * Statements before `super` (preview)
  * FFM API (standard)
  * Unnamed Variable & Patterns
  * Launch Multi-File Source-Code Programs
  * String Templates (preview 2)
  * Stream Gatherers (preview)
  * Structured Concurrency (preview 2)
  * Scoped Value (preview 2)
  * Class-File API (preview)
  * Implicity Declared Classes and Instance Main Methods (preview 2)

* [Java 21](java-21/) (LTS; Sep, 2023)
  * Virtual Threads (standard) :rocket:
  * Record Pattern (standard) :rocket:
  * Pattern Matching for `switch` (standard) :rocket:
  * Sequenced Collections
  * String Templates (preview)
  * Foreign Function & memory API (preview 3)
  * Unnamed Patterns and Variables (preview)
  * Unnamed Classes and Instance Main Methods (preview)
  * Prepare to Disallow the Dynamic Loading of Agents
  * KEM API
  * Structured Concurrency (preview)
  * Scoped Value (preview)

* [Java 20](java-20/) (Mar, 2023)
  * Scoped values (incubator)
  * Record pattern (preview 2)
  * Pattern matching for `switch` (preview 4)
  * Foreign Function & memory API (preview 2)
  * Virtual Threads (preview 2)
  * Structured concurrency (second incubator)

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
  * Sealed classes (standard)
  * Pattern matching for `switch` (preview)
  * Enhanced Pseudo-Random Number Generator
  * Deprecate the Applet API for Removal
  * New macOS rendering for Java 2D API
  * Remove the experimental AOT and JIT compiler

* [Java 16](java-16/) (Mar, 2021)
  * Records (standard)
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

## Helpful Links

* [OpenJDK Projects](https://openjdk.java.net/projects/)
* [JDK Update Releases](https://openjdk.org/projects/jdk-updates/)
* [The Java Version Almanac](https://javaalmanac.io/)
* [Dev.java - tutorials, news and more](https://dev.java/)
  * [Java Platform Evolution](https://dev.java/evolution/)
  * [Java Playground](https://dev.java/playground/)
* [Inside.java - Sip of Java](https://inside.java/2021/10/21/sip24/)
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
* Presentations:
  * [The Amazing Features of Modern Java - Venkat Subramaniam](https://youtu.be/nlZe-y2XvQY)
* Java Projects:
  * [Project Leyden - Capturing Lightning in a Bottle](https://www.youtube.com/watch?v=lnth19Kf-x0&ab_channel=Java)
* Blogs:
  * [JDK Security Enhancements - Sean Mullan](https://seanjmullan.org/blog/)

