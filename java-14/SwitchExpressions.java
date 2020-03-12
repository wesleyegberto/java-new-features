/**
 * We can run with:
 * java <FileName.java>
 */
public class SwitchExpressions {
	public static void main(String[] args) {
		// Now switch expressions are final o/

		FeaturePhase switchWithPatternMatching = FeaturePhase.FINAL;

		var message = switch (switchWithPatternMatching) {
			case FINAL -> "No longer need to use `--preview`";
			case PREVIEW -> "Still needs to use `--preview`";
		};

		System.out.println("Switch expressions with pattern matching: " + message);
	}
}

enum FeaturePhase {
	PREVIEW,
	FINAL
}