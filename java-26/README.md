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

* **Ahead-of-Time Object Caching with Any GC**
    * enhance the AOT cache (improves startup and warmup time) to be used with any GC
    * this is done by making possibl to load cached objects sequentially into memory
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

## Links

* [JDK 26 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=23506)
* [JDK 26 JEPs](https://openjdk.org/projects/jdk/26/)

