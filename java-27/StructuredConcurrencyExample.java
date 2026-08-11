import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.time.Duration;
import static java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * To run: `java --source 27 --enable-preview StructuredConcurrencyExample.java`
 */
public class StructuredConcurrencyExample {
	public static void main(String[] args) throws Exception {
		var example = new StructuredConcurrencyExample();

		System.out.println("=== Parallel search ===");
		System.out.println(example.parallelSearch("42"));

		System.out.println("\n=== Timeout example ===");
		System.out.println(example.timeoutExample("42"));

		System.out.println("\n=== Joiner example ===");
		System.out.println(example.joinerExample("13"));
	}

	private String parallelSearch(String userId) throws InterruptedException, ExecutionException {
		// now we use static factory method `StructuredTaskScope.open` to create a scope
		// we can pass different policy from `Joiner` to control the behavior of the scope
		// default policy of `open()` is `Joiner.allSuccessfulOrThrow()`
		try (var scope = StructuredTaskScope.open()) {
			Subtask<String> userName = scope.fork(() -> this.findName(userId));
			Subtask<String> answer = scope.fork(() -> this.findPower(userId));

			// don't need to call `scope.throwIfFailed()` because now we have policy
			// return null when using default policy or throws a `ExecutionException`
			scope.join();

			// `Subtask::get` behaves like `Future::resultNow`
			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.get(), answer.get());
		} catch (ExecutionException ex) {
			return "Failure: " + ex.getMessage();
		}
	}

	private String timeoutExample(String userId) throws InterruptedException {
		try (var scope = StructuredTaskScope.open(conf -> conf.withTimeout(Duration.ofSeconds(2)))) {
			Subtask<String> userName = scope.fork(() -> this.findName(userId));
			Subtask<String> answer = scope.fork(() -> this.findPower(userId));

			// calls `timeout` and throw exception on timeout
			// throws a ExecutionException with a CancelledByTimeoutException as cause
			scope.join();

			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.get(), answer.get());
		} catch (ExecutionException ex) {
			return "Failure: " + ex.getMessage();
		}
	}

	private String joinerExample(String userId) throws InterruptedException {
		var joiner = StructuredTaskScope.Joiner.allSuccessfulOrThrow(ex -> new AsyncException(ex.getMessage()));
		try (var scope = StructuredTaskScope.open(joiner)) {
			Subtask<String> userName = scope.fork(() -> this.findName(userId));
			Subtask<String> answer = scope.fork(() -> this.findPower(userId));

			// calls `timeout` and throw exception on timeout
			// throws a ExecutionException with a CancelledByTimeoutException as cause
			scope.join();

			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.get(), answer.get());
		} catch (RuntimeException ex) {
			return "Failure: " + ex.getMessage();
		}
	}

	private String findName(String userId) {
		System.out.println("Searching name for user ID: " + userId);
		try {
			Thread.sleep(500);
		} catch (Exception ex) {}
		if ("13".equals(userId)) {
			throw new RuntimeException("User unlucky");
		}
		return "Thomas Anderson";
	}

	private String findPower(String userId) {
		System.out.println("Calculating power for user ID: " + userId);
		try {
			Thread.sleep(3000);
		} catch (Exception ex) {}
		return "Over 9000";
	}
}

class AsyncException extends RuntimeException {
	public AsyncException(String message) {
		super("Erro while executing subtask: " + message);
	}
}
