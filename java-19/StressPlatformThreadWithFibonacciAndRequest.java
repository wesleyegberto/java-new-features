import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Run:
 * - `javac Fibonacci.java`
 * - `java --enable-preview --source 19 <FileName.java>`
 */
public class StressPlatformThreadWithFibonacciAndRequest {
	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();

		IntStream.range(0, 100).forEach(i -> {
			executor.submit(() -> {
				Fibonacci.fibWithRequest(i, 40, executor)
					.thenAccept(result -> {
						System.out.printf("Platform Thread %d - %d%n", i, result);
					})
					.exceptionally(ex -> {
						System.out.printf("Platform Thread %d - error: %s%n", i, ex.getMessage());
						return null;
					});;
			});
		});
	}
}
