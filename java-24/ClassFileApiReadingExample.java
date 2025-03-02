import java.lang.classfile.*;
import java.lang.classfile.constantpool.ClassEntry;
import java.lang.constant.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Run: `javac BasicClass.java && java --source 24 --enable-preview ClassFileApiReadingExample.java`
 */
public class ClassFileApiReadingExample {
	public static void main(String[] args) throws Exception {
		var classBytes = Files.readAllBytes(Paths.get("BasicClass.class"));

		// ClassModel is an immutable description of a class file
		ClassModel cm = ClassFile.of().parse(classBytes);

		// ClasModel is lazy, iterating over it parses the entire class
		for (ClassElement ce : cm) {
			// possible values: https://download.java.net/java/early_access/jdk24/docs/api/java.base/java/lang/classfile/ClassElement.html
			switch (ce) {
				case Superclass cn -> System.out.println("Superclass: " + cn.superclassEntry().name().stringValue());

				case Interfaces i -> {
					var interfaces = i.interfaces().stream()
							.map(ClassEntry::name)
							.collect(Collectors.joining(","));
					System.out.println("Interfaces: " + interfaces);
				}

				// ClassModel.fields()
				case FieldModel fm -> {
					var fieldType = fm.fieldTypeSymbol().displayName();
					var fieldName = fm.fieldName().stringValue();
					System.out.printf("Field: %s %s%n", fieldType, fieldName);
				}

				// ClassModel.methods()
				case MethodModel mm -> {
					var symbol = mm.methodTypeSymbol();
					var returnType = symbol.returnType().displayName();
					var parameters = symbol.parameterList().stream()
							.map(ClassDesc::displayName)
							.collect(Collectors.joining(","));
					var methodName = mm.methodName().stringValue();
					System.out.printf("Method: %s %s(%s)%n", returnType, methodName, parameters);
				}
				default -> System.out.printf("Other: %s%n", ce);
			}
		}
	}
}
