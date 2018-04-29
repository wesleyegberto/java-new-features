# we must set --module-path to JDK modules and our own modules
$JAVA9_HOME/bin/jlink --module-path $JAVA9_HOME/jmods:output/mlibs \
	--add-modules simplecalculator \
	--launcher simplecalculator=simplecalculator/com.github.wesleyegberto.calculator.SimpleCalculator \
	--output output/simplecalculator