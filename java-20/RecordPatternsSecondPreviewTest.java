import java.util.List;
import java.util.ArrayList;

/**
 * To run: `java --enable-preview --source 20 RecordsPatternSecondPreviewTest.java`
 */
public class RecordPatternsSecondPreviewTest {
	public static void main(String[] args) {
		enhancedForLoop();

		genericInferrenceTest();

		recordPatternInEnhancedForLoopHeader();
	}

	public static void enhancedForLoop() {
		var points = new Point[] {
			new Point(10, 10),
			new Point(20, 20),
			new Point(30, 30),
			new Point(20, 50),
			new Point(10, 60)
		};

		// we can now deconstruct a record type in the enhanced for loop
		for (Point(int x, int y) : points) {
			System.out.printf("Drawing at x=%d and y=%d%n", x, y);
		}
	}

	public static void genericInferrenceTest() {
		var point = new Point(42, 42);
		var decoratedPoint = new Decorator(new ColoredPoint(point, "RED"));
		var anotherDecorated = new Decorator(decoratedPoint);

		// here we don't need to use `Decorator<Decorator<ColoredPoint>>(Decorator<ColoredPoint>(ColoredPoint cp))` like in JDK 19
		if (anotherDecorated instanceof Decorator(Decorator(ColoredPoint(Point(int x, int y), String color)))) {
			System.out.println("\nAren't you using too much decorator?");
			System.out.printf("x=%d, y=%d; color=%s%n%n", x, y, color);
		}
	}

	static void recordPatternInEnhancedForLoopHeader() {
		var items = new ColoredPoint[] { new ColoredPoint(new Point(42, 42), "red") };

		for (ColoredPoint(Point(var x, var y), String color) : items) {
			System.out.printf("Point [%d, %d] has color %s", x, y, color);
		}
	}
}

record Point(int x, int y) {}

record ColoredPoint(Point p, String color) {}

record Decorator<T>(T t) {}

