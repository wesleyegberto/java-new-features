# Java 15

To run each example use: `java --enable-preview --source 15 <FileName.java>`

## JEPs

* [339](https://openjdk.java.net/jeps/339) - Edwards-Curve Digital Signature Algorithm (EdDSA)
* [360](https://openjdk.java.net/jeps/360) - Sealed Classes (Preview)
* [371](https://openjdk.java.net/jeps/371) - Hidden Classes
* [372](https://openjdk.java.net/jeps/372) - Remove the Nashorn JavaScript Engine
* [373](https://openjdk.java.net/jeps/373) - Reimplement the Legacy DatagramSocket API
* [374](https://openjdk.java.net/jeps/374) - Disable and Deprecate Biased Locking
* [375](https://openjdk.java.net/jeps/375) - Pattern Matching for instanceof (Second Preview)
* [377](https://openjdk.java.net/jeps/377) - ZGC: A Scalable Low-Latency Garbage Collector
* [378](https://openjdk.java.net/jeps/378) - Text Blocks
* [379](https://openjdk.java.net/jeps/379) - Shenandoah: A Low-Pause-Time Garbage Collector
* [381](https://openjdk.java.net/jeps/381) - Remove the Solaris and SPARC Ports
* [383](https://openjdk.java.net/jeps/383) - Foreign-Memory Access API (Second Incubator)
* [384](https://openjdk.java.net/jeps/384) - Records (Second Preview)
* [385](https://openjdk.java.net/jeps/385) - Deprecate RMI Activation for Removal

## Features

### Language

* Sealed types
  * Restrict which types can extend/implement a type
  * Constraints:
    * The sealed class and its permitted subclasses must belong to the same module, and, if declared in an unnamed module, the same package.
    * Every permitted subclass must directly extend the sealed class.
    * Every permitted subclass must choose a modifier to describe how it continues the sealing initiated by its superclass:
      * `final`
      * `sealed`
      * `non-sealed` (back to a normal class open to extensibility)
* Pattern matching for `instanceof` (second preview - no changes)
* Text blocks (standard)
* Records (second preview)
  * Integration with sealed types in interfaces
    * The combination of record and sealed types is referred as algebraic data types.

### JVM

* GC turned final:
  * [Shenandoah](https://wiki.openjdk.java.net/display/shenandoah/Main)
    * Flag: `-XX:+UseShenandoahGC`
  * [ZGC](https://wiki.openjdk.java.net/display/zgc/Main)
    * Flag: `-XX:+UseZGC`

## Links

* [JDK 15 Jeps](https://openjdk.java.net/projects/jdk/15/)
* [Doc about record and sealed types](https://cr.openjdk.java.net/~briangoetz/amber/datum.html)
* [ZGC | What's new in JDK 15](https://malloc.se/blog/zgc-jdk15)
* [Inside Java 15 JEPs in Five Buckets](https://blogs.oracle.com/javamagazine/inside-java-15-fourteen-jeps-in-five-buckets)
* [Programmer's Guide to Text Blocks](https://openjdk.org/projects/amber/guides/text-blocks-guide)

