#!/usr/bin/env sh

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Resolve links - $0 may be a softlink
PRG="$0"

while [ -h "$PRG" ]; do
  LS=`ls -ld "$PRG"`
  LINK=`expr "$LS" : '.*-> \(.*\)$'`
  if expr "$LINK" : '/.*' > /dev/null; then
    PRG="$LINK"
  else
    PRG=`dirname "$PRG"`/"$LINK"
  fi
done

APP_HOME=`dirname "$PRG"`

# OOM for the JVM
DEFAULT_JVM_OPTS="-Xmx64m -Xms64m"

# Add default JVM options here. You can also use the GRADLE_JVM_OPTS environment variable.
# On Darwin (macOS), the user's default locale can be malformed, which causes the JVM to fail.
# https://github.com/gradle/gradle/issues/21714
if [ "$(uname -s)" = "Darwin" ]; then
    if [ -z "$LC_ALL" ] && [ -z "$LANG" ]; then
        DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS -Duser.language=en"
        DEFAULT_JVM_OPTS="$DEFAULT_JVM_OPTS -Duser.country=US"
    fi
fi

if [ "x$GRADLE_JVM_OPTS" != "x" ]; then
    JVM_OPTS="$GRADLE_JVM_OPTS"
fi

if [ "x$JVM_OPTS" = "x" ]; then
    JVM_OPTS="$DEFAULT_JVM_OPTS"
fi

# Determine the Java command to run.
if [ -n "$JAVA_HOME" ]; then
  if [ -x "$JAVA_HOME/jre/sh/java" ]; then
    # IBM's JDK on AIX uses "$JAVA_HOME/jre/sh/java" as the actual executable.
    JAVA_EXE="$JAVA_HOME/jre/sh/java"
  elif [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA_EXE="$JAVA_HOME/bin/java"
  else
    echo "Warning: JAVA_HOME exists but Java binary not found." >&2
  fi
elif type -p java > /dev/null; then
    JAVA_EXE=java
else
    echo "Error: JAVA_HOME is not set and no 'java' command can be found in your PATH." >&2
    echo "       Please set the JAVA_HOME variable in your environment to match the location of your Java installation." >&2
    exit 1
fi

# Escape application args for Java. It's an array of string literals
APP_ARGS=""
for var in "$@"
do
    APP_ARGS="$APP_ARGS \"$var\""
done

# Split up the JVM_OPTS for the Java command.
JAVA_OPTS=($JVM_OPTS)

# Execute Gradle.
exec "$JAVA_EXE" "${JAVA_OPTS[@]}" -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain $APP_ARGS