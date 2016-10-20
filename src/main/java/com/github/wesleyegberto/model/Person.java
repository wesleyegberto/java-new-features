package com.github.wesleyegberto.model;

import java.util.*;

public class Person {
	private String name;
	private int age;

	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String toString() {
		return name + " " + age;
	}

	public static List<Person> getList() {
		List<Person> persons = new ArrayList<Person>();
		persons.add(new Person("Tony", 10));
		persons.add(new Person("Logan", 18));
		persons.add(new Person("Peter", 21));
		persons.add(new Person("Bruce", 30));
		persons.add(new Person("Rachel", 11));
		persons.add(new Person("Joey", 14));
		persons.add(new Person("Lucas", 22));
		persons.add(new Person("Neo", 24));
		persons.add(new Person("Jorge", 22));
		persons.add(new Person("Matheus", 24));
		persons.add(new Person("Francis", 19));
		persons.add(new Person("Trinity", 23));
		persons.add(new Person("Morpheu", 42));
		persons.add(new Person("Jorge", 17));

		return persons;
	}
}