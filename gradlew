#!/usr/bin/env sh

# Ensure correct line endings and standard execution for Linux/GitHub Runners
# This script launches the local Gradle build automation engine.

DIRNAME=$(dirname "$0")
if [ -z "$DIRNAME" ]; then
    DIRNAME="."
fi

exec "$DIRNAME/gradle/wrapper/gradlew" "$@"
