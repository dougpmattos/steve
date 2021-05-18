
# 
# JAVA_HOME environment variable must be set either externally in your
# environment or internally here by uncommenting out one of the lines
# below and assiging it the location of a valid JDK 15 runtime.
#
# MacOS example
#export JAVA_HOME="~/IDE/jdk-15.jdk/Contents/Home"
# Linux Example
#export JAVA_HOME="~/jdk-15"

#
# Until the jpackage module API is formalized, each JDK release (starting with
# JDK 14), will go through refinements meaning there may be incompatibilities.
# Until the API is cast in stone, we'll check to make sure the JDK version
# in use matches the EXPECTED_JDK_VERSION defined below
#
EXPECTED_JDK_VERSION="15"

#
# Location of JDK with jpackage utility. This is here for legacy reasons.
# First prototype required a separate JDK build.  Starting with JDK 14,
# it's built into the standard JDK.
#
JPACKAGE_HOME=$JAVA_HOME

#
# Vendor string used when creating native installers with the
# jpackage utility.
#
VENDOR_STRING="DOUGLAS.MATTOS"

#
# Unless these script files have been deliberately moved, the parent
# directory of the directory containing these script files houses
# the maven project and source code.
#
PROJECTDIR=..

#
# Determine Operating System platform. Currently only MacOS (PLATFORM=mac)
# and Linux (PLATFORM=linux) are supported.
#
case "$(uname)" in
	Darwin)
		PLATFORM=mac
		;;
	Linux)
		PLATFORM=linux
		;;
	*)
		echo "Only x86_64 versions of MacOS or Linux supported, '$(uname)' unavailable."
	exit 1
esac

#
# Application specific variables
#
PROJECT=STEVE
VERSION=3.0.0
MAINMODULE=steve
MAINCLASS=view.stevePane.Main
MAINJAR=$PROJECT-$VERSION.jar
INSTALLERNAME=$PROJECT-$VERSION
LAUNCHER=$PROJECT

#
# Local gradle repository for jars
#
REPO=~/.gradle/caches/modules-2/files-2.1

#
# Directory under which gradle places compiled classes and built jars
#
TARGET=build

#
# Directory where custom runtime image (via jlink) is created
#
IMAGE=image

#
# Directory where application image (via jpackage) is created
#
APPIMAGE=appimage

#
# Directory where application installer (via jpackage) is created
#
INSTALLER=installer

