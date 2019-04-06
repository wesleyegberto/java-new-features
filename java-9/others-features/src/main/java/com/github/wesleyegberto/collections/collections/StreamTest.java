package com.github.wesleyegberto.collections.collections;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.toList;

public class StreamTest {
    public static void main(String[] args) {
        Stream shouldBeEmpty = Stream.ofNullable(null);

        // Won't print anything
        shouldBeEmpty.forEach(System.out::println);

        List<Integer> numbers = IntStream.range(0, 10)
                .mapToObj(i -> Integer.valueOf(i))
                .collect(filtering(i -> i.intValue() > 5, toList()));

        numbers.forEach(System.out::println);
    }
}