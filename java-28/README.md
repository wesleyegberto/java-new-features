# Java 28

To run each example use: `java --enable-preview --source 28 <FileName.java>`

## JEPs

* [401](https://openjdk.org/jeps/401) - Value Objects (Preview)
* [535](https://openjdk.org/jeps/535) - Shenandoah GC: Generational Mode by Default
* [539](https://openjdk.org/jeps/539) - Strict Field Initialization in the JVM (Preview)
* [540](https://openjdk.org/jeps/540) - Simple JSON API (Incubator)
* [541](https://openjdk.org/jeps/541) - Deprecate the macOS/x64 Port for Removal
* [542](https://openjdk.org/jeps/542) - PEM Encodings of Cryptographic Objects
* [543](https://openjdk.org/jeps/543) - Structured Concurrency
* [544](https://openjdk.org/jeps/544) - Ahead-of-Time Code Compilation
* [545](https://openjdk.org/jeps/545) - Faster Startup and Warmup with ZGC
* [546](https://openjdk.org/jeps/546) - Adaptive Heap Sizing for ZGC

## Features

* **Shenandoah GC: Generational Mode by Default**
  * Shenandoah is a low-pause-time garbage collector
  * switch the default mode of Shenandoah GC to the generational mode
  * deprecate the non-generational mode
  * default mode: `XX:+UseShenandoahGC -XX:ShenandoahGCMode=generational`
  * to use non-generational mode: `-XX:+UseShenandoahGC -XX:ShenandoahGCMode=satb`
* **Simple JSON API**
  * a simple standard API for parsing and generating JSON documents that conform to [RFC 8259](https://www.rfc-editor.org/info/rfc8259/)
  * this JEP replaces the [JEP 198 - Light-Weight JSON API](https://openjdk.org/jeps/198)
  * the API is organized around the sealed interface `JsonValue` which has the following sub-interfaces: `JsonString`, `JsonNumber`, `JsonBoolean`, `JsonNull`, `JsonObject` and `JsonArray`
  * the [`Json.parse`](https://cr.openjdk.org/~naoto/json/javadoc/api/jdk.incubator.json/jdk/incubator/json/Json.html#parse(java.lang.String)) method returns a tree of JsonValue instances that expose the names, types, and values of the parsed JSON data
  * generating JSON text:
    * [`Json.toString`](https://cr.openjdk.org/~naoto/json/javadoc/api/jdk.incubator.json/jdk/incubator/json/JsonValue.html#toString()): returns the JSON string converted from the tree of objects:
    * [`Json.toDisplayString(JsonValue, String)`](https://cr.openjdk.org/~naoto/json/javadoc/api/jdk.incubator.json/jdk/incubator/json/Json.html#toDisplayString(jdk.incubator.json.JsonValue,java.lang.String)): returns the formatted JSON string for better readability, the string parameter is the indentation
  * navigating JSON documents:
    * `JsonValue` methods to read a value:
      * `get(String)`: obtains the value of an object member
      * `get(int)`: obtains an array element
      * `tryGet(String)`: obtains an optional value of an object member
    * it is recommended to use type patterns to handle dynamic structure
    * if the `JsonValue` instance is of the wrong type or the requested member or element does not exist a `JsonValueException` will be thrown
  * converting values to Java types:
    * `JsonValue` methods to convert a value:
      * `asString`
      * `asInt`
      * `asLong()`
      * `asDouble()`
      * `asBoolean()`
      * `asMap()`
      * `asList()`
    * the methods are implemented by the `JsonValue` sub-interfaces (`JsonString`, `JsonNumber`)
    * there is no conversion method for null value, we should check with `instanceof JsonNull` or use [`tryValue`](https://cr.openjdk.org/~naoto/json/javadoc/api/jdk.incubator.json/jdk/incubator/json/JsonValue.html#tryValue()) that returns an `Optional`
    * if a `JsonValue` is not an instance of the appropriate subtype for a conversion method then the method throws a JsonValueException
    * numeric conversions can fail for reasons such as the numeric value not being representable in the target Java numeric type, which also causes a `JsonValueException` to be thrown
* **PEM Encodings of Cryptographic Objects**
    * promotion to standard
    * "introduce an API for encoding objects that represent cryptographic keys, certificates, and certificate revocation lists into the widely-used Privacy-Enhanced Mail (PEM) transport format"
    * support conversions between PEM text and cryptographic objects that have standard representations in the binary formats of PKCS#8 (for private keys), X.509 (public keys, certificates, and certificate revocation lists), and PKCS#8 v2.0 (encrypted private keys and asymmetric keys)
* **Structured Concurrency**
  * promoted to standard without changes
* **Ahead-of-Time Code Compilation**
  * improve startup and warmup time by making optimized native code for an application instantly available when the HotSpot Java Virtual Machine starts
  * goals:
    * enable applications to achieve peak performance more quickly
    * enable applications to sustain peak even as workloads change
    * do not require any change in the application code
    * do not require any change to the configuration of HotSpot beyond the use of AOT cache
    * ensure that shifting from AOT-compiled code to JIT-compiled code is invisible to applications
  * the application code is compiled to native code during training run and stored in the AOT cache
  * the optimized native code stored in AOT cache during training run is called AOT code
  * during a production run, a request for optimized code for a method can be fulfilled instantly if matching AOT code is found in the cache
  * if AOT code is unavailable, incompatible, otherwise unsuitable, or later deoptimized, execution falls back to the existing interpreter and JIT mechanisms
    * regenerate native code dynamically for continued peak performance, providing the best of both ahead-of-time (AOT) and just-in-time (JIT) compilation
  * previous JEP that enabled this:
    * JEP 483: shifting class loading and linking work earlier during training run
    * JEP 515: profiling work earlier during training run
  * this JEP enables the shifting of compilation and optimization work earlier during training run
  * result with all this JEPs:
    * the AOT cache without AOT code improves startup time by about 30%; adding AOT code brings an additional 45% improvement, for a total of about 75%
  * recommendations is the same from JEP 483:
    * try to do a training run with the application as close as possible to production run
    * if external resources cannot be used during the training run, we could mock or disable them (it will be loaded just-in-time)
    * we could use a smoke test to run enough scenarios used by the application, some testing classes will be cached (in the future a filter will be provided)
    * we should avoid using complete large test suites (unit tests, corner cases, so on) because it will increase the size of the cache with useless classes (scenarios with fewer usage)
    * the training run should do similar things as production run, otherwise the cache will be less useful
  * this JEP adds two constraints to use the AOT code:
    * all runs should use CPUs of the same architecture and with the same features
    * all runs should use the same GC, since AOT code contains GC-specific read/write barriers
  * steps (same as before):
    * to create AOT cache (cache will contain pre-linked classes, profiling data and AOT code):
      * `java -XX:AOTCacheOutput=app.aot -cp app.jar com.example.App`
    * to use the AOT cache:
      * `java -XX:AOTCache=app.aot -cp app.jar com.example.App`
  * options:
    * `-XX:+PrintCompilation`: reports AOT code loading and JIT compilation
    * `-XX:AOTMode=required`: requires the JVM to use the full AOT cache, failing if cache violates any constraint
    * `-XX:-AOTCodeCaching`: disable the AOT cache
  * we can use the environment variable `JDK_AOT_VM_OPTIONS` to pass options

## Links

* [JDK 28 JEPs](https://openjdk.org/projects/jdk/28/)
* [JDK 28 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=25101)

