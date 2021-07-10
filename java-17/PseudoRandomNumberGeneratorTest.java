import java.util.*;
import java.util.random.*;

/**
 * Splittable algorithms:
 * - L32X64MixRandom
 * - L32X64StarStarRandom
 * - L64X128MixRandom
 * - L64X128StarStarRandom
 * - L64X256MixRandom
 * - L64X1024MixRandom
 * - L128X128MixRandom
 * - L128X256MixRandom
 * - L128X1024MixRandom
 *
 * Jumpable algorithms:
 * - Xoroshiro128PlusPlus
 * - Xoshiro256PlusPlus
 */
public class PseudoRandomNumberGeneratorTest {
	private static long SEED = 42L;

	public static void main(String[] args) {
		testJumpableRandom();
		testSplittableRandom();
	}

	private static void testJumpableRandom() {
		RandomGenerator.JumpableGenerator gen = (RandomGenerator.JumpableGenerator) RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create(SEED);

		Map<Long, Long> numbers = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			var n = gen.nextLong(100);
			if (numbers.containsKey(n))
				numbers.computeIfPresent(n, (key, val) -> val + 1);
			else
				numbers.put(n, 1L);

			if (i % 100 == 0)
				gen.jump();
		}

		numbers.entrySet().forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
	}

	private static void testSplittableRandom() {
		RandomGenerator.SplittableGenerator gen = (RandomGenerator.SplittableGenerator) RandomGeneratorFactory.of("L32X64MixRandom").create(SEED);

		Map<Long, Long> numbers = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			var n = gen.nextLong(100);
			if (numbers.containsKey(n))
				numbers.computeIfPresent(n, (key, val) -> val + 1);
			else
				numbers.put(n, 1L);
		}

		numbers.entrySet().forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
	}
}
