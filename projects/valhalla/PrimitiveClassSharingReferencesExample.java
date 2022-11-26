/**
 * To run: `javac -XDenablePrimitiveClasses --source 20 --target 20 PrimitiveClassSharingReferencesExample.java && java -XX:+EnablePrimitiveClasses PrimitiveClassSharingReferencesExample`
 */
public class PrimitiveClassSharingReferencesExample {
	public static void main(String[] args) {
		testPrimitiveValues();
	}

	static void testPrimitiveValues() {
		// Point.default is loaded with the class
		log("Default value reference (%s): %s", Point.default.toNotation(), Point.default);

		// the object is shared like in a pool when the fields have the same value
		var origin = new Point(0, 0);
		log("Origin %s value reference: %s", origin.toNotation(), origin);

		log("Is origin equals to default value? " + (origin == Point.default));

		log("Are two new instances with same field values equals? %s", new Point(2, 2), new Point(2, 2));
		log("[1;1] value reference: %s", new Point(1, 1));
		var point = origin.translate(1, 1);
		log("%s value reference: %s", point.toNotation(), point);

		var rect1 = new Rectangle(new Point(10, 50), new Point(50, 30), true, "RED");
		var rect2 = new Rectangle(new Point(10, 50), new Point(50, 30), true, "RED");
		log("Is equals? %b", (rect1 == rect2));
		// when the field has a new object
		var rect3 = new Rectangle(new Point(10, 50), new Point(50, 30), true, new String("RED"));
		log("Is equals even with new String? %b", (rect1 == rect3));
	}

	static void log(String message, Object ...args) {
		System.out.printf(message + "%n", args);
	}
}

primitive class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int x() {
		return this.x;
	}

	public int y() {
		return this.y;
	}

	public Point translate(int dx, int dy) {
		return new Point (x + dx, y + dy);
	}

	public String toNotation() {
		return "%d;%d".formatted(x, y);
	}
}

primitive class Rectangle {
	private Point upperLeftPoint;
	private Point lowerRightPoint;
	private boolean fill;
	private String color;

	public Rectangle(Point ul, Point lr, boolean fill, String color) {
		this.upperLeftPoint = ul;
		this.lowerRightPoint = lr;
		this.fill = fill;
		this.color = color;
	}
}
