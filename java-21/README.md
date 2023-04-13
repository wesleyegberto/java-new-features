# Java 21

To run each example use: `java --enable-preview --source 21 <FileName.java>`

## JEPs

* [431](https://openjdk.java.net/jeps/431) - Sequenced Collections
* [444](https://openjdk.org/jeps/444) - Virtual Threads

## Features

* Virtual threads
  * changed to make virtual threads always support thread-local
    * in preview releases was possible to create a virtual thread without thread-local support
  * flag `jdk.traceVirtualThreadLocals` to show the strack trace when a virtual threads sets a value in thread-local variable
* Sequenced Collections
  * new interfaces to define a common way to iterate throught sequenced collections (list, sets and maps)
  * [collections type hierarchy with new interfaces](https://cr.openjdk.org/~smarks/collections/SequencedCollectionDiagram20220216.png)
  * sequenced collection:
    * a sequenced collection is a collection whose elements have a defined encounter order
    * sequenced collection has a first and last elements, and the elements between them have successors and predecessors
    * it supports common operations at the beginning and the end of the collection, also allow iterate it forward or backward 
    * methods from `SequencedCollection<E>` interface:
      * `reversed`
      * `addFirst`
      * `addLast`
      * `getFirst`
      * `getLast`
      * `removeFirst`
      * `removeLast`
  * sequenced set:
    * sequenced set is a set that is sequenced collcetion that do not allow duplicate elements
    * methods from `SequencedSet<E>` interface:
      * `reversed`
  * sequenced map:
    * sequenced map is a map whose entries have a defined encounter order
    * methods from `SequencedMap<K,V>` interface:
      * `reversed`
      * `sequencedKeySet`
      * `sequencedValues`
      * `sequencedEntrySet`
      * `putFirst`
      * `putLast`
      * `firstEntry`
      * `lastEntry`
      * `pollFirstEntry`
      * `pollLastEntry`
  * new methods in `Collections`:
    * `unmodifiableSequencedCollection`
    * `unmodifiableSequencedSet`
    * `unmodifiableSequencedMap`


## Links

* [JDK 21 Jeps](https://openjdk.org/projects/jdk/21/)
* [JDK 21 Early Access Docs](https://download.java.net/java/early_access/jdk21/docs/api/)
* [JEP 444: Virtual Threads Arrive in JDK 21, Ushering a New Era of Concurrency](https://www.infoq.com/news/2023/04/virtual-threads-arrives-jdk21/)

