#!/usr/bin/env bash
DIR="$(cd "$(dirname "$0")" && pwd)"
if [ -f "$DIR/gradle/wrapper/gradle-wrapper.jar" ]; then
  java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
else
  if command -v gradle >/dev/null 2>&1; then
    gradle "$@"
  else
    echo "Gradle wrapper jar not found and system gradle is not installed."
    exit 1
  fi
fi
