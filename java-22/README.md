# Java 22

To run each example use: `java --enable-preview --source 22 <FileName.java>`

## JEPs

* [423](https://openjdk.org/jeps/423) - Region Pinning for G1
* [447](https://openjdk.org/jeps/447) - Statements before `super()` (Preview)
* [454](https://openjdk.org/jeps/454) - Foreign Function & Memory API
* [456](https://openjdk.org/jeps/456) - Unnamed Variables & Patterns
* [457](https://openjdk.org/jeps/457) - Class-File API (Preview)
* [458](https://openjdk.org/jeps/458) - Launch Multi-File Source-Code Programs
* [459](https://openjdk.org/jeps/459) - String Templates (Second Preview)
* [460](https://openjdk.org/jeps/460) - Vector API (Seventh Incubator)
* [461](https://openjdk.org/jeps/461) - Stream Gatherers (Preview)
* [462](https://openjdk.org/jeps/462) - Structured Concurrency (Second Preview)
* [463](https://openjdk.org/jeps/463) - Implicity Declared Classes and Instance Main Methods (Second Preview)
* [464](https://openjdk.org/jeps/465) - Scoped Values (Second Preview)

## Features

* **Region Pinning for G1**
  * goal: "reduce latency by implementing region pinning in G1, so that garbage collection need not be disable during JNI critial regions"
  * avoid worst case cenarios where the application is stoped for minutes, unnecessary out-of-memory conditions due to thread starvation, premature VM shutdown.
* **Statements before `super()`**
  * allow statements that do not reference the instance being created to appear before an explicit constructor invocation
  * will allow to use statements that use, transform or verify values before call `super`
  * the code before the `super` statement lives in pre-construction context:
    * can perform any statements that don't use any member of the instance being created (or its hierarchy or outter/inner classes that depends on the instance)
  * statements:
    * cannot access any instance member of the class or its parent class
    * can access static members
    * if the class is an inner class, it can access members of its enclosing class (like `Outer.this.field++`)
      * the outer class instance already exists
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
  * Memory segments and arenas:
    * memory segment is a abstraction backed by a contiguous region of memoery (off or on-heap);
    * can be:
      * native segment allocated from a off-heap memory;
      * mapped segment wrapped around a region of mapped off-heap memory;
      * array of buffer segment wrapped around a region of on-heap memory.
    * memory segment has spatial em temporal bounds:
      * spatial bound: guarantee that an access beyond the memory size won't be allowed;
      * temporal bound: guarantee that an access won't be allowed to a memory segment that its backing region was already deallocated.
    * arenas:
      * memory segment is created from an arena
      * arena controls the lifecycle of native memory segments
      * every memory segment allocated from the same arena will have the same temporal bounds
      * types:
        * [global arena](https://cr.openjdk.org/~mcimadamore/jdk/FFM_22_PR/javadoc/java.base/java/lang/foreign/Arena.html#global()):
          * arena with unbounded lifetime, it is always alive
          * `MemorySegment data = Arena.global().allocate(100); // allocates 100 bytes`
        * [automatic arena](https://cr.openjdk.org/~mcimadamore/jdk/FFM_22_PR/javadoc/java.base/java/lang/foreign/Arena.html#ofAuto()):
          * provides a bounded lifetime with non-deterministic lifetime
          * a segment allocated by a automatic arena can be accessed until JVM's GC detects that the memory segment is unreachable, then it is deallocated
          * `MemorySegment data = Arena.ofAuto().allocate(100); // allocates 100 bytes`
          * if it is allocated in a block, after that block the memory will be deallocated:
            * ```java
              void processData() {
                MemorySegment data = Arena.ofAuto().allocate(1000);
                // process data in memory
              } // from here the allocated memory will be released (as long as there isn't a memory leak)
              ```
          * it's lifetime is non-deterministic because the JVM will deallocate at some point
        * [confined arena](https://cr.openjdk.org/~mcimadamore/jdk/FFM_22_PR/javadoc/java.base/java/lang/foreign/Arena.html#ofConfined()):
          * provides a bounded lifetime with deterministic lifetime
          * can be explicity closed by the code
          * can only be accessed by one thread
          * we can use in a try-with-resources:
            * ```java
              try (Arena confinedArena = Arena.ofConfined()) {
                  MemorySegment input = confinedArena.allocate(100);
                  MemorySegment output = confinedArena.allocate(100);
              } // will be deallocated from here
              ```
        * [shared arena](https://cr.openjdk.org/~mcimadamore/jdk/FFM_23_PR/javadoc/java.base/java/lang/foreign/Arena.html#ofShared()):
          * is a confined arena for multi-threading
          * any thread can access and close the memory segment
    * allocators:
      * is an abstraction to define operations to allocate and initialize memory segments
      * memory allocation can be bottleneck when using off-heap memory, allocators help to minimize this
      * we can allocate a large memory and then use allocator to distribute through the application
      * we can create an allocator using an already allocated memory segment
        * the segments allocated by the allocator will have the same bounded lifetime
      * ```java
        MemorySegment segment = Arena.ofAuto().allocate(1024 * 1024 * 1024); // 1 GB
        SegmentAllocator allocator = SegmentAllocator.slicingAllocator(segment);
        for (int i = 0; i < 10; i++) {
            MemorySegment msi = allocator.allocateFrom(ValueLayout.JAVA_INT, i);
        }
        ```
  * Memory layout:
    * [Value layout](https://cr.openjdk.org/~mcimadamore/jdk/FFM_22_PR/javadoc/java.base/java/lang/foreign/ValueLayout.html):
      * abstraction to facilitate the memory value layout usage
        * eg.: to work with int, we can use `ValueLayout.JAVA_INT` to read 4 bytes using the platform endianness to correctly extract an int from a memory segment
        * `MemorySegment int = Arena.global().allocateFrom(ValueLayout.JAVA_INT, 42)`
        * `MemorySegment intArray = Arena.global().allocateFrom(ValueLayout.JAVA_INT, 0, 1, 2, 3, 4, 5)`
        * `MemorySegment text = Arena.global().allocateFrom("Hello World!")`
      * memory segments have simple dereference methods to read values from and write values to memory segments, these methods accept a value layout
      * example how to write value from 1 to 10 in a memory segment:
        * ```java
          MemorySegment segment = Arena.ofAuto().allocate(
              ValueLayout.JAVA_INT.byteSize() * 10, // memory size
              ValueLayout.JAVA_INT.byteAlignment() // memory alignment
          );
          for (int i = 0; i < 10; i++) {
              int value = i + 1;
              // the memory offset is calculated from: value layout byte size * index
              segment.setAtIndex(ValueLayout.JAVA_INT, i, value);
          }
          ```
    * Structured access:
      * the API provides ways to declare any memory layout and also factory method to help calculate the memory size for us
      * eg.: declare a C struct in Java:
        * C code to declare an array of 10 points:
        ```c
        struct Point { int x; int y } pts[10];
        ```
        * we can manually declare the memory layout:
        ```java
        MemorySegment segment = Arena.ofAuto().allocate(
            2 * ValueLayout.JAVA_INT.byteSize() * 10,
            ValueLayout.JAVA_INT.byteAlignment()
        );
        ```
        * we can use memory layout to describe the content of a memory segment and use `VarHandle` to write the values in the struct:
        ```java
        SequenceLayout ptsLayout = MemoryLayout.sequenceLayout(
            10, // size
            MemoryLayout.structLayout( // declare a C struct
                ValueLayout.JAVA_INT.withName("x"),
                ValueLayout.JAVA_INT.withName("y")
            )
        );
        MemorySegment segment = Arena.ofAuto().allocate(ptsLayout);
        ```
  * Foreign functions:
    * native library loaded from a lookup doesn'n belong to any class loader (unlike JNI), allowing the native library to be reloaded by any other class
      * JNI requires the native library to be loaded by only one class loader
    * FFM API doesn't provides any function for native code to access the Java environment (unlike JNI)
      * but we can pass a Java code as function pointer to native function
    * FFM API uses `MethodHandle` to interoperate between Java code and native function
    * steps need to call a foreign function:
      * find the address of a given symbol in a loaded native library
      * link the Java code to a foreign function
    * Symbol lookup:
      * we can use `SymbolLookup` to lookup a function address:
        * `SymbolLookup::libraryLookup(String, Arena)`: creates a library lookup, loads the libreary and associates with the given Arena object;
        * `SymbolLookup::loaderLookup()`: creates a loader lookup that locates all the symbols in all the native libraries that have been loaded by classes in the current class loader (`System::loadLibrary` and `System::load`);
        * `Linker::defaultLookup()`: creates a default lookup that locates all the symbols that are commonly used in the native platform (OS).
      * we can use `SymbolLookup::find(String)` to find a function by its name
        * if it is present then a memory segment, which points to funtion's entry point, is returned
    * Linking Java code to foreign function:
      * interface `Linker` is the main point to allow Java code to interoperate with native code
      * calls:
        * downcall: call from Java code to native code;
        * upcall: call from native code back to Java code (linker a function pointer).
      * the native linker conforms to the Application Binary Interface (BNI) of the native platform
        * ABNI specifies the calling convension, the size, alignment, endianness of scalar types and others details
        * `Linker linker = Linker.nativeLinker()`
      * downcall:
        * we use `Linker::downcallHandle` to link Java code to a foreign function
        * `Linker::downcallHandle(MemorySegment, FunctionDescriptor)` returns a `MethodHandle` to be used to invoke the native function
          * we must provide a `FunctionDescriptor` to define the native function signature (return type and the parameters types)
            * we can create one using `FunctionDescriptor.of` or `FunctionDescriptor.ofVoid` passing the memory layout to define the signature
            * developer must be aware of the current native platform that will be used (size of scalar types used in C functions)
              * eg.: on Linux and macOS, a long is JAVA_LONG; on Windows, a long is JAVA_INT
            * we can use `Linker::canonicalLayouts()` to see the association between scalar C types and Java's ValueLayout
          * JVM guarantee that the functions arguments used in `MethodHandle` will match the `FunctionDescriptor` used to downcall the function
          * this way our types will be verified in the Java level
        * if the native function returns a by-value struct, we must provide an additional `SegmentAllocator` argument that will be used to allocate the memory to hold the struct returned
        * if the native function allocates a memory and returns a pointer to it, a zero-length memory segment is return to Java code
          * because the JVM cannot guarantee the allocated memory size off-heap
          * the client must call `MemorySegment::reinterpret` to tell the JVM the memory's size (the user may not know)
          * this is an unsafe operation (could crash the JVM or leave the memory in a corruption stat)
      * upcall:
        * we can pass Java code as a function pointer to be called by the foreign function
        * we use `Linker::upcallStub` to "externalize" a Java code
          * we must provide a `MethodHandle` that points to the Java method, a `FunctionDescriptor` with the method signature
* **Unnamed Variables and Patterns**
  * promotion to standard
  * no change from JDK 2
* **Class-File API**
  * provide standard API for parsing, generating and transforming Java class file
* **Launch Multi-File Source-Code Programs**
  * enhance the Java launcher's source-file mode to be able to run a program made by multiple Java files
  * the launcher will compile the given Java file and any other Java file that is referenced by the program
  * the referenced class will only be compiled in memory when the class is used
    * any compiler error in the referenced class will be thrown after the program started the execution
  * we can also used pre-compiled classes or module path:
    * `java --class-path '*' MyProgram.java`
    * `java -p . MyProgram.java`
  * limitations:
    * annotation processing is disabled (`--proc:none`)
    * is not possible to run a source-code program whose Java files span multiple modules
* **String Templates**
  * minor change from JDK 21
  * changed the type of template expressions
* **Stream Gatherers**
  * enhance the Stream API to support custom intermediate operations
  * will allow stream pipelines to transform data more easily than the existing built-in intermediate operations
  * some built-in intermediate operations: mapping, filtering, reduction, sorting
  * the goal is to provide an extension point (like the one implemented in `Stream::collect(Collector)`)
  * [`Stream::gather(Gatherer)`](https://cr.openjdk.org/~vklang/gatherers/api/java.base/java/util/stream/Gatherer.html) is an intermediate stream operation 
    * it processes the elements of a stream by applying a user-defined entity called a gatherer
    * a gatherer represents a transform of the elements of a stream
    * can transform elements: one-to-one, one-to-many- many-to-one, many-to-many
    * it can keep track previously seen elements in order to compute some transformation of later elements
    * a gatherer will only be evaluated in parallel if it provides a combiner function
  * gatherer is defined by four functions:
    * initializer (optional): an object that maintains private state while processing the stream, the type is `Supplier`.
    * integrator: integrates a new element from the input stream, also can inspect the private state object, emit elements to the output stream, terminate the processing (by returning false) and so on.
    * combiner (optional): used to evaluate the gatherer in parallel or sequentially (when the operation cannot be parallelized).
    * finisher: invoked when there are no more input elements to consume, can inspect private state object, emit additional output elements.
  * `Stream::gather` performs the equivalent of the following steps:
    * create a `java.util.stream.Gatherer.Downstream` object passes the result (object of gatherer's output type) to the next stage in the pipeline;
    * obtain the gatherer's private state object from initializer method `get()`;
    * obtain the gatherer's integrator to process the stream by invoking the method `integrator()`;
    * while there are more inputs elements, invoke the integrator method `integrate` passing state object, next element and downstream object, terminate if returned false;
    * obtain the gatherer's finisher and invoke it passing the state and downstream object.
  * there are built-in gatherers provided in [`java.util.stream.Gatherers`](https://cr.openjdk.org/~vklang/gatherers/api/java.base/java/util/stream/Gatherers.html):
    * `fold`: stateful many-to-one gatherer;
    * `mapConcurrent`: stateful one-to-one gatherer which invokes a supplied function for each element concurrently;
    * `scan`: stateful one-to-one gatherer which applies a supplied function to the current state and the current element to produce the next element to downstream;
    * `windowFixed`: stateful many-to-many gatherer which groups elements into lists of a supplied size and emit the window to downstream;
    * `windowSliding`: like the `windowFixed` but applying sliding in the stream elements (drop the first element from the previous window and added the current elemenet).
    * `peek`: stateless one-to-one gatherer which applies a function to each element in the stream;
  * is possible to composing gatherers with `andThen(Gatherer)`:
    * `stream.gather(a).gather(b).collect(toList())` is equivalent to `stream.gather(a.andThen(b)).collect(toList())`
* **Structured Concurrency**
  * no change from JDK 20/21
  * re-preview for additional feedback
* **Implicity Declared Classes and Instance Main Methods**
  * minor change from JDK 21
  * changed the concept name from unnamed class to implicitly declared class
    * "source file without an enclosing class declaration is said to implicitly declare a class with a name chosen by the host system"
  * changed the procedure for selecting a main method to invoke
    * first it looks for a method `main(String[])`, if not found then it looks for a method `main()`
* **Scoped Values**
  * no change from JDK 20/21
  * re-preview for additional feedback


## Links

* [JDK 22 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21900)
* [Gathering the Streams](https://cr.openjdk.org/~vklang/Gatherers.html)
* Presentations:
  * [Foreign Function & Memory API - A (Quick) Peek Under the Hood](https://www.youtube.com/watch?v=iwmVbeiA42E)

