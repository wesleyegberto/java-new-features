import java.lang.annotation.*;

/**
 * We can see all the changes here:
 * http://cr.openjdk.java.net/~gbierman/jep359/jep359-20191031/specs/records-jls.html
 */
public class RecordsTest {
	public static void main(String[] args) {
		System.out.println("=== Old point: " + new PointPojo(10, 10));

		System.out.println("\n=== New point: ");
		var newPoint = new PointRecord(10, 10);
		Record aRecord = newPoint;
		System.out.println(aRecord);

		// the field is final and private
		// newPoint.x = 3;
		System.out.println("NewPoint.x = " + newPoint.x());

		System.out.println("\n=== New point with no-arg constructor: ");
		System.out.println(new PointRecord());

		System.out.println("\n=== New point with 1-arg constructor: ");
		System.out.println(new PointRecord(12));

		System.out.println("\n=== New point with 3 args constructor: ");
		System.out.println(new PointRecord(10, 10, 0));

		System.out.println("\n=== Invalid new point: ");
		try {
			System.out.println(new PointRecord(-10));
		} catch (RuntimeException ex) {
			System.err.println("Error: " + ex.getMessage());
		}

		record LocalRecord(int val) {
			public void access() {
				// Record is always static
				// System.out.println("method var: " + aRecord);
			}
		}

		var local = new LocalRecord(5);
		System.out.println("Local: " + local);
	}
}

/**
 * This record has the same effect as PointPojo, but the getters has differents name.
 * Behide the scene a class which extends `java.lang.Record` is generated.
 * The parameters of a Record are called components.
 *
 * To see:
 * Compile: `javac --enable-preview --source 14 RecordsTest.java`
 * See compiled class: `javap PointRecord.class`
 */
record PointRecord(int x, @Validatable(validate = true) int y) {
	// we can declare `serialVersionUID` to use in serialization
	public static final long serialVersionUID = 1L;

	// Only static fields is allowed
	// private int z;

	/**
	 * This is the canonical constructor declared by the compiler when
	 * using Record.
	 * It has the same parameters from record components.
	 */
	/*
	public PointRecord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	*/

	/**
	 * We should use compact constructor to validate the state.
	 * This will be appended to the generated constructor with the parameters
	 * from the declaration.
	 */
	public PointRecord {
		System.out.println("Compact constructor...");
		if (x < 0 || y < 0)
			throw new IllegalArgumentException("x and y cannot be negative");
	}

	// Cannot contain instance initializer
	/*
	{
		System.out.println("Instance initializer");
	}
	*/

	public PointRecord() {
		// We are required to call the record constructor:
		// error: constructor is not canonical, so its first statement must invoke another constructor
		// http://cr.openjdk.java.net/~gbierman/jep359/jep359-20191031/specs/records-jls.html#jls-8.10.4
		this(0, 0);
		System.out.println("No-arg constructor...");
	}

	public PointRecord(int x) {
		this(x, 0);
	}

	/**
	 * We can create a constructor with more args than the default.
	 */
	public PointRecord(int x, int y, int z) {
		this(x, y);
		System.out.println("Alternative constructor...");
	}

	/**
	 * We can have a static factory method.
	 */
	public static PointRecord of(int x, int y) {
		return new PointRecord(x, y);
	}

	/**
	 * We can declare methods that use the fields.
	 */
	public int sum() {
		return x + y;
	}

	/**
	 * Can be nested with class, interface and record, but they will be static.
	 */
	record NestedOne(int z) {

	}
}

/**
 * Now the is a new location for annotation.
 *
 * Behavior depends on type:
 * - RECORD_COMPONENT: will be replicated to field, constructor and accessor method
 * - METHOD: will be replicated only to the accessor method
 * - FIELD: will be replicated only to the field declaration
 * - CONSTRUCTOR: will be replicated only to the constructor
 */
@Target(ElementType.RECORD_COMPONENT)
@Retention(RetentionPolicy.RUNTIME)
public @interface Validatable {
  boolean validate();
}

/**
 * error: classes cannot directly extend Record
 */
// class PointSimulatingRecord extends Record {
// }

/**
 * Old way to write a simple class to hold some data.
 */
class PointPojo {
	private int x;
	private int y;

	public PointPojo(int x, int y) {
		if (x < 0 || y < 0)
			throw new IllegalArgumentException("x and y cannot be negative");
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int sum() {
		return x + y;
	}

	@Override
	public int hashCode() {
		return x * 31 + y * 31;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PointPojo))
			return false;
		var otherPoint = (PointPojo) obj;
		return this.x == otherPoint.x && this.y == otherPoint.y;
	}

	@Override
	public String toString() {
		return "PointPojo{x=" + x + ", y=" + y + "}";
	}
}
