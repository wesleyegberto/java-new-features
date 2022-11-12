/**
 * To run: `java --enable-preview --source 20 PatternMatchingForSwitchFourthPreviewTest.java`
 */
public class PatternMatchingForSwitchFourthPreviewTest {
	public static void main(String[] args) {
		recordErrorInSwitchPatternMatching();

		genericRecordInSwitch();
	}

	static void recordErrorInSwitchPatternMatching() {
		var dot = new OneDimensionalPoint(10);

		switch (dot) {
			// will cause MatchException with wrapped exception (the record pattern completes abruptly with the ArithmeticException)
			case OneDimensionalPoint(var x): System.out.println("1D point");
			// the occurring in guarded clause, it just rethrows the exception
			// will cause ArithmeticException
			// case OneDimensionalPoint p when (p / 0 == 1): System.out.println("Non sense");
		}
	}

	static void genericRecordInSwitch() {
		var w = new Wrapper<String>("some text");

		switch (w) {
			// will infer Wrapper<String>
			case Wrapper(var v): System.out.println("Wrapped value: " + v);
		}
	}
}

record OneDimensionalPoint(int x) {
	public int x() {
		return x / 0;
	}
}

record Wrapper<T>(T t) {}
