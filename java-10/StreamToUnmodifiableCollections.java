import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamToUnmodifiableCollections {
	public static void main(String[] args) {
		List<String> list = List.of("Testing", "Immutable", "List", "From", "Stream");

		List<String> immutableList = list.stream()
				.collect(Collectors.toUnmodifiableList());
		immutableList.forEach(System.out::println);

		Map<String, Integer> wordsLengths = list.stream()
				.collect(Collectors.toUnmodifiableMap(Function.identity(), word -> word.length()));
		wordsLengths.forEach((word, length) -> System.out.println(word + " -> " + length + " letters"));
	}
}
