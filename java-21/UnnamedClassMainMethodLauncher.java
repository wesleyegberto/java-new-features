/**
 * Using JDK manually built from main branch.
 *
 * To run: `java --enable-preview --source 21 UnnamedClassMainMethodLauncher.java`
 */
void main() {
	System.out.println("Hello world from a unnamed class with a main method o/");
}

/**
To see the generated class:
- `javac --enable-preview --source 21 UnnamedClassMainMethodLauncher.java`
- `javap UnnamedClassMainMethodLauncher`

Output:

```
Compiled from "UnnamedClassMainMethodLauncher.java"
final class UnnamedClassMainMethodLauncher {
  UnnamedClassMainMethodLauncher();
  void main();
}
```
*/
