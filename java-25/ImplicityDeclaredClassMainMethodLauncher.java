/**
 * To run: `java ImplicityDeclaredClassMainMethodLauncher.java`
 */
void main() {
	System.out.println("Hello world from a implicity declared class with a main method o/");
}

/**
To see the generated class:
- `javac ImplicityDeclaredClassMainMethodLauncher.java`
- `javap ImplicityDeclaredClassMainMethodLauncher`

Output:

```
Compiled from "ImplicityDeclaredClassMainMethodLauncher.java"
final class ImplicityDeclaredClassMainMethodLauncher {
  ImplicityDeclaredClassMainMethodLauncher();
  void main();
}
```
*/
