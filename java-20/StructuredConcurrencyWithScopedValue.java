import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.RuntimeErrorException;

import jdk.incubator.concurrent.*;

/**
 * To run: `java --source 20 --enable-preview --add-modules jdk.incubator.concurrent StructuredConcurrencyWithScopedValue.java`
 */
public class StructuredConcurrencyWithScopedValue {
	private final static ScopedValue<String> USER_ID = ScopedValue.newInstance();

	public static void main(String[] args) {
		new StructuredConcurrencyWithScopedValue().run();
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
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			// the child scopes can use its parent's scoped value bindings
			Future<String> userName = scope.fork(this::findUserName);
			Future<String> answer = scope.fork(this::findPower);

			scope.join();
			scope.throwIfFailed();

			var userId = USER_ID.get();
			return "The real name of '%s' is '%s' and its power is %s".formatted(userId, userName.resultNow(), answer.resultNow());
		}
	}

	private String findUserName() {
		var userId = USER_ID.get();
		System.out.println("Searching name for user ID: " + userId);
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
