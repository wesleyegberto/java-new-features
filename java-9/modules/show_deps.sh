echo "MathApi deps:"
$JAVA9_HOME/bin/jdeps output/mlibs/mathapi-1.0.jar

echo "\nMathLib deps:"
# we need to set modules path if a module use any other define
$JAVA9_HOME/bin/jdeps --module-path output/mlibs/ output/mlibs/mathlib-1.0.jar

echo "\nCalculator deps:"
$JAVA9_HOME/bin/jdeps --module-path output/mlibs/ output/mlibs/calculator.jar