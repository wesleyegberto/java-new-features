import java.io.*;

public class RecordsSerializationTest {
	public static void main(String[] args) {
		// Try to comment this and change the serialVersionUID value
		write();
		read();
	}

	private static void write() {
		var point = new CustomSerializableRecord(10, 20);

		try (FileOutputStream fos = new FileOutputStream("record_custom.data");
			ObjectOutputStream out = new ObjectOutputStream(fos)) {

			out.writeObject(point);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("Finished");
		}
	}

	private static void read() {
		try (FileInputStream fis = new FileInputStream("record_custom.data");
			ObjectInputStream in = new ObjectInputStream(fis)) {

			var readPoint = (CustomSerializableRecord) in.readObject();
			System.out.println("Read: " + readPoint);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.out.println("Finished");
		}
	}
}

/**
 * Records are serialized different from ordinary classes.
 * Its fields are written in the same order as declared.
 * When it is read, the component constructor is called as normal code would need to do.
 * It cannot customize its serialization, only the method `writeReplace` can be used to
 * create a copy to be serialized.
 *
 * The value of serialVersionUID is fixed to 0L. If we added components to the record,
 * it will contain the default value for that type.
 * If we change the existing componentes it will throw something like:
 * `java.io.InvalidClassException: CustomSerializableRecord; incompatible types for field x`
 *
 * http://cr.openjdk.java.net/~chegar/records/spec/records-serialization.03.html#serialization-of-records
 */
record CustomSerializableRecord(int x, int y) implements Serializable {
	private static final long serialVersionUID = 2L;

	public CustomSerializableRecord {
		System.out.printf("Compact constructor (%s, %s)\n", x, y);
	}

	private Object writeReplace() throws ObjectStreamException {
		System.out.print("Creating a copy to be serialized: ");
		return new CustomSerializableRecord(x + 1, y + 1);
	}

	private Object readResolve() throws ObjectStreamException {
		System.out.printf("Reading from serialized object - x: %s, y: %s\n", x, y);
		return new CustomSerializableRecord(x - 1, y - 1);
	}

	// Won't be called
	private void writeObject(ObjectOutputStream out) throws IOException {
		System.out.println("Writing object...");

		out.writeUTF("Message to write");
		out.writeInt(x);
		out.writeInt(y);
	}

	// Won't be called
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		System.out.println("Reading object...");

		String message = in.readUTF();
		System.out.println("Message read: " + message);
		System.out.println("X = " + in.readInt());
		System.out.println("y = " + in.readInt());
	}
}
