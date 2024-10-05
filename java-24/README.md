# Java 24

To run each example use: `java --enable-preview --source 24 <FileName.java>`

## JEPs

* [472](https://openjdk.org/jeps/472) - Prepare to Restrict the Use of JNI
* [475](https://openjdk.org/jeps/475) - Late Barrier Expansion for G1
* [484](https://openjdk.org/jeps/484) - Class-File API

## Features

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

## Links

* [JDK 24 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=22701)
* [JDK 24 JEPs](https://openjdk.org/projects/jdk/24/)

