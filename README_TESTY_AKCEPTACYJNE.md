# Testy Akceptacyjne FitNesse - Szybki Start

## Co zostało zaimplementowane?

System testów akceptacyjnych dla 5 przypadków użycia systemu bankomatu:
- ✅ Wypłata gotówki
- ✅ Weryfikacja tożsamości
- ✅ Monitorowanie bezpieczeństwa
- ✅ Zdalne blokowanie bankomatu
- ✅ Blokowanie karty

## Uruchomienie w 3 krokach

### 1. Kompilacja projektu
```bash
mvn clean compile test-compile
```

### 2. Uruchomienie FitNesse
```bash
./uruchom-fitnesse-simple.sh
```
lub ręcznie:
```bash
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8080
```

### 3. Otwórz przeglądarkę
```
http://localhost:8080
```

## Struktura plików

```
src/
├── Model/                         # Warstwa modelu (z nowymi metodami)
│   ├── Model.java                 # + pobierzInwentarz(), odblokujBankomat()
│   ├── Inwentarz.java             # + ileKlientow(), pobierzOstatniegoDodanegoKlienta()
│   ├── Klient.java                # + ileKart(), pobierzWszystkieKarty()
│   ├── DAO.java                   # + pobierzLiczbeZdarzen(), pobierzRejestrZdarzen()
│   └── IDAO.java                  # Zaktualizowany interfejs
│
├── Kontroler/                     # Warstwa kontrolerów (bez zmian)
│
└── test/
    └── testyfitnesse/             # ⭐ NOWY PAKIET
        ├── SetUp.java             # Klasa inicjalizująca testy
        ├── TestWyplatyGotowki.java
        ├── TestWeryfikacjiTozsamosci.java
        ├── TestMonitorowaniaBezpieczenstwa.java
        ├── TestZdalnegoBlokowania.java
        └── TestBlokowaniaKarty.java

📄 FitNesse_Instrukcja.md         # Szczegółowa instrukcja tworzenia stron
📄 SPRAWOZDANIE_FITNESSE.md        # Pełne sprawozdanie z kodem
📄 README_TESTY_AKCEPTACYJNE.md    # Ten plik
📄 ROZWIAZYWANIE_PROBLEMOW.md      # Przewodnik rozwiązywania problemów
🔧 uruchom-fitnesse-simple.sh      # Skrypt uruchomieniowy (ZALECANY)
```

## Konfiguracja stron FitNesse

Po uruchomieniu FitNesse, należy utworzyć:

### 1. Strona Suite (BankomatIO)
```
Testy akceptacyjne systemu bankomatu

!path /Users/daniel/Desktop/io/BankomatIO/target/classes
!path /Users/daniel/Desktop/io/BankomatIO/target/test-classes
```

### 2. Strona SetUp
```
!|testyfitnesse.SetUp|
```

### 3. Strony testujące
- TestWyplatyGotowki
- TestWeryfikacjiTozsamosci
- TestMonitorowaniaBezpieczenstwa
- TestZdalnegoBlokowania
- TestBlokowaniaKarty

**📖 Szczegóły w pliku `FitNesse_Instrukcja.md`**

## Dane testowe

System automatycznie tworzy 3 klientów z kartami:

| ID Karty | PIN  | Saldo   | Klient           |
|----------|------|---------|------------------|
| 1001     | 1234 | 1000 zł | Jan Kowalski     |
| 1002     | 5678 | 500 zł  | Anna Nowak       |
| 1003     | 9999 | 2500 zł | Piotr Wiśniewski |

## Przykładowe testy

### ✅ Test poprawnej wypłaty
- Karta: 1001, PIN: 1234, Kwota: 100 → Sukces, Saldo: 900

### ❌ Test niepoprawnej wypłaty
- Karta: 1001, PIN: 9999, Kwota: 100 → Błąd weryfikacji
- Karta: 1003, PIN: 9999, Kwota: 6000 → Przekroczenie limitu

### 🔒 Test monitorowania
- Strumień: "obraz_bezpieczny" → Brak blokady
- Strumień: "obraz_zagrożenie" → Automatyczne zablokowanie bankomatu

## Weryfikacja poprawności

Po skompilowaniu powinieneś zobaczyć:
```
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  ~2s
```

## Dokumentacja

- **FitNesse_Instrukcja.md** - Krok po kroku tworzenie stron
- **SPRAWOZDANIE_FITNESSE.md** - Pełny kod i sprawozdanie
- **instrukcjaFitnesse** - Oryginalna instrukcja laboratoryjna

## Rozwiązywanie problemów

### Najczęstsze problemy

#### NoClassDefFoundError: commons-lang3
✅ **Naprawione!** Użyj `./uruchom-fitnesse-simple.sh`

#### Port 8080 zajęty
```bash
# Użyj innego portu
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8081
```

#### FitNesse nie znajduje klas
Sprawdź ścieżki w stronie Suite - muszą być absolutne:
```
!path /Users/daniel/Desktop/io/BankomatIO/target/classes
!path /Users/daniel/Desktop/io/BankomatIO/target/test-classes
```

#### Więcej rozwiązań
📖 **Zobacz plik `ROZWIAZYWANIE_PROBLEMOW.md` dla szczegółowego przewodnika**

## Zgodność z instrukcją

✅ **Zadanie 1**: Dodano metody sprawdzające stan warstwy encji  
✅ **Zadanie 2**: Utworzono pakiet testyfitnesse z klasą SetUp i klasami testującymi  
✅ **Zadanie 3**: Przygotowano instrukcje tworzenia stron FitNesse  

Wszystkie wymagania z pliku `instrukcjaFitnesse` zostały spełnione.

## Następne kroki

1. Uruchom FitNesse
2. Utwórz strony według instrukcji
3. Uruchom zestaw testów (Suite)
4. Zrób screenshot wyników
5. Dodaj do sprawozdania

---

**Powodzenia! 🚀**
