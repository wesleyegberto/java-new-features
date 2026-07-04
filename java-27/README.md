# Java 27

To run each example use: `java --enable-preview --source 27 <FileName.java>`

## JEPs

* [523](https://openjdk.org/jeps/523) - Make G1 the Default Garbage Collector in All Environments
* [527](https://openjdk.org/jeps/527) - Post-Quantum Hybrid Key Exchange for TLS 1.3
* [531](https://openjdk.org/jeps/531) - Lazy Constants (Third Preview)
* [532](https://openjdk.org/jeps/532) - Primitive Types in Patterns, instanceof, and switch (Fifth Preview)
* [533](https://openjdk.org/jeps/533) - Structured Concurrency (Seventh Preview)
* [534](https://openjdk.org/jeps/534) - Compact Object Headers by Default
* [536](https://openjdk.org/jeps/536) - JFR In-Process Data Redaction
* [537](https://openjdk.org/jeps/537) - Vector API (Twelfth Incubator)
* [538](https://openjdk.org/jeps/538) - PEM Encodings of Cryptographic Objects

## Features

* **Make G1 the Default Garbage Collector in All Environments**
    * HotSpot will use G1 by default
    * G1 will be default GC in all environments when no GC is specified
    * in some scenarios, Serial GC could be selected depending on memory and CPU
        * single CPU or memory less than 1792 MB
* **Lazy Constants**
    * re-preview with minor changes
    * changes:
        * removed low-level methods `isInitialized` and `orElse`
        * added new factory method `Set.ofLazy`
* **Primitive Types in Patterns, instanceof and switch**
    * re-preview without change
* **Structured Concurrency**
    * re-preview with minor changes
    * changes:
        * added a parameter type in `StructuredTaskScope` and `Joiner` to define the exception that can be thrown by [`join()`](https://cr.openjdk.org/~alanb/sc-jdk27/api/java.base/java/util/concurrent/StructuredTaskScope.html#join())
        * new static [`open`](https://cr.openjdk.org/~alanb/sc-jdk27/api/java.base/java/util/concurrent/StructuredTaskScope.html#open(java.util.function.UnaryOperator)) method in StructuredTaskScope implements the default join policy and uses a given UnaryOperator to produce the StructuredTaskScope configuration
        * `Joiner` factory methods `allSucessfulOrThrow`, `anySucessfullOrThrow` and `awaitAllSucessfulOrThrow` returns a joiner that throws `ExecutionException` when the outcome is an exception
        * removed method `Joiner.awaitAll`
        * method `onTimeout` from `Joiner` has been replaced by [`timeout`](https://cr.openjdk.org/~alanb/sc-jdk27/api/java.base/java/util/concurrent/StructuredTaskScope.Joiner.html#timeout())
* **Compact Object Headers by Default**
    * compact object headers will be default object header layout in the HotSpot JVM
    * "reduce object headers from 96 bits down to 64 bits on 64-bit architectures, thereby reducing heap size, improving deployment density, and increasing data locality"
    * to disabled: `java -XX:-UseCompactObjectHeaders ...`
* **PEM Encodings of Cryptographic Objects**
    * promotion to standard
    * "introduce an API for encoding objects that represent cryptographic keys, certificates, and certificate revocation lists into the widely-used Privacy-Enhanced Mail (PEM) transport format"
    * support conversions between PEM text and cryptographic objects that have standard representations in the binary formats of PKCS#8 (for private keys), X.509 (public keys, certificates, and certificate revocation lists), and PKCS#8 v2.0 (encrypted private keys and asymmetric keys)

## Links

* [JDK 27 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=24503)
* [JDK 27 JEPs](https://openjdk.org/projects/jdk/27/)

