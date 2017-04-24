/**
 * This file defines the module.
 * We can define the modules it requires and the packages it exports.
 */
module mathlib {
	// requires java.base // default import

	// export any public class (visibility)
	exports com.github.wesleyegberto.mathlib;
	// subpackages won't be exported

	// we can ignore Illegal access error using
	// the flag in java: --permit-illegal-access
}