#
# Required external modules for this application
#
EXTERNAL_MODULES=(
	  "$REPO/commons-io/commons-io/2.5/2852e6e05fbb95076fc091f6d1780f1f8fe35e0f/commons-io-2.5.jar"
#
    "$REPO/com.googlecode.json-simple/json-simple/1.1.1/c9ad4a0850ab676c5c64461a05ca524cdfff59f1/json-simple-1.1.1.jar"
#
    "$REPO/net.sf.jgrapht/jgrapht/0.8.3/f6e272c8440b2d216576ba53642ea693106f9ec7/jgrapht-0.8.3.jar"
#
    "$REPO/jgraph/jgraph/5.13.0.0/577a30b3c2cf7decbb68471f5c96bfa1647b98dd/jgraph-5.13.0.0.jar"
#
    "$REPO/br.uff.midiacom.ana/aNa.jar"
#
    "$REPO/com.clarifai.clarifai-api2/core/2.8.1/7ef0978deebe8386d0ef8470f0559b0b3109bf9c/core-2.8.1.jar"
#
    "$REPO/com.squareup.okhttp3/okhttp/3.12.0/b36f4a04584c0fb0d9af2d3401cdff8dacb1ea54/okhttp-3.12.0.jar"
#
    "$REPO/com.squareup.okio/okio/1.15.0/bc28b5a964c8f5721eb58ee3f3c47a9bcbf4f4d8/okio-1.15.0.jar"
#
#    "$REPO/com.google.guava/guava/26.0-android/ef69663836b339db335fde0df06fb3cd84e3742b/guava-26.0-android.jar"
#
    "$REPO/com.jfoenix/jfoenix/9.0.8/b5a497c80b2ea69e9a68ad81d866ea3c7b7a08d9/jfoenix-9.0.8.jar"
#
    "$REPO/com.google.code.findbugs/jsr305/3.0.2/25ea2e8b0c338a877313bd4672d3fe056ea78f0d/jsr305-3.0.2.jar"
#
    "$REPO/com.google.code.gson/gson/2.8.5/f645ed69d595b24d4cf8b3fbb64cc505bede8829/gson-2.8.5.jar"
#
    "$REPO/io.grpc/grpc-core/1.17.1/bd3c50e6eb7d5024e56cb8737d98d2cc9e1ae4ef/grpc-core-1.17.1.jar"
#
    "$REPO/com.google.errorprone/error_prone_annotations/2.5.1/562d366678b89ce5d6b6b82c1a073880341e3fba/error_prone_annotations-2.5.1.jar"

#    "$REPO/org.checkerframework/checker-compat-qual/2.5.2/dc0b20906c9e4b9724af29d11604efa574066892/checker-compat-qual-2.5.2.jar"

    "$REPO/com.google.j2objc/j2objc-annotations/1.3/ba035118bc8bac37d7eff77700720999acd9986d/j2objc-annotations-1.3.jar"
#
    "$REPO/com.google.protobuf/protobuf-java/3.6.1/d06d46ecfd92ec6d0f3b423b4cd81cb38d8b924/protobuf-java-3.6.1.jar"
#
    "$REPO/com.google.protobuf/protobuf-java-util/3.6.1/35f62815e87c32b01bf1ed8c5aa3f9e33a08c2f3/protobuf-java-util-3.6.1.jar"
#
    "$REPO/io.grpc/grpc-stub/1.17.1/456090f1a3935fc869ba80b8429d750d4f3b05c4/grpc-stub-1.17.1.jar"
#
    "$REPO/io.grpc/grpc-protobuf/1.17.1/797b899fa461411414177368679fe37435e2b977/grpc-protobuf-1.17.1.jar"
#
    "$REPO/io.grpc/grpc-protobuf-lite/1.17.1/fb539b6c9f1e33a6ece97ed2a278d1d7c7a2f316/grpc-protobuf-lite-1.17.1.jar"
#
    "$REPO/io.grpc/grpc-netty-shaded/1.17.1/3629761de2800e6dab395fa44b064160adcd8b/grpc-netty-shaded-1.17.1.jar"
#
#    "$REPO/io.opencensus/opencensus-contrib-grpc-metrics/0.17.0/4b82972073361704f57fa2107910242f1143df25/opencensus-contrib-grpc-metrics-0.17.0.jar"
#
    "$REPO/com.google.guava/guava/30.1.1-jre/87e0fd1df874ea3cbe577702fe6f17068b790fd8/guava-30.1.1-jre.jar"
#
    "$REPO/com.google.guava/failureaccess/1.0.1/1dcf1de382a0bf95a3d8b0849546c88bac1292c9/failureaccess-1.0.1.jar"
#
    "$REPO/org.checkerframework/checker-qual/3.8.0/6b83e4a33220272c3a08991498ba9dc09519f190/checker-qual-3.8.0.jar"
#
    "$REPO/com.h2database/h2/1.4.200/f7533fe7cb8e99c87a43d325a77b4b678ad9031a/h2-1.4.200.jar"
#
    "$REPO/org.jetbrains/annotations/20.1.0/2fcd1f3225bca0c4a7bc931142076f8c1e80993f/annotations-20.1.0.jar"
#
    "$REPO/io.opencensus/opencensus-api/0.17.0/b9c91321f9c9f20f3a4627bfd9e3097164f85e6/opencensus-api-0.17.0.jar"
#
    "$REPO/io.opencensus/opencensus-contrib-grpc-metrics/0.17.0/4b82972073361704f57fa2107910242f1143df25/opencensus-contrib-grpc-metrics-0.17.0.jar"
#
    "$REPO/com.google.guava/listenablefuture/9999.0-empty-to-avoid-conflict-with-guava/b421526c5f297295adef1c886e5246c39d4ac629/listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar"

    "$REPO/org.codehaus.mojo/animal-sniffer-annotations/1.17/f97ce6decaea32b36101e37979f8b647f00681fb/animal-sniffer-annotations-1.17.jar"

#    "$REPO/org.conscrypt/conscrypt-openjdk-uber/2.5.2/d858f142ea189c62771c505a6548d8606ac098fe/conscrypt-openjdk-uber-2.5.2.jar"
#
    "$REPO/com.google.api.grpc/proto-google-common-protos/1.0.0/86f070507e28b930e50d218ee5b6788ef0dd05e6/proto-google-common-protos-1.0.0.jar"
#
#    "$REPO/com.google.android/android/4.1.1.4/3fb039385e71e9aa2ba547ea9ea8caa34a4ffac7/android-4.1.1.4.jar"
#
#    "$REPO/org.apache.httpcomponents/httpcore/4.3.2/31fbbff1ddbf98f3aa7377c94d33b0447c646b6e/httpcore-4.3.2.jar"
#
#	  "$REPO/org.apache.httpcomponents/httpclient/4.3.4/a9a1fef2faefed639ee0d0fba5b3b8e4eb2ff2d8/httpclient-4.3.4.jar"
#
#	  "$REPO/xmlpull/xmlpull/1.1.3.4a/9de939345e15660dfe90bc9b26194925e28bd8d4/xmlpull-1.1.3.4a.jar"
#
#	  "$REPO/org.khronos/opengl-api/gl/8827d279add29cf9115820671e7879de7bf80390/opengl.jar"

    "$REPO/org.openjfx/javafx-controls/15.0.1/cb743a50156d7eca281bfc3547aff4d146972e02/javafx-controls-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-fxml/15.0.1/120a83ddfd726d9ef09e9569f53459106e8de467/javafx-fxml-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-base/15.0.1/b91bc3dfc230b46f11146686648a42921de0c23/javafx-base-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-media/15.0.1/2799b4e418310939d9328ec6bf35e5b2c7036164/javafx-media-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-web/15.0.1/588cc1415c506b4f98fa6fc82ce03b4a0b3cd2ce/javafx-web-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-swing/15.0.1/1dcabb4f0d50c00fc9cc92306131762fc4977abd/javafx-swing-15.0.1-$PLATFORM.jar"
#
    "$REPO/org.openjfx/javafx-graphics/15.0.1/98ced13a06ad705ce0014df60794cedbf297080b/javafx-graphics-15.0.1-$PLATFORM.jar"
    )

