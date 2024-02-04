import static java.util.stream.Collectors.toList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;
import java.util.stream.Gatherer;
import java.util.stream.Gatherers;

/*
 * To run: `java --enable-preview --source 22 StreamGatherersExamples.java`
 */
public class StreamGatherersExamples {
	public static void main(String[] args) {
		deduplicateStream();
		filterIfHasAtLeastFiveEvenNumbers();
	}

	static void deduplicateStream() {
		var uniques = Stream.of(1, 2, 5, 8, 1, 9, 3, 5, 4, 8, 7, 3)
			.gather(deduplicate())
			.collect(toList());
		System.out.println("Unique elements: " + uniques);
	}

	static Gatherer<Integer, Set<Integer>, Integer> deduplicate() {
		return Gatherer.ofSequential(
			// initializer: start the state with a Set
			() -> new TreeSet<>(),
			// integrator: only add if doesn't exists in the set
			Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
				if (state.contains(element)) {
					// ask for the next element if the downstream is on
					return !downstream.isRejecting();
				}
				state.add(element);
				// pushes downstream and ask for more if the downstream is on
				return downstream.push(element);
			})
		);
	}

	static void filterIfHasAtLeastFiveEvenNumbers() {
		var list = Stream.generate(() -> (int) (Math.random() * 10) + 1)
			.limit(10)
			.gather(filterAtLeastFiveEvenNumbers())
			.collect(toList());
		if (list.isEmpty())
			System.out.println("There were not enough even numbers");
		else
			System.out.println("Even numbers: " + list);
	}

	static Gatherer<Integer, List<Integer>, Integer> filterAtLeastFiveEvenNumbers() {
		return Gatherer.of(
			// initializer: start a new list
			LinkedList::new,
			// integrator: only add if it is an even number, also increments
			(state, element, downstream) -> {
				if (element % 2 == 0) {
					state.add(element);
				}
				return !downstream.isRejecting();
			},
			// combiner: merge the parallel gathering
			(leftState, rightState) -> {
				leftState.addAll(rightState);
				return leftState;
			},
			// finisher: only pushes if has at least five even numbers
			(state, downstream) -> {
				if (state.size() < 5) {
					return;
				}
				state.forEach(i -> {
					downstream.push(i);
				});
			}
		);
	}
}
