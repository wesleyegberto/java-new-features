# Java 19

To run each example use: `java --enable-preview --source 19 <FileName.java>`

## JEPs

* [405](https://openjdk.java.net/jeps/405) - Record Patterns (Preview)
* [422](https://openjdk.java.net/jeps/422) - Linux/RISC-V Port
* [424](https://openjdk.java.net/jeps/424) - Foreign Function & Memory API (Preview)
* [425](https://openjdk.java.net/jeps/425) - Virtual Threads (Preview)
* [426](https://openjdk.java.net/jeps/426) - Vector API (Fourth Incubator)
* [427](https://openjdk.java.net/jeps/427) - Pattern Matching for switch (Third Preview)
* [428](https://openjdk.java.net/jeps/428) - Structured Concurrency (Incubator)

## Features

### Language

* Pattern matching for `switch`
  * minor improvements from JDK 18:
    * changed guarded pattern from `&&` to `when` keyword
    * definition: guard is the boolean expression, guarded pattern is the case with guard
      * guarded pattern: `case Hero h when h.getCity() == Cities.NEW_YORK`
      * guard: `h.getCity() == Cities.NEW_YORK`
* Record patterns
  * added suport to deconstruct record values in pattern matcher
    * record pattern: `Point(int x, int y)`
    * the variable `int x` is initialized with the result of accessor method `Point.x()`, not directly from the field `x`
    * the variable doesn't need to be the same as the name of the record component
    * the null value does not match any record pattern
  * now we can use type pattern and record pattern together
  * we can check the type and extract the record components using `instanceof` operator
    * `o instanceOf Point(int x, int y)`
* Virtual Threads:
  * also called user-mode threads or [fiber](https://en.wikipedia.org/wiki/Fiber_(computer_science))
  * more notes about Project Loom [here](../projects/loom/)
  * `Virtual threads are lightweight threads that dramatically reduce the effort of writing, maintaining, and observing high-throughput concurrent applications`
  * definitions:
    * virtual thread is an instance of `java.lang.Thread` that is not tied to a particular OS thread, only consumes an OS thread only while it performs calculations on the CPU;
    * platform thread is an instance of `java.lang.Thread` implemented a thin wrapper around an OS thread.
  * virtual threads employ M:N scheduling, where a large number (M) of virtual threads is scheduled to run on a smaller number (N) of OS threads.
  * `Virtual threads are not faster threads — they do not run code any faster than platform threads. They exist to provide scale (higher throughput), not speed (lower latency). There can be many more of them than platform threads, so they enable the higher concurrency needed for higher throughput according to Little's Law.`
  * blocking:
    * `when code running in a virtual thread calls a blocking I/O operation in the java.* API, the runtime performs a non-blocking OS call and automatically suspends the virtual thread until it can be resumed later`;
    * `virtual threads preserve the reliable thread-per-request style that is harmonious with the design of the Java Platform while utilizing the hardware optimally`
  * for virtual threads, the JDK is using a `ForkJoinPool` as scheduler:
    * operates in FIFO mode;
    * the number of thread workers is the number of processors;
      * we can change the size of thread workers with `-Djdk.virtualThreadScheduler.parallelism`.
      * we can change the maximum number of platform threads with `-Djdk.virtualThreadScheduler.maxPoolSize`
    * it _does not_ implement any time sharing between virtual threads, the OS will do it at thread level;
    * `The platform thread to which the scheduler assigns a virtual thread is called the virtual thread's carrier`;
    * there is no affinity between virtual thread and the platform thread used to perform the work (could change throughout the virtual thread lifetime);
      * a native code called by a virtual thread may se different OS thread each time.
    * unmount and mount:
      * virtual thread will unmount when it blocks on I/O or some blocking operation in the JDK;
      * when the blocking operation is ready, it will be scheduled for execution.
    * pinned virtual thread:
      * scenarios where the virtual thread cannot be unmounted (became pinned to its carrier):
        * inside a `synchronized` block or method (is recommended to use `ReentrantLock`)
        * if virtual thread calls native code (JNI)
      * to debug this scenarios we can use:
        * `-Djdk.tracePinnedThreads=full` to print the full native and thread frames;
        * `-Djdk.tracePinnedThreads=short` to print only the frames locking;
        * monitor JDK Flight Recorder for thread block pinned events.
  * from perpective of Java code, a running virtual thread is logically independent from its carrier thread:
    * the identity of the carrier is unavailable for virtual thread, `Thread.currentThread` is always the virtual thread itself;
    * the stack traces of the carrier and the virtual thread are separete;
      * exception thrown in the virtual thread will not include carrier's stack (nor will thread dumps), and vice-versa;
      * thread-local variables of the carrier are unavailable to the virtual thread, and vice-versa.
  * characteristics of virtual thread:
    * is always daemon thread;
    * has normal priority;
    * is not member of any thread group;
    * supports concurrent lock API;
    * supports thread-local variables (be careful cause we will tend to use a lot of virtual threads, using too much thread-local variables could easily increase de memory footprint)
    * interruption and LockSupport;
    * can opt-out from having a thread locals (using Builder);
    * doesn't supports ThreadGroup (deprecated in favor of structured concurrency);
  * scenarios where it can significantly improve throughput when:
    * number of concurrent tasks is high, and
    * work is not CPU-bound.
  * creating virtual tasks:
    * `Executors.newVirtualThreadPerTaskExecutor`;
    * `Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory())`;
    * `Thread.ofPlatform().start(() -> {})`;
    * **do not use** any cached method from `Executors`.
  * [here](platform-thread-vs-virtual-thread.md) is some details about the Platform Thread vs Virtual Thread examples
* Structured Concurrency
  * goal is simplify multithreaded programming;
  * treats multiple tasks running in different threads as a single unit of work:
    * streamlining error handling and cancellation
    * improving reliability
    * enhancing observability
  * steps to use:
    * create a scope (using `StructuredTaskScope`);
    * fork concurrent subtasks in the scope:
      * any subtask or the scope owner can call `shutdown` to request cancelation of all remaining subtasks;
    * scope's owner joins the scope:
      * blocks until all subtasks either complete (sucessfully or not) or any call shutdown;
    * after joining, handle any errors and process theirs results:
      * we should only query the subtasks results after `join` returns (when all subtasks are known to be completed or canceled)
    * close the scope (implicity when using try-with-resources).
  * characteristics:
    * each fork creates its own thread (by default is a virtual thread)
    * allow nested scope (when creating a scope in a subtask)
    * shutdown causes the threads of all forks that are still active in the scope to be interrupted
      * causes `join`/`joinUntil` methods to return
      * all subtasks should be written to be responsive to interruption
    * when calling `close`, it propagates the the state of the unit of work (either awaits all subtasks to finish or cancel each one of them)
  * [output from the structured concurrency example](structured-concurrency-example.md):

## Links

* [JDK 19 Jeps](https://openjdk.java.net/projects/jdk/19/)
* [Chat project comparing Platform vs Virtual Threads](https://github.com/wesleyegberto/java-virtualthreads-chat)
