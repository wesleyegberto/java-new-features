import java.time.Duration;
import java.util.stream.IntStream;
import java.util.ArrayList;

import jdk.jfr.consumer.RecordingStream;

/***
 * The profiles configurations is stored in: `$JAVA_HOME/lib/jrf/`
 */
public class JFREventStreamTest {
	public static void main(String[] args) throws InterruptedException {
		var task = new FibonacciCalculator(Thread.currentThread());

		var thread = new Thread(task, "t-fib");
		thread.start();

		try (var rs = new RecordingStream()) {
			rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
			rs.enable("jdk.GarbageCollection").withPeriod(Duration.ofSeconds(1));
			rs.enable("jdk.GCHeapSummary").withPeriod(Duration.ofSeconds(1));

			rs.onEvent("jdk.CPULoad", event -> {
				System.out.printf("\tCPU load: %f%% - %f%%%n",
					(event.getFloat("jvmUser") * 100),
					(event.getFloat("machineTotal") * 100)
				);
			});
			rs.onEvent("jdk.GCHeapSummary", event -> {
				System.out.printf("\tGC - Summary - %s %f MB%n",
					event.getString("when"),
					(event.getFloat("heapUsed") / 1_000_000)
				);

			});
			rs.onEvent("jdk.GarbageCollection", event -> {
				System.out.printf("\tGC - Cause %s - Durantion: %.04fms%n", event.getString("cause"), (event.getFloat("duration") / 1_000_000));
			});
			rs.startAsync();

			thread.join();
		}

	}
}

class FibonacciCalculator implements Runnable {
	private Thread thread;

	FibonacciCalculator(Thread thread) {
		this.thread = thread;
	}

	public void run() {
		System.out.println("Starting calculation");
		int number = 40;
		while (true) {
			System.out.println("Calculating for " + number);
			var result = calculate(number);
			System.out.println("Result for " + number + " is " + result);
			if (!thread.isAlive()) {
				System.out.println("Ending calculation");
				break;
			}

			number += 5;
		}
	}

	/**
	 * Bad implementation to take much time.
	 */
	public long calculate(int n) {
		if (n <= 1)
			return 1;
		var list = new ArrayList<>(n);
		IntStream.range(0, n).forEach(__ -> list.add(new Object()));
		return calculate(n - 1) + calculate(n - 2);
	}
}