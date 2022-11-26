/**
 * To run: `javac -XDenablePrimitiveClasses --source 20 --target 20 PrimitiveValueNullabilityExample.java && java -XX:+EnablePrimitiveClasses PrimitiveValueNullabilityExample`
 */
public class PrimitiveValueNullabilityExample {
	public static void main(String[] args) {
		var superman = new Hero("Superman", new Power("fly, strength, laser beam", 10));
		System.out.println(superman);

		// we cannot assign null to primitive value
		// error: incompatible types: <null> cannot be converted to Power
		// var batman = new Hero("Batman", null);
		var batman = new Hero("Batman", Power.default);
		System.out.println(batman);

		// using Power.ref we can use it like a normal object reference
		var humanBatman = new HumanHero("Batman", null);
		System.out.println(humanBatman);

		// infers Power
		var supermanPower = superman.getPower();

		// infers Power.ref
		var powerRef = humanBatman.getPower();

		// explicit value object conversion (Power to Power.ref)
		Power.ref power1 = superman.getPower();

		// implicit primitive value converson (Power.ref to Power) - can occurr NPE
		Power power2 = power1;

		try {
			// error: NPE Cannot cast to null-free type "QPower;"
			Power power = humanBatman.getPower();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}

		// array of Power will initialize each element with Power.default
		var powers = new Power[3];
		System.out.println("Array with default powers:");
		for (var p : powers) {
			System.out.println(p);
		}

		// array of Power.ref will initialize with null
		var refPowers = new Power.ref[3];
		System.out.println("Array with ref powers:");
		for (var p : refPowers) {
			System.out.println(p);
		}

		// we can assign subtypes of array
		Object[] arr1 = refPowers;
		// we can set a null because it is Power.ref[]
		arr1[0] = null;

		arr1 = powers;
		// we cannot set null because it is Power[], occurrs a NPE during primitive value conversion
		// error: Cannot store to object array because "<local10>" is null or is a null-free array and there's an attempt to store null in it
		arr1[0] = null;
	}
}

primitive class HumanHero {
	private String name;
	private Power.ref power;

	public HumanHero(String name, Power.ref power) {
		this.name = name;
		this.power = power;
	}

	public Power.ref getPower() {
		return power;
	}

	public String toString() {
		return "[name=%s, powers=%s]".formatted(name, power);
	}
}

primitive class Hero {
	private String name;
	// null-free type
	private Power power;

	public Hero(String name, Power power) {
		this.name = name;
		this.power = power;
	}

	public Power getPower() {
		return power;
	}

	public String toString() {
		return "[name=%s, powers=%s]".formatted(name, power);
	}
}

primitive class Power {
	private String powers;
	private int value;

	public Power(String powers, int value) {
		this.powers = powers;
		this.value = value;
	}

	public String toString() {
		return "[powers=%s, value=%d]".formatted(powers, value);
	}
}
