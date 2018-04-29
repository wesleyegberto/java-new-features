# create the temp folders
mkdir -p output/mlibs


# Building the API module
echo 'Building MathApi module'
mkdir output/classes
# compile the files
$JAVA9_HOME/bin/javac -d output/classes --module-version 1.0 `find math_api -name *.java`
# build the modules
$JAVA9_HOME/bin/jar -cf output/mlibs/mathapi-1.0.jar -C output/classes .
rm -rf output/classes


# Building the implementation module
echo 'Building MathImpl module'
mkdir output/classes
# compile the classes defining the modules path (-p) we use and our module version
$JAVA9_HOME/bin/javac -p output/mlibs -d output/classes --module-version 1.0 `find math_lib -name *.java`
# build the modules
$JAVA9_HOME/bin/jar -cf output/mlibs/mathlib-1.0.jar -C output/classes .
rm -rf output/classes


# Building the user module
echo 'Building Calculator module'
mkdir output/classes
$JAVA9_HOME/bin/javac -p output/mlibs -d output/classes `find calculator -name *.java`
# build the module defining the main class to run (when no class is specified)
$JAVA9_HOME/bin/jar -c -f output/mlibs/calculator.jar \
	--main-class com.github.wesleyegberto.calculator.SimpleCalculator \
	-C output/classes .
rm -rf output/classes