import java.util.Scanner;
import java.util.concurrent.Executors;

/**
 * Try to run in JDK 21 and 24.
 * To run: `java -Djdk.virtualThreadScheduler.parallelism=2 -Djdk.tracePinnedThreads=short SynchronizeVirtualThreadTest.java`
 */
public class SynchronizeVirtualThreadTest {
	public static void main(String[] args) {
		// platform threads runs normally
		// var executor = Executors.newFixedThreadPool(2);

		// until Java 24, with parallelism 2, only two virtual thread will run by time
		// the virtual threads gets pinned and blocked waiting forever
		var factory = Thread.ofVirtual().name("VT-", 0).factory();
		var executor = Executors.newThreadPerTaskExecutor(factory);

		var riskOperation = new RiskOperation();

		for (int i = 0; i < 5; i++) {
			executor.submit(riskOperation::doSomething);
		}

		while (!executor.isTerminated()) {
			;
		}
	}
}

class RiskOperation {
	void doSomething() {
		log("About to start a synchronized work");
		this.doSynchronizedWork();
		log("Work done");
	}

	synchronized void doSynchronizedWork() {
		log("Doing synchronized work...");
		try {
			Thread.sleep(1_000);
		} catch (InterruptedException ex) {}
	}

	void log(String message) {
		System.out.printf("%s - %s%n", Thread.currentThread().toString(), message);
	}
}
