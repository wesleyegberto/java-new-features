import java.util.*;

/**
 * Java Doc:
 * https://cr.openjdk.org/~smarks/collections/specdiff-sequenced-20230327/java.base/java/util/SequencedCollection.html
 */
public class SequencedCollectionExample {
	public static void main(String[] args) {
		testCollections();
		testSets();
	}

	public static void testCollections() {
		System.out.println("=== List ===");

		List<String> list = new ArrayList<>();
		SequencedCollection<String> sequenced = new ArrayList<>();

		// add operations (from Collection)
		list.add("elem");
		sequenced.add("elem");
		print(list, sequenced);

		// add first (head)
		list.set(0, "First"); // replace :s
		sequenced.addFirst("First"); // push
		print(list, sequenced);

		// add last (tail)
		list.add("last");
		sequenced.addLast("last");
		print(list, sequenced);

		// get first
		list.get(0);
		sequenced.getFirst();

		// get last
		list.get(list.size() - 1);
		sequenced.getLast();

		// remove first
		list.remove(0);
		sequenced.removeFirst();
		print(list, sequenced);

		// remove last
		list.remove(list.size() - 1);
		sequenced.removeLast();
		print(list, sequenced);

		// descending iterator
		ListIterator<String> reversed = list.listIterator(list.size());
		while (reversed.hasPrevious()) {
			reversed.previous();
		}
		Iterator<String> reversedSequence = sequenced.reversed().iterator();
	}

	public static void testSets() {
		System.out.println("=== Set ===");

		Set<String> set = new LinkedHashSet<>();
		SequencedSet<String> sequenced = new LinkedHashSet<>();
		// SortedSet won't support `addFirst` and `addLast`
		// SequencedSet<String> sortedSequenced = new SortedSet<>();

		// add element (from Set)
		set.add("elem");
		sequenced.add("elem");
		print(set, sequenced);

		// get first
		set.iterator().next();
		sequenced.getFirst();

		// get last
		Iterator<String> iterator = set.iterator();
		String last;
		while (iterator.hasNext())
			last = iterator.next();
		last = sequenced.getLast();

		// descending iterator
		Iterator<String> descendingIterator = new TreeSet<>(set).descendingSet().iterator();
		descendingIterator = sequenced.reversed().iterator();
	}

	public static void print(Collection<?> collection, Collection<?> newCollection) {
		System.out.printf("%s: %s%n", collection.getClass().getSimpleName(), collection);
		System.out.printf("%s: %s%n", newCollection.getClass().getSimpleName(), newCollection);
	}
}
