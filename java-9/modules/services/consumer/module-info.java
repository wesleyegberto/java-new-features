
module consumer {
	requires provider;
	// declares the use of the services
	uses com.github.wesleyegberto.provider.Calculator;
}