# Example of Structured Concurrency

The output that we can see that the `StructuredTaskScope` canceled other subtask (throwing a `InterruptedException`):

![](img/2022-06-12-18-08-57.png)

Thread view of concurrency using `ExecutorService`:

![](img/2022-06-12-17-50-45.png)

Thread view of concurency using `StructuredTaskScope`:

![](img/2022-06-12-17-51-30.png)

We can not the virtual thread running with ForkJoinPool.
