# Java 26

To run each example use: `java --enable-preview --source 26 <FileName.java>`

## JEPs

* [500](https://openjdk.org/jeps/500) - Prepare to Make Final Mean Final
* [504](https://openjdk.org/jeps/504) - Remove the Applet API
* [516](https://openjdk.org/jeps/516) - Ahead-of-Time Object Caching with Any GC
* [517](https://openjdk.org/jeps/517) - HTTP/3 for the HTTP Client API
* [522](https://openjdk.org/jeps/522) - G1 GC: Improve Throughput by Reducing Synchronization
* [524](https://openjdk.org/jeps/524) - PEM Encodings of Cryptographic Objects (Second Preview)
* [525](https://openjdk.org/jeps/525) - Structured Concurrency (Sixth Preview)
* [526](https://openjdk.org/jeps/526) - Lazy Constants (Second Preview)
* [529](https://openjdk.org/jeps/529) - Vector API (Eleventh Incubator)
* [530](https://openjdk.org/jeps/530) - Primitive Types in Patterns, instanceof, and switch (Fourth Preview)

## Features

* **Prepare to Make Final Mean Final**
    * issue warnings about use of deep reflection to mutate final fields
    * prepare developers for a future release that ensures integrity by default by restricting final field mutation
    * goals:
        * prepare Java ecosystem for the future where, by default, disallows the mutation of final fields by deep reflection
        * align final fields of classes with the implicity declared fields of record classes, which cannot be mutated
        * allow serialization to continue working with Serializable classes, even classes with final fields
    * the mutation of final fields by deep reflection was introduced in JDK 5 to allow serialization to initialize the objects with final fields
        * `Field.setAccessible(boolean)`
    * deep reflection is disallowed in hidden and record classes
    * the JVM cannot consider a final field as really final, thus optimizations are not applied
    * by default, JVM will print a warning every time that a final field is mutated
        * in the future, an exception will be thrown
    * enabling final field mutation
        * to avoid the warning, and the exception in the future, we can pass inform the JVM with a new flag `enable-final-field-mutation`
            * we can enable all modules or a specific module
            * `java --enable-final-field-mutation=ALL-UNNAMED`
            * `java --enable-final-field-mutation=M1,M2`
        * to allow the mutation, we must enable with the flag and the module enabled also must be open to deep reflection
        * we have different options to pass this new flag:
            * passing the flag in the env `JDK_JAVA_OPTIONS`
            * passing the flag in the argument file
            * adding `Enable-Final-Field-Mutation` with value `ALL-UNNAMED` (only accepted option) in the manifest of a JAR file
            * passing the flag to jlink via the `--add-options` option
            * in JNI, passing the flag to JVM creation
    * final field mutation restrictions
        * it's illegal to mutate a final field if either:
            * the code is in a module for which final field mutation is not enabled
            * the code is in a module to which the field's package is not open
        * the behavior of the JVM when an illegal final field mutation occurs is controled by a new flag `illegal-final-field-mutation`
        * the options are `allow`, `warn`, `debug` and `deny`
        * in JDK 26 the default option is `warn`, in the future will be `deny`
    * a new event in JDK Flight Recorder was added: `jdk.FinalFieldMutation`
    * serialization libraries should use `sun.reflect.ReflectionFactory`
        * it initialize the class instance bypassing the constructor and setting its fields directly without having to mutate it
        * this is the only mechanism to mutate final field if final field mutation is not enabled
        * it adds a restriction because the target class need to implements `java.io.Serializable` interface
        * the JVM will treat the final field as immutable if it detects that a handle returned by `ReflectionFactory` is not used to mutate a final field
* **Ahead-of-Time Object Caching with Any GC**
    * enhance the AOT cache (improves startup and warmup time) to be used with any GC
    * this is done by making possible to load cached objects sequentially into memory
        * using a GC-agnostic format
        * previously the objects were mapped into memory using GC-specific format (bitwise heap format)
    * main goal is to allow all GC to work with AOT cache introduced by Project Leyden
        * this step will be optional though
    * the challenge is how to handle object references, because it depends on each GC and its policies
    * to achieve this the AOT cache will use logical indices in a streaming format to store the objects references
    * we can use the parameter `-XX:+AOTStreamableObjects` to create a cache in a GC-agnostic format
* **HTTP/3 for the HTTP Client API**
    * added support for HTTP/3 in the HTTP Client API
    * HTTP/3 was standardized in 2022
    * setting version in HttpClient builder:
        * ```java
          var client = HttpClient.newBuilder()
              .version(HttpClient.Version.HTTP_3)
              .build();
          ```
    * setting version in HttpRequest builder:
        * ```java
          var request = HttpRequest.newBuilder(URI.create("https://example.com"))
              .version(HttpClient.Version.HTTP_3)
              .GET()
              .build();
          ```
* **G1 GC: Improve Throughput by Reducing Synchronization**
    * increase application throughput when using G1 GC by reducing the amount of synchronization required between application threads and GC threads
    * goals:
        * reduce the G1 GC's synchronization overhead
        * reduce the size of the injected code for G1's write barriers
        * maintain the overall architecture of G1
    * G1 will use a second card table so that optimizer and application threads can run freely
    * the change will reduce overhead both while the application is running and during GC pauses
* **Structured Concurrency**
    * re-preview with minor changes
    * changes:
        * a new method `Joiner.onTimeout` to allow implementation to return a result when a timeout expires
        * `Joiner.allSuccessfulOrThrow` now returns a list of results rather than a stream of subtasks
        * `Joiner.anySuccessfulResultOrThrow` was renamed to `anySuccessfulOrThrow`
        * static method [`StructuredTaskScope.open`](https://cr.openjdk.org/~alanb/sc-jdk26/api/java.base/java/util/concurrent/StructuredTaskScope.html#open(java.util.concurrent.StructuredTaskScope.Joiner,java.util.function.UnaryOperator)) that takes a Joiner and a function to modify the default configuration now takes a `UnaryOperator` instead of a `Function`
* **Lazy Constants**
    * re-preview with changes
    * Stable Values was renamed to Lazy Constants
    * changes from first preview:
        * changed the API to focus on high-level use cases
        * removed low-level methods like `orElseSet`, `setOrThrow`, `trySet`, `function` and `intFunction`
        * moved the factory methods for lazy lists and maps to `List` and `Map` interfaces
        * disallow null as a computed value to improve performance and align with constructs (unmodifiable collections, `ScopedValue`)
            * we must use `Optional`
    * now we always must create a lazy constant using `of(Supplier)`, the previous unset state isn't allowed
    * `get()` will trigger the lazy constant initialization using the given supplier in factory method `of(Supplier)`
        * after calling the state will be initialized (`isInitialized()` will return true)
    * the lazy constant can be in an uninitialized state, when the method `get()` wasn't called yet
        * `orElse(T)` will return the given value in this case
* **Primitive Types in Patterns, instanceof and switch**
    * re-preview with two changes
    * enhance the definition of [unconditional exactness](https://openjdk.org/jeps/530#Safety-of-conversions)
    * apply tighter dominance check in switch constructs by expanding the definition of [dominance](https://openjdk.org/jeps/530#Dominance) to also cover primitive type patterns
        * if a case label is `float f`, we cannot have a case label `42` because 42 will be captured by the first match

### API

- removed `Thread.stop` method
- `java.lang.Process` now implements `AutoCloseable` and `Closeable`

## Links

* [JDK 26 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=23506)
* [JDK 26 JEPs](https://openjdk.org/projects/jdk/26/)

