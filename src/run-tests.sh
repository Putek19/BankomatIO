#!/bin/bash
set -e

JUNIT_VERSION="1.10.0"
JUNIT_JAR="junit-platform-console-standalone-${JUNIT_VERSION}.jar"
JUNIT_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/${JUNIT_VERSION}/${JUNIT_JAR}"

MOCKITO_VERSION="5.8.0"
MOCKITO_JAR="mockito-core-${MOCKITO_VERSION}.jar"
MOCKITO_URL="https://repo1.maven.org/maven2/org/mockito/mockito-core/${MOCKITO_VERSION}/${MOCKITO_JAR}"

BYTEBUDDY_VERSION="1.14.11"
BYTEBUDDY_JAR="byte-buddy-${BYTEBUDDY_VERSION}.jar"
BYTEBUDDY_URL="https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy/${BYTEBUDDY_VERSION}/${BYTEBUDDY_JAR}"

BYTEBUDDY_AGENT_JAR="byte-buddy-agent-${BYTEBUDDY_VERSION}.jar"
BYTEBUDDY_AGENT_URL="https://repo1.maven.org/maven2/net/bytebuddy/byte-buddy-agent/${BYTEBUDDY_VERSION}/${BYTEBUDDY_AGENT_JAR}"

OBJENESIS_VERSION="3.3"
OBJENESIS_JAR="objenesis-${OBJENESIS_VERSION}.jar"
OBJENESIS_URL="https://repo1.maven.org/maven2/org/objenesis/objenesis/${OBJENESIS_VERSION}/${OBJENESIS_JAR}"

SUITE_API_VERSION="1.10.0"
SUITE_API_JAR="junit-platform-suite-api-${SUITE_API_VERSION}.jar"
SUITE_API_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-suite-api/${SUITE_API_VERSION}/${SUITE_API_JAR}"

SUITE_ENGINE_JAR="junit-platform-suite-engine-${SUITE_API_VERSION}.jar"
SUITE_ENGINE_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-suite-engine/${SUITE_API_VERSION}/${SUITE_ENGINE_JAR}"

SUITE_COMMONS_JAR="junit-platform-suite-commons-${SUITE_API_VERSION}.jar"
SUITE_COMMONS_URL="https://repo1.maven.org/maven2/org/junit/platform/junit-platform-suite-commons/${SUITE_API_VERSION}/${SUITE_COMMONS_JAR}"

LIB_DIR="lib"
TEST_DIR="test"
TEST_CLASSES_DIR="${TEST_DIR}/classes"
CLASSES_DIR="classes"

download_if_missing() {
    local file=$1
    local url=$2
    if [ ! -f "${LIB_DIR}/${file}" ]; then
        echo "Pobieranie ${file}..."
        curl -L -o "${LIB_DIR}/${file}" "${url}"
        echo "${file} pobrany pomyślnie"
    else
        echo "${file} już istnieje"
    fi
}

echo "=== Przygotowanie środowiska ==="
mkdir -p "${LIB_DIR}"
mkdir -p "${CLASSES_DIR}"
mkdir -p "${TEST_CLASSES_DIR}"

download_if_missing "${JUNIT_JAR}" "${JUNIT_URL}"
download_if_missing "${MOCKITO_JAR}" "${MOCKITO_URL}"
download_if_missing "${BYTEBUDDY_JAR}" "${BYTEBUDDY_URL}"
download_if_missing "${BYTEBUDDY_AGENT_JAR}" "${BYTEBUDDY_AGENT_URL}"
download_if_missing "${OBJENESIS_JAR}" "${OBJENESIS_URL}"
download_if_missing "${SUITE_API_JAR}" "${SUITE_API_URL}"
download_if_missing "${SUITE_ENGINE_JAR}" "${SUITE_ENGINE_URL}"
download_if_missing "${SUITE_COMMONS_JAR}" "${SUITE_COMMONS_URL}"

# Tworzymy classpath dla bibliotek
LIB_CP="${LIB_DIR}/${JUNIT_JAR}:${LIB_DIR}/${MOCKITO_JAR}:${LIB_DIR}/${BYTEBUDDY_JAR}:${LIB_DIR}/${BYTEBUDDY_AGENT_JAR}:${LIB_DIR}/${OBJENESIS_JAR}:${LIB_DIR}/${SUITE_API_JAR}:${LIB_DIR}/${SUITE_ENGINE_JAR}:${LIB_DIR}/${SUITE_COMMONS_JAR}"

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
    -cp "${CLASSES_DIR}:${LIB_CP}" \
    ${TEST_DIR}/Model/*.java \
    ${TEST_DIR}/Kontroler/*.java \
    ${TEST_DIR}/Suite*.java 2>/dev/null || \
javac -d "${TEST_CLASSES_DIR}" -sourcepath "${TEST_DIR}:." \
    -cp "${CLASSES_DIR}:${LIB_CP}" \
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
    --class-path "${CLASSES_DIR}:${TEST_CLASSES_DIR}:${LIB_DIR}/${MOCKITO_JAR}:${LIB_DIR}/${BYTEBUDDY_JAR}:${LIB_DIR}/${BYTEBUDDY_AGENT_JAR}:${LIB_DIR}/${OBJENESIS_JAR}:${LIB_DIR}/${SUITE_API_JAR}:${LIB_DIR}/${SUITE_ENGINE_JAR}:${LIB_DIR}/${SUITE_COMMONS_JAR}" \
    --scan-class-path "${TEST_CLASSES_DIR}"

echo ""
echo "=== Testy zakończone ==="
