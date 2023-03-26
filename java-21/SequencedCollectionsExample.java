import java.util.*;

public class SequencedCollectionExample {
	public static void main(String[] args) {
		testCollections();
	}

	public void testCollections() {
		System.out.println("=== List ===");

		List<String> list = new ArrayList<>();
		SequencedCollection<String> sequenced = new ArrayList<>();

		var print = () -> {
			System.out.println("List: " + list);
			System.out.println("SequencedCollection: " + sequenced);
		};

		// add operations
		list.add("elem");
		sequenced.addLast("elem");
		print();

		// add first (head)
		list.set(0, "First"); // replace :s
		sequenced.addFirst("First"); // push
		print();

		// add last (tail)
		list.add("last");
		sequenced.addLast("last");
		print();

		// get first
		list.get(0);
		sequenced.getFirst();

		// get last
		list.get(list.size() - 1);
		sequenced.getLast();

		// remove first
		list.remove(0);
		sequenced.removeFirst();
		print();

		// remove last
		list.remove(list.size() - 1);
		sequenced.removeLast();
		print();
	}

	public void testSets() {
		System.out.println("=== Set ===");

		Set<String> set = new LinkedHashSet<>();
		SequencedSet<String> sequenced = new LinkedHashSet<>();
		// SortedSet will only support SequencedSet.reversed
		// SequencedSet<String> sortedSequenced = new SortedSet<>();
	}
}
