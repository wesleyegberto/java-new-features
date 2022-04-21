/**
 * Simple project to show how to use the VirtualThread.
 * To run: `java --enable-preview --source 19 VirtualThreadHelloWorld.java`
 */
public class VirtualThreadHelloWorld {
	public static void main(String[] args) throws Exception {
		Thread.startVirtualThread(() -> {
			String name = Thread.currentThread().getName();
			System.out.printf("Hello World from virtual Thread called %s!\n", name);
		});

		Thread.ofVirtual()
			.name("Virtual-Thread-From-Builder")
			.start(() -> {
				String name = Thread.currentThread().getName();
				System.out.printf("Hello World from virtual Thread called %s!\n", name);
			});

		Thread.ofPlatform()
			.name("Real-Thread-From-Builder")
			.start(() -> {
				String name = Thread.currentThread().getName();
				System.out.printf("Hello World from real Thread called %s!\n", name);
			});

		Thread.sleep(5000);
	}
}