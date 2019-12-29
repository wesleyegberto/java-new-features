# Java 12

To run each example use:
`java --enable-preview --source 12 <FileName.java>`

## Features

### Language

* Switch expression
* Collectors.teeing
* More Versatile Error Recovery With CompletableFuture
  * `exceptionallyAsync(Function<Throwable, ? extends T> f)`
  * `exceptionallyAsync(Function<Throwable, ? extends T> f, Executor e)`
  * `exceptionallyCompose(Function<Throwable, ? extends CompletionStage<T>> f)`
  * `exceptionallyComposeAsync(Function<Throwable, ? extends CompletionStage<T>> f)`
  * `exceptionallyComposeAsync(Function<Throwable, ? extends CompletionStage<T>> f, Executor e)`
* Compact Number Format

### JVM

* Default CDS Archives
  * `-Xshare:off` to disable it
* Garbage Collection
  * Shenandoah: Red Hat low-pause-time GC, experimental;
  * G1: improvements and promptly returns unused memory SO;

## JEPs

* [12](https://openjdk.java.net/jeps/12) - Preview language and VM features
* [325](http://openjdk.java.net/jeps/325) - Switch expressions (preview)
* [341](http://openjdk.java.net/jeps/341) - Default CDS Archives
* [189](http://openjdk.java.net/jeps/189) - Low-Pause-Time GC
* [344](http://openjdk.java.net/jeps/344) - Abortable mixed collections for G1
* [345](http://openjdk.java.net/jeps/346) - Promptly return unused memory from G1
* [334](http://openjdk.java.net/jeps/334) - JVM Constants API

## Warnings

Tools:

* Maven: 3.5.0
  * compiler plugin: 3.8.0
  * surefire and failsafe: 2.22.0

```
<plugin>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgs>
            --enable-preview
        </compilerArgs>
    </configuration>
</plugin>
<plugin>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <argLine>--enable-preview</argLine>
    </configuration>
</plugin>
```

Libs which manipulate bytecode that will need update:

* Spring
* Hibernate
* Mockiot

## Links

* [Java 12 Guide](https://blog.codefx.org/java/java-12-guide/)
* [Should you adopt Java 12 or stick on Java 11?](https://blog.joda.org/2018/10/adopt-java-12-or-stick-on-11.html)