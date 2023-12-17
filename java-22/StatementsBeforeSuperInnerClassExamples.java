/*
 * To run: `java --enable-preview --source 22 StatementsBeforeSuperInnerClassExamples.java`
 */
public class StatementsBeforeSuperInnerClassExamples {
	public static void main(String[] args) {
		var e1 = new Example1();
		var i1 = e1.new Inner1();

		var e2 = new Example2();

		var e3 = Example3.VALUE_3;
	}
}

class Base {
	Base() {
		System.out.println("Base constructor");
	}
}

class Example1 {
	private int counter;

	void hello() {
		System.out.println("Hello from Example1");
	}

	class Inner1 extends Base {
		Inner1() {
			// allowed cause when a new instance is created, the outer already exists
			Example1.this.counter++;
			hello();
			super();
			System.out.println("Inner1 constructor");
		}
	}

	Example1() {
		// Error: cannot reference this before supertype constructor has been called
		// new Inner1();

		super();
	}
}

class Example2 {
	static class Inner2 extends Base {
		Inner2() {
			System.out.println("Inner2 constructor");
		}
	}

	Example2() {
		System.out.println("Example2 constructor");
		new Inner2();
		super();
	}
}

enum Example3 {
	VALUE_1(1), VALUE_2(2), VALUE_3(3);

	private final int value;
	private final int valueSquared;

	Example3(int v, int vSquared) {
		System.out.printf("Enum - %d ^ 2 = %d%n", v, vSquared);
		this.value = v;
		this.valueSquared = vSquared;
	}

	Example3(int v) {
		var vv = v * v;
		this(v, vv);
	}

	public int getValue() {
		return value;
	}

	public int getValueSquared() {
		return valueSquared;
	}
}
