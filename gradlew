#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Add default JVM options here. You can also use JAVA_OPTS in the environment.
DEFAULT_JVM_OPTS=""

APP_NAME="Gradle"
APP_BASE_NAME=$(basename "$0")

# Use the maximum available file descriptors.
[ -z "$ULIMIT" ] && ULIMIT="-n 4096"
if [ $(command -v ulimit) ] ; then
    ulimit $ULIMIT
fi

# For Darwin, add the path to JNI libraries to LD_LIBRARY_PATH
if [ "$(uname)" = "Darwin" ] ; then
    if [ -n "$JAVA_HOME" ] ; then
        # The Java home path contains '/Contents/Home'
        export LD_LIBRARY_PATH="$LD_LIBRARY_PATH:$JAVA_HOME/../Libraries"
    fi
fi

# Determine the Java command to use to launch the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses "$JAVA_HOME/jre/sh/java" as the actual executable.
        JAVA_EXE="$JAVA_HOME/jre/sh/java"
    elif [ -x "$JAVA_HOME/bin/java" ] ; then
        JAVA_EXE="$JAVA_HOME/bin/java"
    fi
elif [ "$(uname)" = "Darwin" ] ; then
    JAVA_EXE=$(/usr/libexec/java_home 2>/dev/null)/bin/java
    if [ -z "$JAVA_EXE" ] || [ ! -x "$JAVA_EXE" ] ; then
        JAVA_EXE=$(command -v java)
    fi
else
    JAVA_EXE=$(command -v java)
fi

if [ -z "$JAVA_EXE" ] ; then
    echo "Error: A Java installation could not be found." >&2
    echo "Please set the JAVA_HOME environment variable or add 'java' to your PATH." >&2
    exit 1
fi

# Determine the directory of the script and its project root.
APP_HOME=$(dirname "$0")

# For Cygwin, ensure paths are in UNIX format before anything else
cygwin=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
esac

if "$cygwin" ; then
    APP_HOME=$(cygpath --unix "$APP_HOME")
fi

# Resolve symlinks in APP_HOME. This ensures that the script knows its real location.
# This loop handles nested symlinks
while [ -L "$APP_HOME" ]; do
    APP_HOME=$(readlink "$APP_HOME")
done

# Absolute path to the root of the project
APP_HOME=$(cd "$APP_HOME" && pwd -P)

# Calculate the wrapper classpath
GRADLE_WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"

if [ ! -f "$GRADLE_WRAPPER_JAR" ]; then
    echo "Error: The Gradle wrapper jar '$GRADLE_WRAPPER_JAR' does not exist." >&2
    echo "This can happen if the 'gradle wrapper' task has not been run or if the project has been cleaned." >&2
    echo "Please check your project setup and try again." >&2
    exit 1
fi

# Collect all JVM options.
# JAVA_OPTS and GRADLE_OPTS are expected to be set in the environment.
# GRADLE_OPTS can be used for things like heap size and other JVM args specific to Gradle.
# JAVA_OPTS is for general Java options that might apply to any Java process.
ALL_JVM_OPTS="$DEFAULT_JVM_OPTS"
if [ -n "$JAVA_OPTS" ]; then
    ALL_JVM_OPTS="$ALL_JVM_OPTS $JAVA_OPTS"
fi
if [ -n "$GRADLE_OPTS" ]; then
    ALL_JVM_OPTS="$ALL_JVM_OPTS $GRADLE_OPTS"
fi


# Exec the Gradle wrapper.
exec "$JAVA_EXE" $ALL_JVM_OPTS -classpath "$GRADLE_WRAPPER_JAR" org.gradle.wrapper.GradleWrapperMain "$@"

# If we get here, the exec command failed.
echo "Failed to execute Gradle wrapper." >&2
exit 1