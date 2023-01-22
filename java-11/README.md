# Java 11

To run each example use:

```sh
sh build.sh
java -cp bin <ClassName>
```

## JEPs

* [181](https://openjdk.java.net/jeps/181) - Nest-Based Access Control
* [309](https://openjdk.java.net/jeps/309) - Dynamic Class-File Constants
* [315](https://openjdk.java.net/jeps/315) - Improve Aarch64 Intrinsics
* [318](https://openjdk.java.net/jeps/318) - Epsilon: A No-Op Garbage Collector
* [320](https://openjdk.java.net/jeps/320) - Remove the Java EE and CORBA Modules
* [321](https://openjdk.java.net/jeps/321) - HTTP Client (Standard)
* [323](https://openjdk.java.net/jeps/323) - Local-Variable Syntax for Lambda Parameters
* [324](https://openjdk.java.net/jeps/324) - Key Agreement with Curve25519 and Curve448
* [327](https://openjdk.java.net/jeps/327) - Unicode 10
* [328](https://openjdk.java.net/jeps/328) - Flight Recorder
* [329](https://openjdk.java.net/jeps/329) - ChaCha20 and Poly1305 Cryptographic Algorithms
* [330](https://openjdk.java.net/jeps/330) - Launch Single-File Source-Code Programs
* [331](https://openjdk.java.net/jeps/331) - Low-Overhead Heap Profiling
* [332](https://openjdk.java.net/jeps/332) - Transport Layer Security (TLS) 1.3
* [333](https://openjdk.java.net/jeps/333) - ZGC: A Scalable Low-Latency Garbage Collector (Experimental)
* [335](https://openjdk.java.net/jeps/335) - Deprecate the Nashorn JavaScript Engine
* [336](https://openjdk.java.net/jeps/336) - Deprecate the Pack200 Tools and API

## Features

### Language

* Removal of modules and deprecated API
  * Removal of Java EE Modules
    * JAF
    * CORBA
    * JTA
    * JAXB
    * JAX-WS
    * Commons Annotation
  * JavaFX
* Scripting
* Shebang
  * `#!/opt/jdk-11/bin/java --source 11`
* var in lambda expressions
  * allowing to use annotation without specifying the type: `.filter((@Nonnull var item) -> isAllowed(item))`
* API improvements
  * String
    * `lines()`: to streaming the lines from a string
    * `strip()`, `stripLeading()` and `stripTrailing()`
    * `repeat(int)`
    * `isBlank()`
  * Path
    * `of(String, String...)` and `of(URI)`
  * Files
    * `readString(Path)`: read the entire content from a file as a String - `Files.readString(Path.of("message.txt"))`
    * `writeString(Path, CharSequence, OpenOption...)` write a String to a file - `Files.writeString(Path.of("message.txt"), updatedMessage)`
  * Null I/O
    * `InputStream.nullInputStream()`: empty input stream
    * `OutputStream.nullOutputStream()`: output stream that discards input bytes
    * `Reader.nullReader()`: empty reader
    * `Writer.nullWriter()`: writer that discards input content
  * New better way to turn a collection into an array
    * `String[] array = list.toArray(String[]::new)`
  * `Optional::isEmpty`
  * `Predicate::not`
  * `Pattern::asMatchPredicate`

### JVM

* CDS
  * The Class data sharing (CDS) feature helps reduce the startup time and memory footprint between multiple Java Virtual Machines (JVM).
  * To use CDS we need permition to write to lib dir: `$JAVA_HOME/lib/server/classes.jsa`
  * Steps:
    * First we need to generate the class list with the loaded classes: `java -XX:DumpLoadedClassList=<my-classes-list-file.lst>`
    * Then generate the dump using the class list: `java -XX:SharedClassListFile=<my-classes-list-file.jsa> -Xshare:dump`
    * To use: `java -XX:SharedArchiveFile=<my-classes-list-file.jsa>`

## Links

* [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/index.html)
* [Java 11 JEPs](https://openjdk.java.net/projects/jdk/11/)
* [Java 11 Migration Guide](https://blog.codefx.org/java/java-11-migration-guide/)
* [Oracle's Z GC](https://wiki.openjdk.java.net/display/zgc/Main)
* [Replacements for deprecated JPMS modules with Java EE APIs](https://stackoverflow.com/questions/48204141/replacements-for-deprecated-jpms-modules-with-java-ee-apis/48204154#48204154)
* [Scripting Java 11, Shebang And All](https://blog.codefx.org/java/scripting-java-shebang/)
* [Class Data Sharing](https://docs.oracle.com/en/java/javase/11/vm/class-data-sharing.html#GUID-EC975B2E-B4AB-45B4-B91F-51C3A264D0CE)
