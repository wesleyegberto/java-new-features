# Java 8

## JEPs

* [126](https://openjdk.java.net/jeps/126) - Lambda Expressions & Virtual Extension Methods
* [138](https://openjdk.java.net/jeps/138) - Autoconf-Based Build System
* [160](https://openjdk.java.net/jeps/160) - Lambda-Form Representation for Method Handles
* [161](https://openjdk.java.net/jeps/161) - Compact Profiles
* [162](https://openjdk.java.net/jeps/162) - Prepare for Modularization
* [164](https://openjdk.java.net/jeps/164) - Leverage CPU Instructions for AES Cryptography
* [174](https://openjdk.java.net/jeps/174) - Nashorn JavaScript Engine
* [176](https://openjdk.java.net/jeps/176) - Mechanical Checking of Caller-Sensitive Methods
* [179](https://openjdk.java.net/jeps/179) - Document JDK API Support and Stability
* [142](https://openjdk.java.net/jeps/142) - Reduce Cache Contention on Specified Fields
* [122](https://openjdk.java.net/jeps/122) - Remove the Permanent Generation
* [173](https://openjdk.java.net/jeps/173) - Retire Some Rarely-Used GC Combinations
* [136](https://openjdk.java.net/jeps/136) - Enhanced Verification Errors
* [147](https://openjdk.java.net/jeps/147) - Reduce Class Metadata Footprint
* [148](https://openjdk.java.net/jeps/148) - Small VM
* [171](https://openjdk.java.net/jeps/171) - Fence Intrinsics
* [153](https://openjdk.java.net/jeps/153) - Launch JavaFX Applications
* [101](https://openjdk.java.net/jeps/101) - Generalized Target-Type Inference
* [104](https://openjdk.java.net/jeps/104) - Annotations on Java Types
* [105](https://openjdk.java.net/jeps/105) - DocTree API
* [106](https://openjdk.java.net/jeps/106) - Add Javadoc to javax.tools
* [117](https://openjdk.java.net/jeps/117) - Remove the Annotation-Processing Tool (apt)
* [118](https://openjdk.java.net/jeps/118) - Access to Parameter Names at Runtime
* [120](https://openjdk.java.net/jeps/120) - Repeating Annotations
* [139](https://openjdk.java.net/jeps/139) - Enhance javac to Improve Build Speed
* [172](https://openjdk.java.net/jeps/172) - DocLint
* [103](https://openjdk.java.net/jeps/103) - Parallel Array Sorting
* [107](https://openjdk.java.net/jeps/107) - Bulk Data Operations for Collections
* [109](https://openjdk.java.net/jeps/109) - Enhance Core Libraries with Lambda
* [112](https://openjdk.java.net/jeps/112) - Charset Implementation Improvements
* [119](https://openjdk.java.net/jeps/119) - javax.lang.model Implementation Backed by Core Reflection
* [135](https://openjdk.java.net/jeps/135) - Base64 Encoding & Decoding
* [149](https://openjdk.java.net/jeps/149) - Reduce Core-Library Memory Usage
* [150](https://openjdk.java.net/jeps/150) - Date & Time API
* [155](https://openjdk.java.net/jeps/155) - Concurrency Updates
* [170](https://openjdk.java.net/jeps/170) - JDBC 4.2
* [177](https://openjdk.java.net/jeps/177) - Optimize java.text.DecimalFormat.format
* [178](https://openjdk.java.net/jeps/178) - Statically-Linked JNI Libraries
* [180](https://openjdk.java.net/jeps/180) - Handle Frequent HashMap Collisions with Balanced Trees
* [127](https://openjdk.java.net/jeps/127) - Improve Locale Data Packaging and Adopt Unicode CLDR Data
* [128](https://openjdk.java.net/jeps/128) - BCP 47 Locale Matching
* [133](https://openjdk.java.net/jeps/133) - Unicode 6.2
* [184](https://openjdk.java.net/jeps/184) - HTTP URL Permissions
* [113](https://openjdk.java.net/jeps/113) - MS-SFU Kerberos 5 Extensions
* [114](https://openjdk.java.net/jeps/114) - TLS Server Name Indication (SNI) Extension
* [115](https://openjdk.java.net/jeps/115) - AEAD CipherSuites
* [121](https://openjdk.java.net/jeps/121) - Stronger Algorithms for Password-Based Encryption
* [123](https://openjdk.java.net/jeps/123) - Configurable Secure Random-Number Generation
* [124](https://openjdk.java.net/jeps/124) - Enhance the Certificate Revocation-Checking API
* [129](https://openjdk.java.net/jeps/129) - NSA Suite B Cryptographic Algorithms
* [130](https://openjdk.java.net/jeps/130) - SHA-224 Message Digests
* [131](https://openjdk.java.net/jeps/131) - PKCS#11 Crypto Provider for 64-bit Windows
* [140](https://openjdk.java.net/jeps/140) - Limited doPrivileged
* [166](https://openjdk.java.net/jeps/166) - Overhaul JKS-JCEKS-PKCS12 Keystores
* [185](https://openjdk.java.net/jeps/185) - Restrict Fetching of External XML Resources

## Features

### Languages

* new methods from APIs
* Stream API
* Project Lambda:
  * lambda expression to create anonymous method
  * lambda access scope (enclosing scope)
  * lambda expression to call constructors through method reference
* Annotation improvements:
  * Annotations on Java Types
  * Repeating annotations
* Method parameter reflection

Lambda expression consiste of:

* A comma-separated list of formal parameters enclosed in parentheses: (paramX, paramY) or singleParam or ()
* An arrow token: `->`
* A body, which consists of a single expression or a statement block: {} or a single expression without ;

E.g.:

* `(x, y) -> return x + y`
* `(x, y) -> { System.out.println(x); System.out.println(y); }`
* `name -> System.out.println(name)`

For further information read the Java Tutorial at section about [Lambda Expressions] (http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html).

## Links

* [JDK 8 Documentation](https://docs.oracle.com/javase/8/)
* [JDK 8](https://openjdk.java.net/projects/jdk8/)
* [Java Programming Language Enhancements](https://docs.oracle.com/javase/8/docs/technotes/guides/language/enhancements.html#javase8)
