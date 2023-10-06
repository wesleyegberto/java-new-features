# Java 22

To run each example use: `java --enable-preview --source 22 <FileName.java>`

## JEPs

* [454](https://openjdk.org/jeps/454) - Foreign Function & Memory API

## Features

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


## Links

- [JDK 22 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21900)

