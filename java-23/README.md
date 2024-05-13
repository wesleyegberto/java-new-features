# Java 23

To run each example use: `java --enable-preview --source 23 <FileName.java>`

## JEPs

* [455](https://openjdk.org/jeps/455) - Primitive Types in Patterns, instanceof, and switch (Preview)
* [466](https://openjdk.org/jeps/466) - Class-File API (Second Preview)
* [467](https://openjdk.org/jeps/467) - Markdown Documentation Comments
* [469](https://openjdk.org/jeps/469) - Vector API (Eighth Incubator)
* [473](https://openjdk.org/jeps/473) - Stream Gatherers (Second Preview)
* [474](https://openjdk.org/jeps/474) - ZGC: Generational Mode by Default
* [476](https://openjdk.org/jeps/476) - Module Import Declarations (Preview)

## Features

* **Primitive Types in Patterns, instance of and switch**
    * goal: "enhance pattern matching by allowing primitive type patterns in all pattern contexts"
    * in JDK 21, primitive type patterns are allowed only as nested patterns in record pattern (e.g.: `v instanceof JsonNumber(double d)`)
    * this JEP will:
        * extend pattern matching to allow primitive type patterns in all places (e.g.: `v instanceof JsonNumber(int i)`);
        * enhance `instanceof` and `switch` to support primitive type patterns as top level pattern
        * enhance `instanceof` to check the primitive type, beside the reference type checking, and also verify the primitive support value:
            * allowing to do an implicity cast during matching if, and only if, there is no loss of information (exact conversion)
            * e.g.: if an `int` is to large for a `byte` it will not be matched
            * `instanceof` will be used as safeguard for all conversions
            * in [JLS §5.5](https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html#jls-5.5-320) we can see the permited conversions
        * enhance the `switch` to works with literals of all primitive types:
            * until JDK 21, `boolean`, `float`, `double` and `long` cannot be used as case value
            * switch will be exhaustive if it contains a unconditional type pattern
                * an `int` is always a `long`, so a label `case long l` will match any integer
            * switch and floating-points:
                * if the switch's selector expression is a float then any case constants must be a floating-point literals
                * floating-point literal in case labels is defined in terms of [representation equivalence](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Double.html#repEquivalence)
                    * a label `case 1.0f` is considered the same as `case 0.99999999f`, will throw a duplicate label error
* **Stream Gatherers**
    * re-preview without change
* **ZGC: Generational Mode by Default**
    * switch the default mode of ZGC to use generational mode
    * deprecate the non-generational mode to be removed in the future
        * reduce the maintenance cost of supporting two models
    * default mode will act like using command-line args: `-XX:+UseZGC -XX:+ZGenerational`
    * non-generational can still be used with: `-XX:+UseZGC -XX:-ZGenerational`
    * Generational ZSG should be a better solution for most use cases
* **Module Import Declarations**
    * allow import all packages exported by a module
    * goal is simplify the learning and exploring of APIs without having to know its exactly package
    * when importing a module, it will automatically import all its exported packages and its transitive dependencies
    * syntax:
        * `import module [Module Name]`
        * `import module java.sql`
    * ambiguous import:
        * in case of two module exporting the same class name, we will have to import the class directly
        * ```java
          import module java.base;
          import module java.sql;
          import java.sql.Date;
          ```

## Links

* [JDK 23 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=22205)

