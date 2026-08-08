#!/usr/bin/env bash
MAVEN_WRAPPER_DIR=".mvn/wrapper"
DIR="$(cd "$(dirname "$0")" && pwd)"
JAVA_EXE="java"

if [ -n "$MAVEN_HOME" ]; then
  MAVEN_CMD="$MAVEN_HOME/bin/mvn"
fi

if [ -z "$MAVEN_CMD" ]; then
  MAVEN_CMD="$DIR/$MAVEN_WRAPPER_DIR/maven-wrapper.jar"
fi

exec "$JAVA_EXE" -jar "$DIR/$MAVEN_WRAPPER_DIR/maven-wrapper.jar" "$@"
