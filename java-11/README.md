# Java 11

## Features

### Language

* Removal of modules and deprecated API
  * Removal of Java EE Modules
    * JAF
    * CORBA
    * JTA
    * JAXB
    * JAX-WS
    * Commons Annotation
  * JavaFX
* Scripting
* Shebang
  * `#!/opt/jdk-11/bin/java --source 11`
* var in lambda expressions
  * allowing to use annotation without specifying the type: `.filter((@Nonnull var item) -> isAllowed(item))`
* API improvements
  * String
    * `lines()`: to streaming the lines from a string
    * `strip()`, `stripLeading()` and `stripTrailing()`
    * `repeat(int)`
    * `isBlank()`
  * Path
    * `of(String, String...)` and `of(URI)`
  * Files
    * `readString(Path)`: read the entire content from a file as a String - `Files.readString(Path.of("message.txt"))`
    * `writeString(Path, CharSequence, OpenOption...)` write a String to a file - `Files.writeString(Path.of("message.txt"), updatedMessage)`
  * Null I/O
    * `InputStream.nullInputStream()`: empty input stream
    * `OutputStream.nullOutputStream()`: output stream that discards input bytes
    * `Reader.nullReader()`: empty reader
    * `Writer.nullWriter()`: writer that discards input content
  * New better way to turn a collection into an array
    * `String[] array = list.toArray(String[]::new)`
  * `Optional::isEmpty`
  * `Predicate::not`
  * `Pattern::asMatchPredicate`

## Links

* [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/index.html)
* [Java 11 Migration Guide](https://blog.codefx.org/java/java-11-migration-guide/)
* [Oracle's Z GC](https://wiki.openjdk.java.net/display/zgc/Main)
* [Replacements for deprecated JPMS modules with Java EE APIs](https://stackoverflow.com/questions/48204141/replacements-for-deprecated-jpms-modules-with-java-ee-apis/48204154#48204154)
* [Scripting Java 11, Shebang And All](https://blog.codefx.org/java/scripting-java-shebang/)
