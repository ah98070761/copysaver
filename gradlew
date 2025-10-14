#!/usr/bin/env sh

##############################################################################
##
##  Gradle wrapper script for UNIX
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
APP_HOME=`dirname "$PRG"`

# Need this for relative symlinks
while [ -h "$PRG" ] ; do
    LS=`ls -ld "$PRG"`
    LINK=`expr "$LS" : '.*-> \(.*\)$'`
    if expr "$LINK" : '/.*' > /dev/null; then
        PRG="$LINK"
    else
        PRG=`dirname "$PRG"`/"$LINK"
    fi
done

APP_HOME=`dirname "$PRG"`
APP_HOME=`cd "$APP_HOME" && pwd`

# OS specific support (must be 'true' or 'false').
cygwin=false
darwin=false
mingw=false
case "`uname`" in
  CYGWIN*) cygwin=true ;;
  Darwin*) darwin=true
           if [ -z "$JAVA_HOME" ]; then
             if [ -x '/usr/libexec/java_home' ]; then
               export JAVA_HOME=`/usr/libexec/java_home`
             elif [ -d '/Library/Java/Home' ]; then
               export JAVA_HOME='/Library/Java/Home'
             fi
           fi
           ;;
  MINGW*) mingw=true ;;
esac

# For Darwin, add options to allow Java to be still run on older Java versions
if $darwin; then
  DEFAULT_JVM_OPTS="-Xdock:name=Gradle -Xdock:icon=\"$APP_HOME/gradle/wrapper/gradle-wrapper.jar\""
fi

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
  if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
    # IBM's JDK on AIX uses "$JAVA_HOME/jre/sh/java" as the actual executable.
    JAVA_CMD="$JAVA_HOME/jre/sh/java"
  else
    JAVA_CMD="$JAVA_HOME/bin/java"
  fi
  if [ ! -x "$JAVA_CMD" ] ; then
    die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME environment variable to the root directory of your Java installation."
  fi
else
  JAVA_CMD="java"
  # Check for Java in PATH
  if [ ! `command -v $JAVA_CMD` ]; then
    die "ERROR: JAVA_HOME is not set and no 'java' command can be found in your PATH.

Please set the JAVA_HOME environment variable to the root directory of your Java installation (or alter your PATH environment variable to include the location of the 'java' executable)."
  fi
fi

# Add default JVM options for better performance and memory management
# Ensure this doesn't override user-defined options
if [ -z "$DEFAULT_JVM_OPTS" ]; then
  DEFAULT_JVM_OPTS="-Xmx1024m -Dfile.encoding=UTF-8" # Example: 1GB max heap, UTF-8 encoding
fi

# The Java system properties and classpath for the wrapper.
# These will be passed to the JVM that runs the wrapper.
GRADLE_OPTS=""
if [ -f "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" ] ; then
  CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
else
  die "ERROR: Cannot find $APP_HOME/gradle/wrapper/gradle-wrapper.jar
This might indicate a corrupted Gradle distribution. If this is a project that uses the Gradle wrapper, please try to delete the '$APP_HOME/gradle' folder and re-run your build to download a fresh one."
fi

# Stop compiling in-process on older Android Gradle Plugin versions, as they can conflict
# with newer Gradle versions (e.g. Kotlin compiler version mismatches)
# This check is not strictly necessary for modern projects, but harmless.
if [ -n "$ANDROID_GRADLE_PLUGIN_VERSION" ] && [ "$(printf '%s\n' "3.6.0" "$ANDROID_GRADLE_PLUGIN_VERSION" | sort -V | head -n1)" = "3.6.0" ] && [ "$(printf '%s\n' "4.0.0" "$ANDROID_GRADLE_PLUGIN_VERSION" | sort -V | head -n1)" != "4.0.0" ]; then
    DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS -Dorg.gradle.daemon.profile=false -Dorg.gradle.parallel=true -Dorg.gradle.internal.launcher.welcome=full -Dorg.gradle.jvmargs=\"-Xmx1536M -Dfile.encoding=UTF-8 -XX:+HeapDumpOnOutOfMemoryError\" -Dorg.gradle.workers.max=4 -Dorg.gradle.configureondemand=true"
fi


# Main class
MAIN_CLASS="org.gradle.wrapper.GradleWrapperMain"

# Collect all arguments for the Java command
# This allows users to pass JVM options via JAVA_OPTS
exec "$JAVA_CMD" $DEFAULT_JVM_OPTS $JAVA_OPTS -classpath "$CLASSPATH" "$MAIN_CLASS" "$@"
