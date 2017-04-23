package com.github.wesleyegberto.interfaces;

/**
 * @author Wesley Egberto
 */
public class InterfaceTest implements B/*, C*/ {
	@Override
	public void abstractMethod() {
		System.out.println("In class implemented method");
	}

	public void go() {
		concreteMethod();
		A.staticMethod();
		B.staticMethod();
		// staticMethod(); isn't inherited
	}

	public static void main(String[] args) {
		new InterfaceTest().go();
	}
}

interface A {
	void abstractMethod();

	default void concreteMethod() {
		System.out.println("In interface A concrete method");
	}

	static void staticMethod() {
		System.out.println("In interface A static method");
	}
}

interface B extends A {
	default void concreteMethod() {
		System.out.println("In interface B concrete method");
	}

	static void staticMethod() {
		System.out.println("In interface B static method");
	}
}

interface C {
	default void concreteMethod() {
		System.out.println("In interface C concrete method");
	}
}