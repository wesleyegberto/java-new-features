import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Run: `java --enable-preview --source 19 <FileName.java>`
 */
public class StressPlatformThread {
	public static void main(String[] args) {
		var executor = Executors.newCachedThreadPool();
		// WARNING: 10K threads sleeping 500 may restart your computer (save everything before run it!)
		// IntStream.range(0, 10_000).forEach(i -> {
		IntStream.range(0, 1_000).forEach(i -> {
			executor.submit(() -> {
				System.out.printf("Thread %d - %s%n", i, Thread.currentThread().getName());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.printf("Thread %d - %s interrupted%n", i, Thread.currentThread().getName());
				}
			});
		});
	}
}
