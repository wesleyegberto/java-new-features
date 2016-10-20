package com.github.wesleyegberto.lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Wesley Egberto.
 */
public class Example_Consumers {
	public static void main(String[] args) {
		List<String> names = Arrays.asList("Bruce", "Logan", "Peter");

		System.out.println("Using anonymous class");
		names.forEach(new Consumer<String>() {
			@Override
			public void accept(String name) {
				System.out.println(name);
			}
		});

		System.out.println("\nUsing lambda expression");
		names.forEach(name -> System.out.println(name));

		System.out.println("\nUsing method reference (will create a class on the fly)");
		Consumer<String> myConsumer = System.out::println;
		names.forEach(System.out::println);

		System.out.println("\nUsing stream to map to another class");
		names.stream().map(Hero::new)
					.map(Hero::getSecretIdentity)
					.forEach(myConsumer);

	}
}

class Hero {
	private String secretIdentity;

	public Hero(String secretIdentity) {
		this.secretIdentity = "Mr. " + secretIdentity;
	}

	public String getSecretIdentity() {
		return secretIdentity;
	}
}
