# Java 26

To run each example use: `java --enable-preview --source 26 <FileName.java>`

## JEPs

* [504](https://openjdk.org/jeps/504) - Remove the Applet API
* [517](https://openjdk.org/jeps/517) - HTTP/3 for the HTTP Client API
* [522](https://openjdk.org/jeps/522) - G1 GC: Improve Throughput by Reducing Synchronization

## Features

* **HTTP/3 for the HTTP Client API**
    * added support for HTTP/3 in the HTTP Client API
    * HTTP/3 was standardized in 2022
    * setting version in HttpClient builder:
        * ```java
          var client = HttpClient.newBuilder()
              .version(HttpClient.Version.HTTP_3)
              .build();
          ```
    * setting version in HttpRequest builder:
        * ```java
          var request = HttpRequest.newBuilder(URI.create("https://example.com"))
              .version(HttpClient.Version.HTTP_3)
              .GET()
              .build();
          ```

## Links

* [JDK 26 - JEP Dashboard](https://bugs.openjdk.org/secure/Dashboard.jspa?selectPageId=23506)
* [JDK 26 JEPs](https://openjdk.org/projects/jdk/26/)

