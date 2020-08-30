import static java.util.stream.Collectors.toList;

import java.util.stream.IntStream;

/**
 * To run: `java -XX:+ShowCodeDetailsInExceptionMessages HelpfulNPEMessages.java`
 */
public class HelpfulNPEMessages {
	public static void main(String[] args) {
		simpleObjectAccess();
		insideStreams();
	}

	private static void simpleObjectAccess() {
		System.out.println("=== Simple Object Attribute Access ===");
		try {
			var thing = new Thing(11);
			System.out.println("Value " + thing.weight.value);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}

	private static void insideStreams() {
		System.out.println("%n=== Attribute Access Inside Streams ===");
		try {
			IntStream.of(2, 4, 6, 8, 9)
				.mapToObj(Thing::new)
				.map(thing -> thing.weight.value)
				.forEach(v -> System.out.println("Val " + v));
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
	}
}

class Thing {
	Weight weight;

	Thing(int weight) {
		if (weight % 2 == 0) {
			this.weight = new Weight(weight);
		}
	}

	public Weight getWeight() {
		return weight;
	}
}

class Weight {
	Integer value;

	Weight(int value) {
		if (value % 2 == 0) {
			this.value = value;
		}
	}

	public int getValue() {
		return value;
	}
}