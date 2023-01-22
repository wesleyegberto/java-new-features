# Java 12

To run each example use: `java --enable-preview --source 12 <FileName.java>`

## JEPs

* [12](https://openjdk.java.net/jeps/12) - Preview language and VM features
* [189](https://openjdk.java.net/jeps/189) - Low-Pause-Time GC (experimental)
* [230](https://openjdk.java.net/jeps/230) - Microbenchmark Suite
* [325](https://openjdk.java.net/jeps/325) - Switch expressions (preview)
* [334](https://openjdk.java.net/jeps/334) - JVM Constants API
* [340](https://openjdk.java.net/jeps/340) - One AArch64
* [341](https://openjdk.java.net/jeps/341) - Default CDS Archives
* [344](https://openjdk.java.net/jeps/344) - Abortable mixed collections for G1
* [345](https://openjdk.java.net/jeps/346) - Promptly return unused memory from G1

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

## Links

* [Java 12 Documentation](https://docs.oracle.com/en/java/javase/12/index.html)
* [Java 12 Guide](https://blog.codefx.org/java/java-12-guide/)
* [Should you adopt Java 12 or stick on Java 11?](https://blog.joda.org/2018/10/adopt-java-12-or-stick-on-11.html)
* [Shenandoah GC](https://wiki.openjdk.java.net/display/shenandoah/Main)
