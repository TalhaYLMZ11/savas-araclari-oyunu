#!/bin/sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
if command -v mvn >/dev/null 2>&1; then
  exec mvn -q -f "$SCRIPT_DIR/pom.xml" "$@"
fi

echo "Bu repoda Linux/macOS icin tam Maven Wrapper betigi yok."
echo "Windows'ta 'mvnw.cmd' kullanabilir veya Maven kuruluysa 'mvn' ile calistirabilirsin."
exit 1

