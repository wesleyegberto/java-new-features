import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.TimeUnit;

public class TransferQueueExample {
	public static void main(String[] args) throws Exception {
		TransferQueue<String> queue = new LinkedTransferQueue<>();

		var producer = new Producer(queue);
		var consumer = new Consumer(queue);

		new Thread(consumer).start();
		new Thread(producer).start();

		Thread.currentThread().join();
	}
}

class Producer implements Runnable {
	private TransferQueue<String> queue;
	private Scanner input;

	Producer(TransferQueue<String> queue) {
		this.queue = queue;
		this.input = new Scanner(System.in);
	}

	public void run() {
		log("started");
		while (true) {
			log("=== Enter the number: 1 - wait; 2 - wait with timeout; 3 - transfer timedout");

			var command = input.nextLine();
			switch (command) {
				case "1":
					send("wait");
					break;
				case "2":
					send("timeout");
					break;
				case "3":
					send("transfer_timeout");
					waitAndSendWithTimeout("command will not be received");
					break;
				default:
					log("Invalid command");
			}
		}
	}

	private void send(String command) {
		try {
			log("Sending command");
			queue.transfer(command);
			log("Command sent and received");
		} catch (Exception ex) {
			log(ex.getMessage());
		}
	}

	private void sendWithTimeout(String command) {
		try {
			boolean received = queue.tryTransfer(command, 3, TimeUnit.SECONDS);
			log("Command sent, received: " + received);
		} catch (Exception ex) {
			log(ex.getMessage());
		}
	}

	private void waitAndSendWithTimeout(String command) {
		try {
			log("Sleeping...");
			Thread.sleep(1000);
			log("Sending command waiting for 3s");
			boolean received = queue.tryTransfer(command, 3, TimeUnit.SECONDS);
			log("Command sent, received: " + received);
		} catch (Exception ex) {
			log(ex.getMessage());
		}
	}

	private void log(String message) {
		System.out.printf("[Producer] %s%n", message);
	}
}

class Consumer implements Runnable {
	private BlockingQueue<String> queue;

	Consumer(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	public void run() {
		log("started");
		String command = waitIndefinitely();

		while (true) {
			switch (command) {
				case "wait":
					command = waitIndefinitely();
					break;
				case "timeout":
					command = waitWithTimeout(5);
					break;
				case "transfer_timeout":
					sleep();
					command = waitWithTimeout(5);
					break;
				default:
					log("Invalid command");
					command = waitIndefinitely();
			}
		}
	}

	private String waitIndefinitely() {
		log("Waiting...");
		try {
			String command = queue.take();
			log("Command received: " + command);
			return command;
		} catch (Exception ex) {
			log(ex.getMessage());
			return null;
		}
	}

	private String waitWithTimeout(long seconds) {
		log("Waiting " + seconds + "s");
		try {
			String command = queue.poll(seconds, TimeUnit.SECONDS);

			if (command == null) {
				log("Timedout");
				return "";
			}

			log("Command received: " + command);
			return command;
		} catch (Exception ex) {
			log(ex.getMessage());
			return null;
		}
	}

	private void sleep() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException ex) {}
	}

	private void log(String message) {
		System.out.printf("[Consumer] %s%n", message);
	}
}
