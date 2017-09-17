/**
 * Define the module which provides the service.
 */
module provider {
	// exports the interface
	exports com.github.wesleyegberto.provider;
	// define the implementation for the interface
	provides com.github.wesleyegberto.provider.Calculator
		with com.github.wesleyegberto.provider.dadams.HitchhikerCalculator;
}