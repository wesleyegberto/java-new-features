/**
 * To run: `java --enable-preview --source 21 UnnamedClassWithInitializers.java`
 */

static {
	System.out.println("static initializer");
}

{
	System.out.println("instance initializer");
}

void main() {
	System.out.println("instance main method");
}
