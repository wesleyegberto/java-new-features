import java.util.Random;

/*
 * To run: `java --enable-preview --source 22 StatementsBeforeSuperExamples.java`
 */
public class StatementsBeforeSuperExamples {
	public static void main() {
		new SimpleExample(42);
		new SimpleExample();
		new SimpleExample("42");
	}
}

class BaseClass {
	protected int value;

	BaseClass(int value) {
		System.out.println("BaseClass constructor: " + value);
		this.value = value;
	}
}

class SimpleExample extends BaseClass {
		SimpleExample(int v) {
		// before
		// super(validPositive(v))

		// we can perform validation (fail fast)
		if (v <= 0)
			throw new IllegalArgumentException("Value must be positive");
		super(v);
		System.out.println("Initialized with value: " + v);
	}

	SimpleExample() {
		// now we can call static method
		System.out.println("Randomizing");
		int v = (int) (Math.random() * 1000);
		super(v);
		System.out.println("Initialized with random value: " + v);
	}

	SimpleExample(String value) {
		// we can only access static members of this class
		int v = parseValue(value);
		super(v);
		System.out.println("Initialized with parsed value: " + v);
	}

	static int parseValue(String value) {
		System.out.println("Parsing");
		return Integer.parseInt(value);
	}
}
