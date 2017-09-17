# create the temp folders
mkdir -p output/mlibs


# Building the service provider module
echo 'Building provider module'
mkdir output/classes
# compile the files
$JAVA9_HOME/bin/javac -d output/classes --module-version 1.0 `find provider -name *.java`
# build the modules
$JAVA9_HOME/bin/jar -cf output/mlibs/provider-1.0.jar -C output/classes .
rm -rf output/classes


# Building the services consumer module
echo 'Building consumer module'
mkdir output/classes
# compile the files
$JAVA9_HOME/bin/javac -p output/mlibs -d output/classes --module-version 1.0 `find consumer -name *.java`
# build the modules
$JAVA9_HOME/bin/jar -c -f output/mlibs/consumer-1.0.jar \
	--main-class com.github.wesleyegberto.consumer.MadScientist \
    -C output/classes .
rm -rf output/classes

# Run the builds
echo 'Running'
$JAVA9_HOME/bin/java -p output/mlibs -m consumer