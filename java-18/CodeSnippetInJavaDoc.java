/**
 * To generate the javadoc:
 * `javadoc -d code-snippets-doc CodeSnippetInJavaDoc.java`
 */
public class CodeSnippetInJavaDoc {
	/**
	 * The <code>@snippet</code> will render something like this:
	 *
	 * <pre>
	 * // sum two numbers
	 * int sum = MathOperationBuilder.<strong>builder</strong>()
	 *     .withA(...)
	 *     .withB(...)
	 *     .<strong>sum</strong>();
	 * {@link System#out System.out}.println(sum);
	 * </pre>
	 */
	public void methodWithSimpleDoc() {
	}

	/**
	* Builder to help execute math operation =).
	* Usage:
	* {@snippet :
	* // sum two numbers
	* int sum = MathOperationBuilder.builder()  // @highlight substring="builder"
	*     .withA(1)                             // @replace regex="\d+" replacement="..."
	*     .withB(2)                             // @replace regex="\d+" replacement="..."
	*     .sum();                               // @highlight substring="sum"
	* System.out.println(sum);                  // @link substring="System.out" target="System#out"
	* }
	*/
	public MathOperationBuilder methodWithSnippetDoc() {
		return new MathOperationBuilder();
	}

	static class MathOperationBuilder {
		private int a;
		private int b;

		public MathOperationBuilder withA(int a) {
			this.a = a;
			return this;
		}

		public MathOperationBuilder withB(int b) {
			this.b = b;
			return this;
		}

		public int sum() {
			return a + b;
		}

		public int subtract() {
			return a - b;
		}

		public int multiply() {
			return a * b;
		}

		public int divide() {
			return a / b;
		}
	}
}