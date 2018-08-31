package com.github.wesleyegberto.collections.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionsTest {
	public static void main(String[] args) {
		List.of("First", "Second", "Third")
			.forEach(System.out::println);
		
		Set.of(1, 2, 3, 4, 5)
			.forEach(System.out::println);
		
		Map.of("K1", "V1", "K2", "V2", "K3", "V3", "K4", "V4", "K5", "V5")
			.forEach((key, value) -> System.out.println(key + " -> " + value));
		
		Map.ofEntries(
				Map.entry("K1", "V1"),
				Map.entry("K2", "V2"),
				Map.entry("K3", "V3"),
				Map.entry("K4", "V4"),
				Map.entry("K5", "V5")
			)
			.forEach((key, value) -> System.out.println(key + " -> " + value));
	}
}
