import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.RuntimeErrorException;

import jdk.incubator.concurrent.StructuredTaskScope;

/**
 * This class shows an example of unstructured concurrency shown in the JEP.
 *
 * To run: `java --source 19 --enable-preview --add-modules jdk.incubator.concurrent StructuredConcurrencyMotivation.java`
 */
public class StructuredConcurrencyMotivation {
	public static void main(String[] args) throws Exception {
		var example = new StructuredConcurrencyMotivation();

		System.out.println("\nExecutorService:");
		try {
			// at subtask exception, the other subtasks will continue to run
			System.out.println(example.unstructuredHandle());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("\nStructuredTaskScope:");
		try {
			// at subtask failure, we can see other subtasks will be canceled (and return much earlier)
			System.out.println(example.structuredHandle());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Problems:
	 * - If findUser throws, then handle will throw when calling user.get(). fetchOrder will continue to run in its own
	 *    thread even after handle has failed. This is a thread leak, which, at best, wastes resources;
	 *    at worst, fetchOrder will interfere with other tasks.
	 * - If the thread executing handle is interrupted, the interruption is not propagated to subtasks. Both the findUser
	 *    and fetchOrder threads will leak, continuing to run even after handle has failed.
	 * - If findUser takes a long time to execute, but fetchOrder fails in the meantime, then handle will wait unnecessarily
	 *    for findUser by blocking on user.get() rather than canceling it. Only after findUser completes and user.get()
	 *    returns will order.get() throw, causing handle to fail.
	 */
	Response unstructuredHandle() throws ExecutionException, InterruptedException {
		var es = Executors.newCachedThreadPool();
		try {
			Future<String> user = es.submit(() -> findUser());
			Future<Integer> order = es.submit(() -> fetchOrder());
			String theUser = user.get(); // Join findUser
			int theOrder = order.get(); // Join fetchOrder
			return new Response(theUser, theOrder);
		} finally {
			es.shutdown();
		}
	}

	/**
	 * Benefits:
	 * - Error handling with short-circuiting: If either findUser or fetchOrder fail, the other will be cancelled if it
	 *    hasn't yet completed (this is managed by the cancellation policy implemented by ShutdownOnFailure;
	 *    other policies are possible too).
	 * - Cancellation Propagation: If the thread running handle is interrupted before or during the call to join, both
	 *    forks will be automatically cancelled when the scope is exited.
	 * - Clarity: The above code has a clear structure: set up the child subtasks, wait for them (either to complete
	 *    or to becanceled), and then decide whether to succeed (and process the results of the child tasks, which are
	 *    already finished) or fail (and the subtasks are already finished, so there's nothing more to clean up.)
	 * - Observability: A thread dump, as described below, will clearly demonstrate the task hierarchy, with the threads
	 *    running findUser and fetchOrder shown as children of the scope.
	 */
	Response structuredHandle() throws ExecutionException, InterruptedException {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Future<String> user = scope.fork(() -> findUser());
			Future<Integer> order = scope.fork(() -> fetchOrder());

			scope.join(); // Join both forks
			scope.throwIfFailed(); // ... and propagate errors

			// Here, both forks have succeeded, so compose their results
			return new Response(user.resultNow(), order.resultNow());
		}
	}

	String findUser() {
		System.out.println("Finding user");
		sleep("Finding user");
		System.out.println("Found user");
		return "Marley";
	}

	Integer fetchOrder() {
		System.out.println("Fetching order");
		// sleep("Fetching order");
		throw new RuntimeException();
		// System.out.println("Fetched order");
		// return Integer.valueOf(42);
	}

	void sleep(String task) {
		try {
			Thread.sleep(10000);
		} catch (Exception ex) {
			System.out.println(task + " canceled");
		}
	}
}

record Response(String user, Integer order) {}
