# Java 25

To run each example use: `java --enable-preview --source 25 <FileName.java>`

## JEPs

* [502](https://openjdk.org/jeps/502) - Stable Values (Preview)
* [503](https://openjdk.org/jeps/503) - Remove the 32-bit x86 Port
* [505](https://openjdk.org/jeps/505) - Structured Concurrency (Fifth Preview)
* [506](https://openjdk.org/jeps/506) - Scoped Values
* [507](https://openjdk.org/jeps/507) - Primitive Types in Patterns, instanceof, and switch (Third Preview)
* [508](https://openjdk.org/jeps/508) - Vector API (Tenth Incubator)
* [510](https://openjdk.org/jeps/510) - Key Derivation Function API
* [511](https://openjdk.org/jeps/511) - Module Import Declarations
* [512](https://openjdk.org/jeps/512) - Compact Source Files and Instance Main Methods
* [513](https://openjdk.org/jeps/513) - Flexible Constructor Bodies
* [514](https://openjdk.org/jeps/514) - Ahead-of-Time Command-Line Ergonomics
* [515](https://openjdk.org/jeps/515) - Ahead-of-Time Method Profiling
* [519](https://openjdk.org/jeps/519) - Compact Object Headers

## Features

* **Primitive Types in Patterns, instance of and switch**
    * re-preview without change
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
    * facilitate the writing of first program for students without needing to know another features designed for large programs
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
* **Flexible Constructor Bodies**
    * promotion to standard without change
    * allow statements that do not reference the instance being created to appear before an explicit constructor invocation
    * will allow to use statements that use, transform or verify values before call `super`
    * the code before the `super` lives in pre-construction context:
        * can perform any statements that don't use any member of the instance being created (or its hierarchy or outter/inner classes that depends on the instance)
    * we can:
        * can access static members
        * initialize fields in the same class before explicitly invoking a constructor
            * this will allow a superclass never executes code with use subclass values with their default value (`0`, `false` or `null`)
        * if the class is an inner class, it can access members of its enclosing class (like `Outer.this.field++`)
            * the outer class instance already exists
    * inicialization flow:
        ```
        C prologue
        --> B prologue
            --> A prologue
                --> Object constructor body
            --> A epilogue
        --> B epilogue
        C epilogue
        ```
* **Ahead-of-Time Command-Line Ergonomics**
    * simplify the process to create ahead-of-time caches by introducing a new command-line option that will run the record (`AOTMode=record`) and create mode (`AOTMode=create`)
    * introduced the command-line option `AOTCacheOutput` that receives the AOT cache output file
        * this command-line will run each step (record and create mode) for us
    * introduced new environment variable `JDK_AOT_VM_OPTIONS` that can be used to pass command-line options to run with create mode (won't pass to record mode)
        * the syntax is the same as for `JAVA_TOOL_OPTIONS`
    * two-step workflow:
        * record mode:
            ```bash
            java -XX:AOTMode=record -XX:AOTConfiguration=app.aotconf -cp app.jar com.example.App
            ```
        * create mode:
            ```bash
            java -XX:AOTMode=create -XX:AOTConfiguration=app.aotconf -XX:AOTCache=app.aot
            ```
    * one-step workflow:
        ```bash
        java -XX:AOTCache=app.aot -cp app.jar com.example.App
        ```
* **Ahead-of-Time Method Profiling**
    * improve warmup time by making method-execution profiles from a previous run
    * this will enable the JIT compiler to generate native code immediately upon application startup, rather than having to wait for profiles to be collected
    * the only way to really know what the application does is running it
        * one reason for this uncertainly is that, in the absence of final or sealed modifiers, any class can be subclassed at any time
        * another reason is that new classes can be loaded in response to external input
    * [static analysis can always be defeated by program complexity](https://en.wikipedia.org/wiki/Rice's_theorem)
    * AOT cache was extended to collect method profiles during training runs
* **Compact Object Headers**
    * promotion to standard without change
    * reduce the size of object headers in the HotSpot JVM from between 96 and 128 bits to 64 bits on 64-bit architecture
    * goals:
        * reduce object sizes and memory footprint on realistic workloads
        * not introduce more than 5% throughput or latency overheads
    * can be enable with:
        * `-XX:+UseCompactObjectHeaders`

## Links

* [JDK 25 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=23200)
* [JDK 25 JEPs](https://openjdk.org/projects/jdk/25/)

