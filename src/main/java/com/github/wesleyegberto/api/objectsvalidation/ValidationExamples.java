package com.github.wesleyegberto.api.objectsvalidation;

import java.util.Objects;

/**
 * @author Wesley Egberto
 */
public class ValidationExamples {

	public static void doFoo(String aVar) {
		// will throw a NPE with the given message
		Objects.requireNonNull(aVar, "Got a null argument");

		System.out.println("Requires non-null: " + aVar);
	}

	public static void doBar(String anotherVar) {
		if(Objects.isNull(anotherVar))
			System.out.println("Got a null");
		else
			System.out.println("Got a non-null: " + anotherVar);
	}
}
