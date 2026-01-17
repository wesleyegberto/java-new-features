import java.lang.reflect.*;

/**
 * Default behavior (a warning will be shown):
 *     `java --source 26 FinalFieldMutationExemplo.java`
 *
 * Preparing for migration:
 *     `java --source 26 --illegal-final-field-mutation=deny --enable-final-field-mutation=ALL-UNNAMED FinalFieldMutationExemplo.java`
 *
 * Disable mutation:
 *     `java --source 26 --illegal-final-field-mutation=deny FinalFieldMutationExemplo.java`
 *
 * Enable mutation only for a specific module (base module is an example):
 *     `java --source 26 --illegal-final-field-mutation=deny --enable-final-field-mutation=base FinalFieldMutationExemplo.java`
 *
 * Using JDK Flight Record:
 *     `java -XX:StartFlightRecording:filename=recording.jfr FinalFieldMutationExemplo.java`
 *     `jfr print --events jdk.FinalFieldMutation recording.jfr`
 */
public class FinalFieldMutationExemplo {

	public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
		Field f = Foo.class.getDeclaredField("result");
		f.setAccessible(true);

		var foo = new Foo();
		System.out.println("Result is: " + foo.result);

		f.set(foo, "42 was changed to this message =)");
		System.out.println("Result now is: " + foo.result);
	}
}

class Foo {
	final String result;

	Foo() {
		this.result = "42";
	}
}
