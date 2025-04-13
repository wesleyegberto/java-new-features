/**
 * Used in Class File API transforming example.
 */
public class ClassFileApiTestTransformedBasicClass {
	public static void main(String[] args) {
		System.out.println("Using the transformed BasicClass");
		var obj = new BasicClass("The next arg will not be returned", 10203040, true);
		// won't exists after transformation
		// obj.setTimestamp(123);
		System.out.println("Return of getTimestamp: " + obj.getTimestamp());
	}
}
