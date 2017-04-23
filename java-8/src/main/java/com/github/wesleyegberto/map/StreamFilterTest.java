package com.github.wesleyegberto.map;

import com.github.wesleyegberto.model.Person;

import java.util.*;
import java.util.stream.Stream;

public class StreamFilterTest {

	public static void main(String[] args) {
		List<Person> persons = Person.getList();
		
		// get the stream (sequential version) to apply the filter operation
		Stream<Person> personsStream = persons.stream();

		// apply the filter
		Stream<Person> personsFilters = personsStream.filter(p -> p.getAge() >= 18);
		System.out.println("Persons filtered:");
		personsFilters.forEach((p) -> System.out.println(p));
	}
}