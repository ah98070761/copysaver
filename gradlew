#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Set GRADLE_HOME to the directory where the gradle system is installed.
# We assume that it is the directory in which this script resides.
APP_BASE_NAME=`basename "$0"`
APP_NAME="Gradle"
APP_DIR=`dirname "$0"`
APP_HOME=`cd "$APP_DIR"; pwd`

# Add default JVM options here. You may also use JAVA_OPTS and GRADLE_OPTS.
DEFAULT_JVM_OPTS=""

# Use the relevant OS component to find the Java binary.
# On macOS, use /usr/bin/java which is a symlink to the current JDK's java binary.
# On other OS, use the java binary found in JAVA_HOME or in the PATH.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/jre/bin/java"
    elif [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVACMD="$JAVA_HOME/bin/java"
    fi
fi

if [ -z "$JAVACMD" ] ; then
    if [ `uname -s` = "Darwin" ] ; then
        JAVACMD="/usr/bin/java"
    else
        JAVACMD=`which java`
    fi
fi

if [ -z "$JAVACMD" ] ; then
    echo "ERROR: JAVA_HOME is not set and no 'java' command can be found in your PATH."
    echo "Please set the JAVA_HOME variable in your environment to match the location of your Java installation."
    exit 1
fi

# Determine the Java version (major part)
JAVA_VERSION=`"$JAVACMD" -version 2>&1 | sed -E -n 's/.*version "([0-9]*)\..*".*/\1/p'`

# Default to 1.8 if Java version cannot be determined (should not happen with modern Java)
if [ -z "$JAVA_VERSION" ]; then
    JAVA_VERSION="8"
fi

# Define the wrapper jar and its path
GRADLE_WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

# Check if wrapper jar exists
if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    echo "ERROR: Cannot find wrapper jar '$GRADLE_WRAPPER_JAR'."
    echo "Please ensure that the gradle-wrapper.jar is present in your project."
    echo "If you're upgrading Gradle, you might need to run 'gradle wrapper' again."
    exit 1
fi

# Set the classpath
CLASSPATH="$GRADLE_WRAPPER_JAR"

# Collect all arguments
ARGS=()
while [ "$#" -gt 0 ]; do
    ARGS+=("$1")
    shift
done

# Launch the JVM
exec "$JAVACMD" \
    "${DEFAULT_JVM_OPTS}" \
    "${JAVA_OPTS}" \
    "${GRADLE_OPTS}" \
    "-classpath" "$CLASSPATH" \
    "org.gradle.wrapper.GradleWrapperMain" \
    "${ARGS[@]}"