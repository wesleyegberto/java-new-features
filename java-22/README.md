# Java 22

To run each example use: `java --enable-preview --source 22 <FileName.java>`

## JEPs

* [423](https://openjdk.org/jeps/423) - Region Pinning for G1
* [447](https://openjdk.org/jeps/447) - Statements before `super()` (Preview)
* [454](https://openjdk.org/jeps/454) - Foreign Function & Memory API
* [456](https://openjdk.org/jeps/456) - Unnamed Variables & Patterns
* [457](https://openjdk.org/jeps/457) - Class-File API (Preview)
* [459](https://openjdk.org/jeps/459) - String Templates (Second Preview)
* [460](https://openjdk.org/jeps/460) - Vector API (Seventh Incubator)
* [461](https://openjdk.org/jeps/461) - Stream Gatherers (Preview)
* [463](https://openjdk.org/jeps/463) - Implicity Declared Classes and Instance Main Methods (Second Preview)

## Features

* **Region Pinning for G1**
  * goal: "reduce latency by implementing region pinning in G1, so that garbage collection need not be disable during JNI critial regions"
  * avoid worst case cenarios where the application is stoped for minutes, unnecessary out-of-memory conditions due to thread starvation, premature VM shutdown.
* **Foreign Function & Memory API**
  * promotion to standard
  * API to allow Java to interoperate with code and data outside of JVM;
  * will replace JNI, allowing efficiently invoking foreign functions and safely accessing foreign memory;
  * goals:
    * productivity: replace native methods and the JNI with a concise, readable, and pure-Java API;
    * performance: provide access to foreign functions and memory with overhead comparable or better than JNI and `sun.misc.Unsafe`;
    * broad platform support: enable the discovery and invocation of native libraries on every platform where the JVM runs;
    * uniformity: provide ways to operate on structured and unstructured data, of unlimited size, in multiple kinds of memory (e.g., native memory, persistent memory, and managed heap memory).
    * soundness: guarantee no use-after-free (dangling pointers) bugs, even when memory is allocated and deallocated across multiple threads.
    * integrity: allow programs to perform unsafe operations with native code and data, but warn users about such operations by default.
  * FFM API defines classes and interfaces (in package `java.lang.foreign`) to:
    * control the allocation and deallocation of foreign memory: `MemorySegment`, `Arena`, `SegmentAllocator`;
    * manipulate and access structured foreign memory: `MemoryLayout`, `VarHandle`;
    * call foreign functions: `Linker`, `SymbolLookup`, `FunctionDescriptor`, `MethodHandle`.
* **Unnamed Variables and Patterns**
  * promotion to standard
* **Class-File API**
  * provide standard API for parsing, generating and transforming Java class files
* **String Templates**
  * minor change from JDK 21
  * changed the type of template expressions
* **Implicity Declared Classes and Instance Main Methods**
  * minor change from JDK 21
  * changed the concept name from unnamed class to implicity declared class
    * "source file without an enclosing class declaration is said to implicitly declare a class with a name chosen by the host system"
  * changed the procedure for selecting a main method to invoke
  * first it looks for a method `main(String[])`, if not found then it looks for a method `main()`


## Links

- [JDK 22 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21900)

