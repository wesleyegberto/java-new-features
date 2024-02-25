import java.lang.foreign.*;
import java.lang.foreign.MemoryLayout.PathElement;
import java.lang.invoke.*;
import java.util.*;

/**
 * To run: `java --source 22 ForeignFunctionAndMemoryApiAllocationTest.java`
 */
public class ForeignFunctionAndMemoryApiAllocationTest {
	public static void main(String[] args) {
		memorySegmentAllocationExample();
		memorySegmentStructManualAllocation();
		memorySegmentStructAutoAllocation();
		memorySegmentUsingSegmentAllocator();
	}

	static void memorySegmentAllocationExample() {
		System.out.println("Allocating 4 bytes to write an int 4 and 2");
		// if we try to write/read an outbound memory: IndexOutOfBoundsException
		MemorySegment data = Arena.ofAuto().allocate(8);
		System.out.println("Memory allocated: " + data);

		// memory offset is calculated from given value layout byte size * index
		data.setAtIndex(
				ValueLayout.JAVA_INT, // memory layout
				0, // index
				4 // value
		);
		data.setAtIndex(ValueLayout.JAVA_INT, 1, 2);

		// read the value start at index 0
		var firstByte = data.get(ValueLayout.JAVA_INT, 0);
		// index 4 because we offset to the second int (4-bytes long)
		var secondByte = data.get(ValueLayout.JAVA_INT, 4);

		System.out.printf("Value read: %d and %d%n", firstByte, secondByte);
	}

	static void memorySegmentStructManualAllocation() {
		// struct Point { int x; int y } pts[10];

		// define the memory manually
		MemorySegment segment = Arena.ofAuto().allocate(
				// 2 is the qty of struct fields
				2 * ValueLayout.JAVA_INT.byteSize() * 10,
				ValueLayout.JAVA_INT.byteAlignment()
		);

		for (int i = 0; i < 10; i++) {
			var value = i + 1;
			// handle the array and struct offset for each field
			var offset = i * 2;
			segment.setAtIndex(ValueLayout.JAVA_INT, offset, value); // pts[i].x
			segment.setAtIndex(ValueLayout.JAVA_INT, offset + 1, value); // pts[i].y
		}
	}

	static void memorySegmentStructAutoAllocation() {
		// struct Point { int x; int y } pts[10];

		// define the memory content layout
		SequenceLayout ptsLayout = MemoryLayout.sequenceLayout(
			10, // array size
			MemoryLayout.structLayout( // declare a C struct
				ValueLayout.JAVA_INT.withName("x"),
				ValueLayout.JAVA_INT.withName("y")
			)
		);
		System.out.println("We can see the memory layout (size, struct with fields and its type): " + ptsLayout);

		// extract the var handle for each field of the struct
		VarHandle xHandle = ptsLayout.varHandle(PathElement.sequenceElement(), PathElement.groupElement("x"));
		VarHandle yHandle = ptsLayout.varHandle(PathElement.sequenceElement(), PathElement.groupElement("y"));

		// allocate the memory for the layout
		MemorySegment segment = Arena.ofAuto().allocate(ptsLayout);
		System.out.println("Memory allocated for the layout: " + segment);

		for (int i = 0; i < ptsLayout.elementCount(); i++) {
			var value = i + 1;
			// here we don't need to handle the offset for x and y, only the array index
			xHandle.set(segment, 0L, i, value);
			yHandle.set(segment, 0L, i, value);
		}
	}

	static void memorySegmentUsingSegmentAllocator() {
		try (Arena arena = Arena.ofConfined()) {
			MemorySegment segment = arena.allocate(1024 * 1024); // 1 MB
			SegmentAllocator allocator = SegmentAllocator.slicingAllocator(segment);
			for (int i = 0; i < 10; i++) {
				MemorySegment msi = allocator.allocateFrom(ValueLayout.JAVA_INT, i);
			}
			System.out.println("Memory allocated to use with allocator: " + segment);
		}
	}
}
