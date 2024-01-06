/*
 * To run: `java --enable-preview --source 22 LaunchMultiFileSourceCodeProgramMain.java`
 */
public class LaunchMultiFileSourceCodeProgramMain {
	static {
		System.out.println("LaunchMultiFileSourceCodeProgramMain static initializer");
	}

	void main() {
		System.out.println("Here the launcher will compile and load the other Java file");
		var value = LaunchMultiFileSourceCodeHelper.generateValue();
		System.out.println("Value: " + value);
	}
}
