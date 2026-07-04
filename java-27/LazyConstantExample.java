import java.util.NoSuchElementException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.IO.println;

/**
 * To run: `java --source 27 --enable-preview LazyConstantExample.java`
 *
 * https://cr.openjdk.org/~pminborg/lazy-constants-third-preview/api/java.base/java/lang/LazyConstant.html
 */
public class LazyConstantExample {
	static final LazyConstant<ElementClass> ELEMENT = LazyConstant.of(() -> {
		System.out.println("Initializing ELEMENT");
		return new ElementClass(42);
	});

	static final List<ElementClass> ELEMENTS_LIST = List.ofLazy(5, index -> {
		System.out.println("[List] Initializing ElementClass at index " + index);
		return new ElementClass(index);
	});

	static final Map<String, ElementClass> ELEMENTS_MAP = Map.ofLazy(Set.of("foo", "bar"), key -> {
		System.out.println("[Map] Initializing ElementClass for key `" + key + "`");
		return new ElementClass(key);
	});

	static final Set<String> ELEMENTS_SET = Set.ofLazy(Set.of("one", "two", "three", "four", "five"), key -> {
		System.out.println("[Set] Initializing ElementClass for key `" + key + "`");
		// only items with predicate true will be kept
		if (key.length() > 4) {
			System.out.println("\tKey `" + key + "` won't be initialized");
			return false;
		}
		return true;
	});

	public static void main(String[] args) {
		basicApiExample();
		lazyInitializationExample();
		stableListExample();
		stableMapExample();
		stableSetExample();
	}

	static void basicApiExample() {
		println("=== Basic API Example ===");
		// removed factory method `of()` to always have a initializer
		var stableValue = LazyConstant.of(() -> {
			System.out.println("Initializing lazy constant");
			return 42;
		});

		// removed `orElseThrow`
		// removed `isInitialized`
		// removed `trySet`
		// removed `orElse`

		// `get` will trigger initialization
		println("Reading with initialization: " + stableValue.get());

		println("Reading an already initialized: " + stableValue.get());
	}

	static void lazyInitializationExample() {
		println("\n=== Lazy Initialization Example ===");
		println("First call to getElement() will initialize ElementClass lazily");
		println("First call: " + ELEMENT.get());
		println("Subsequent calls will return the already initialized value without re-initialization");
		println("Second call: " + ELEMENT.get());
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
			println("\tElement for key `" + key + "`: " + ELEMENTS_MAP.get(key));
		}

		println("Accessing an already initialized key foo: " + ELEMENTS_MAP.get("foo"));

		try {
			println("Accessing a non-existent key: " + ELEMENTS_MAP.get("non-existent"));
		} catch (NoSuchElementException e) {
			println("Caught exception for non-existent key");
		}
	}

	static void stableSetExample() {
		println("\n=== Stable Set Example ===");
		println("Accessing ELEMENTS_SET will initialize ElementClass lazily for each key");

		for (String key : ELEMENTS_SET) {
			println("\tElement key `" + key + "` is in the set");
		}

		println("Accessing again (it should not print key 'three')");

		for (String key : ELEMENTS_SET) {
			println("\tElement key `" + key + "` is in the set");
		}
	}
}

class ElementClass {
	ElementClass(Object arg) {
		// expensive initialization logic
		println("\tElementClass constructor called with arg: " + arg);
	}
}
