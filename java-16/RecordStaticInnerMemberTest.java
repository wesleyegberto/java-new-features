public class RecordStaticInnerMemberTest {
	public static void main(String[] args) {
		var outer = new OutterClass();
		var inner = outer.new InnerClass();
		var staticInnerInner = new OutterClass.InnerClass.InnerInnerStaticClass();
		System.out.println("Secret: " + staticInnerInner.innerSecret.secret());
	}

}

/**
 * JEP 395:
 * Relax the longstanding restriction whereby an inner class cannot declare a member that is explicitly or
 * implicitly static. This will become legal and, in particular, will allow an inner class to declare a
 * member that is a record class.
 */
class OutterClass {
	class InnerClass {
		private String innerClassAttr = "My field";

		// only allowed on JDK 16
		static class InnerInnerStaticClass {
			InnerRecord innerSecret;

			InnerInnerStaticClass() {
				this.innerSecret = new InnerRecord("only allowed in Java 16");
			}
		}

		// nested record is implicit static (that way this JEP was done - allow static inner class/record)
		record InnerRecord(String secret) {
			public String mix() {
				return this.secret(); // + InnerClass.this.innerClassAttr;
			}
		}
	}
}