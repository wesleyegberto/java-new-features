# Java 21

To run each example use: `java --enable-preview --source 21 <FileName.java>`

## JEPs

* [430](https://openjdk.org/jeps/430) - String Templates (Preview)
* [431](https://openjdk.org/jeps/431) - Sequenced Collections
* [439](https://openjdk.org/jeps/439) - Generational ZGC
* [440](https://openjdk.org/jeps/440) - Record Patterns
* [441](https://openjdk.org/jeps/441) - Pattern Matching for switch
* [442](https://openjdk.org/jeps/442) - Foreign Function & Memory API (Third Preview)
* [443](https://openjdk.org/jeps/443) - Unnamed Patterns and Variables (Preview)
* [444](https://openjdk.org/jeps/444) - Virtual Threads
* [445](https://openjdk.org/jeps/445) - Unnamed Classes and Instance Main Methods (Preview)
* [446](https://openjdk.org/jeps/446) - Scoped Values (Preview)
* [448](https://openjdk.org/jeps/448) - Vector API (Sixth Incubator)
* [449](https://openjdk.org/jeps/449) - Deprecate the Windows 32-bit x86 Port for Removal
* [451](https://openjdk.org/jeps/451) - Prepare to Disallow the Dynamic Loading of Agents
* [452](https://openjdk.org/jeps/452) - Key Encapsulation Mechanism API
* [453](https://openjdk.org/jeps/453) - Structured Concurrency (Preview)

## Features

* **Virtual Threads**
  * [notes about Virtual Threads](../java-19/README.md)
  * promotion to standard
  * changed to make virtual threads always support thread-local
    * in preview releases was possible to create a virtual thread without thread-local support
  * flag `jdk.traceVirtualThreadLocals` to show the strack trace when a virtual threads sets a value in thread-local variable
* **Scoped Values**
  * promoted from incubated to preview feature
  * minor change from JDK 19
    * moved package from `jdk.incubator.concurrent` to `java.util.concurrent`
* **Structured Concurrency**
  * promoted from incubated to preview feature
  * moved from pacote `jdk.incubator.concurrent` to `java.util.concurrent`
  * changes from JDK 19 and 20:
    * changed method `StructuredTaskScope::fork` to return a `Subtask` instanceof of a `Future`
    * the sealed interface `Subtask` extends `Supplier<T>`
    * the method `Subtask::get` behaves exaclty like `Future::resultNow`
    * calling the method `Subtask::get` never blocks, it throws an `IllegalStateException` when calling before `join` or when the subtask has not completed successfully
* **Record Patterns**
  * promotion to standard
  * the main change is remove the support for record pattern in header of an enhanced for loop
* **Pattern Matching for `switch`**
  * promotion to standard
  * main changes from last JEPs:
    * removed parenthesized patterns (`case int i when (i > 0)`)
    * allow qualified enum constants as case constants (`case MyEnum.ITEM`)
    * exhaustiveness and compatibility:
      * compiler will only require exhaustiveness if the `switch` uses any pattern, `null` label or if selector expression isn't from a legacy type (`char`, `byte`, `short`, `int`, `Character`, `Byte`, `Short`, `Integer`, `String` or a enum)
  * improved switch to support pattern matching for types (like `instanceof`)
  * support for `null` case
  * support for guards where we can use a boolean expression (`case String s when s.length > 10`)
  * scope for pattern variable:
    * the scope of pattern variable in visible only in the guard clause and the case body (expression, block or throw)
    * fall through:
      * `switch` with type pattern doesn't support falling through
      * if using case label with `:`, we must end the block with `break` or `yield` (`case String s: ...; break;`, `case String s: ...; yield s.length();`)
* **String Templates**
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
      ```java
      STR."""
      Hello \{name} from multi-line template expression.
      """
      ```
    * the template expressions is evaluated at run time, the template processor combines the literal text with the values of the embedded expressions to produce a result
    * the result of the template processor is often a string, but it could be transform to any other value
  * embedded expression
    * an embedded expression can perform any Java expression (access variable, arithmetic operation, call method and fields, another template expression)
    * the embedded expressions are evaluated from left to right
    * double-quote character doesn't need to be escaped inside an embedded expression
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
  * template processors:
    * `STR` template processor:
      * `STR` is a template processor provided by Java Platform, it performs the interpolation by replacing the embedded expressions with the stringified value of the expression
      * `STR` is a public static final field `StringTemplate.STR` that is automatically imported into every Java source file
    * `FMT` template processor:
      * `FMT` is another template processor provided by Java Platform, it behaves like `STR` but allows value formatting like those in `Formatter`
      * is a static final field of type `java.util.FormatProcessor` and must be explicity referenced with `java.util.FormatProcessor.FMT`
      * it uses the default locale
        * we can create a custom template processor with another locale:
        ```java
        Locale locale = Locale.forLanguageTag("pt-BR"):
        FormatProcessor FMT_BR = FormatProcessor.create(locale);
        FMT_BR."Total: R$ %1.2f{42.42}";
        ```
      * ex.:
      ```java
      StringTemplate.Processor.FMT."Total price: $%5.2f\{120.50}";
      ```
    * `RAW` template processor:
      * is the standard template processor thar produces a unprocessed `StringTemplate` object
      * must be explicity referenced with `StringTemplate.RAW.""`
      * the `STR` template processor is just a lambda wrapper that calls `RAW` and return the `StringTemplate` processed
        * ex.:
        ```java
        StringTemplate st = StringTemplate.RAW."My name is \{"Neo"}";
        String message = STR.process(st);
        ```
    * user-defined template processor:
      * template processor is an object of the functional interface `StringTemplate.Processor` that receives a `StringTemplate` and returns an object
      * `StringTemplate` has the following attributes:
        * `List<String> fragments`: list of the text fragments coming before and after the embedded expressions
        * `List<Object> values`: list of the values produced by evaluating the embedded expressions in the order they appear
      * to create a template processor we can just class that implements `StringTemplate.Processor`, a lambda expression or use static factory method:
        * ex:
        ```java
        // both are the same
        StringTemplate.Processor<String, RuntimeException> STR_NULL_SANITIZER = (StringTemplate st) -> { /* code */ };
        // here we use `StringTemplate.Processor::of` so the `var` is inferred correctly to `StringTemplate.Processor<String, RuntimeException>`
        var STR_NULL_SANITIZER = StringTemplate.Processor.of((StringTemplate st) -> {
            var sb = new StringBuilder();
            var fragments = st.fragments().iterator();
            for (Object value : st.values()) {
              sb.append(fragments.next());
              if (value == null) {
                sb.append("<NULL>");
              } else {
                sb.append(value);
              }
            }
            sb.append(fragments.next());
            return sb.toString();
        });
        ```
      * we can use normally:
        * ex:
        ```java
        var message = STR_NULL_SANITIZER."Test: \{null}":

        // is equivalent to
        StringTemplate st = StringTemplate.RAW."Test: \{null}";
        var message = STR_NULL_SANITIZER.process(st);
        ```
* **Sequenced Collections**
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
* **Unnamed Classes and Instance Main Methods**
  * a.k.a. relaxed launch protocol
  * facilitate the writing of first programm for students without needing to know another features designed for large programs
  * unnamed class:
    * any method declared in a source file without an enclosed class will be considered to be member of an unnamed top-level class
    * the compiler requires an unnamed method to have a valid main method to be launched
    * we cannot use javadoc tool to generate documation from a unnamed class (doesn't have a accessible API from other class)
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
      * the default modifers are the same (package access and instance membership)
    * the main difference is will only have an impliticy default zero-paramater constructor and cannot have initializers (static nor instance)
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
* **Unnamed Patterns and Variables**
  * goals:
    * improve the readability of record patterns by ediling unnecessary nested patterns
    * improve the maintanability of code by identifying variables that must be declared but will not be used
  * unnamed pattern is denoted by `_`
  * it is a shorthand for the type pattern `var _`
  * it will facilitate the pattern matching where we are interested in the data types or structure
  * unnamed variables:
    * it never shadows another variable, can be used many times
    * it can be used when:
      * declaring a local variable (`int _ = q.remove()`)
      * resource specification of a try-with-resources (`try (var _ = ScopedContxt.acquire())`)
      * exception parameter in a catch clause (`catch (NumberFormatException _)`)
      * header of an enhanced for loop (`for (int i = 0; _ = sideEffect(); i++)`)
      * header of an enhanced for loop (`for (Order _ : orders)`)
      * lambda parameter (`(int x, int _) -> x + x`, `_ -> "lambda with single parameter"`)
  * unnamed patterns:
    * unnamed pattern is an unconditional pattern which binds nothing
    * we can use it in a nested position of a type pattern or a record pattern
      * `p instanceof Point(_, int y)`
      * `case Point(int x, _)`
      * `p instanceof ColoredPoint(Point _, Color c)`
  * unnamed pattern variables:
    * we can use it in any type pattern
      * `p instanceof Point _`
      * `case Point _`
* **Generational ZGC**
  * improve application performance by extending the Z Garbage Collection (ZGC) to maintain separate generations for young and old objects
  * ZGC will be able to collect young objects more often
  * possible gains:
    * lower risk of allowcations stalls
    * lower required heap memory overhead
    * lower garbage collection CPU overhead
  * JVM flags:
    * `java -XX:+UseZGC -XX:+ZGenerational`

### API

* changes in `Thread` class:
  * improve `Thread::sleep(millis, nanos)` to actually perform sleep with sub-millisecond time
  * added method `Thread::sleep(Durantion)`, `Thread::join(Durantion)` and `Thread::isVirtual`
* added methos in `Future` that doesn't declare any checked exception (but will throw a `IllegalStateException` if is not completed):
  * `T resultNow()`
  * `Throwable exceptionNow()`
  * `State state()`: returns the current state of the task (values: `RUNNING`, `SUCCESS`, `FAILED`, `CANCELLED`)
* added method `String::indexOf(String substr, int startIndex, int endIndex)` to find substring withing a index range
* added method `StringBuilder::repeat(String str, int numberOfTimes)` to repeat a string # times
* new methods to create collection with initial capacity:
  * `HashMap::newHashMap(int)`
  * `HashSet::newHashSet(int)`
  * `LinkedHashMap::newLinkedHashMap(int)`
  * `LinkedHashSet::newLinkedHashSet(int)`
* added method `InputStream::transferTo(OutputStream)` to read the stream and send to the received output stream
* [`java.net.http.Http Client` is now `AutoCloseable`](https://jdk.java.net/21/release-notes#JDK-8267140) and new methods were added to close/shutdown the connection pool.
* classes `ExecutorService` and `ForkJoinPool` changed to implement `AutoCloseable`
* added methods to `Math` class:
  * `int ceilDiv(int, int)`
  * `long ceilDiv(long, int)`
  * `long ceilDiv(long, long)`
  * `int ceilDivExact(int, int)`
  * `long ceilDivExact(long, long)`
  * `int ceilMod(int, int)`
  * `long ceilMod(long, int)`
  * `long ceilMod(long, long)`
  * division that throws `ArithmeticException` in case of overflow:
    * `int divideExact(int, int)`
    * `long divideExact(long, long)`
    * `int floorDivExact(int, int)`
    * `long floorDivExact(long, long)`
  * methods to clamp a value in a range for each type `int`, `long`, `float` and `double`:
    * `clamp(value, min, max)`
* added method `BigInteger::parallelMultiply(BigInteger)`

Deprecations:

* `Object::finalize`
* constructors in `Integer` class: `Integer(int)` and `Integer(String)`
  * should use `Integer::valueOf(int)` and `Integer::valueOf(String)`

## Links

* [The Arrival of Java 21](https://inside.java/2023/09/19/the-arrival-of-java-21/)
* [JDK 21 JEPs](https://openjdk.org/projects/jdk/21/)
* [JDK 21 Early Access Docs](https://download.java.net/java/early_access/jdk21/docs/api/)
* [JEP 444: Virtual Threads Arrive in JDK 21, Ushering a New Era of Concurrency](https://www.infoq.com/news/2023/04/virtual-threads-arrives-jdk21/)
* [Upgrading from Java 17 to 21](https://www.youtube.com/watch?v=5jIkRqBuSBs)
* [Java SE - Virtual Threads](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html)
* Presentations:
  * [JDK 21 Release Notes - Inside Java Newscast #55](https://www.youtube.com/watch?v=h13oIb9L1Fw)
  * [Java Virtual Threads - Oracle DevLive Level Up](https://www.youtube.com/watch?v=MOgynY7VIJI)
  * [The Challenges of Indroducing Virtual Threads to the Java Platform](https://www.youtube.com/watch?v=WsCJYQDPrrE)
  * [Java 21 New Feature: Virtual Threads](https://www.youtube.com/watch?v=5E0LU85EnTI)
  * [Java 21 API New Features](https://www.youtube.com/watch?v=4mPd2eL0wYQ)
  * [Java 21 Brings Full Pattern Matching](https://www.youtube.com/watch?v=QrwFrm1R8OY)
  * [Java's Virtual Threads - Next Steps](https://www.youtube.com/watch?v=KBW4LbCoo6c)

