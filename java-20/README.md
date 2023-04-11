# Java 20

To run each example use: `java --enable-preview --source 20 <FileName.java>`

## JEPs

* [429](https://openjdk.java.net/jeps/429) - Scoped Values (Incubator)
* [432](https://openjdk.java.net/jeps/432) - Record Patterns (Second Preview)
* [433](https://openjdk.java.net/jeps/433) - Pattern Matching for switch (Fourth Preview)
* [434](https://openjdk.java.net/jeps/434) - Foreign Function & Memory API (Second Preview)
* [436](https://openjdk.java.net/jeps/436) - Virtual Threads (Second Preview)
* [437](https://openjdk.java.net/jeps/437) - Structured Concurrency (Second Incubator)
* [438](https://openjdk.java.net/jeps/438) - Vector API (Fifth Incubator)

## Features

* Record patterns (second preview)
  * added support for inference of type arguments of generic record patterns;
    * now it generic type can be inferred
    * given `record Decorator<T>(T t) {}` and variable `Decorator<Decorator<String>> wr`, the record pattern generic type can be inferred in `w insteaceof Decorator(Decorator(var s))`
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
    * `ExecutorService` extends `AutoClosable`
* Scoped values (incubator)
  * enable the sharing of immutable data within and across threads
  * it were inspired by the way that Lipst dialects provide support for dyanamically scoped free variables
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
  * we use an attribute of type [`ScopedValue`](https://download.java.net/java/early_access/loom/docs/api/jdk.incubator.concurrent/jdk/incubator/concurrent/ScopedValue.html) declared as final static
    * scoped value has multiple incarnations, one per thread
    * once the value is written to scoped value it becomes immutable and is available only for a bounded period during execution of a thread
  * we can create nested scope when inside a scope we use another variable to create a scope
  * we can create nested scope with rebinded value when using the same `ScopedValue` to create a new scope
    * the created scope will have the new value binded
    * the current scope will still have its original value
  * usage:
    * we use `ScopedValue.where` to set the value
    * then use the method `run` or `call` to bind the scoped value with the current thread and defining the lambda expression
    * the scope of the methods `run`/`call`, the lambda expression (and every method called by it) can access the scoped value using the method `get` from static final attribute
  * ex.:
    *
    ```java
    public final static ScopedValue<String> PRINCIPAL = ScopedValue.newInstance();
    // [...]
    ScopedValue.where(PRINCIPAL, "guest")
    	.run(() -> {
    		var userRole = PRINCIPAL.get();
    	});
    ```
  * `ScopedValue.where` provides a one-way sharing to the execution scope of method `run`
  * to receive a result from another execution scope we need to use `call` instead of `run`
  * ex.:
    *
    ```java
    var universeAnswer = ScopedValue.where(SUBJECT, "Deep Thought")
    	.call(() -> {
    		// some magic
    		return 42;
    	});
    ```
   * virtual thread and cross-thread sharing
     * to share data between a thread and its child thread we need to make the scoped values inherited by child thread
       * we can use the Structured Concurrency API
       * the class `StructuredTaskScope` automatically make the scoped value inherited by child thread
       * the fork must occurrs inside the `ScopedValue` method `run`/`call`
       * the binding will remains until the child thread are finished and we can use `StructuredTaskScoped.join` to ensure
       that the child threads will terminate before `run`/`call`
     * when we try to access a scoped value not shared with current thread an exception `NoSuchElementException` will be thrown
* Structured concurrency (second incubator)
  * no changes in API since JDK 19
  * added support to inheritance of scoped values

## Links

* [JDK 20 Jeps](https://openjdk.java.net/projects/jdk/20/)
* [JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21004)
* [JDK 20 EA Builds](https://jdk.java.net/20/)

