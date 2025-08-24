import java.util.*;
import java.util.stream.*;

/**
 * Compile: `javac ProfilingTest.java`
 * Test AOT cache with two-step:
 * * Record mode: `java -XX:AOTMode=record -XX:AOTConfiguration=app.aotconf ProfilingTest`
 * * Create mode: `java -XX:AOTMode=create -XX:AOTConfiguration=app.aotconf -XX:AOTCache=app.aot`
 * * Run with AOT cache: `java -XX:AOTCache=app.aot ProfilingTest`
 */
public class ProfilingTest {
	static String greeting(int n) {
		var words = List.of("Hello", "" + n, "world!");
		return words.stream()
				.filter(w -> !w.contains("0"))
				.collect(Collectors.joining(", "));
	}

	public static void main(String... args) {
		for (int i = 0; i < 100_000; i++)
			greeting(i);
		System.out.println(greeting(0));
	}
}
