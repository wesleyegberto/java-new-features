# Java 25

To run each example use: `java --enable-preview --source 25 <FileName.java>`

## JEPs

* [502](https://openjdk.org/jeps/502) - Stable Values (Preview)
* [503](https://openjdk.org/jeps/503) - Remove the 32-bit x86 Port
* [511](https://openjdk.org/jeps/511) - Module Import Declarations
* [512](https://openjdk.org/jeps/512) - Compact Source Files and Instance Main Methods

## Features

* **Module Import Declarations**
    * promotion to standard without change
    * allow import all packages exported by a module
    * goal is simplify the learning and exploring of APIs without having to know its exactly package
    * when importing a module, it will automatically import all its exported packages and its transitive dependencies
        * import module `java.se` will import the entire Java SE API
    * syntax:
        * `import module [Module Name]`
        * `import module java.sql`
    * ambiguous import:
        * in case of two module exporting the same class name, we can import the class directly or its package
        * allow any type-import-on-demand declarations to shadow module import declarations
        *
          ```java
          // module imports
          import module java.base; // java.util.Date
          import module java.sql; // java.sql.Date

          // package imports
          import java.util.*; // every class used will shadow the class from module imports

          // single-type imports
          import java.sql.Date; // will shadow all above imports
          ```
* **Compact Source Files and Instance Main Methods**
    * promotion to standard with minor changes
        * changed terminology from "simple source file" to "compact source file"
        * changed package of `IO` to `java.lang.IO`
    * facilitate the writing of first programm for students without needing to know another features designed for large programs
    * implicitly declared class:
        * any method declared in a source file without an enclosed class will be considered to be member of an implicitly declared class whose members are unenclosed fields and methods
        * implicitly declared class is always final and cannot implement or extends any class other than `Object`
        * the compiler requires an implicitly declared method to have a launchable main method
        * we cannot use javadoc tool to generate documation from a implicitly declared class (doesn't have a accessible API from other class)
        * is equivalent to the following usage of anonymous class declaration:
            ```java
            new Object() {
                void main() {}
            }.main();
            ```
        * as it doesn't have a name, so we cannot use its static methods using its class name (`ClassName.staticMethodName()`)
        * it still will have the impliticy `this` reference
        * it can have:
            * instance and static methods
            * instance and static fields
            * the default modifers are the same (package access and instance membership)
        * the main difference is will only have an impliticy default zero-paramater constructor and cannot have initializers (static nor instance)
        * implicitly class will automatically import all of the public top-level classes and interfaces from module `java.base`
        * `Class.isSynthetic` method returns true
    * launchable main method:
        * change to how Java programs are launched
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
        * allowing implicitly declared class:
            ```java
            void main() {
                System.out.println("Hello World!");
            }
            ```
        * order to select a main method (must be non-private and it prioritize the methods in current class before the superclass):
            * `static void main(String[])`
            * `void main(String[])`
            * `static void main()`
            * `void main()`
    * `java.lang.IO`
        * new class created for convenience I/O
        * provides methods: `print(Object)`, `println(Object)`, `println()`, `readln(String)` and `readln()`
        * this class uses return from `System.console()` to print and read from the default input and output streams
        * we can use with: `IO.println("Hello World")`

## Links

* [JDK 25 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=23200)
* [JDK 25 JEPs](https://openjdk.org/projects/jdk/25/)

