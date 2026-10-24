import java.util.ArrayList;
import java.util.List;

/**
 * Small workload used by the AOT cache and GC scripts.
 * It has a hot method (to be JIT/AOT compiled) and keeps allocating objects (to exercise the GC).
 *
 * To run: `java Workload.java [iterations]`
 */
public class Workload {
	record Order(int id, String customer, double amount) {}

	public static void main(String[] args) {
		int iterations = args.length > 0 ? Integer.parseInt(args[0]) : 200;
		long start = System.nanoTime();
		double total = 0;

		// retained objects force the GC to promote them (generational mode makes a difference here)
		List<Order> retained = new ArrayList<>();
		for (int i = 0; i < iterations; i++) {
			total += process(i, retained);
		}

		long elapsedMs = (System.nanoTime() - start) / 1_000_000;
		var rt = Runtime.getRuntime();
		System.out.printf("total=%.2f retained=%d elapsed=%dms%n", total, retained.size(), elapsedMs);
		System.out.printf("max heap=%dMB used=%dMB%n", rt.maxMemory() >> 20, (rt.totalMemory() - rt.freeMemory()) >> 20);
	}

	// hot method: called many times, so HotSpot compiles it (with AOT code it is available from the start)
	static double process(int round, List<Order> retained) {
		double sum = 0;
		for (int i = 0; i < 10_000; i++) {
			var order = new Order(i, "customer-" + (i % 100), i * 1.5);
			sum += order.amount() * 0.01;
			if (i % 500 == 0) {
				retained.add(order); // long-lived
			}
		}
		return sum;
	}
}
