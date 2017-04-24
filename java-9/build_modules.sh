# create the temp folders
mkdir -p output/mlibs


# Building the first module
echo 'Building Math module'
mkdir output/classes
# compile the files
$JAVA9_HOME/bin/javac -d output/classes `find math_lib -name *.java`
# build the modules
$JAVA9_HOME/bin/jar -cf output/mlibs/mathlib.jar -C output/classes .
rm -rf output/classes


# Building the second module that use the first one
echo 'Building Calculator module'
mkdir output/classes
# -p define the modules folder, will get all JAR files
$JAVA9_HOME/bin/javac -p output/mlibs -d output/classes `find calculator -name *.java`

$JAVA9_HOME/bin/jar -c -f output/mlibs/calculator.jar \
	--main-class com.github.wesleyegberto.calculator.SimpleCalculator \
	-C output/classes .
rm -rf output/classes