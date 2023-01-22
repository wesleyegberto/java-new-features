# Java 13

To run each example use: `java --enable-preview --source 13 <FileName.java>`

## JEPs

* [354](https://openjdk.java.net/jeps/354) - Switch expression (preview 2)
* [355](https://openjdk.java.net/jeps/355) - Text blocks (preview)
* [350](https://openjdk.java.net/jeps/350) - Dynamic CDS Archives
* [351](https://openjdk.java.net/jeps/351) - ZGC: Uncommit Unused Memory
* [353](https://openjdk.java.net/jeps/353) - Reimplement the Legacy Socket API

## Features

### Language

* Switch expressions improvements
  * Changed `break` from Java 12 to `yield`
* Text blocks
  * flag to compile: `-Xlint:text-blocks`
* API improvements
  * String methods:
    * `stripIdent`: same operation done by the compiler to strip incidental identation from text block';
    * `translateEscapes`: same operation done by the compiler to translate escapes in strings;
    * `formatted`: shortcut to String.format '`"%s years old".formatted(42)`)
  * NIO improvements
* Socket and ServerSocket reimplementation (alignment to [Project Loom](https://openjdk.java.net/projects/loom/))

### JVM

* Creating Class-Data archives For AppCDS
  * flag to java `-XX:ArchiveClassesAtExit=<FileHere.jsa>` to generate class-data on exit
  * run with it: `-XX:SharedArchiveFile=<FileHere.jsa>`
* ZGC (Oracle's Z GC) returns unused memory to SO
  * `-XX:ZUncommitDelay=<seconds>` to set the delay in seconds
  * `-XX:SoftMaxHeapSize`: soft (won't generate OutOfMemoryError - will request more) limit to avoid the JVM to grow in memory

## Links

* [Java 13 Documentation](https://docs.oracle.com/en/java/javase/13/index.html)
* [Java 13 Guide](https://blog.codefx.org/java/java-13-guide/)
* [Switch Expression](https://blog.codefx.org/java/switch-expressions/)
* [Text Block Guid](http://cr.openjdk.java.net/~jlaskey/Strings/TextBlocksGuide_v9.html)

