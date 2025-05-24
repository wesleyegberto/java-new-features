import module java.base;

import java.util.concurrent.*;

/**
 * Run: `java ScopedValueUsageExample.java`
 */
public class ScopedValueUsageExample {
	final static ScopedValue<String> MAIN_SCOPE = ScopedValue.newInstance();

	public static void main(String[] args) {
		System.out.println("Starting scoped value");

		// we can share a value from here
		ScopedValue.where(MAIN_SCOPE, "message from main")
			.run(() -> {
				var worker = new Worker();
				worker.execute();
			});
		System.out.println("main ended");
	}
}

class Worker {
	final static ScopedValue<String> WORKER_SCOPE = ScopedValue.newInstance();

	public void execute() {
		System.out.println("=== Read value ===");
		// accessing the value from the scope
		System.out.println("shared value from main: " + ScopedValueUsageExample.MAIN_SCOPE.get());

		// we can check if the scope is bounded to current thread
		System.out.println("Is scope bounded to [" + Thread.currentThread().getName() + "]: " + WORKER_SCOPE.isBound());

		// we cannot access it from outside its scope (NoSuchElementException)
		// there is also a `orElseThrow` to provide a different exception
		try {
			var invalidAccess = WORKER_SCOPE.get();
			invalidAccess = WORKER_SCOPE.orElseThrow(() -> new IllegalStateException("Value not bounded"));
		} catch (NoSuchElementException ex) {
			System.out.println("Exception trying to access an unbounded scope: " + ex.getMessage());
		}

		// we can use `orElse` to provide a default value of an unbounded scope
		var defaultValue = WORKER_SCOPE.orElse("default value");
		System.out.println("read default value from a unbounded scope without exception (orElse): " + defaultValue);

		System.out.println("=== Nested Scope ===");
		// we can create a nested scope
		ScopedValue.where(WORKER_SCOPE, "message from worker")
				.run(() -> {
					// the outmost scope will still works
					var messageFromMain = ScopedValueUsageExample.MAIN_SCOPE.get();
					var messageFromWorker = WORKER_SCOPE.get();
					System.out.println("shared value to inner scope from main: " + messageFromMain);
					System.out.println("shared value to inner scope from worker: " + messageFromWorker);
				});

		System.out.println("=== Return value ===");
		// we use `call` to run a scope and get it returned value
		var result = ScopedValue.where(WORKER_SCOPE, "21")
			.call(() -> {
				var calculator = new Calculator();
				return calculator.calculate();
			});
		System.out.println("Result from calculation: " + result);

		System.out.println("=== Rebinded Scope Value ===");
		// we can create a new scope that rebinds a new value to the created scope (when using the same variable)
		ScopedValue.where(ScopedValueUsageExample.MAIN_SCOPE, "message from worker over main")
				.run(() -> {
					// the outmost scope will still works
					var rebindedMessage = ScopedValueUsageExample.MAIN_SCOPE.get();
					System.out.println("shared value from shadowed scope: " + rebindedMessage);
				});

		// but the original scope from main will still have its initial value (immutable)
		System.out.println("shared value from main after all scopes: " + ScopedValueUsageExample.MAIN_SCOPE.get());

		System.out.println("=== Failure Handle ===");
		try {
			// with `where` we get checked exception type inference (requiring try-catch block)
			var riskResult = ScopedValue.where(WORKER_SCOPE, "message for a risk move")
					.call(() -> {
						var message = WORKER_SCOPE.get();

						if (message.length() > 15) {
							throw new MessageTooLongException();
						}
						return 42;
					});
			System.out.println("Result from risk move: " + riskResult);
		} catch (MessageTooLongException ex) {
			System.err.println("Error during risk move: " + ex.getMessage());
		}
	}
}

class Calculator {
	public String calculate() {
		var seed = ScopedValueUsageExample.MAIN_SCOPE.get();
		return seed + " concat " + 42;
	}
}

class MessageTooLongException extends Exception {
	MessageTooLongException() {
		super("Message provided in the scope was too long");
	}
}

