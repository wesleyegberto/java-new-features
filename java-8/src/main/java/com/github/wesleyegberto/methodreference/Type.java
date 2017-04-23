package com.github.wesleyegberto.methodreference;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Wesley Egberto
 */
public class Type {
	public static Type create(Supplier<Type> supplier) {
		return supplier.get();
	}

	public void printModel() {
		System.out.println(hashCode());
	}

	public static void test(Type t) {
		System.out.println(t.toString());
	}

	public static void main(String[] args) {
		Type t1 = Type.create(Type::new);

		List<Type> list = Arrays.asList(t1);

		list.forEach(Type::printModel);

		list.forEach(Type::test);
	}
}
