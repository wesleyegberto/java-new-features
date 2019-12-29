import java.util.stream.*;
import java.util.Optional;

public class CollectorsTeeingTest {
	public static void main(String[] args) {
		Range range = Stream
			.of(1, 8, 2, 5)
			.collect(Collectors.teeing(
				// the collectors produce Optional<Integer>
				Collectors.minBy(Integer::compareTo),
				Collectors.maxBy(Integer::compareTo),
				// merger
				Range::ofOptional)
			);

		System.out.println("Range: " + range.getRange());
	}
}

class Range {
	private String range;

	Range(String range) {
		this.range = range;
	}

	public static Range ofOptional(Optional<Integer> min, Optional<Integer> max) {
		if (min.isEmpty() || max.isEmpty()) {
			return new Range("EMPTY");
		}
		return new Range(String.format("from %d to %d", min.get(), max.get()));
	}

	public String getRange() {
		return range;
	}
}