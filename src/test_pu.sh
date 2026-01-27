#!/bin/bash

# Skrypt do uruchamiania testów przypadków użycia (Kontroler)
# Uruchamia tylko testy warstwy kontroli, które reprezentują przypadki użycia systemu

set -e

JUNIT_VERSION="1.10.0"
JUNIT_JAR="junit-platform-console-standalone-${JUNIT_VERSION}.jar"

MOCKITO_VERSION="5.8.0"
MOCKITO_JAR="mockito-core-${MOCKITO_VERSION}.jar"

BYTEBUDDY_VERSION="1.14.11"
BYTEBUDDY_JAR="byte-buddy-${BYTEBUDDY_VERSION}.jar"
BYTEBUDDY_AGENT_JAR="byte-buddy-agent-${BYTEBUDDY_VERSION}.jar"

OBJENESIS_VERSION="3.3"
OBJENESIS_JAR="objenesis-${OBJENESIS_VERSION}.jar"

SUITE_API_VERSION="1.10.0"
SUITE_API_JAR="junit-platform-suite-api-${SUITE_API_VERSION}.jar"
SUITE_ENGINE_JAR="junit-platform-suite-engine-${SUITE_API_VERSION}.jar"
SUITE_COMMONS_JAR="junit-platform-suite-commons-${SUITE_API_VERSION}.jar"

LIB_DIR="lib"
TEST_DIR="test"
TEST_CLASSES_DIR="${TEST_DIR}/classes"
CLASSES_DIR="classes"

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║  TESTY PRZYPADKÓW UŻYCIA (USE CASES)                          ║"
echo "║  Uruchamianie testów warstwy Kontroler                        ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Sprawdzenie czy istnieją wymagane biblioteki
if [ ! -f "${LIB_DIR}/${JUNIT_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${MOCKITO_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${BYTEBUDDY_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${BYTEBUDDY_AGENT_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${OBJENESIS_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${SUITE_API_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${SUITE_ENGINE_JAR}" ] || \
   [ ! -f "${LIB_DIR}/${SUITE_COMMONS_JAR}" ]; then
    echo "⚠️  Brak wymaganych bibliotek. Uruchom najpierw: ./run-tests.sh"
    exit 1
fi

# Sprawdzenie czy kod jest skompilowany
if [ ! -d "${CLASSES_DIR}" ] || [ ! -d "${TEST_CLASSES_DIR}" ]; then
    echo "⚠️  Kod nie jest skompilowany. Uruchom najpierw: ./run-tests.sh"
    exit 1
fi

# Tworzymy classpath dla bibliotek
LIB_CP="${LIB_DIR}/${JUNIT_JAR}:${LIB_DIR}/${MOCKITO_JAR}:${LIB_DIR}/${BYTEBUDDY_JAR}:${LIB_DIR}/${BYTEBUDDY_AGENT_JAR}:${LIB_DIR}/${OBJENESIS_JAR}:${LIB_DIR}/${SUITE_API_JAR}:${LIB_DIR}/${SUITE_ENGINE_JAR}:${LIB_DIR}/${SUITE_COMMONS_JAR}"

echo "📋 Przypadki użycia do przetestowania:"
echo "   • Obsługa klienta (KontrolerKlientaTest)"
echo "   • Obsługa administratora (KontrolerAdministratoraTest)"
echo "   • Wypłata gotówki (WyplataGotowkiTest, WyplataGotowkiMockTest)"
echo "   • Weryfikacja tożsamości (WeryfikacjaTozsamosciTest, WeryfikacjaTozsamosciMockTest)"
echo "   • Monitorowanie bezpieczeństwa (MonitorowanieBezpieczenstwaTest, MonitorowanieBezpieczenstwaMockTest)"
echo "   • Blokowanie karty (ZablokowanieKartyTest)"
echo "   • Zdalne blokowanie bankomatu (ZdalneBlokowanieBankomatuTest)"
echo ""
echo "=== Uruchamianie testów przypadków użycia ==="
echo ""

java -jar "${LIB_DIR}/${JUNIT_JAR}" \
    --class-path "${CLASSES_DIR}:${TEST_CLASSES_DIR}:${LIB_DIR}/${MOCKITO_JAR}:${LIB_DIR}/${BYTEBUDDY_JAR}:${LIB_DIR}/${BYTEBUDDY_AGENT_JAR}:${LIB_DIR}/${OBJENESIS_JAR}:${LIB_DIR}/${SUITE_API_JAR}:${LIB_DIR}/${SUITE_ENGINE_JAR}:${LIB_DIR}/${SUITE_COMMONS_JAR}" \
    --scan-class-path "${TEST_CLASSES_DIR}" \
    --include-tag "kontroler" \
    --fail-if-no-tests

EXIT_CODE=$?

echo ""
if [ $EXIT_CODE -eq 0 ]; then
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║  ✅ WSZYSTKIE TESTY PRZYPADKÓW UŻYCIA PRZESZŁY POMYŚLNIE      ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
else
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║  ❌ NIEKTÓRE TESTY PRZYPADKÓW UŻYCIA NIE POWIODŁY SIĘ         ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
fi

exit $EXIT_CODE
