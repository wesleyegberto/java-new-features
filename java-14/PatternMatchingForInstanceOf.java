public class PatternMatchingForInstanceOf {
	public static void main(String[] args) {
		Animal dog1 = new Dog("Bob");
		Animal c = new Cat();

		// Old verbose way
		if (dog1 instanceof Dog) {
			// A cast just to manipulate the specific type
			Dog d1 = (Dog) dog1;
			play(d1);
		}

		// New way
		if (dog1 instanceof Dog d1) {
			play(d1);
		}
		if (c instanceof Cat c1) {
			play(c1);
		}

		Animal dog2 = new Dog("Bob");
		System.out.println("\nAre both equal? " + dog1.equals(dog2));
	}

	static void play(Dog d) {
		System.out.println("Playing with Dog...");
		d.makeSound();
	}

	static void play(Cat c) {
		System.out.println("Playing with Cat...");
		c.makeSound();
	}
}

class Animal {
	void makeSound() {
		System.out.println("Generic sound");
	}
}

class Cat extends Animal {
	void makeSound() {
		System.out.println("Miau");
	}
}

class Dog extends Animal {
	// here we assume that are equal when they have the same name
	private String name;

	Dog(String name) {
		this.name = name;
	}

	void makeSound() {
		System.out.println("Au au");
	}

	/*
	 * Old way where we need to cast after the `instanceof`.
	 */
	public boolean equalsOldWay(Object o) {
		// need to check if is the same type
		if (!(o instanceof Dog))
			return false;
		// then we cast to work with this type
		Dog d1 = (Dog) o;
		// only now we can performe our [business] logic to compare
		return d1.name.equals(d1.name);
	}

	public boolean equals(Object o) {
		// more straightforward
		return (o instanceof Dog d1)
			&& d1.name.equals(d1.name);
	}
}