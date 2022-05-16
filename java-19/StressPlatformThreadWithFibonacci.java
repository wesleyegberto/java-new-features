import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Run:
 * - `javac Fibonacci.java`
 * - `java --enable-preview --source 19 <FileName.java>`
 */
public class StressPlatformThreadWithFibonacci {
	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();

		IntStream.range(0, 50).forEach(i -> {
			executor.submit(() -> {
				Fibonacci.parallelFib(47, executor)
					.thenAccept(result -> {
						System.out.printf("Platform Thread %d - %d%n", i, result);
					});
			});
		});
	}
}
