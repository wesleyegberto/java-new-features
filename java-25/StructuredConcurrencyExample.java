import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.StructuredTaskScope.FailedException;
import static java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * To run: `java --source 25 --enable-preview StructuredConcurrencyWithSubtaskExample.java`
 */
public class StructuredConcurrencyWithSubtaskExample {
	public static void main(String[] args) throws Exception {
		var message = new StructuredConcurrencyWithSubtaskExample().parallelSearch("42");
		System.out.println(message);
	}

	private String parallelSearch(String userId) throws InterruptedException, ExecutionException {
		// now we use static factory method `StructuredTaskScope.open` to create a scope
		// we can pass different policy from `Joiner` to control the behavior of the scope
		// default policy of `open()` is `Joiner.allSuccessfulOrThrow()`
		try (var scope = StructuredTaskScope.open()) {
			// JDK 21: changed `fork` to return `Subtask` instead of `Future`
			Subtask<String> userName = scope.fork(() -> this.findName(userId));
			Subtask<String> answer = scope.fork(() -> this.findPower(userId));

			// don't need to call `scope.throwIfFailed()` because now we have policy
			// return null when using default policy or throws a `FailedException`
			scope.join();

			// `Subtask::get` behaves like `Future::resultNow`
			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.get(), answer.get());
		} catch (FailedException ex) {
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
