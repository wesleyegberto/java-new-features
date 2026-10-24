import java.time.Duration;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shows the policies (Joiner) available to a StructuredTaskScope.
 *
 * To run: `java StructuredConcurrencyJoinersExample.java`
 */
public class StructuredConcurrencyJoinersExample {
	public static void main(String[] args) throws Exception {
		var example = new StructuredConcurrencyJoinersExample();

		System.out.println("=== allSuccessfulOrThrow ===");
		System.out.println(example.allSuccessful());

		System.out.println("\n=== anySuccessfulOrThrow ===");
		System.out.println(example.anySuccessful());

		System.out.println("\n=== awaitAllSuccessfulOrThrow ===");
		System.out.println(example.awaitAllSuccessful());

		System.out.println("\n=== allUntil ===");
		example.allUntil();

		System.out.println("\n=== Custom joiner ===");
		System.out.println(example.customJoiner());

		System.out.println("\n=== Custom joiner with timeout ===");
		System.out.println(example.customJoinerWithTimeout());
	}

	/**
	 * Subtasks return the same type and we want to process all of them.
	 * `join()` returns the list of results (in the order the subtasks were forked) or throws if any subtask fails.
	 */
	List<String> allSuccessful() throws Exception {
		try (var scope = StructuredTaskScope.open(Joiner.<String>allSuccessfulOrThrow())) {
			scope.fork(() -> fetchPrice("store-a", 300, false));
			scope.fork(() -> fetchPrice("store-b", 100, false));
			scope.fork(() -> fetchPrice("store-c", 200, false));

			return scope.join();
		}
	}

	/**
	 * Race between replicas: the first subtask that succeeds wins and the others are cancelled.
	 * It only throws if all subtasks fail.
	 */
	String anySuccessful() throws Exception {
		try (var scope = StructuredTaskScope.open(Joiner.<String>anySuccessfulOrThrow())) {
			scope.fork(() -> fetchPrice("replica-1", 2000, false));
			scope.fork(() -> fetchPrice("replica-2", 300, false));
			scope.fork(() -> fetchPrice("replica-3", 100, true)); // fails fast, but doesn't matter

			return scope.join();
		}
	}

	/**
	 * Subtasks return different types and we handle each one individually.
	 * `join()` returns null, it only waits and propagates any failure.
	 */
	String awaitAllSuccessful() throws Exception {
		try (var scope = StructuredTaskScope.open(Joiner.<Object>awaitAllSuccessfulOrThrow())) {
			Subtask<String> name = scope.fork(() -> fetchPrice("catalog", 100, false));
			Subtask<Integer> stock = scope.fork(() -> fetchStock(200));

			scope.join();

			return "%s (stock: %d)".formatted(name.get(), stock.get());
		}
	}

	/**
	 * The scope is cancelled when the predicate returns true (here, after 2 successful subtasks).
	 * `join()` returns all subtasks in the order they were forked, so we must check the state of each one.
	 */
	void allUntil() throws Exception {
		var successes = new AtomicInteger();
		var joiner = Joiner.<String>allUntil(subtask -> subtask.state() == Subtask.State.SUCCESS
				&& successes.incrementAndGet() >= 2);

		try (var scope = StructuredTaskScope.open(joiner)) {
			scope.fork(() -> fetchPrice("store-a", 100, false));
			scope.fork(() -> fetchPrice("store-b", 200, false));
			scope.fork(() -> fetchPrice("store-c", 3000, false));
			scope.fork(() -> fetchPrice("store-d", 150, true));

			// we are not interested in the individual results only on the final state
			for (var subtask : scope.join()) {
				var outcome = switch (subtask.state()) {
					case SUCCESS -> "SUCCESS: " + subtask.get();
					case FAILED -> "FAILED: " + subtask.exception().getMessage();
					case UNAVAILABLE -> "UNAVAILABLE (scope cancelled before it completed)";
				};
				System.out.println(outcome);
			}
		}
	}

	/**
	 * Our own policy: ignore the failures and collect only the successful results.
	 */
	List<String> customJoiner() throws Exception {
		try (var scope = StructuredTaskScope.open(new CollectSuccessesJoiner<String>())) {
			scope.fork(() -> fetchPrice("store-a", 100, false));
			scope.fork(() -> fetchPrice("store-b", 200, true));
			scope.fork(() -> fetchPrice("store-c", 300, false));

			return scope.join();
		}
	}

	/**
	 * `Joiner.timeout()` is called when the timeout expires, so the joiner can return a partial result
	 * instead of failing.
	 */
	List<String> customJoinerWithTimeout() throws Exception {
		try (var scope = StructuredTaskScope.open(new CollectSuccessesJoiner<String>(),
				conf -> conf.withTimeout(Duration.ofMillis(500)))) {
			scope.fork(() -> fetchPrice("store-a", 100, false));
			scope.fork(() -> fetchPrice("store-b", 200, false));
			scope.fork(() -> fetchPrice("store-slow", 5000, false));

			return scope.join();
		}
	}

	/**
	 * A joiner instance must never be reused, always create a new one.
	 */
	static class CollectSuccessesJoiner<T> implements Joiner<T, List<T>, RuntimeException> {
		private final Queue<T> results = new ConcurrentLinkedQueue<>();

		@Override
		public boolean onComplete(Subtask<T> subtask) {
			if (subtask.state() == Subtask.State.SUCCESS) {
				results.add(subtask.get());
			}
			return false; // don't cancel the scope
		}

		@Override
		public List<T> result() {
			return List.copyOf(results);
		}

		@Override
		public List<T> timeout() {
			System.out.println("Timeout reached, returning partial result");
			return result();
		}
	}

	String fetchPrice(String store, long delayMillis, boolean fail) throws InterruptedException {
		System.out.println("Fetching price from " + store);
		try {
			Thread.sleep(delayMillis);
		} catch (InterruptedException ex) {
			System.out.println(store + " canceled");
			throw ex;
		}
		if (fail) {
			throw new IllegalStateException(store + " unavailable");
		}
		System.out.println("Fetched price from " + store);
		return store + ": $" + delayMillis;
	}

	Integer fetchStock(long delayMillis) throws InterruptedException {
		Thread.sleep(delayMillis);
		return 42;
	}
}
