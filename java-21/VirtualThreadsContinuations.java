import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import java.util.Timer;
import java.util.TimerTask;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

/**
 * Examples from "Continuations: The magic behind virtual threads in Java by Balkrishna Rawool @ Spring I/O 2024"
 *
 * Run:
 * ```bash
 * javac --enable-preview --source 21 --target 21 --add-exports java.base/jdk.internal.vm=ALL-UNNAMED VirtualThreadsContinuations.java
 * java --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED VirtualThreadsContinuations
 * ```
 */
public class VirtualThreadsContinuations {
	public static void main(String[] args) {
		System.out.println("=== Continuation Example ===");
		basicExample();

		System.out.println("\n=== Generator Example ===");
		generatorExample();

		System.out.println("\n=== Custom VirtualThread Example ===");
		virtualThreadExample();
	}

	static void basicExample() {
		var scope = new ContinuationScope("my-scope");
		// continuation needs a scope and a task
		var continuation = new Continuation(scope, () -> {
			System.out.println("A");
			// `yield` pauses the scope execution and returns the execution to another scope
			// the execution will be paused at the point where `Continuation.yield` is called
			// the stacktrace is copied to heap
			// will be paused until `run` is called again
			Continuation.yield(scope);
			System.out.println("B");
			Continuation.yield(scope);
			System.out.println("C");
		});

		System.out.println("Starting continuation");
		continuation.run();
		System.out.println("Resuming continuation");
		continuation.run();
		continuation.run();
		// if we try to run a running continuation or terminated continuation, a IllegalStateException is thrown
		// continuation.run();
		if (continuation.isDone()) {
			System.out.println("Continuation terminated");
		} else {
			System.out.println("Continuation is pending");
		}
	}

	static void generatorExample() {
		var generator = new Generator<String>(source -> {
			try {
				Files.list(Paths.get("/"))
						.forEach(path -> source.yield(path.toString()));
			} catch (IOException ex) {
				source.yield("Error: " + ex.getMessage());
			}
		});

		while (generator.hasNext()) {
			System.out.println("Generated: " + generator.next());
		}
	}

	static void virtualThreadExample() {
		// start the scheduler in a new thread just for it
		new Thread(VirtualThreadScheduler.SCHEDULER::start).start();

		var vt1 = new VirtualThread(() -> {
			System.out.println("1.1");
			System.out.println("1.2");
			WaitingOperation.perform("Network", 2);
			System.out.println("1.3");
			System.out.println("1.4");
		});
		var vt2 = new VirtualThread(() -> {
			System.out.println("2.1");
			System.out.println("2.2");
			WaitingOperation.perform("Disk", 2);
			System.out.println("2.3");
			System.out.println("2.4");
		});

		VirtualThreadScheduler.SCHEDULER.schedule(vt1);
		VirtualThreadScheduler.SCHEDULER.schedule(vt2);
	}
}

class Generator<T> {
	private ContinuationScope scope;
	private Continuation continuation;
	private Source source;

	public class Source {
		private T value;

		public T getValue() {
			var v = this.value;
			continuation.run();
			return v;
		}

		public void yield(T value) {
			this.value = value;
			Continuation.yield(scope);
		}
	}

	public Generator(Consumer<Source> consumer) {
		this.scope = new ContinuationScope("generator");
		this.source = new Source();
		this.continuation = new Continuation(this.scope, () -> consumer.accept(source));
		this.continuation.run();
	}

	public boolean hasNext() {
		return !continuation.isDone();
	}

	public T next() {
		return this.source.getValue();
	}
}

class VirtualThread {
	private static final AtomicInteger COUNTER = new AtomicInteger(1);
	public static final ContinuationScope SCOPE = new ContinuationScope("virtual-threads");

	private int id;
	private Continuation continuation;

	public VirtualThread(Runnable runnable) {
		this.id = COUNTER.getAndIncrement();
		this.continuation = new Continuation(SCOPE, runnable);
	}

	public void run() {
		System.out.println("VirtualThread " + id + " is running on " + Thread.currentThread());
		continuation.run();
	}
}

class VirtualThreadScheduler {
	public static final VirtualThreadScheduler SCHEDULER = new VirtualThreadScheduler();
	public static final ScopedValue<VirtualThread> CURRENT_VIRTUAL_THREAD = ScopedValue.newInstance();

	private Queue<VirtualThread> queue = new ConcurrentLinkedQueue<>();
	private ExecutorService executor = Executors.newFixedThreadPool(3);

	public void start() {
		while (true) {
			if (!queue.isEmpty()) {
				var vt = queue.remove();
				executor.submit(() -> ScopedValue.where(CURRENT_VIRTUAL_THREAD, vt).run(vt::run));
			}
		}
	}

	public void schedule(VirtualThread virtualThread) {
		queue.add(virtualThread);
	}
}

class WaitingOperation {
	public static void perform(String task, int durationInSeconds) {
		System.out.printf("Waiting for %s for %d seconds\n", task, durationInSeconds);
		var virtualThread = VirtualThreadScheduler.CURRENT_VIRTUAL_THREAD.get();

		var timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				VirtualThreadScheduler.SCHEDULER.schedule(virtualThread);
				timer.cancel();
			}
		}, durationInSeconds * 1000L);

		Continuation.yield(VirtualThread.SCOPE);
	}
}
