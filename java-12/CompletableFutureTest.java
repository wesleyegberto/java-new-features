import java.util.concurrent.*;

public class CompletableFutureTest {
	public static void main(String[] args) {
		CompletableFuture.runAsync(CompletableFutureTest::workMethod)
			.exceptionallyCompose(ex -> {
				return CompletableFuture.runAsync(() -> System.out.println("Got some error but now I can keep going"));
			});

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ex) {}
	}

	public static void workMethod() {
		System.out.println("Working...");
		throw new IllegalStateException();
	}
}