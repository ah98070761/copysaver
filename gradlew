#!/usr/bin/env bash

##############################################################################
##
##  Gradle start up script for POSIX systems
##
##############################################################################

# Helper function for debugging
# debug() {
#    echo "DEBUG: $@"
# }

# Set up the classpath for the wrapper
CLASSPATH=lib/gradle-launcher-8.4.jar:lib/gradle-wrapper-8.4.jar

# Find the project directory
APP_HOME=$(dirname "$0")

# Determine whether we use a custom java
if [ -z "$JAVA_HOME" ]; then
    JAVA_CMD="java"
else
    JAVA_CMD="$JAVA_HOME/bin/java"
fi

# Execute the Java code
exec "$JAVA_CMD" \
    -Xmx1024m \
    -Xms256m \
    -classpath "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" \
    org.gradle.wrapper.GradleWrapperMain \
    "$@"