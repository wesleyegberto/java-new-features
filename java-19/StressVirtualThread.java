import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Run: `java --enable-preview --source 19 <FileName.java>`
 */
public class StressVirtualThread {
	public static void main(String[] args) {
		var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().factory());

		IntStream.range(0, 10_000).forEach(i -> {
			executor.submit(() -> {
				System.out.printf("VirtualThread %d - %s%n", i, Thread.currentThread().getName());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.printf("VirtualThread %d - %s interrupted%n", i, Thread.currentThread().getName());
				} finally {
					System.out.printf("VirtualThread %d - %s woke up%n", i, Thread.currentThread().getName());
				}
			});
		});

		while (!executor.isTerminated()) {
			;
		}
	}
}
