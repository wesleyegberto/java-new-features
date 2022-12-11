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

