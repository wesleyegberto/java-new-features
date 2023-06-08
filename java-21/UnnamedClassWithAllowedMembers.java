/**
 * Using JDK manually built from main branch.
 *
 * To run: `java --enable-preview --source 21 UnnamedClassWithAllowedMembers.java`
 */

static String staticField = "can have static field";
static String privateStaticField = "can have private static field";

String instanceField = "can have instance field";
private String privateInstanceField = "can have private instance field";

static void staticMethod() {
	System.out.println("can have static method");
}

private static void privateStaticMethod() {
	System.out.println("can use any modifier");
}

static void instanceMethod() {
	System.out.println("can have any instance method");
}

void main() {
	System.out.println("must always have a valid main method to launch");

	System.out.println("static field: " + staticField);
	System.out.println("instance field: " + instanceField);

	staticMethod();
	this.instanceMethod();
}
