/**
 * We can run with:
 * java <FileName.java>
 */
public class SwitchExpressions {
	public static void main(String[] args) {
		// Now switch expressions are final o/
		// kinds of switch case statements
		System.out.println(oldWay(Mood.BAD));
		System.out.println(lambdaWay(Mood.GOOD));
		System.out.println(mixingSwitchKinds(Mood.REGULAR));

		FeaturePhase switchWithPatternMatching = FeaturePhase.FINAL;

		var message = switch (switchWithPatternMatching) {
			case FINAL -> "No longer need to use `--preview`";
			case PREVIEW -> "Still needs to use `--preview`";
		};

		System.out.println("Switch expressions with pattern matching: " + message);
	}

	 static String oldWay(Mood mood) {
		// old way (set a var or return method value)
		switch (mood) {
			// we use lambda here, swith isn't used in a statement
			// error: not a statement
			// case GOOD -> "All right";

			// can mix kinds of switch
			// error: different case kinds used in the switch
			// case GOOD -> { return "All right"; }

			case GOOD: return "All right";
			case REGULAR: return "Can do better";
			default: return "Let's improve!";
		}
	}

	static String lambdaWay(Mood mood) {
		return switch (mood) {
			case GOOD -> {
				// yield can only be used in a switch statement
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
			// here we can return because isn't a switch statement
			case BAD -> {
				return "Not today";
			}
			// we still can change vars
			default -> {
				message = "We still can go";
			}
		}

		// switch used in a statement
		var result = switch (mood) {
			// we cannot mix return here (will cause `not a statement` in the following cases)

			case GOOD -> {
				// we cannot return when switch is used as statement
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
