# Java 8 New Features
@author Wesley Egberto

A project to explore more about the new features of Java...

- new methods from APIs
- lambda expression to create anonymous method
- lambda access scope (enclosing scope)
- lambda expression to call constructors through method reference
- Streams


Lambda expression consiste of: 

- A comma-separated list of formal parameters enclosed in parentheses: (paramX, paramY) or singleParam or ()
- An [weird] arrow token: ->
- A body, which consists of a single expression or a statement block: {} or a single expression without ;

E.g.:

- (x, y) -> return x + y
- (x, y) -> { System.out.println(x); System.out.println(y); }
- name -> System.out.println(name)


For further information read the Java Tutorial at section about [Lambda Expressions] (http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html).

