import java.lang.classfile.*;
import java.lang.constant.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.OpenOption;

/**
 * CodeBuilder doc: https://docs.oracle.com/en/java/javase/24/docs/api/java.base/java/lang/classfile/CodeBuilder.html
 *
 * Run: `java ClassFileApiWritingExample.java`
 */
public class ClassFileApiWritingExample {
	public static void main(String[] args) {
		var className = "HelloWorldFromClassFile";

		var CD_System = ClassDesc.of("java.lang.System");
		var CD_PrintStream = ClassDesc.of("java.io.PrintStream");
		// we can use ClassDesc.of to get String class descriptor or use ConstantDescs with fixed class descriptors
		// arrayType returns a class descriptors for array of that type
		var MTD_void_StringArray = MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_String.arrayType());
		var MTD_void_String = MethodTypeDesc.of(ConstantDescs.CD_void, ClassDesc.of("java.lang.String"));

		System.out.println("Building class");
		byte[] bytes = ClassFile.of().build(
				ClassDesc.of(className),
				clb -> clb.withFlags(ClassFile.ACC_PUBLIC)
						.withMethod(ConstantDescs.INIT_NAME, ConstantDescs.MTD_void, ClassFile.ACC_PUBLIC,
								mb -> mb.withCode(
										cob -> cob.aload(0)
												// invokes Object constructor
												.invokespecial(ConstantDescs.CD_Object, ConstantDescs.INIT_NAME, ConstantDescs.MTD_void)
												// calls System.out.println
												.getstatic(CD_System, "out", CD_PrintStream)
												.ldc("Class " + className + " constructor")
												.invokevirtual(CD_PrintStream, "println", MTD_void_String)
												.return_()
								)
						)
						// another way to build method without using MethodBuilder.withCode
						.withMethodBody("main", MTD_void_StringArray, ClassFile.ACC_PUBLIC + ClassFile.ACC_STATIC,
								cob -> cob.getstatic(CD_System, "out", CD_PrintStream)
										.ldc("Hello World from class built from Class File API")
										.invokevirtual(CD_PrintStream, "println", MTD_void_String)
										.return_()
						)
		);

		// writes the class to file
		// Inspect with: `javap HelloWorldFromClassFile.class`
		// Run with: `java HelloWorldFromClassFile`
		try {
			System.out.println("Writing class");
			Files.write(Paths.get(className + ".class"), bytes);
		} catch (Exception ex) {
			System.err.println("Error during writing: " + ex.getMessage());
		}

		// loads the written class
		try {
			System.out.println("Loading and running class");
			var clazz = ClassLoader.getSystemClassLoader().loadClass(className);
			var instance = clazz.getConstructors()[0].newInstance();
			clazz.getMethods()[0].invoke(instance, new Object[] { args });
		} catch (ClassNotFoundException ex) {
			System.err.println("Class not found");
		} catch (Exception ex) {
			System.err.println("Error during running: " + ex.getMessage());
		}
	}
}
