import java.util.*;

/**
 * To run: `java RecordPatternTest.java`
 */
public class RecordPatternTest {
	public static void main(String[] args) {
		instanceofExample();
		switchExample();
		genericInferenceTest();
	}

	public static void instanceofExample() {
		System.out.println("=== instanceof example ===");
		var point = new Point(10, 10);
		var coloredPoint = new ColoredPoint(point, "blue");
		Object obj = coloredPoint;

		// JDK 16
		if (obj instanceof ColoredPoint cp) {
			System.out.println("obj is a ColoredPoint: " + cp);
		}

		if (obj instanceof Point p) {
			System.out.println("obj is a point: " + p);
		} else {
			System.out.println("obj is not a point");
		}

		// JDK 20
		if (obj instanceof ColoredPoint(Point(int x, var y), String color)) {
			System.out.printf("Point [%d,%d] has color %s%n", x, y, color);
		}
	}

	public static void switchExample() {
		System.out.println("=== switch example ===");
		var list = new Decorator[] {
			new Decorator(new Point(21, 21)),
			new Decorator(new ColoredPoint(new Point(42, 42), "red")),
			new Decorator("test")
		};

		for (Decorator d : list) {
			switch (d) {
				case Decorator(Point(var x, var y)) -> {
					System.out.printf("Point [%d,%d]%n", x, y);
				}
				case Decorator(ColoredPoint(Point(var x, var y), String color)) -> {
					System.out.printf("ColoredPoint [%d,%d] with color %s%n", x, y, color);
				}
				case Decorator(String s) -> {
					System.out.println("Decorated string: " + s);
				}
				// required to be exhaustive
				default -> System.out.println("None matched");
			}
		}
	}

	public static void genericInferenceTest() {
		System.out.println("=== generic type inference example ===");
		var point = new Point(42, 42);
		var decoratedPoint = new Decorator(new ColoredPoint(point, "RED"));
		var anotherDecorated = new Decorator(decoratedPoint);

		// JDK 20: type inference in Decorator<T>(Decorator<T>(T))
		if (anotherDecorated instanceof Decorator(Decorator(ColoredPoint(Point(int x, int y), String color)))) {
			System.out.printf("x=%d, y=%d; color=%s%n%n", x, y, color);
		}
	}

	static void removedSupportToUseRecordPatternInEnhancedForLoop() {
		var items = new ColoredPoint[] { new ColoredPoint(new Point(42, 42), "red") };

		// support removed in JDK 21
		/*
		// only works in JDK 20
		for (ColoredPoint(Point(var x, var y), String color) : items) {
			System.out.printf("Point [%d, %d] has color %s", x, y, color);
		}
		*/
	}
}

record Point(int x, int y) {}

record ColoredPoint(Point p, String color) {}

record Decorator<T>(T t) {}

