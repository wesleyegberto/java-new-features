/**
 * This file defines the module.
 * We can define the modules it requires and the packages it exports.
 */
module mathlib {
	// requires java.base // default import

	// use transitive to also "export" a dependency
	// thus we won't need to import mathapi in the clients
	requires transitive mathapi;

	// export any public class (visibility)
	exports com.github.wesleyegberto.mathlib;
	// subpackages won't be exported

}
