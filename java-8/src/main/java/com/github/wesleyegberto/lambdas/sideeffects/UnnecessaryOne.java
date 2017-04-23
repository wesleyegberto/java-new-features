package com.github.wesleyegberto.lambdas.sideeffects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Wesley Egberto
 */
public class UnnecessaryOne {
	public static void main(String[] args) {
		// Example of an unnecessary side-effect lambda to store the result
		List<Integer> uglyOddNumbers = new ArrayList<>();
		IntStream.range(0, 10)
			.filter(i -> i % 2 != 0)
			.forEach(i -> uglyOddNumbers.add(i));

		uglyOddNumbers.forEach(System.out::println);

		// Correct one using collector
		List<Integer> correctOddNumbers = IntStream.range(0, 10)
													.filter(i -> i % 2 != 0)
													.boxed()
													.collect(Collectors.toList());
		correctOddNumbers.forEach(System.out::println);

	}
}
