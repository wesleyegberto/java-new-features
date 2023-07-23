
/**
 * To run: `java --enable-preview --source 21 StringTemplateCustomProcessorExample.java`
 */
public class StringTemplateCustomProcessorExample {

	public static void main(String[] args) {
		usingClass();
		usingLambda();
		usingStaticFactoryMethod();
	}

	static void usingClass() {
		class StringNullSanitizer implements StringTemplate.Processor<String, RuntimeException> {
			public String process(StringTemplate st) {
				return interpolate(st);
			}
		}

		System.out.println(new StringNullSanitizer()."Test null: \{null}");
	}

	static void usingLambda() {
		// lambda we must define the type
		StringTemplate.Processor<String, RuntimeException> STR_NULL_SANITIZER = StringTemplateCustomProcessorExample::interpolate;
		System.out.println(STR_NULL_SANITIZER."Test null: \{null}");
	}

	static void usingStaticFactoryMethod() {
		var STR_NULL_SANITIZER = StringTemplate.Processor.of(StringTemplateCustomProcessorExample::interpolate);
		System.out.println(STR_NULL_SANITIZER."Test null: \{null}");
	}

	static String interpolate(StringTemplate st) {
		var sb = new StringBuilder();
		var fragments = st.fragments().iterator();
		for (Object value : st.values()) {
		  sb.append(fragments.next());
		  if (value == null) {
			sb.append("<NULL>");
		  } else {
			sb.append(value);
		  }
		}
		sb.append(fragments.next());
		return sb.toString();
	}
}
