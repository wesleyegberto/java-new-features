# Java 28

To run each example use: `java --enable-preview --source 28 <FileName.java>`

## JEPs

* [401](https://openjdk.org/jeps/401) - Value Objects (Preview)
* [535](https://openjdk.org/jeps/535) - Shenandoah GC: Generational Mode by Default
* [539](https://openjdk.org/jeps/539) - Strict Field Initialization in the JVM (Preview)
* [540](https://openjdk.org/jeps/540) - Simple JSON API (Incubator)
* [541](https://openjdk.org/jeps/541) - Deprecate the macOS/x64 Port for Removal
* [542](https://openjdk.org/jeps/542) - PEM Encodings of Cryptographic Objects

## Featuers

**Shenandoah GC: Generational Mode by Default**
  * Shenandoah is a low-pause-time garbage collector
  * switch the default mode of Shenandoah GC to the generational mode
  * deprecate the non-generational mode
  * default mode: `XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational`
  * to use non-generational mode: `-XX:+UseShenandoahGC -XX:ShenandoahGCMode=satb`

## Links

* [JDK 28 JEPs](https://openjdk.org/projects/jdk/28/)
* [JDK 28 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=25101)

