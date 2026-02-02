# Instrukcja testów FitNesse - System Bankomatu

## Uruchomienie FitNesse

### Sposób 1: Użycie skryptu (ZALECANE)
```bash
./uruchom-fitnesse-simple.sh
```

### Sposób 2: Ręcznie
1. Skompiluj projekt:
```bash
mvn clean compile test-compile
```

2. Pobierz wszystkie zależności FitNesse:
```bash
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs
```

3. Uruchom serwer FitNesse:
```bash
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8080
```

4. Otwórz przeglądarkę: http://localhost:8080

## Tworzenie stron testowych

### 1. Strona zestawu testów (Suite Page)

1. Na stronie głównej FrontPage wybierz: **Add → Suite Page**
2. Nazwa strony: `BankomatIO`
3. Treść strony:

```
Testy akceptacyjne systemu bankomatu - realizacja przypadków użycia

!path /Users/daniel/Desktop/io/BankomatIO/target/classes
!path /Users/daniel/Desktop/io/BankomatIO/target/test-classes
```

4. Zapisz (Save)
5. Dodaj link do FrontPage:
   - Wejdź na http://localhost:8080/FrontPage
   - Kliknij **Edit**
   - Dodaj: `[[System Bankomatu][FrontPage.BankomatIO]]`
   - Zapisz

### 2. Strona SetUp (Static Page)

1. Wejdź na stronę `BankomatIO`
2. Wybierz: **Add → Static Page**
3. Nazwa: `SetUp`
4. Treść:

```
!|testyfitnesse.SetUp|
```

5. Zapisz

### 3. Strona testująca - Wypłata Gotówki

1. Na stronie `BankomatIO` wybierz: **Add → Test Page**
2. Nazwa: `TestWyplatyGotowki`
3. Treść:

```
!|testyfitnesse.TestWyplatyGotowki|
|idKarty|pin |kwotaWyplaty|wykonajWyplateGotowki?|sprawdzSaldoPoWyplacie?|sprawdzCzyKartaNieZablokowana?|
|1001   |1234|100.00      |true                   |900.0                   |true                              |
|1001   |1234|200.00      |true                   |700.0                   |true                              |
|1002   |5678|50.00       |true                   |450.0                   |true                              |
|1001   |9999|100.00      |false                  |700.0                   |true                              |
|1003   |9999|6000.00     |false                  |2500.0                  |true                              |
```

4. Zapisz

### 4. Strona testująca - Weryfikacja Tożsamości

1. Na stronie `BankomatIO` wybierz: **Add → Test Page**
2. Nazwa: `TestWeryfikacjiTozsamosci`
3. Treść:

```
!|testyfitnesse.TestWeryfikacjiTozsamosci|
|idKarty|pin |weryfikujTozsamosc?|sprawdzCzyKartaJestAktywna?|sprawdzSaldoKarty?|
|1001   |1234|true               |true                        |1000.0            |
|1002   |5678|true               |true                        |500.0             |
|1001   |0000|false              |true                        |1000.0            |
|1003   |1234|false              |true                        |2500.0            |
|1003   |9999|true               |true                        |2500.0            |
```

4. Zapisz

### 5. Strona testująca - Monitorowanie Bezpieczeństwa

1. Na stronie `BankomatIO` wybierz: **Add → Test Page**
2. Nazwa: `TestMonitorowaniaBezpieczenstwa`
3. Treść:

```
!|testyfitnesse.TestMonitorowaniaBezpieczenstwa|
|strumienObrazow  |analizujObraz?|uruchomMonitoring?|sprawdzCzyBankomatZablokowany?|
|obraz_bezpieczny |false         |false             |false                         |
|obraz_zagrożenie |true          |true              |true                          |
```

4. Zapisz

### 6. Strona testująca - Zdalne Blokowanie

1. Na stronie `BankomatIO` wybierz: **Add → Test Page**
2. Nazwa: `TestZdalnegoBlokowania`
3. Treść:

```
!|testyfitnesse.TestZdalnegoBlokowania|
|zablokujBankomatZdalnie?|sprawdzCzyBankomatZablokowany?|odblokujBankomat?|
|true                     |true                          |true             |
|true                     |true                          |true             |
```

4. Zapisz

### 7. Strona testująca - Blokowanie Karty

1. Na stronie `BankomatIO` wybierz: **Add → Test Page**
2. Nazwa: `TestBlokowaniaKarty`
3. Treść:

```
!|testyfitnesse.TestBlokowaniaKarty|
|idKarty|zablokujKarte?|sprawdzCzyKartaZablokowana?|sprawdzCzyMoznaWyplacicZKarty?|
|1001   |true          |true                        |false                          |
|1002   |true          |true                        |false                          |
|1003   |true          |true                        |false                          |
```

4. Zapisz

## Uruchomienie testów

### Pojedynczy test
1. Wejdź na stronę testującą (np. `TestWyplatyGotowki`)
2. Kliknij **Test** w menu górnym

### Cały zestaw testów
1. Wejdź na stronę `BankomatIO`
2. Kliknij **Suite** w menu górnym

## Interpretacja wyników

- **Zielony** - test przeszedł pomyślnie
- **Czerwony** - test zakończył się niepowodzeniem
- **Żółty** - test został pominięty lub zawiera ostrzeżenia

## Uwagi

- Upewnij się, że ścieżki w `!path` są prawidłowe dla Twojego systemu
- Przed każdym uruchomieniem zestawu testów, stan systemu jest resetowany przez klasę SetUp
- Testy zawierają zarówno poprawne, jak i niepoprawne scenariusze
