import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Run:
 * - `javac Fibonacci.java`
 * - `java --enable-preview --source 19 <FileName.java>`
 */
public class StressVirtualThreadWithFibonacci {
	public static void main(String[] args) {
		var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

		IntStream.range(0, 50).forEach(i -> {
			executor.submit(() -> {
				Fibonacci.parallelFib(47, executor)
					.thenAccept(result -> {
						System.out.printf("Virtual Thread %d - %d%n", i, result);
					});
			});
		});

		while (!executor.isTerminated()) {
			;
		}
	}
}
