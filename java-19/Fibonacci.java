import java.util.concurrent.Executor;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.time.Duration;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * fib of 47 takes ~11s
 * fib of 49 fakes ~30s
 */
public class Fibonacci {
	public static long fib(int n) {
		if (n <= 2)
			return 1;
		return fib(n - 1) + fib(n - 2);
	}

	public static CompletableFuture<Long> parallelFib(int n, Executor executor) {
		CompletableFuture<Long> partOne = CompletableFuture.supplyAsync(() -> fib(n - 1), executor);
		CompletableFuture<Long> partTwo = CompletableFuture.supplyAsync(() -> fib(n - 2), executor);
		return partOne.thenCombineAsync(partTwo, (resultOne, resultTwo) -> resultOne + resultTwo, executor);
	}

	public static CompletableFuture<Long> fibWithRequest(int i, int n, Executor executor) {
		return CompletableFuture.supplyAsync(() -> fib(n), executor)
				.thenComposeAsync(result -> {
					var request = HttpRequest.newBuilder()
							.GET()
							.uri(URI.create("https://httpbin.org/delay/5"))
							.build();
					var httpClient = HttpClient.newBuilder()
							.version(HttpClient.Version.HTTP_1_1)
							.connectTimeout(Duration.ofSeconds(30))
							.executor(executor)
							.build();
					try {
						System.out.printf("Thread %d - requesting%n", i);
						return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
							.thenApplyAsync(resp -> result);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}, executor);
	}
}
