import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import javax.management.RuntimeErrorException;

/**
 * To run: `java --source 25 --enable-preview StructuredConcurrencyWithScopedValueExample.java`
 */
public class StructuredConcurrencyWithScopedValueExample {
	private final static ScopedValue<String> USER_ID = ScopedValue.newInstance();

	public static void main(String[] args) {
		new StructuredConcurrencyWithScopedValueExample().run();
	}

	public void run() {
		try {
			var result = ScopedValue.where(USER_ID, "neo").call(this::parallelHandle);
			System.out.println(result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String parallelHandle() throws InterruptedException, ExecutionException {
		// when we create a scope, the scoped values are captured
		try (var scope = StructuredTaskScope.open()) {
			// the child scopes can use its parent's scoped value bindings
			Subtask<String> userName = scope.fork(this::findUserName);
			Subtask<String> answer = scope.fork(this::findPower);

			scope.join();

			return "The real name of '%s' is '%s' and its power is %s"
					.formatted(USER_ID.get(), userName.get(), answer.get());
		}
	}

	private String findUserName() {
		var userId = USER_ID.get();
		System.out.println("Searching name for user ID: " + userId);
		try {
			Thread.sleep(500);
		} catch (Exception ex) {}
		return userId;
	}

	private String findPower() {
		var userId = USER_ID.get();
		System.out.println("Calculating power for user ID: " + userId);
		try {
			Thread.sleep(3000);
		} catch (Exception ex) {}
		return "Over 9000";
	}
}
