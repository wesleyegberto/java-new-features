#!/usr/bin/env bash
# To run: ./aot-code-example.sh
set -euo pipefail
cd "$(dirname "$0")"

OUT=target
rm -rf "$OUT" && mkdir -p "$OUT"

javac -d "$OUT/classes" Workload.java
jar --create --file "$OUT/app.jar" --main-class Workload -C "$OUT/classes" .

echo "=== 1. Without AOT cache ==="
java -cp "$OUT/app.jar" Workload

echo
echo "=== 2. Training run: creates the AOT cache (pre-linked classes + profiling data + AOT code) ==="
java -XX:AOTCacheOutput="$OUT/app.aot" -cp "$OUT/app.jar" Workload
ls -lh "$OUT/app.aot"

echo
echo "=== 3. Production run using the AOT cache ==="
time java -XX:AOTCache="$OUT/app.aot" -cp "$OUT/app.jar" Workload

echo
echo "=== 4. AOT code loading in -XX:+PrintCompilation (look for Workload::process) ==="
java -XX:AOTCache="$OUT/app.aot" -XX:+PrintCompilation -cp "$OUT/app.jar" Workload | grep -i "Workload::process" | head -5 || true

echo
echo "=== 5. Cache is required (fails if any constraint is violated, e.g. a different GC) ==="
java -XX:AOTCache="$OUT/app.aot" -XX:AOTMode=required -cp "$OUT/app.jar" Workload
java -XX:AOTCache="$OUT/app.aot" -XX:AOTMode=required -XX:+UseZGC -cp "$OUT/app.jar" Workload \
	|| echo "-> expected: AOT code contains GC-specific barriers, so the GC must be the same as the training run"

echo
echo "=== 6. Without AOT code (-XX:+UnlockDiagnosticVMOptions -XX:-AOTCodeCaching) ==="
time java -XX:AOTCache="$OUT/app.aot" -XX:+UnlockDiagnosticVMOptions -XX:-AOTCodeCaching -cp "$OUT/app.jar" Workload
