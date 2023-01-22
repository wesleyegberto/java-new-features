# Java 14

To run each example use: `java --enable-preview --source 14 <FileName.java>`

## JEPs

* [305](https://openjdk.java.net/jeps/305) - Pattern Matching for instanceof (Preview)
* [343](https://openjdk.java.net/jeps/343) - Packaging Tool (Incubator)
* [345](https://openjdk.java.net/jeps/345) - NUMA-Aware Memory Allocation for G1
* [349](https://openjdk.java.net/jeps/349) - JFR Event Streaming
* [352](https://openjdk.java.net/jeps/352) - Non-Volatile Mapped Byte Buffers
* [358](https://openjdk.java.net/jeps/358) - Helpful NullPointerExceptions
* [359](https://openjdk.java.net/jeps/359) - Records (Preview)
* [361](https://openjdk.java.net/jeps/361) - Switch Expressions (Standard)
* [362](https://openjdk.java.net/jeps/362) - Deprecate the Solaris and SPARC Ports
* [363](https://openjdk.java.net/jeps/363) - Remove the Concurrent Mark Sweep (CMS) Garbage Collector
* [364](https://openjdk.java.net/jeps/364) - ZGC on macOS
* [365](https://openjdk.java.net/jeps/365) - ZGC on Windows
* [366](https://openjdk.java.net/jeps/366) - Deprecate the ParallelScavenge + SerialOld GC Combination
* [367](https://openjdk.java.net/jeps/367) - Remove the Pack200 Tools and API
* [368](https://openjdk.java.net/jeps/368) - Text Blocks (Second Preview)
* [370](https://openjdk.java.net/jeps/370) - Foreign-Memory Access API (Incubator)

## Features

### Language

* Switch expressions
  * Promotion to standard
* Text blocks improvements (preview)
  * added new flags
* Pattern matching for `instanceof`
  * added as Preview
* Records
  * added as Preview
  * Notes:
    * Does not have a `extends` clause, only extends `java.lang.Record`;
    * Is implicitly final, thus cannot be extended;
    * Cannot declare other fields and cannot contain instance initializers, it only contains its components declared at record header;
    * Its components are final and cannot be updated via reflection (throw `IllegalAccessException`);
    * Cannot declare native methods;
    * Local records is always static (local class and enum as never static).
* JFR Event Streaming
  * [Here](https://github.com/flight-recorder/health-report) is a great example of a tool built

### JVM

* Helpful NullPointerExpections
  * flag to enable: `-XX:+ShowCodeDetailsInExceptionMessages`
* Packaging tool to create self-contained Java applications
  * bin: `jpackage`

## Links

* [Java 14 Documentation](https://docs.oracle.com/en/java/javase/14/index.html)
* [Java 14 JEPs](https://openjdk.java.net/projects/jdk/14/)
* [Java Magazine about Java 14](https://blogs.oracle.com/javamagazine/java-14-arrives-with-a-host-of-new-features)
