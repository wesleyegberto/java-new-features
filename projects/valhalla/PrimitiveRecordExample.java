/**
 * To run: `javac -XDenablePrimitiveClasses --source 20 --target 20 PrimitiveRecordExample.java && java -XX:+EnablePrimitiveClasses PrimitiveRecordExample`
 */
public class PrimitiveRecordExample {
	public static void main(String[] args) {
		var p1 = new Point(10, 10);
		Point.ref p2 = p1;

		System.out.println("Is default value equals to new instancee with 0s? " + (new Point(0, 0) == Point.default));
		System.out.println("Are two new instance with same components equals? " + (new Point(42, 42) == new Point(42, 42)));
	}
}

primitive record Point(int x, int y) {}

// still using value keyword from early version
primitive record A() {}
value record B() {}
