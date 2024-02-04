import java.lang.foreign.*;
import java.lang.invoke.*;
import java.util.*;

/**
 * To run: `java --enable-preview --source 22 ForeignFunctionAndMemoryApiFunctionExemple.java`
 */
public class ForeignFunctionAndMemoryApiFunctionExemple {
	public static void main(String[] args) {
		functionLookupExample();
	}

	static void functionLookupExample() {
		Linker linker = Linker.nativeLinker();
		SymbolLookup stdlib = linker.defaultLookup();
		Optional<MemorySegment> radixsortMem = stdlib.find("radixsort");
	}
}
