/**
 * Simple project to show how to use the VirtualThread.
 * To run: `java --enable-preview --source 19 VirtualThreadCountThreads.java`
 */
public class VirtualThreadCountThreads {
	public static void main(String[] args) throws Exception {
		System.out.println("Open Java VisualVM to see threads");

		// pause to open Java VisualVM
		Thread.sleep(10_000);
		System.out.println("Starting Threads");

		new Thread(() -> {
				String name = Thread.currentThread().getName();
				System.out.printf("Hello World from real Thread called %s!\n", name);

				try {
					System.out.printf("Thread %s is going to sleep\n", name);
					Thread.sleep(15000);
				} catch (Exception ex) {}
				finally {
					System.out.printf("Thread %s woke up\n", name);
				}
			})
			.start();

		Thread.startVirtualThread(() -> {
			String name = Thread.currentThread().getName();
			System.out.printf("Hello World from VirtualThread called %s!\n", name);
			try {
				System.out.printf("VirtualThread %s is going to sleep\n", name);
				Thread.sleep(15000);
			} catch (Exception ex) {}
			finally {
				System.out.printf("VirtualThread %s woke up\n", name);
			}
		});

		// Virtual thread does not count as a thread
		System.out.println("Number of threads: " + Thread.activeCount());

		Thread.sleep(60000);
	}
}
