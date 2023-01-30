import jdk.incubator.concurrent.*;

/**
 * Run: `java --source 20 --enable-preview --add-modules jdk.incubator.concurrent ScopedValueUsageWithReturnValueExample.java`
 */
public class ScopedValueUsageWithReturnValueExample {
	final static ScopedValue<Integer> MAIN_SCOPE = ScopedValue.newInstance();

	public static void main(String[] args) throws Exception {
		// we use `call` to run a scope and get it returned value
		var result = ScopedValue.where(MAIN_SCOPE, 42)
			.call(() -> { // throws Exception
				var calculator = new Calculator();
				return calculator.calculate();
			});
		System.out.println("Result from calculation: " + result);
	}
}

class Calculator {
	public int calculate() {
		var seed = ScopedValueUsageWithReturnValueExample.MAIN_SCOPE.get();
		return seed + 42;
	}
}
