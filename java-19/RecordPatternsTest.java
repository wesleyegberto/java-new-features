/**
 * Run: `java --enable-preview --source 19 RecordPatternsTest.java`
 */
public class RecordPatternsTest {
	public static void main(String[] args) {
		simpleExampleOfDeconstruct();

		var p1 = new Point(10, 10);
		var p2 = new ColoredPoint(new Point(10, 10), Color.GREEN);

		System.out.println("Has collision: " + checkCollisionIfs(p1, p2));

		System.out.println("Has collision: " + checkCollisionSwitches(p1, p2));
	}

	static void simpleExampleOfDeconstruct() {
		record ComputedPoint (int x, int y) {
			// the accessor method is used is the deconstruction
			public int y() {
				return this.y + 10;
			}
		}

		Object point = new ComputedPoint(42, 42);

		// `int x` will receive the result of implitcy accessor `Point.x()`
		// `int y` will receive the result of explicity accesor `Point.y()`
		if (point instanceof ComputedPoint(int x, var y)) {
			System.out.printf("Deconstruction: x=%d, y=%d%n%n", x, y);
		} else {
			System.out.println("Not a instance of ComputedPoint");
		}
	}

	static boolean checkCollisionIfs(Object p1, Object p2) {
		if (p1 instanceof Point(int x1, int y1) && p2 instanceof Point(int x2, int y2)) {
			return x1 == x2 && y1 == y2;
		}
		if (p1 instanceof Point(int x1, int y1) && p2 instanceof ColoredPoint(Point(int x2, int y2), Color c)) {
			return x1 == x2 && y1 == y2;
		}
		if (p1 instanceof ColoredPoint(Point(int x1, int y1), Color c) && p2 instanceof Point(int x2, int y2)) {
			return x1 == x2 && y1 == y2;
		}
		if (p1 instanceof ColoredPoint(Point(int x1, int y1), Color c1)
			&& p2 instanceof ColoredPoint(Point(int x2, int y2), Color c2)) {
			return x1 == x2 && y1 == y2;
		}
		throw new IllegalArgumentException("Invalid type");
	}

	static boolean checkCollisionSwitches(Object p1, Object p2) {
		int x1, y1, x2, y2;

		// Record pattern on switch should be exhaustive (careful with generics on record)
		switch (p1) {
			case Point(int px1, int py1) -> {
				x1 = px1;
				y1 = py1;
			}
			case ColoredPoint(Point(int px1, int py1), Color c) -> {
				x1 = px1;
				y1 = py1;
			}
			case null, default -> throw new IllegalArgumentException("Invalid type");
		}

		switch (p2) {
			case Point(int px2, int py2) -> {
				x2 = px2;
				y2 = py2;
			}
			case ColoredPoint(Point(int px2, int py2), Color c) -> {
				x2 = px2;
				y2 = py2;
			}
			case null, default -> throw new IllegalArgumentException("Invalid type");
		}
		return x1 == x2 && y1 == y2;
	}
}

record Point(int x, int y) {}

enum Color { RED, GREEN, BLUE }

record ColoredPoint(Point p, Color c) {}

