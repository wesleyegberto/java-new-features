# Java 7

## Features

* Collections API:
  * `TransferQueue<E>`
    * extends `BlockingQueue<E>` interface
    * implementated by `LinkedTransferQueue<E>`
    * blocking queue in which producer may wait for consumers to receive elements
    * it can publish an element and wait for consumers or just publish and forget
    * it can use a timeout to wait for a consumer
    * methods:
      * `getWaitingConsumerCount()`: returns an estimate of the number of waiting consumer to receive elements via `BlockingQueue` `take` or timed `poll`.
      * `hasWaitingConsumer()`: returns true if there is any waiting consumer.
      * `transfer(E)`: send an element and wait for the consumer.
      * `tryTransfer(E)`: send an element to a waiting consumer, return false immediately if there isn't any consumer.
      * `tryTransfer(E, long, TimeUnit)`: like `tryTransfer` but with a timeout to wait for a consumer before returning false.

