import java.util.*;

/**
 * To run: `java PatternMatchingForSwitchTest.java`
 */
public class PatternMatchingForSwitchTest {
	public static void main(String[] args) {
		switchExhaustivenessAndCompatibility();
		switchExpressionWithPatternMatching();
		switchScopeWithPatternMatching();
		switchEnhancedTypeChecking();
		switchWithGuardedCaseLabel();
		switchWithEnumConstants();
		recordErrorInSwitchPatternMatching();
	}

	static void switchExhaustivenessAndCompatibility() {
		/**
		 * compiler won't require exhaustiveness if it doesn't use any pattern or null label, or if selector
		 * expression is from a legacy type (char, byte, short, int, Character, Byte, Short, Integer, String or a enum)
		 */
		int i = 42;
		switch (i) {
			case 0:
				System.out.println("Zero");
				break;
			case 42:
				System.out.println("42!");
				break;
		}

		var shape = Shape.TRIANGLE;
		// default case way
		switch (shape) {
			case TRIANGLE:
				System.out.println("Is a triangle");
				break;
			case CIRCLE:
				System.out.println("Is a circle");
				break;
		}

		// lambda expression way
		switch (shape) {
			case TRIANGLE -> System.out.println("Is a triangle");
			case CIRCLE -> System.out.println("Is a circle");
		}
	}

	static void switchExpressionWithPatternMatching() {
		Object obj = 42;

		// if a switch use any feature from the JEP, the compiler will check for exhaustiveness
		String message = switch (obj) {
			case Integer i -> String.format("int %d", i);
			case String s -> String.format("string %s", s);
			case Double d -> String.format("double %d", d);
			// JDK 17: we can test null here, without check before the switch =)
			// switch case without null will throw a NullPointerException (compatibility)
			case null -> "null =s";
			// required to be exhaustive
			default -> obj.toString();
			// we can only combine null with default in a the same case
			// null, default -> obj.toString();
		};
		System.out.println(message);
	}

	static void switchScopeWithPatternMatching() {
		Number number = 42;

		String message = null;

		switch (number) {
			case Integer i when i == 0:
				System.out.println("zero =0");
				// doesn't allow fall-through (must have the break or yield when using `:` in a switch statement or expression)
				// error: illegal fall-through to a pattern
				break;

			case Integer x when x < 0:
				message = "zero or lower";
				break;
			case Integer n when n == 21:
				message = "half of the answer";
				break;
			case Integer n when n == 42:
				message = "answer";
				break;
			default:
				message = "unhandled";
		}
		System.out.println("switch statement: " + message);

		message = switch (number) {
			case Integer i when i == 0:
				System.out.println("zero =0");
				yield "zero =0";
			case Integer x when x < 0:
				yield "zero or lower";
			case Integer n when n == 21:
				yield "half of the answer";
			case Integer n when n == 42:
				yield "answer";
			default:
				yield "unhandled";
		};
		System.out.println("switch expression: " + message);
	}

	static void switchEnhancedTypeChecking() {
		Object value = 42;
		var message = switch (value) {
			case null -> "The value is `null`";
			case String s -> "Is String: " + s;
			case Integer n -> "is an integer: " + n;
			case Number n -> "Is a Number: " + n;
			case int[] ar -> "Is an array of number: " + ar;
			case List list -> "Is a list of some type: " + list;
			// can infer the record generic type
			case Wrapper(var v) -> "Wrapped value: " + v;
			default -> "Is untested type =(: " + value.toString();
		};
		System.out.println(message);
	}

	static void switchWithGuardedCaseLabel() {
		Object obj = 42;

		// JDK 20 changed `&&` to `when`
		String message = switch (obj) {
			// JDK 21 removes the ()
			// case Integer i when (i < 0) -> "negative number";
			case Integer i when i < 0 -> "negative number";
			case Integer i when i == 0 -> "zero";
			case Integer i when i > 0 && i <= 100 -> "positive number between 0 and 100";
			// must be after the guarded case as it is dominant
			case Integer i -> "number";
			// required to be exhaustive
			default -> obj.toString();
		};
		System.out.println(message);
	}

	static void switchWithEnumConstants() {
		Object shape = Shape.TRIANGLE;

		// before
		var shapeName = switch (shape) {
			case Shape s when s == Shape.CIRCLE -> "Circle";
			case Shape s when s == Shape.RECTANGLE -> "Rectangle";
			case Shape s when s == Shape.TRIANGLE -> "Triangle";
			default -> "none";
		};

		// JDK 21: we can use the qualified name when switching over an enum type
		// we should use all qualified name when at least one case is
		shapeName = switch (shape) {
			case Shape.CIRCLE -> "Circle";
			case Shape.RECTANGLE -> "Rectangle";
			case Shape.TRIANGLE -> "Triangle";
			default -> "other type";
		};
		System.out.println(shapeName);
	}

	static void recordErrorInSwitchPatternMatching() {
		var dot = new OneDimensionalPoint(10);

		switch (dot) {
			// will cause MatchException with wrapped exception (the record pattern completes abruptly with the ArithmeticException)
			case OneDimensionalPoint(var x): System.out.println("1D point");
			// the occurring in guarded clause, it just rethrows the exception
			// will cause ArithmeticException
			// case OneDimensionalPoint p when (p / 0 == 1): System.out.println("Non sense");
		}
	}
}

enum Shape { CIRCLE, RECTANGLE, TRIANGLE }

record OneDimensionalPoint(int x) {
	public int x() {
		return x / 0;
	}
}

record Wrapper<T>(T t) {}

