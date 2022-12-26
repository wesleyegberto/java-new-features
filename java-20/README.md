# Java 20

To run each example use: `java --enable-preview --source 20 <FileName.java>`

## Features

* Record patterns (second preview)
  * added support for inference of type arguments of generic record patterns;
    * now it generic type can be inferred
    * given `record Decorator<T>(T t) {}` and variable `Decorator<Decorator<String>> wr`, thee record pattern generic type can be inferred in `w insteaceof Decorator(Decorator(var s))`
  * added support for record patterns to appear in the headere of an enhanced for statement;
    * `for (Point(var x, var y) : shapePoints)`
  * remove support for named record pattner.
* Patteren matching for `switch` (fourth preview)
  * added support for inference of type arguments for generic record patterns
* Virtual threads (second preview)
  * small numbeer of API changes described by JEP 425 were made permanent in JDK 19 and are not proporsed to preview:
    * `Thread.join(Durantion)`
    * `Thread.sleep(Duration)`
    * `Thread.threadId()`
    * `Future.resultNow()`
    * `Future.exceptionNow()`
    * `Future.state()`
    * `ExecutorSerevice` extends `AutoClosable`
* Scoped values (incubator)
  * enable the sharing of immutable data within and across threads
  * they are preferred to thread-local variables (specially using virtual threads)
  * goals:
    * provide a programming model to share data both within a thread and with child threads
    * make the lifetime of shared data visible from structure of code
    * ensura that data sahred by a caller can be retrieved only by legitimate callees
    * thread shared data as immuatable so as to allow sharing by a large number of threads and to enable runtime otimiziations
  * problems with thread-local variables:
    * mutability: any object can update the variable if it has access to it
    * unbounded lifetime: it is stored until the thread is destroyed, if is used in a pool it can take a long time
    * expensive inheritance: a child thread must allocate memory for every variable stored in the parent thread
  * a scoped value allows data to be safely and efficiently shared between components
  * we use an attribute of type `ScopedValue` declared as final static
    * scoped value has multiple incarnations, one per thread
    * once the value is written to scoped value it becomes immutable and is available only for a bounded period during execution of a thread
  * usage:
    * we use `ScopedValue.where` to set the value
    * then use the method `run` or `call` to bind the scoped value with the current thread and defining the lambda expression
    * the scope of the methods `run`/`call`, the lambda expression (and every method called by it) can access the scoped value using the method `get` from static final attribute
  * ex.:
    *
    ```java
    public final static ScopedValue<String> PRINCIPAL = new ScopedValue<>();
    // [...]
    ScopedValue.where(PRINCIPAL, "guest")
    	.run(() -> {
    		var userRole = PRINCIPAL.get();
    	});
    ```
  
## JEPs

JEPs proposed to target:

* [429](https://openjdk.java.net/jeps/429) - Scoped Values (Incubator)
* [432](https://openjdk.java.net/jeps/432) - Record Patterns (Second Preview)
* [433](https://openjdk.java.net/jeps/433) - Pattern Matching for switch (Fourth Preview)
* [434](https://openjdk.java.net/jeps/434) - Foreign Function & Memory API (Second Preview)
* [436](https://openjdk.java.net/jeps/436) - Virtual Threads (Second Preview)
* [437](https://openjdk.java.net/jeps/437) - Structured Concurrency (Second Incubator)

## Links

* [JDK 20 Jeps](https://openjdk.java.net/projects/jdk/20/)
* [JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21004)
* [JDK 20 EA Builds](https://jdk.java.net/20/)

