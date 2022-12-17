# Project Valhalla

From the project's wiki:

> Project Valhalla plans to augment the Java object model with value objects and user-defined primitives,
> combining the abstractions of object-oriented programming with the performance characteristics of simple
> primitives.

## Running the Examples

To run the examples we need to compile first with primitive classe feature activated.
Then run it enabling primitive class, using single-file source code feature doesn't work.

```java
javac -XDenablePrimitiveClasses --source 20 <ClassName>.java

java -XX:+EnablePrimitiveClasses <ClassName>
```

## JEPs

### [JEP 401 - Primitive Classes](https://openjdk.org/jeps/401)

It will allow developer-declared primitive types in Java.

A primitive class is a special kind of value class that refine a new primitive type.

#### Motivations

> Primitives offer better performance, because they are typically inlined—stored directly (without headers or pointers) in
> variables, on the computation stack, and, ultimately, in CPU registers.
> 
> Hence, memory reads do not have additional indirections, primitive arrays are stored densely and contiguously in memory,
> primitive-typed fields can be similarly compact, primitive values do not require garbage collection, and primitive operations
> are performed within the CPU.

Certain properties of objects limit how much they can be optimized:

- reference variable may be null, requiring extra bit to encode null (a int of size 32bits will need to use 64bits just to enable the extra bit);
- reference variable must be modified atomically, it would require to inline the entire object to make an efficient atomic modification.

Primitive classes will give the capability to define new primitive types without thosese limitations.

#### Specification

A primitive class defines a new primitive type that have no identity.

Primitive type characteristics:

- can be thought as a sequence of field values, without any headers or extra pointers;
- it can have superinterfaces, type parameters, eenclosing instances, inner classes, overloaded constructors, static members, so on;
  - its fields are implicity final;
  - its fields cannot be a primitive type that depends on the declaring class (directly or indirectly);
    - the class must allow for flat, fized-size layout without cycles;
    - the only exception is a reference-type field were it only store a fixed object address.
- freely convert an instance between its value object and simpler primitive value (use its reference or its value);
- supports the `==` and `!=` operators recursively throughout its fields values;
- cannot be used as the operand of a synchronized statement;
- reads and writes are not guaranteed to be atomic;
- it is a null-free type:
  - the primitive value is a null-free type, cannot be assign to null without explicity using it as reference;
    - to use as reference we need to use `.ref` keyword (like `Point.ref aPoint`);
  - the variable will receive a default value;
  - default value for primitive type:
    - the default value is the initial instance whose fields are all set to their own default values;
    - we can use `default` keyword to access the default value;
    - the creation of the default value does not invoke any constructors or instance initializers, the JVM just spawn an instance with default values;
    - we cannot define a default value;
- supports array subtypes;


##### Usage

We can declare a primitive class with:

```java
primitive class Point implements Shape {
	private double x;
	private double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double x() { return x; }

	public double y() { return y; }

	public Point translate(double dx, double dy) {
		return new Point (x + dx, y + dy);
	}

	public boolean contains(Point p) {
		return equals(p);
	}
}

interface Shape {
	bool contains(Point p);
}
```

Primitive types support the `==` and `!=` operators, in this case, it will recursively compare the fields values.


```java
var p1 = new Point(1.1, 3.0);
var p2 = new Point(1.1, 3.0);

assert p1 == p2;

var p3 = p2.translate(0, 0);

assert p1 == p3;
```

Default values and null:

```java
// array initialize with default values
var points = new Point[5];

assert points[0].x() == 0.0;

// accessing the default value
assert Point.default.x() == 0.0;

assert points[0] == Point.default;
```

###### Reference Types

> Primitive values are monomorphic—they belong to a single type with a specific set of fields known at compile time and runtime.
> Values of different primitive types can't be mixed.

We must convert a primitive type to value object, using _value object conversion_ to use as polymorphic reference type.

The converstion will implicitly occurs when assigning from a primitive type to a reference type.
The result is an instance of the same class with a _different form_.

```java
var p1 = new Point(5.0, 5.0);

// implicity value object conversion
Shape s = p1;

asert s.getClass() == Point.class;
```

> When invoking an inherited method of a primitive type, the receiver value undergoes value object conversion to have the type
> expected by the method declaration.

**`ref` keyword**

We can use the `ref` contextual keyword to use reference type of a primitive type.
This way we can assign null to a variable.

```java
Point.ref p = null;

// the array will be filled with null
var points = new Point.ref[10];

assert points[0] == null;
```

> The relationship between the types `Point` and `Point.ref` is similar to the traditional relationship between the types `int` and `Integer`.
> However, `Point` and `Point.ref` both correspond to the same class declaration; the values of both types are instances of a single `Point` class.
> At run time, the conversion between a primitive value and a value object is more lightweight than traditional boxing conversion.

We can convert a reference type to a primitive value:

```java
Point.ref p = new Point(1.0, 1.0);
Point p2 = p;

Point.ref p3 = null;
Point p4 = p3; // NPE
```

> When invoking a method overridden by a primitive class, the receiver object undergoes primitive value conversion to have the
> type expected by the method declaration.

```java
Shape s = new Point(0.7, 3.2);
// 'contains' is declared by Point
s.contains(Point.default); // primitive value conversion
```

**Overload Resolution and Type Arguments**

> Value object conversion and primitive value conversion are allowed in loose, but not strict, invocation contexts.

It will behave like the matching for boxing and unboxing: the method overload that does not require conversion will be used.

```java
void m(Point p, int i) { ... }
void m(Point.ref pr, Integer i) { ... }

void test(Point.ref pr, Integer i) {
	m(pr, i); // prefers the second declaration
	m(pr, 0); // ambiguous
}
```

For now (until another JEP), generics only work with reference types. If we use a primitive type, it will do a value object conversion:

```java
// infers List<Point.ref>
var list = List.of(new Point(1.0, 10.0));
```

**Array Subtyping**

Until the JEP 402 is ready, we cannot mix primitive array types and reference array types:

```java
// error: incompatible types
Object[] arr = new int[5];
```

With primitive types we can mix them, a `Point[]` is a subtype of `Point.ref[]`, which is a subtype of `Object[]`.

```java
// primitive values array
var points = new Point[5];

// the compiler checks the type hierarchy
Object[] arr = points;

// value object conversion (primitive value Point to Object)
Object o1 = arr[0];

// primitive value conversion because the arr points to a primitive values array
arr[0] = new Point(5.0, 5.0);

// NPE because a primitive value conversion occurs
arr[1] = null;
```

## Links

* [Project Wiki](https://openjdk.org/projects/valhalla/)
* [Early-Access Builds](https://jdk.java.net/valhalla/)
* [JEP draft - Value Objects](https://openjdk.org/jeps/8277163)
* [JEP 401 - Primitive Classes](https://openjdk.org/jeps/401)
* [Java Value Objects in Action with Valhalla](https://www.youtube.com/watch?v=ViZkEgshiXI)

