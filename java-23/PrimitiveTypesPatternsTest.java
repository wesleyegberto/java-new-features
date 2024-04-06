/*
 * To run: `java --enable-preview --source 23 PrimitiveTypesPatternsTest.java`
 */

void main() {
	beforeJdk23();

	instancePrimitiveTypePattern();
	switchPrimitiveTypePattern();
}

void beforeJdk23() {
	int status = 2;
	var message = switch (status) {
		case 0 -> "ok";
		case 1 -> "not bad";
		case 2 -> "not ok";
		default -> "unknown - " + status;
	};
	System.out.println("Staus: " + message);

	Number statusRef = 0;
	message = switch (statusRef) {
		case Integer i when i == 0 -> "ok";
		case Integer i when i > 0 && i < 10 -> "not bad";
		case Integer i -> "not ok";
		case Long _ -> "not allowed to use long";
		default -> "unknown type - " + statusRef;
	};
	System.out.println("Staus: " + message);
}

void instancePrimitiveTypePattern() {
	System.out.println("=== instanceof ===");
	int i = 42;

	if (i instanceof byte b) { // exact conversion
		System.out.println("byte is " + b);
	}
	if (i instanceof short s) { // exact conversion
		System.out.println("short is " + s);
	}

	int i2 = 16_777_217;
	System.out.println("i2 is short? " + (i2 instanceof short));
	System.out.println("i2 is long? " + (i2 instanceof long)); // unconditionally exact
	System.out.println("i2 is float? " + (i2 instanceof float)); // not exact conversion
	System.out.println("i2 is double? " + (i2 instanceof double)); // exact conversion
	System.out.println("i2 is Number? " + (i2 instanceof Number)); // unconditionally exact

	double d = 42.0d;
	System.out.println("d is short? " + (d instanceof short)); // exact conversion
	System.out.println("d is float? " + (d instanceof float)); // exact conversion
	System.out.println("d is int? " + (d instanceof int)); // exact conversion
}

void switchPrimitiveTypePattern() {
	System.out.println("=== switch ===");

	var message = switch (42) {
		case byte b -> "byte matched";
		case short s -> "short matched";
		case long l -> "long matched";
	};
	System.out.println(message);

	boolean truth = true;
	message = switch (truth) {
		case true -> "It is true";
		case false -> "Oddly, it is false";
	};
	System.out.println("Boolean var: " + message);

	float f = 41.99999f;
	message = switch (f) {
		// if we don't use `f`: error: constant label of type int is not compatible with switch selector type float
		case 0f -> "zero";
		case 42.9f -> "almost universe answer";
		case 41.99999f -> "almost universe answer";
		// float <= 32 or >= 32 with more than 5 decimals will be rounded
		// we get an error: duplicate case label (rounded to case 42f)
		// case 41.999999f -> "almost universe answer?";
		case 42f -> "universe answer";
		case 42.09f -> "almost universe answer?";
		case 42.5f -> "universe answer plus half";
		case int i2 when i2 == 84 -> "universe answer times two";
		case int _ -> "is a int but not 42";
		// unconditional type pattern (a float is always a double)
		case double d -> "default case =)";
	};
	System.out.println("Switch f: " + message);
}
