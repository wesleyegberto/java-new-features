import java.lang.foreign.*;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.invoke.*;
import java.util.*;

/**
 * To run: `java --enable-native-access=ALL-UNNAMED --source 22 -Djava.library.path=$(pwd)/ ForeignFunctionAndMemoryApiFunctionExemple.java`
 */
public class ForeignFunctionAndMemoryApiFunctionExemple {
	public static void main(String[] args) {
		functionLookupExample();
		downcallForeignFunction();
		upcallJavaCodeToForeignFunction();
		downcallJavaCodeToForeignFunctionPassingObject();
	}

	static void functionLookupExample() {
		Linker linker = Linker.nativeLinker();
		SymbolLookup stdlib = linker.defaultLookup();

		// find the function in stdlib
		MemorySegment strlenPtr = stdlib.find("strlen").get();

		// function signature: returns a long and receives a char* (address pointer)
		FunctionDescriptor signature = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
		// method handle to be used in Java code to invoke the foreign function
		MethodHandle strlen = linker.downcallHandle(strlenPtr, signature);
	}

	/**
	 * This method will invoke a standard C library function:
	 * size_t strlen(const char *s)
	 */
	static void downcallForeignFunction() {
		System.out.println("=== Downcall - passing array pointer (string) and receiving a long ===");
		Linker linker = Linker.nativeLinker();
		SymbolLookup stdlib = linker.defaultLookup();
		MemorySegment strlenPtr = stdlib.find("strlen").get();
		FunctionDescriptor signature = FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS);
		MethodHandle strlen = linker.downcallHandle(strlenPtr, signature);

		try (Arena arena = Arena.ofConfined()) {
			// allocates the memory (char*)
			MemorySegment str = arena.allocateFrom("Hello FFM API!");
			// invokes the native function passing the memory with the string
			long len = (long) strlen.invoke(str);

			System.out.println("strlen returned: " + len);
		} catch (Throwable ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * This method will invoke a standard C library function:
	 * void qsort(void *base, size_t nmemb, size_t size, int (*compar)(const void *, const void *))
	 */
	static void upcallJavaCodeToForeignFunction() {
		System.out.println("=== Downcall and Upcall - passing array pointer and function pointer ===");
		Linker linker = Linker.nativeLinker();
		SymbolLookup stdlib = linker.defaultLookup();

		MethodHandle qsort = linker.downcallHandle(
			stdlib.find("qsort").get(),
			// signature: (array pointer, long, long, function pointer)
			FunctionDescriptor.ofVoid(ValueLayout.ADDRESS, ValueLayout.JAVA_LONG, ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
		);

		// method handle to invoke the Java method
		MethodHandle comparatorHandle = null;
		try {
			comparatorHandle = MethodHandles.lookup().findStatic(
				MemorySegmentIntComparator.class,
				"compare", // method name
				MethodType.methodType(int.class, MemorySegment.class, MemorySegment.class) // signature (return and parameters)
			);
		} catch(NoSuchMethodException | IllegalAccessException ex) {}

		// Java method pointer
		MemorySegment comparatorPtr = linker.upcallStub(
			comparatorHandle,
			FunctionDescriptor.of( // signature
				ValueLayout.JAVA_INT,
				ValueLayout.ADDRESS.withTargetLayout(ValueLayout.JAVA_INT),
				ValueLayout.ADDRESS.withTargetLayout(ValueLayout.JAVA_INT)
			),
			Arena.ofAuto() // arena to be used to allocate the foreign function arguments to pass to Java code
		);

		try (Arena arena = Arena.ofConfined()) {
			int[] array = new int[] { 4, 0, 8, 7, 1, 6, 5, 9, 3, 2 };
			System.out.println("Array to be sorted: " + Arrays.toString(array));

			MemorySegment arrayPtr = arena.allocateFrom(ValueLayout.JAVA_INT, array);

			// the array will be sorted in-place
			qsort.invoke(arrayPtr, array.length, ValueLayout.JAVA_INT.byteSize(), comparatorPtr);

			// gets the sorted array from the memory
			int[] sortedArray = arrayPtr.toArray(ValueLayout.JAVA_INT);

			System.out.println("Sorted array: " + Arrays.toString(sortedArray));
		} catch (Throwable ex) {
			System.err.println("Error: " + ex.getMessage());
		}
	}

	/**
	 * This method will invoke the C function `validate_person` defined in the `person_validator.c`.
	 * The C code must be compiled first: `gcc -c person_validator.c`
	 * Function's signature: char* validate_person(struct Person* person)
	 * Struct: `struct Person { char* name; short age; }`
	 */
	static void downcallJavaCodeToForeignFunctionPassingObject() {
		System.out.println("=== Downcall - passing struct pointer and receiving an array pointer (string) ===");
		System.load(System.getProperty("java.library.path") + "libpersonvalidator.so");

		SymbolLookup symbolLookup = SymbolLookup.loaderLookup();

		MemoryLayout personStructMemoryLayout = MemoryLayout.structLayout(
			ValueLayout.ADDRESS.withName("name"),
			ValueLayout.JAVA_SHORT.withName("age")
		);

		MethodHandle personValidator = Linker.nativeLinker().downcallHandle(
			symbolLookup.find("validate_person").get(),
			// signature: struct pointer -> string pointer
			FunctionDescriptor.of(ValueLayout.ADDRESS, ValueLayout.ADDRESS)
		);

		VarHandle nameHandle = personStructMemoryLayout.varHandle(PathElement.groupElement("name"));
		VarHandle ageHandle = personStructMemoryLayout.varHandle(PathElement.groupElement("age"));

		var people = List.of(
			new Person(1, null, (short) 0),
			new Person(2, "", (short) 0),
			new Person(3, "Marley", (short) 0),
			new Person(4, "Marley", (short) 10),
			new Person(5, "Marley", (short) 19)
		);

		people.forEach(person -> {
			try (Arena arena = Arena.ofConfined()) {
				MemorySegment personPtr = arena.allocate(personStructMemoryLayout);
				if (person.name() == null) {
					nameHandle.set(personPtr, 0L, MemorySegment.NULL);
				} else {
					nameHandle.set(personPtr, 0L, arena.allocateFrom(person.name()));
				}
				ageHandle.set(personPtr, 0L, person.age());

				// returns a zero-length memory segment (address pointer)
				MemorySegment resultPtr = (MemorySegment) personValidator.invoke(personPtr);
				// reinterpret the pointer to tell the size (max value to read until `\0`)
				MemorySegment messagePtr = resultPtr.reinterpret(Integer.MAX_VALUE);
				String message = messagePtr.getString(0);

				System.out.println("ID " + person.id() + " validation result: " + message);
			} catch (Throwable ex) {
				System.err.println("Error: " + ex.getMessage());
			}
		});
	}
}

class MemorySegmentIntComparator {
	// function to be called by the foreign function
	static int compare(MemorySegment x, MemorySegment y) {
		System.err.println("\tJava method called with " + x + " and " + y);
		// get the int in the memory
		return Integer.compare(x.get(ValueLayout.JAVA_INT, 0), y.get(ValueLayout.JAVA_INT, 0));
	}
}

record Person(int id, String name, short age) {}

