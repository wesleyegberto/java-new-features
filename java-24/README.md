# Java 24

To run each example use: `java --enable-preview --source 24 <FileName.java>`

## JEPs

* [404](https://openjdk.org/jeps/404) - Generational Shenandoah (Experimental)
* [450](https://openjdk.org/jeps/450) - Compact Object Headers (Experimental)
* [472](https://openjdk.org/jeps/472) - Prepare to Restrict the Use of JNI
* [475](https://openjdk.org/jeps/475) - Late Barrier Expansion for G1
* [478](https://openjdk.org/jeps/478) - Key Derivation Function API (Preview)
* [479](https://openjdk.org/jeps/479) - Remove the Windows 32-bit x86 Port
* [483](https://openjdk.org/jeps/483) - Ahead-of-Time Class Loading & Linking
* [484](https://openjdk.org/jeps/484) - Class-File API
* [485](https://openjdk.org/jeps/485) - Steam Gatherers
* [486](https://openjdk.org/jeps/486) - Permanently Disable the Security Manager
* [487](https://openjdk.org/jeps/487) - Scoped Values (Fourth Preview)
* [488](https://openjdk.org/jeps/488) - Primitive Types in Patterns, instanceof, and switch (Second Preview)
* [489](https://openjdk.org/jeps/489) - Vector API (Ninth Incubator)
* [490](https://openjdk.org/jeps/490) - ZGC Remove the Non-Generational Mode
* [491](https://openjdk.org/jeps/491) - Synchronize Virtual Threads without Pinning
* [492](https://openjdk.org/jeps/492) - Flexible Constructor Bodies (Third Preview)
* [493](https://openjdk.org/jeps/493) - Linking Run-Time Images without JMODs
* [494](https://openjdk.org/jeps/494) - Module Import Declarations (Second Preview)
* [495](https://openjdk.org/jeps/495) - Simple Source Files and Instance Main Mathods (Fourth Preview)
* [496](https://openjdk.org/jeps/496) - Quantum-Resistant Module-Lattice-Based Key Encapsulation Mechanism
* [497](https://openjdk.org/jeps/497) - Quantum-Resistant Module-Lattice-Based Digital Signature Algorithm
* [498](https://openjdk.org/jeps/498) - Warn Upon Use of Memory-Access Methods in sun.misc.Unsafe
* [499](https://openjdk.org/jeps/499) - Structured Concurrency (Fourth Preview)
* [501](https://openjdk.org/jeps/501) - Deprecate the 32-bit x86 Port for Removal

## Features

* **Compact Object Headers**
    * reduce the size of object headers in the HotSpot JVM from between 96 and 128 bits to 64 bits on 64-bit architecture
    * goals:
        * reduce object sizes and memory footprint on realistic workloads
        * not introduce more than 5% throughput or latency overheads
    * can be enable with:
        * `-XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders`
* **Prepare to Restrict the Use of JNI**
    * align the command-line options to be used equally between JNI and FFM API
    * align how FFM API restrict the native method usage with warning instead of throwing an exception
        * JNI shows warnings unless `--illegal-native-access=deny` is provided
    * to use the previous behave we need to provide the following command-line options:
        * `--enable-native-access=MyModule --illegal-native-access=deny`
        * `--illegal-native-access=warn` is the default mode, in the future will be `deny`
        * this ways it will thrown an exception
    * added new JDK tool `jnativescan` to help identify the use of restricted methods and declarations of native methods
* **Late Barrier Expansion for G1**
    * reduce the overhead of C2 JIT compiler
    * reduce the execution time of C2 when using G1
* **Ahead-of-Time Class Loading & Linkin**
    * improve startup time by making the class instantly available, in a loaded and linked state, when JVM starts
    * the application is monitored during a training run to store the loaded and linked classes in a cache
    * only the classes loaded from the class path, the module path and JDK itself will be cached
        * we must ensure that the class paths and module configurations are the same in all runs
    * there are two steps:
        * training run to record AOT configuration:
            * `java -XX:AOTMode=record -XX:AOTConfiguration=app.aotconf -cp app.jar com.example.App`
        * create cache from the AOT configuration:
            * `java -XX:AOTMode=create -XX:AOTConfiguration=app.aotconf -XX:AOTCache=app.aot -cp app.jar`
        * run the application with cache:
            * `java -XX:AOTCache=app.aot -cp app.jar com.example.App`
    * recommendations:
        * try to do a training run with the application as close as possible to production run
        * if external resources cannot be used during the training run, we could mock or disable them (it will be loaded just-in-time)
        * we could use a smoke test to run enough scenarios used by the application, some testing classes will be cached (in the future a filter will be provided)
        * we should avoid using complete large test suites (unit tests, corner cases, so on) because it will increase the size of the cache with useless classes (scenarios with fewer usage)
        * the training run should do similiar things as production run, otherwise the cache will be less useful
* **Class-File API**
    * provide standard API for parsing, generating and modifying Java class file
    * provide standard API that is up to date with the features from current JDK released
    * design goals:
        * class-file entities are represented by immutable objects
        * tree-structured representation (e.g.: class is represented by `ClassModel` and can contain `MethodModel`, `FieldModel`, etc)
        * user-driven navigation
        * laziness when parsing class
        * unified streaming and materialized views
        * emergent transformation (transformation can be viewed as a flat-map operation)
        * detail hiding (constant pool, block scoping, stack maps, labels, variables index offset, etc)
    * API [`java.lang.classfile`](https://download.java.net/java/early_access/jdk24/docs/api/java.base/java/lang/classfile/package-summary.html) defines three main abstractions:
        * element: immutable description of some part of class file (instruction, attribute, field, method or an entire class)
        * builder: each compound element has a builder with methods to help construct the element
        * transform: represents a function that takes an element and a builder and mediates how that element is transformed (replaced, droped or passed to another builder)
    * we can iterate, transform or build elements using streams and Lambdas
    * we can use `transformClass`, `transformMethod` and `transformCode` to transform an element
    * to read a classe we must use `ClassFile`:
        * `ClassModel cm = ClassFile.of().parse(bytes);`
    * `ClassFile` is an iterable where we can navigates all its elements (fields, methods, etc) using pattern matching (instead of visitor in other libs):
        *
        ```java
        ClassModel cm = ClassFile.of().parse(bytes);
        for (ClassElement ce : cm) {
            case FieldModel fm -> System.out.println("Field " + mm.methodName().stringValue());
            case MethodModel mm -> System.out.println("Method " + mm.methodName().stringValue());
            default -> System.out.println("Other " + ce);
        }
        ```
    * `ClassFile` also provides methods to access the elements we need (parsing only the part need to get the methods):
        *
        ```java
        ClassModel cm = ClassFile.of().parse(bytes);
        for (FieldModel fm : cm.fields()) {
            System.out.println("Field " + mm.methodName().stringValue());
        }
        for (MethodModel mm : cm.methods()) {
            System.out.println("Method " + mm.methodName().stringValue());
        }
        ```
* **Steam Gatherers**
    * promotion to standard without change
* **Scoped Values**
    * re-preview with API changes
    * removed methods `callWhere` and `runWhere` to make API more fluent
    * `ScopedValue.where(SCOPE, "my-value").run(() -> {});` instead of `ScopedValue.callWhere(SCOPE, "my-value", () -> {})`
    * we can bind multiples values:
        * `ScopedValue.where(SCOPE_A, "my-value").where(SCOPE_B, "other-value").run(() -> {})`
* **Primitive Types in Patterns, instanceof, and switch**
    * re-preview without change
* **ZGC - Remove the Non-Generational Mode**
    * remove non-generational mode of the ZGC to reduce maintenance cost
    * changes in command-line options:
        * `-XX:+UseZGC`: will use generational ZGC
        * `-XX:+UseZGC -XX:+ZGenerational`: will use generational ZGC and print a warning about obsolete option
        * `-XX:+UseZGC -XX:-ZGenerational`: will use generational ZGC and print a warning about obsolete option
* **Flexible Constructor Bodies**
    * re-preview without change
* **Synchronize Virtual Threads without Pinning**
    * change the virtual threads to release the platform threads when blocked by synchronized methods and statements
    * eliminates nearly all cases of virtual threads being pinned to platform thread
    * scenarios where the virtual threads can be pinned:
        * when inside a synchronized block, if it tries to get another object's monitor, the virtual thread is blocked and pinned until it can adquire the monitor
        * when it tries to invoke a synchronized method that is already locked by another thread, the virtual thread is pinned and blocked until it can adquire the monitor
    * the system property `jdk.tracePinnedThreads` was removed
    * recommendations:
        * use synchronized where practical, since it is more convenient and less error prone
        * use `ReentrantLock` and other APIs when more flexibility is required
* **Module Import Declarations**
    * re-preview with two changes
    * import module `java.se` will import the entire Java SE API (`java.base`)
    * allow any type-import-on-demand declarations to shadow module import declarations:
        *
        ```java
        import module java.base;
        import module java.sql;

        import java.util.*; // every class used will shadow the class from module imports
        import java.sql.Date; // will shadow all above imports
        ```
* **Simple Source Files and Instance Main Methods**
    * re-preview with no change
    * introduced new terminology "simple source file" to indicate a Java file with a implicitly declared class

## Links

* [JDK 24 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=22701)
* [JDK 24 JEPs](https://openjdk.org/projects/jdk/24/)

