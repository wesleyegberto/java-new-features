import javax.swing.JOptionPane;

/**
 * Steps:
 *
 * - compile: `javac PackagingTool.java`
 * - create JAR: `jar -cfe packaging-tool-example.jar PackagingTool PackagingTool.class`
 * - test JAR: `java -jar packaging-tool-example.jar`
 * - create the Package: `jpackage --name pack-tool-sample --input . --main-jar packaging-tool-example.jar --main-class PackagingTool`
 *
 * Package will generate:
 * - `dmg` on macOS
 * - `msi` on Windows
 * - `app` on Linux
 */
public class PackagingTool {
	public static void main(String[] args) {
		System.out.println("Main =)");
		JOptionPane.showMessageDialog(null, "Hello world from self-contained application", null,
				JOptionPane.INFORMATION_MESSAGE);
	}
}