# Java 24

To run each example use: `java --enable-preview --source 24 <FileName.java>`

## JEPs

* [472](https://openjdk.org/jeps/472) - Prepare to Restrict the Use of JNI
* [475](https://openjdk.org/jeps/475) - Late Barrier Expansion for G1

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

## Links

* [JDK 24 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=22701)
* [JDK 24 JEPs](https://openjdk.org/projects/jdk/24/)

