import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simple project to show how to use the VirtualThread.
 * To run: `java --enable-preview --source 19 VirtualThreadExecutorTest.java`
 */
public class VirtualThreadExecutorTest {
	public static void main(String[] args) throws Exception {
		var threadFactory = Thread.ofVirtual().factory();

		ExecutorService executorService = Executors.newThreadPerTaskExecutor(threadFactory);

		var result = executorService.submit(() -> {
			String name = Thread.currentThread().getName();
			System.out.printf("Hello World from virtual Thread called %s!\n", name);
			return 42;
		});

		System.out.println("Answer: " + result.get());
	}
}
