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
* **Unnamed Variables and Patterns**
  * promotion to standard
  * no change from JDK 21
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
* **Structured Concurrency**
  * no change from JDK 20/21
  * re-preview for additional feedback
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
* **Implicity Declared Classes and Instance Main Methods**
  * minor change from JDK 21
  * changed the concept name from unnamed class to implicity declared class
    * "source file without an enclosing class declaration is said to implicitly declare a class with a name chosen by the host system"
  * changed the procedure for selecting a main method to invoke
    * first it looks for a method `main(String[])`, if not found then it looks for a method `main()`
* **Scoped Values**
  * no change from JDK 20/21
  * re-preview for additional feedback


## Links

* [JDK 22 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21900)
* [Gathering the Streams](https://cr.openjdk.org/~vklang/Gatherers.html)

