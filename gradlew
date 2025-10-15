#!/usr/bin/env bash
# gradlew stub: tries to use included wrapper, else calls system gradle
if [ -x "./gradlew" ] && [ "$0" != "./gradlew" ]; then
  exec ./gradlew "$@"
fi
if command -v gradle >/dev/null 2>&1; then
  gradle "$@"
else
  echo "No gradle wrapper or system gradle available on runner. Try adding Gradle wrapper or ensure Gradle is installed."
  exit 1
fi
