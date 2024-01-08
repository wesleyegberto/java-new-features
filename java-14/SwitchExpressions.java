/**
 * We can run with:
 * java <FileName.java>
 */
public class SwitchExpressions {
	public static void main(String[] args) {
		// Now switch expressions are final o/
		// kinds of switch case statements
		System.out.println(switchStatement(Mood.BAD));
		System.out.println(switchLabeledStatementGroup(Mood.REGULAR));
		System.out.println(arrowLabels(Mood.GOOD));
		System.out.println(mixingSwitchKinds(Mood.REGULAR));
		System.out.println(switchExpressionTypeResolution(Mood.GOOD));

		FeaturePhase switchWithPatternMatching = FeaturePhase.FINAL;

		var message = switch (switchWithPatternMatching) {
			case FINAL -> "No longer need to use `--preview`";
			case PREVIEW -> "Still needs to use `--preview`";
		};

		System.out.println("Switch expressions with pattern matching: " + message);
	}

	 static String switchStatement(Mood mood) {
		// switch statement: old way (set a local variable or return method value)
		switch (mood) {
			case GOOD: return "All right";
			case REGULAR: return "Can do better";

			// Java 14: we cannot mix switch statement and switch expression
			// cannot use arrow here, switch is used in a statement
			// error: not a statement
			// case GOOD -> "All right";

			// can mix kinds of switch labels (traditional and arrow)
			// error: different case kinds used in the switch
			// case GOOD -> { return "All right"; }

			default: return "Let's improve!";
		}
	}

	static String switchLabeledStatementGroup(Mood mood) {
		// a switch label in the format of `case L:`
		// is now called: switch labeled statement group
		return switch (mood) {
			case GOOD:
				yield "All right";
			case REGULAR:
				yield "Can do better";
			case BAD:
				yield "Let's improve!";
		};
	}

	static String arrowLabels(Mood mood) {
		return switch (mood) {
			case GOOD -> {
				// yield can only be used in a switch expression
				// in arrow label with a block, we must yield a value or throw an exception
				yield "All right";
			}
			// lambda without block requires semi-colon
			case REGULAR -> "Can do better";
			default -> "Let's improve!";
		};
	}

	static String mixingSwitchKinds(Mood mood) {
		String message;
		switch (mood) {
			// here we can return because isn't a switch expression
			case BAD -> {
				return "Not today";
			}
			// we still can change variables
			default -> {
				message = "We still can go";
			}
		}

		// switch used in a statement
		var result = switch (mood) {
			// we cannot use return nor continue here (will cause `not a statement` in the following cases)

			case GOOD -> {
				// we cannot return when switch is used as expression
				// error: attempt to return out of a switch expression
				// return "All right";
				yield "All right";
			}

			// can mix kinds of switch
			// error: different case kinds used in the switch
			// default: return "Still can improve";

			default -> "Still can improve";
		};
		return result;
	}

	static Number switchExpressionTypeResolution(Mood mood) {
		// if there is more than one return type,
		// it will resolve to the most common supertype when the target type is unknown
		return switch (mood) {
			case BAD -> Short.valueOf("1");
			case REGULAR -> Integer.valueOf("5");
			case GOOD -> Long.valueOf("10");
			default -> Byte.valueOf("0");
		};
	}
}

enum FeaturePhase {
	PREVIEW,
	FINAL
}

enum Mood {
	GOOD,
	REGULAR,
	BAD
}
