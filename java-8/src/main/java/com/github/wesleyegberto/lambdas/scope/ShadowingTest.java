package com.github.wesleyegberto.lambdas.scope;

public class ShadowingTest {

	int x = 23;

	// the lambda expression does not introduce a new level of scoping (it is an anonymous class)
	public void doStuff(int x) {

		// x = 298;
		// the above line generates error because the lambda expression can only directly access fields, methods, and 
		// local variables or parameters that are final or effectively final (was not changed in anywhere else of method)
		// of the enclosing scope (like anonymous class).

		// Note for Runnable interface: for method arguments, the compiler determines the target type with two other
		// language features: overload resolution and type argument inference
		Thread t = new Thread(() -> {
			// this lambda expression has the scope of doStuff() method, so
			// int x = 32; cannot define because the parameter x from doStuff() is considered local
			System.out.println("ShadowingTest.this.x = " + ShadowingTest.this.x);
			System.out.println("this.x = " + this.x); // use ShadowingTest.this.x
			System.out.println("x = " + x);
			int y = 530; // this new var is only visible in this anonymous method
			System.out.println("y = " + y);
		});

		// x = 116; // here also will gerenate a error because x must be final or effectively final (not modified)

		t.start();
		int y = 3; // so we can define another var with the same name
		System.out.println("y = " + y);
	}

	public static void main(String[] args) {
		ShadowingTest c = new ShadowingTest();
		c.doStuff(30);
	}
}