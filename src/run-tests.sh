#!/bin/bash

# Skrypt do uruchamiania testów JUnit 5 bez Apache Ant

set -e

JUNIT_VERSION="1.10.0"
JUNIT_JAR="junit-platform-console-standalone-${JUNIT_VERSION}.jar"
JUNIT_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/${JUNIT_JAR}"

LIB_DIR="lib"
TEST_DIR="test"
TEST_CLASSES_DIR="${TEST_DIR}/classes"
CLASSES_DIR="classes"

echo "=== Przygotowanie środowiska ==="
mkdir -p "${LIB_DIR}"
mkdir -p "${CLASSES_DIR}"
mkdir -p "${TEST_CLASSES_DIR}"
if [ ! -f "${LIB_DIR}/${JUNIT_JAR}" ]; then
    echo "Pobieranie JUnit 5..."
    curl -L -o "${LIB_DIR}/${JUNIT_JAR}" "${JUNIT_URL}"
    echo "JUnit 5 pobrany pomyślnie"
else
    echo "JUnit 5 już istnieje"
fi

echo ""
echo "=== Kompilacja kodu źródłowego ==="
javac -d "${CLASSES_DIR}" -sourcepath . \
    Model/*.java \
    Kontroler/*.java

if [ $? -eq 0 ]; then
    echo "✓ Kod źródłowy skompilowany pomyślnie"
else
    echo "✗ Błąd kompilacji kodu źródłowego"
    exit 1
fi

echo ""
echo "=== Kompilacja testów ==="
javac -d "${TEST_CLASSES_DIR}" -sourcepath "${TEST_DIR}:." \
    -cp "${CLASSES_DIR}:${LIB_DIR}/${JUNIT_JAR}" \
    ${TEST_DIR}/Model/*.java \
    ${TEST_DIR}/Kontroler/*.java

if [ $? -eq 0 ]; then
    echo "✓ Testy skompilowane pomyślnie"
else
    echo "✗ Błąd kompilacji testów"
    exit 1
fi

echo ""
echo "=== Uruchamianie testów ==="
java -jar "${LIB_DIR}/${JUNIT_JAR}" \
    --class-path "${CLASSES_DIR}:${TEST_CLASSES_DIR}" \
    --scan-class-path "${TEST_CLASSES_DIR}"

echo ""
echo "=== Testy zakończone ==="
