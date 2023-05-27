# Java 21

To run each example use: `java --enable-preview --source 21 <FileName.java>`

## JEPs

* [404](https://openjdk.java.net/jeps/404) - Generational Shenandoah (Experimental)
* [430](https://openjdk.java.net/jeps/430) - String Templates (Preview)
* [431](https://openjdk.java.net/jeps/431) - Sequenced Collections
* [439](https://openjdk.java.net/jeps/439) - Generational ZGC
* [440](https://openjdk.java.net/jeps/440) - Record Patterns
* [441](https://openjdk.java.net/jeps/441) - Pattern Matching for switch
* [442](https://openjdk.org/jeps/442) - Foreign Function & Memory API (Third Preview)
* [443](https://openjdk.org/jeps/443) - Unnamed Patterns and Variables (Preview)
* [444](https://openjdk.org/jeps/444) - Virtual Threads
* [445](https://openjdk.org/jeps/445) - Unnamed Classes and Instance Main Methods (Preview)
* [448](https://openjdk.org/jeps/448) - Vector API (Sixth Incubator)
* [449](https://openjdk.org/jeps/449) - Deprecate the Windows 32-bit x86 Port for Removal

## Features

* Virtual threads
  * changed to make virtual threads always support thread-local
    * in preview releases was possible to create a virtual thread without thread-local support
  * flag `jdk.traceVirtualThreadLocals` to show the strack trace when a virtual threads sets a value in thread-local variable
* Record patterns
  * the main change is remove the support for record pattern in header of an enhanced for loop
* Pattern matching for `switch`
  * main changes from last JEPs:
    * removed parenthesized patterns (`case int i when (i > 0)`)
    * allow qualified enum constants as case constants
    * exhaustiveness and compatibility:
      * compiler will only require exhaustiveness if the switch uses any pattern, null label or if selector expression isn't from a legacy type (char, byte, short, int, Character, Byte, Short, Integer, String or a enum)
* String templates:
  * improve the string with embedded expressions and template processors
  * goals:
    * simply the creation and expression of string with computed computed
    * improve the security to work with strings provided by users with validations and transformations
    * enable the cration of non-string values computed from literal text and embedded expressions without having to use a intermediate string representation
  * string templates introduces the string interpolatins in Java and also provides a mechanism to validate/transform the string to avoid security issues
  * template expressions are a new kind of expressions that performs string interpolation and also provides a way to programmatically validate or transform the interpolation result
    * `String greetings = STR."Hello \{name}"`
    * template expression is composed by:
      * a template processor (`STR`)
      * dot character (`.`)
      * template (`"My name is \{name}"`) which contains an embedded expresion (`\{name}`)
    * the template literal can be defined with single or multi-line (using `"""` like text blocks)
      ```
      STR."""
      Hello from multi-line template expression.
      """
      ```
    * the template expressions is evaluated at run time, the template processor combines the literal text with the values of the embedded expressions to produce a result
    * the result of the template processor is often a string, but it could be transform to any other value
  * embedded expression
    * an embedded expression can perform any Java expression (access variable, arithmetic operation, call method and fields, another template expression)
    * the embedded expressions are evaluated from left to right
    * double-quote character doesn't need to be espaced inside an embedded expression
      * `STR."The file exists: \{file.exists() ? "yes" : "no"}"`
    * the embedded expression can be spread over multiples lines without introducing newlines
      * the expression is interpolated in the same line from `\` character
      ```java
      String time = STR."The time is \{
        DateTimeFormatter
          .ofPattern("HH:mm:ss")
          .format(LocalTime.now())
      } right now"
      ```
  * `STR` template processor
    * `STR` is a template processor provided by Java Platform, it performs the interpolation by replacing the embedded expresions with the stringified value othe expression
    * `STR` is a public static final field that is automatically imported into every Java source file
* Sequenced Collections
  * new interfaces to define a common way to iterate throught sequenced collections (list, sets and maps)
  * [collections type hierarchy with new interfaces](https://cr.openjdk.org/~smarks/collections/SequencedCollectionDiagram20220216.png)
  * sequenced collection:
    * a sequenced collection is a collection whose elements have a defined encounter order
    * sequenced collection has a first and last elements, and the elements between them have successors and predecessors
    * it supports common operations at the beginning and the end of the collection, also allow iterate it forward or backward 
    * methods from `SequencedCollection<E>` interface:
      * `reversed`
      * `addFirst`
      * `addLast`
      * `getFirst`
      * `getLast`
      * `removeFirst`
      * `removeLast`
  * sequenced set:
    * sequenced set is a set that is sequenced collcetion that do not allow duplicate elements
    * methods from `SequencedSet<E>` interface:
      * `reversed`
  * sequenced map:
    * sequenced map is a map whose entries have a defined encounter order
    * methods from `SequencedMap<K,V>` interface:
      * `reversed`
      * `sequencedKeySet`
      * `sequencedValues`
      * `sequencedEntrySet`
      * `putFirst`
      * `putLast`
      * `firstEntry`
      * `lastEntry`
      * `pollFirstEntry`
      * `pollLastEntry`
  * new methods in `Collections`:
    * `unmodifiableSequencedCollection`
    * `unmodifiableSequencedSet`
    * `unmodifiableSequencedMap`
* Unnamed classes and instance main methods
  * facilitate the writing of first programm for students without needing to know another features designed for large programs
  * unnamed class:
    * any method declared in a source file without an enclosed class will be considered to be member of an unnamed top-level class
    * the compiler requires an unnamed method to have a valid main method to be launched
    * unnamed class is always final and cannot implement or extends any class other than `Object`
      * is equivalent to the following usage of anonymous class declaration:
      ```java
      new Object() {
          void main() {}
      }.main();
      ```
    * as it doesn't have a name, so we can use its static methods like `ClassName.staticMethodName()`
    * it still will have the impliticy `this` reference
    * it can have:
      * instance and static methods
      * instance and static fields
      * instance and static initializers
      * the default modifers are the same (package access and instance membership)
    * the main difference is will only have an impliticy default zero-paramater constructor
    * `Class.isSynthetic` method returns true
  * this JEPs introduces changes to how Java programs are launched:
    * instance main method:
      * the class must have a non-private zero-paramater constructor
      * the main method doesn't need to be static, public nor have parameter
      * example:
      ```java
      class HelloWord {
          void main() {
              System.out.println("Hello World!");
          }
      }
      ```
    * allowing unnamed class:
    ```java
    void main() {
        System.out.println("Hello World!");
    }
    ```
  * order to select a main method (all must be non-private and it prioritize the methods in current class before the superclass):
    * `static void main(String[])`
    * `static void main()`
    * `void main(String[])`
    * `void main()`
* APIs:
  * improve `Thread.sleep(millis, nanos)` to actually perform sleep with sub-millisecond time
  * [`java.net.http.Http Client` is now `AutoCloseable`](https://jdk.java.net/21/release-notes#JDK-8267140) and new methods were added to close/shutdown the connection pool.


## Links

* [JDK 21 Jeps](https://openjdk.org/projects/jdk/21/)
* [JDK 21 Early Access Docs](https://download.java.net/java/early_access/jdk21/docs/api/)
* [JEP 444: Virtual Threads Arrive in JDK 21, Ushering a New Era of Concurrency](https://www.infoq.com/news/2023/04/virtual-threads-arrives-jdk21/)
* [Java Virtual Threads - Oracle DevLive Level Up](https://www.youtube.com/watch?v=MOgynY7VIJI)

