import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.IO.println;

/**
 * To run: `java --source 25 --enable-preview StableValueExample.java`
 */
public class StableValueExample {
	static final StableValue<ElementClass> ELEMENT = StableValue.of();

	static final List<ElementClass> ELEMENTS_LIST = StableValue.list(5, index -> {
		System.out.println("Initializing ElementClass at index " + index);
		return new ElementClass(index);
	});

	static final Map<String, ElementClass> ELEMENTS_MAP = StableValue.map(Set.of("foo", "bar"), key -> {
		System.out.println("Initializing ElementClass for key " + key);
		return new ElementClass(key);
	});

	public static void main(String[] args) {
		basicApiExample();
		lazyInitializationExample();
		stableListExample();
		stableMapExample();
	}

	static ElementClass getElement() {
		return ELEMENT.orElseSet(() -> {
			System.err.println("orElseSet called, initializing ElementClass");
			return new ElementClass("Lazy Initialization");
		});
	}

	static void basicApiExample() {
		println("=== Basic API Example ===");
		// example of using a stable value
		var stableValue = StableValue.of();

		try {
			println(stableValue.orElseThrow());
		} catch (NoSuchElementException e) {
			println("No value present");
		}

		var updated = stableValue.trySet("Value trySet");
		println("Was the value updated? " + updated);
		println("Value: " + stableValue.orElseThrow());

		// Attempting to set a new value again
		var secondUpdate = stableValue.trySet("Second Value trySet");
		println("Was the value updated again? " + secondUpdate);
	}

	static void lazyInitializationExample() {
		println("\n=== Lazy Initialization Example ===");
		println("First call to getElement() will initialize ElementClass lazily");
		println("First call: " + getElement());
		println("Subsequent calls will return the already initialized value without re-initialization");
		println("Second call: " + getElement());
	}

	static void stableListExample() {
		println("\n=== Stable List Example ===");
		println("Accessing ELEMENTS_LIST will initialize ElementClass lazily for each index");
		for (int i = 0; i < ELEMENTS_LIST.size(); i++) {
			println("\tElement at index " + i + ": " + ELEMENTS_LIST.get(i));
		}

		println("Accessing an already initialized index 2: " + ELEMENTS_LIST.get(2));
	}

	static void stableMapExample() {
		println("\n=== Stable Map Example ===");
		println("Accessing ELEMENTS_MAP will initialize ElementClass lazily for each key");
		for (String key : ELEMENTS_MAP.keySet()) {
			println("\tElement for key '" + key + "': " + ELEMENTS_MAP.get(key));
		}

		println("Accessing an already initialized key foo: " + ELEMENTS_MAP.get("foo"));

		try {
			println("Accessing a non-existent key: " + ELEMENTS_MAP.get("non-existent"));
		} catch (NoSuchElementException e) {
			println("Caught exception for non-existent key");
		}
	}
}

class ElementClass {
	ElementClass(Object arg) {
		// expensive initialization logic
		println("\tElementClass constructor called with arg: " + arg);
	}
}
