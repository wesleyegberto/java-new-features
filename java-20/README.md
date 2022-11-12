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

## JEPs

* [432](https://openjdk.java.net/jeps/432) - Record Patterns (Second Preview)
* [433](https://openjdk.java.net/jeps/433) - Pattern Matching for switch (Fourth Preview)

## Links

* [JDK 20 Jeps](https://openjdk.java.net/projects/jdk/20/)
* [JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=21004)
* [JDK 20 EA Builds](https://jdk.java.net/20/)

