#!/bin/bash

echo "=== Kompilacja projektu ==="
mvn clean compile test-compile

if [ $? -ne 0 ]; then
    echo "Błąd kompilacji!"
    exit 1
fi

echo ""
echo "=== Pobieranie zależności FitNesse ==="
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs

echo ""
echo "=== Uruchamianie FitNesse (Java 22 compatible) ==="
echo "Serwer będzie dostępny pod adresem: http://localhost:8080"
echo "Naciśnij Ctrl+C aby zatrzymać serwer"
echo ""

# Budowanie classpath ze wszystkich jar-ów
CLASSPATH=""
for jar in target/fitnesse-libs/*.jar; do
    if [ -z "$CLASSPATH" ]; then
        CLASSPATH="$jar"
    else
        CLASSPATH="$CLASSPATH:$jar"
    fi
done

# Flagi dla Java 17+ - wyłączenie Security Manager
java -Djava.security.manager=allow \
     --add-opens java.base/java.lang=ALL-UNNAMED \
     --add-opens java.base/java.util=ALL-UNNAMED \
     -cp "$CLASSPATH" fitnesseMain.FitNesseMain -p 8080
