# Java 19

To run each example use: `java --enable-preview --source 19 <FileName.java>`

## Features

### Language

* Pattern matching for `switch`
  * Minor improvements from JDK 18:
    * changed guarded pattern from `&&` to `when` keyword
    * definition: guard is the boolean expression, guarded pattern is the case with guard
      * guarded pattern: `case Hero h when h.getCity() == Cities.NEW_YORK`
      * guard: `h.getCity() == Cities.NEW_YORK`
* Virtual Threads
  * also called user-mode threads or [fiber](https://en.wikipedia.org/wiki/Fiber_(computer_science))
  * `Virtual threads are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications`
  * definitions:
    * virtual thread is an instance of `java.lang.Thread` that is not tied to a particular OS thread, only consumes an OS thread only while it performs calculations on the CPU;
    * platform thread is an instance of `java.lang.Thread` implemented a thin wrapper around an OS thread.
  * blocking:
    * `when code running in a virtual thread calls a blocking I/O operation in the java.* API, the runtime performs a non-blocking OS call and automatically suspends the virtual thread until it can be resumed later`;
    * `virtual threads preserve the reliable thread-per-request style that is harmonious with the design of the Java Platform while utilizing the hardware optimally`
  * virtual threads employ M:N scheduling, where a large number (M) of virtual threads is scheduled to run on a smaller number (N) of OS threads.
  * `Virtual threads are not faster threads — they do not run code any faster than platform threads. They exist to provide scale (higher throughput), not speed (lower latency). There can be many more of them than platform threads, so they enable the higher concurrency needed for higher throughput according to Little's Law.`
  * scenarios where it can significantly improve throughput when:
    * number of concurrent tasks is high, and
    * work is not CPU-bound.
  * support thread-local variables and interruption
  * more notes about Project Loom [here](../java-loom/)
  * we can use `Executors.newVirtualThreadPerTaskExecutor` to create virtual threads
  * [here](platform-thread-vs-virtual-thread.md) is some details about the Platform Thread vs Virtual Thread examples

## JEPs

* [422](https://openjdk.java.net/jeps/422) - Linux/RISC-V Port
* [424](https://openjdk.java.net/jeps/424) - Foreign Function & Memory API (Preview)
* [425](https://openjdk.java.net/jeps/425) - Virtual Threads (Preview)
* [426](https://openjdk.java.net/jeps/426) - Vector API (Fourth Incubator)
* [427](https://openjdk.java.net/jeps/427) - Pattern Matching for switch (Third Preview)

## Links

* [JDK 19 Jeps](https://openjdk.java.net/projects/jdk/19/)
