# Java 28

To run each example use: `java --enable-preview --source 28 <FileName.java>`

## JEPs

* [401](https://openjdk.org/jeps/401) - Value Objects (Preview)
* [535](https://openjdk.org/jeps/535) - Shenandoah GC: Generational Mode by Default
* [539](https://openjdk.org/jeps/539) - Strict Field Initialization in the JVM (Preview)
* [540](https://openjdk.org/jeps/540) - Simple JSON API (Incubator)
* [541](https://openjdk.org/jeps/541) - Deprecate the macOS/x64 Port for Removal
* [542](https://openjdk.org/jeps/542) - PEM Encodings of Cryptographic Objects

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

## Links

* [JDK 28 JEPs](https://openjdk.org/projects/jdk/28/)
* [JDK 28 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=25101)

