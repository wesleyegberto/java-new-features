import java.lang.classfile.*;
import java.lang.constant.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * CodeBuilder doc: https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/lang/classfile/CodeBuilder.html
 *
 * - compile BasicClass and ClassFileApiTestTransformedBasicClass classes
 * - run transformation code in the BasicClass bytes
 * - run BasicClass testing class
 *
 * Run: `javac ClassFileApiTestTransformedBasicClass.java && java ClassFileApiTransformingExample.java && java ClassFileApiTestTransformedBasicClass`
 */
public class ClassFileApiTransformingExample {
	public static void main(String[] args) throws Exception {
		var classBytes = Files.readAllBytes(Paths.get("BasicClass.class"));

		var transformedClassBytes = dropMethod(classBytes);
		transformedClassBytes = transformMethod(transformedClassBytes);

		writeFile(transformedClassBytes);
	}

	static byte[] dropMethod(byte[] classBytes) {
		System.out.println("Dropping setters");
		ClassModel originalClassModel = ClassFile.of().parse(classBytes);
		byte[] transformedClassBytes = ClassFile.of().build(
				originalClassModel.thisClass().asSymbol(),
				classBuilder -> {
					for (ClassElement ce : originalClassModel) {
						// removes every setter
						if (!(ce instanceof MethodModel mm && mm.methodName().stringValue().startsWith("set"))) {
							classBuilder.with(ce);
						}
					}
				}
		);

		// there are helpful methods in ClassFile to do the transformations
		var classfile = ClassFile.of();
		transformedClassBytes = classfile.transformClass(
				classfile.parse(classBytes),
				ClassTransform.dropping(ce -> ce instanceof MethodModel mm && mm.methodName().stringValue().startsWith("set"))
		);
		return transformedClassBytes;
	}

	static byte[] transformMethod(byte[] classBytes) {
		System.out.println("Transforming timestamp getter");
		ClassModel originalClassModel = ClassFile.of().parse(classBytes);
		byte[] transformedClassBytes = ClassFile.of().build(
				originalClassModel.thisClass().asSymbol(),
				cb -> {
					for (ClassElement ce : originalClassModel) {
						// replaces the getTimestamp method
						if (ce instanceof MethodModel mm && mm.methodName().stringValue().equals("getTimestamp")) {
							var mtdInt = MethodTypeDesc.of(ConstantDescs.CD_int);
							cb.withFlags()
									.withMethodBody(
											mm.methodName().stringValue(), mtdInt, ClassFile.ACC_PUBLIC,
											cob -> cob.ldc(42).ireturn()
									);
						} else {
							cb.with(ce);
						}
					}
				}
		);
		return transformedClassBytes;
	}

	static void writeFile(byte[] bytes) {
		System.out.println("Writing BasicClass file");
		try {
			Files.write(Paths.get("BasicClass.class"), bytes);
		} catch (Exception ex) {
			System.err.println("Error during writing: " + ex.getMessage());
		}
	}
}
