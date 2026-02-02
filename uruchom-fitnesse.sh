#!/bin/bash

echo "=== Kompilacja projektu ==="
mvn clean compile test-compile

if [ $? -ne 0 ]; then
    echo "Błąd kompilacji!"
    exit 1
fi

echo ""
echo "=== Sprawdzanie FitNesse JAR ==="
FITNESSE_JAR=$(find ~/.m2/repository/org/fitnesse/fitnesse -name "fitnesse-*.jar" | head -1)

if [ -z "$FITNESSE_JAR" ]; then
    echo "Pobieranie zależności..."
    mvn dependency:copy-dependencies -DoutputDirectory=target/lib
    FITNESSE_JAR=$(find target/lib -name "fitnesse-*.jar" | head -1)
fi

if [ -z "$FITNESSE_JAR" ]; then
    echo "Nie znaleziono fitnesse.jar!"
    echo "Uruchom: mvn dependency:resolve"
    exit 1
fi

echo "Znaleziono: $FITNESSE_JAR"
echo ""
echo "=== Pobieranie zależności FitNesse ==="
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs

echo ""
echo "=== Przygotowanie classpath ==="
CLASSPATH="$FITNESSE_JAR"
for jar in target/fitnesse-libs/*.jar; do
    CLASSPATH="$CLASSPATH:$jar"
done

echo ""
echo "=== Uruchamianie FitNesse ==="
echo "Serwer będzie dostępny pod adresem: http://localhost:8080"
echo "Naciśnij Ctrl+C aby zatrzymać serwer"
echo ""

java -cp "$CLASSPATH" fitnesseMain.FitNesseMain -p 8080
