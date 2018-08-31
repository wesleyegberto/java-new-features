rm -rf target

CLASSES_DIR="target/classes"
JAR_DIR="target/jars"

mkdir -p $CLASSES_DIR
mkdir -p $JAR_DIR

$JAVA9_HOME/bin/javac -p jdk.incubator.httpclient -d $CLASSES_DIR --module-version 1.0 `find src/main/java -name *.java`

	# --main-class HttpClientTest \
$JAVA9_HOME/bin/jar -c -f $JAR_DIR/http-client-test.jar \
    --main-class CollectionsCopyOf.CollectionsTest \
	-C $CLASSES_DIR .


$JAVA9_HOME/bin/java -p $JAR_DIR -m J9NewFeatures