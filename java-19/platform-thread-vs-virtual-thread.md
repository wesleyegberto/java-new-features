# Platform Thread vs Virtual Thread

## Start 10K Threads

The codes in `StressPlatformThread` and `StressVirtualThread` tries to show some different beetween platform and virtual thread using VisualVM to inspect the threads running.

### Platform Thread

The example _tries_ to start 10K [real] threads to sleep for 500ms.
My Macbook 2015 couldn't handle 4K threads (we default JVM settings), after sometime it started to fail task submit and even GC were unable to run, then my macbook crashed.

My macOS currently has the default settings in maximum num of threads that can be created and threads per process:

![](img/macos-threads-settings.png)

Using 1K threads or changing to sleep for 100ms my macbook didn't crashed, so I could see VisualVM:

**1K Threads Started**

Looking at the Threads tab, we can see (by the scroll size) how many threads was started:

| Monitor | Timeline |
|---|---|
| ![](img/platform-thread-count.png) | ![](img/platform-thread-timeline.png) |

**10K sleeping 500ms**

When trying to run 10K threads sleeping for 500ms, I reached the macOS limit and things started to crash.

Before my Macbook restart, I could see:

![](img/platform-thread-500ms-count.png)

And right before restart:

![](img/platform-thread-500ms-error.png)

### Virtual Thread

The example starts 10K virtual threads to sleep for 30s.

The code runs easily as virtual threads is just sleeping, so ins't doing CPU-bound work.
The JVM just starts them all and puts them aside util they wake up to really use the thread (just print).

Note that the default implementation is using ForkJoinPool to run the virtual threads work.

| Monitor | Timeline |
|---|---|
| ![](img/virtual-thread-count.png) | ![](img/virtual-thread-timeline.png) |

## Fibonacci (CPU-bound)

The CPU-bound work I was expecting Virtual Thread version to take more time to execute.

Worth note that the Platform Thread version used more resources (memory and OS threads) which caused more GC pressure.

### Platform Thread

Time taken to run:

```
933.01s user 3.05s system 681% cpu 2:17.38 total
```

| Monitor | Timeline |
|---|---|
| ![](img/platform-thread-fib-monitor.png) | ![](img/platform-thread-fib-timeline.png) |

### Virtual Thread

Time taken to run:

```
957.94s user 3.43s system 614% cpu 2:36.55 total
```

| Monitor | Timeline |
|---|---|
| ![](img/virtual-thread-fib-monitor.png) | ![](img/virtual-thread-fib-timeline.png) |

## Fibonacci and HTTP Request (CPU-bound and IO-bound)

100 fibonacci of 40 and request during 5s.

Again, note the resources used in Platform Thread version

Note the different between the memory and thread graphs for Platform and Virtual threads.

**My interpretation:**

The Platform Thread used OS thread for each task (100), there were concorrency that caused all the calculation to end together, thus doing all the requesting also together (high slope in memory and thread count graph).

The Virtual Thread version had few OS threads (pool with 8 threads), the OS [maybe] gave more time to each ask allowing them to finish sooner, thus causing the requestings in small batches (smoothier slope in memory and thread count graph).

### Platform Thread

```
104.54s user 1.20s system 448% cpu 23.598 total
```

| Monitor | Timeline |
|---|---|
| ![](img/platform-thread-fibreq-monitor.png) | ![](img/platform-thread-fibreq-timeline.png) |


### Virtual Thread

Time taken to run:

```
77.55s user 0.89s system 403% cpu 19.458 total
```

| Monitor | Timeline |
|---|---|
| ![](img/virtual-thread-fibreq-monitor.png) | ![](img/virtual-thread-fibreq-timeline.png) |

## WebServer

The code in `WebServerThreads` shows how to use Virtual Thread to handle HTTP requests using `HttpServer` from Java 18.

The goal here is to show resources usage in VisualVM, the response was a simple text message.

The response time and throughput aren't much different here as the processing is a simple message write.
We should see some difference if the processing was making some IO-bound work.

To run stress test: `jmeter -n -t WebServer-StressTest.jmx -l jmeter_requests_logs.csv -e -o jmeter-report/`

### Platform Thread

JMeter Summary

![](img/webserver-platform-thread-jmeter-summary.png)

| Response Time | Monitor |
|---|---|
| ![](img/webserver-platform-thread-response-time.png) | ![](img/webserver-platform-thread-monitor.png) |

### Virtual Thread

JMeter Summary

![](img/webserver-virtual-thread-jmeter-summary.png)

| Response Time | Monitor |
|---|---|
| ![](img/webserver-virtual-thread-response-time.png) | ![](img/webserver-virtual-thread-monitor.png) |


