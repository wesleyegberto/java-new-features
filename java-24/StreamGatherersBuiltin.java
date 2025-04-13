import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

/*
 * To run: `java StreamGatherersBuiltin.java`
 */
public class StreamGatherersBuiltin {
	public static void main(String[] args) {
		System.out.println("=== Fold ===");
		fold();
		System.out.println("=== Scan ===");
		scan();
		System.out.println("=== Window Fixed ===");
		windowFixed();
		System.out.println("=== Window Sliding ===");
		windowSliding();
		System.out.println("=== Map Concurrent ===");
		mapConcurrent();
	}

	static void fold() {
		var sum = generate(10)
				.gather(
					Gatherers.fold(() -> 0, (acc, elem) -> acc + elem)
				)
				.findFirst()
				.get();
		System.out.println("The sum of all elements: " + sum);
	}

	static void scan() {
		var sums = generate(10)
			.gather(
				Gatherers.scan(() -> 0, (acc, elem) -> acc + elem)
			)
			.collect(toList());
		System.out.println("The sum of each element with its previous: " + sums);
	}

	static void windowFixed() {
		var pairs = generate(10)
				.gather(
					Gatherers.windowFixed(2)
				)
				.collect(toList());
		System.out.println("Pair of elements: " + pairs);
	}

	static void windowSliding() {
		var neighbors = generate(10)
				.gather(
					Gatherers.windowSliding(2)
				)
				.collect(toList());
		System.out.println("Elements that are neighbor: " + neighbors);
	}

	static void mapConcurrent() {
		// mapConcurrent uses VirtualThreads
		// preserves the ordering of the stream
		var first10thSquares = generate(50)
				.gather(Gatherers.mapConcurrent(5, elem -> elem * elem))
				.collect(toList());
		System.out.println("First 50th squares: " + first10thSquares);
	}

	static Stream<Integer> generate(int size) {
		return Stream.iterate(1, acc -> acc + 1).limit(size);
	}
}
