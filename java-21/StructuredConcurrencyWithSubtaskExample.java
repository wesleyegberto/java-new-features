import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.*;
import static java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * To run: `java --source 21 --enable-preview StructuredConcurrencyWithSubtaskExample.java`
 */
public class StructuredConcurrencyWithSubtaskExample {
	public static void main(String[] args) throws Exception {
		var message = new StructuredConcurrencyWithSubtaskExample().parallelSearch("42");
		System.out.println(message);
	}

	private String parallelSearch(String userId) throws InterruptedException, ExecutionException {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			// JDK 21: changed `fork` to return `Subtask` instead of `Future`
			Subtask<String> userName = scope.fork(() -> this.findName(userId));
			Subtask<String> answer = scope.fork(() -> this.findPower(userId));

			scope.join();
			scope.throwIfFailed();

			// `Subtask::get` behaves like `Future::resultNow`
			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.get(), answer.get());
		}
	}

	private String findName(String userId) {
		System.out.println("Searching name for user ID: " + userId);
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
