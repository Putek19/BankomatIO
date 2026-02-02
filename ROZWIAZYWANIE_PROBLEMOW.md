# Rozwiązywanie problemów FitNesse

## Problem: NoClassDefFoundError: org/apache/commons/lang3/StringUtils

### Przyczyna
Brakuje zależności Apache Commons Lang3 w classpath.

### Rozwiązanie
✅ **Już naprawione w projekcie!** Użyj nowego skryptu:

```bash
./uruchom-fitnesse-simple.sh
```

Lub ręcznie:
```bash
# Pobierz wszystkie zależności
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs

# Uruchom FitNesse z pełnym classpath
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8080
```

---

## Problem: Błąd kompilacji testów FitNesse

### Objawy
```
[ERROR] cannot find symbol: class Fixture
```

### Rozwiązanie
```bash
# Pobierz wszystkie zależności
mvn dependency:resolve

# Rekompiluj projekt
mvn clean compile test-compile
```

---

## Problem: Port 8080 jest zajęty

### Objawy
```
Address already in use: bind
```

### Rozwiązanie 1: Użyj innego portu
```bash
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8081
```
Następnie otwórz: http://localhost:8081

### Rozwiązanie 2: Zabij proces na porcie 8080
```bash
# macOS/Linux
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID [PID] /F
```

---

## Problem: Strony FitNesse nie znajdują klas

### Objawy
Czerwony komunikat: "Could not invoke constructor for testyfitnesse.SetUp"

### Rozwiązanie
Sprawdź ścieżki w stronie Suite:

```
!path /Users/daniel/Desktop/io/BankomatIO/target/classes
!path /Users/daniel/Desktop/io/BankomatIO/target/test-classes
```

**WAŻNE**: Ścieżki muszą być ABSOLUTNE i wskazywać na faktyczne lokalizacje.

Zweryfikuj:
```bash
ls -la target/classes/Model/
ls -la target/test-classes/testyfitnesse/
```

---

## Problem: Testy FitNesse zawsze są czerwone

### Możliwe przyczyny i rozwiązania

#### 1. Źle zdefiniowana tabela
Sprawdź czy:
- Nazwy kolumn odpowiadają nazwom metod (bez `?` dla wejść, z `?` dla wyjść)
- Nazwy atrybutów w klasie testującej są identyczne jak w tabeli
- Nie ma literówek

#### 2. Nieprawidłowe typy danych
```
|idKarty|pin |kwotaWyplaty|
|1001   |1234|100.00      |  ✅ Dobrze
|"1001" |1234|100.00      |  ❌ Źle - idKarty to int, nie String
```

#### 3. SetUp nie został załadowany
- Upewnij się, że strona SetUp jest typu "Static Page"
- Upewnij się, że SetUp znajduje się w tym samym Suite co testy

---

## Problem: Maven nie może pobrać FitNesse

### Objawy
```
Could not resolve dependencies for project
```

### Rozwiązanie
```bash
# Wyczyść cache Maven
mvn dependency:purge-local-repository

# Pobierz ponownie
mvn dependency:resolve

# Jeśli nadal nie działa, sprawdź połączenie internetowe i proxy
mvn -U dependency:resolve
```

---

## Problem: Dane testowe nie są resetowane między testami

### Przyczyna
SetUp nie tworzy nowych instancji dla każdego testu.

### Rozwiązanie
W FitNesse każdy test w Suite używa tej samej instancji SetUp. Aby zresetować dane:

1. Zatrzymaj FitNesse (Ctrl+C)
2. Uruchom ponownie
3. Wykonaj Suite od nowa

Alternatywnie, dodaj metodę resetującą w SetUp i wywołaj ją na początku każdego testu.

---

## Problem: Błąd "Method not found" w FitNesse

### Objawy
```
Method: wykonajWyplateGotowki? not found in testyfitnesse.TestWyplatyGotowki
```

### Rozwiązanie
FitNesse szuka metody odpowiadającej nazwie kolumny:
- Kolumna: `wykonajWyplateGotowki?`
- Metoda: `public boolean wykonajWyplateGotowki()`

Sprawdź:
1. Czy metoda jest publiczna
2. Czy nazwa metody dokładnie odpowiada nazwie kolumny (bez `?`)
3. Czy typ zwracany jest poprawny (dla kolumn z `?`)
4. Czy klasa została przekompilowana (`mvn test-compile`)

---

## Przydatne komendy diagnostyczne

```bash
# Sprawdź czy klasy zostały skompilowane
find target/test-classes/testyfitnesse -name "*.class"

# Powinno zwrócić 6 plików:
# SetUp.class
# TestWyplatyGotowki.class
# TestWeryfikacjiTozsamosci.class
# TestMonitorowaniaBezpieczenstwa.class
# TestZdalnegoBlokowania.class
# TestBlokowaniaKarty.class

# Sprawdź strukturę jar FitNesse
jar tf target/fitnesse-libs/fitnesse-20230503.jar | grep FitNesseMain

# Sprawdź czy wszystkie zależności są pobrane
ls -la target/fitnesse-libs/ | wc -l
# Powinno być około 20-25 jar-ów
```

---

## Pomoc online

- **FitNesse User Guide**: http://fitnesse.org/FitNesse.UserGuide
- **FitNesse FAQ**: http://fitnesse.org/FitNesse.UserGuide.WritingAcceptanceTests.FitLibraryUserGuide.FaqAndTroubleshooting
- **Maven Dependencies**: https://mvnrepository.com/artifact/org.fitnesse/fitnesse

---

## Kontakt w przypadku problemów

Jeśli żaden z powyższych kroków nie pomógł:

1. Sprawdź wersję Java: `java -version` (wymagana Java 11+)
2. Sprawdź wersję Maven: `mvn -version` (wymagana Maven 3.6+)
3. Wyczyść projekt: `mvn clean`
4. Zrestartuj IDE
5. Zrestartuj komputer (czasami pomaga!)
