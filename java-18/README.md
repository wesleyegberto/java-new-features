# Java 18

To run each example use: `java --enable-preview --source 18 <FileName.java>`

## Features

### Language

* Pattern matching for `switch`
  * Minor improvements from JDK 17:
    * refined to use dominance checking that will force constant case label to appear before a guarded pattern of the same type;
    * exhaustiveness checking is now more precise with sealed hierarchies.

### JVM

* Reimplemented code reflection to use Method Handles

## JEPs

* [400](https://openjdk.java.net/jeps/400) - UTF-8 by Default
* [408](https://openjdk.java.net/jeps/408) - Simple Web Server
* [413](https://openjdk.java.net/jeps/413) - Code Snippets in Java API Documentation
* [416](https://openjdk.java.net/jeps/416) - Reimplement Code Reflection with Method Handles
* [417](https://openjdk.java.net/jeps/417) - Vector API (Third Incubator)
* [418](https://openjdk.java.net/jeps/418) - Internet-Address Resolution SPI
* [419](https://openjdk.java.net/jeps/419) - Foreign Function & Memory API (Second Incubator)
* [420](https://openjdk.java.net/jeps/420) - Pattern matching for `swatch` (Second Preview)

## Links

* [JDK 18 Jeps](https://openjdk.java.net/projects/jdk/18/)
