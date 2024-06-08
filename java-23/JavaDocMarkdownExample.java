/// To run:
/// - `javadoc JavaDocMarkdownExample.java`
public class JavaDocMarkdownExample {
	/// Code to generate **Universe** _answer_:
	///
	/// Restrictions:
	///
	/// - can take as long as Universe life
	/// - can use [java.util.Random]
	/// - requires only [base module][java.base/]
	///
	/// ```java
	/// (int) (new java.util.Random().nextDouble() * 42);
	/// ```
	///
	/// @return int with the answer
	/// @throws IllegalStateException if the Universe has ended
	public int calculateAnswer() {
		return (int) (new java.util.Random().nextDouble() * 42);
	}

	public static void main(String[] args) {
		System.out.println("Universe answer: " + new JavaDocMarkdownExample().calculateAnswer());
	}
}