#
# Create a module-path for the java command.  It either includes the classes
# in the $TARGET directory or the $TARGET/$MAINJAR (if it exists) and the
# $EXTERNAL_MODULES defined in env.sh.
#
if [ -f $PROJECTDIR/$TARGET/$MAINJAR ]
then
	MODPATH=$TARGET/libs/$MAINJAR
else
	MODPATH=$TARGET/libs/
fi
for ((i=0; i<${#EXTERNAL_MODULES[@]}; i++ ))
do
    MODPATH=${MODPATH}":""${EXTERNAL_MODULES[$i]}"
done

#
# Function to print command-line options to standard output
#
print_options() {
	echo usage: $0 [-?,--help,-e,-n,-v]
	echo -e "\t-? or --help - print options to standard output and exit"
	echo -e "\t-e - echo the jdk command invocations to standard output"
	echo -e "\t-n - don't run the java commands, just print out invocations"
	echo -e "\t-v - --verbose flag for jdk commands that will accept it"
	echo
}

#
# Process command-line arguments:  Not all flags are valid for all invocations,
# but we'll parse them anyway.
#
#   -? or --help  print options to standard output and exit
#   -e	echo the jdk command invocations to standard output
#   -n  don't run the java commands, just print out invocations
#   -v 	--verbose flag for jdk commands that will accept it
#
VERBOSE_OPTION=""
ECHO_CMD=false
EXECUTE_OPTION=true

for i in $*
do
	case $i in
		"-?")
			print_options
			exit 0
			;;
		"--help")
			print_options
			exit 0
			;;
		"-e")
			ECHO_CMD=true
			;;
		"-n")
			ECHO_CMD=true
			EXECUTE_OPTION=false
			;;
		"-v")
			VERBOSE_OPTION="--verbose"
			;;
                *)
			echo "$0: bad option '$i'"
			print_options
			exit 1
			;;
	esac
done

#
# Function to execute command specified by arguments.  If $ECHO_CMD is true
# then print the command out to standard output first.
#
exec_cmd() {
	if [ "$ECHO_CMD" = "true" ]
	then
		echo
		echo $*
	fi
        if [ "$EXECUTE_OPTION" = "true" ]
	then
		eval $*
	fi
}

#
# Check if $PROJECTDIR exists
#
if [ ! -d $PROJECTDIR ]
then
	echo Project Directory "$PROJECTDIR" does not exist. Edit PROJECTDIR variable in sh/env.sh
	exit 1
fi

#
# Check if JAVA_HOME is both set and assigned to a valid Path
#
if [ -z $JAVA_HOME ]
then
    echo "JAVA_HOME Environment Variable is not set. Set the JAVA_HOME variable to a vaild JDK runtime location in your environment or uncomment and edit the 'export JAVA_HOME=' statement at the beginning of the sh/env.sh file." 
	exit 1
elif [ ! -d $JAVA_HOME ]
then
    echo "Path for JAVA_HOME \"$JAVA_HOME\" does not exist. Set the JAVA_HOME variable to a vaild JDK runtime location in your environment or uncomment and edit the 'export JAVA_HOME=' statement at the beginning of the sh\env.sh file."
	exit 1
fi

#
# Check to make sure we have the proper Java Version
#
java_version_output=`$JAVA_HOME/bin/java -version 2>&1`
jdk_version_unfiltered=`echo $java_version_output | awk -F" " '{print $3}'`
# Some versions return the Java version in double quotes ("").  Git rid of
# them for a proper comparison.
jdk_version_untruncated=`echo $jdk_version_unfiltered | sed 's/"//g'`
# Truncate anything after major release i.e. 14.0.2 => 14, 11-redhat.0.1 => 11
jdk_version=`echo $jdk_version_untruncated | cut -d"." -f1 | cut -d"-" -f1`
if [ "$jdk_version" != "$EXPECTED_JDK_VERSION" ]
then
    echo "JDK version '$jdk_version' does not match expected version: '$EXPECTED_JDK_VERSION'. JAVA_HOME should be set to a JDK $EXPECTED_JDK_VERSION implementation."
	exit 1
fi


#
# Check if $JPACKAGE_HOME exists
#
if [ ! -d $JPACKAGE_HOME ]
then
	echo "jpackage home \"$JPACKAGE_HOME\" does not exist. Edit JPACKAGE_HOME variable in sh/env.sh"
	exit 1
fi

cd $PROJECTDIR
