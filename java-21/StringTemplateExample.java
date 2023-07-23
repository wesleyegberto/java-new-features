import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * To run: `java --enable-preview --source 21 StringTemplateExample.java`
 */
public class StringTemplateExample {
	static Type type = Type.HACKER;
	static String firstName = "Thomas";
	static String lastName = "Anderson";
	static String hackerName = "Neo";

	public static void main(String[] args) {
		rawTemplateProcessor();
		simpleTemplate();
		allowedExpressionsInsideEmbeddedTemplate();
	}

	static void rawTemplateProcessor() {
		StringTemplate st = StringTemplate.RAW."String template built from RAW template processor: \{firstName}";
		String message = StringTemplate.STR.process(st);
		// same as
		message = STR."String template built from RAW template processor: \{firstName}";
		System.out.println(message);
	}

	static void simpleTemplate() {
		// STR template processor returns a string
		String str = STR."Simple string template without embedded expression";

		// we can use template embedded with string literal
		System.out.println(STR."Wake up \{hackerName}...");

		// we can also use template expression with template string
		System.out.println(STR."""
			Mr. \{lastName}! Welcome back, we missed you!
			""");

		// we can multiline the embedded expression
		System.out.println(STR."The time is \{
			DateTimeFormatter
			  .ofPattern("HH:mm:ss")
			  .format(LocalTime.now())
		  } right now");

		// we don't need to escape "
		System.out.println(STR."Follow the white \{"rabbit"}...");
	}

	// we can use any Java expression
	static void allowedExpressionsInsideEmbeddedTemplate() {
		boolean isCaptured = true;

		// arithmetic expression
		var counter = 0;
		System.out.println(STR."Counting... \{++counter}... \{++counter}... \{++counter}...");
		System.out.println(STR."How long has he been arrested? \{4 + 2} hours");
		System.out.println(STR."How long is his name? \{firstName.length() + lastName.length()}");

		// call method
		System.out.println(STR."What was his first program? \{greetings()}");

		// ternary expression
		System.out.println(STR."As you can see Mr. \{
			isCaptured ? lastName : hackerName
		}, we've been tracking you...");

		// switch expression
		System.out.println(STR."What is he? \{
			switch (type) {
				case HACKER -> "hackerman";
				case PERSON -> "bot";
			}
		}");
	}

	static String greetings() {
		return "Hello matrix";
	}
}

enum Type {
	HACKER,
	PERSON
}
