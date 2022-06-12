import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

/**
 * This preview changed `&&` to `when` in guarded clause.
 *
 * Run: `java --enable-preview --source 19 SwitchWithPatternMatchingThirdPreview.java`
 */
public class SwitchWithPatternMatchingThirdPreview {
	public static void main(String[] args) {
		System.out.println(stringify(42));
		System.out.println(stringify(-42));
		System.out.println(stringify("Some text"));
		System.out.println(stringify(""));
		System.out.println(stringify(null));
	}

	static String stringify(Object value) {
		return switch (value) {
			// the constant must be before the guarded pattern (otherwise it will never hit)
			case Integer i when i == 42 -> "42 is the answer";
			case Integer i when i > 0 -> "positive number";
			case Integer i when i < 0 -> "negative number";
			// this must be after because it will match all integers
			case Integer i -> "should be 0";

			case String s when s.isEmpty() -> "empty string";
			case String s when s.length() > 50 -> "long string";
			// this must be after because it will match all strings
			case String s -> "non-empty string";
			// same here
			case CharSequence cs -> "any other CharSequence";

			case null -> "null =s";
			default -> "unhandled type";
		};
	}
}