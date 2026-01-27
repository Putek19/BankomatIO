# Opis Testów JUnit 5

## Uruchomienie testów

### Wszystkie testy (Model + Kontroler)
```bash
cd src
./run-tests.sh
```

### Tylko przypadki użycia (Kontroler)
```bash
cd src
./test_pu.sh  # Wymaga wcześniejszego ./run-tests.sh
```
## Dane Testowe

### Podstawowe wartości używane w testach:
- **ID Karty**: 100 (istniejąca), 999 (nieistniejąca)
- **PIN**: "1234" (poprawny), "0000" (niepoprawny)
- **Saldo początkowe**: 1000.00 PLN
- **ID Klienta**: 1
- **Imię klienta**: "Jan"
- **Nazwisko**: "Kowalski"
- **PESEL**: 123456789
- **Maksymalna kwota wypłaty**: 5000.00 PLN
- **Maksymalna liczba prób PIN**: 5

## Testy Model

### KartaTest (8 testów) @Tag("model", "karta", "saldo")
- **Konstruktor**: ID=1, PIN="1234", saldo=1000.00
- **Sprawdzanie PIN**: poprawny "1234", niepoprawny "0000", null
- **Zmiana salda**: +100.00, -100.00
- **Zablokowana karta**: nie zmienia salda przy +100.00, nie akceptuje PIN

### KlientTest (8 testów)
- **Klient**: nr=1, imię="Jan", nazwisko="Kowalski", PESEL=123456789
- **Karty**: ID=1 (PIN="1234", saldo=1000.00), ID=2 (PIN="5678", saldo=2000.00), ID=3 (PIN="9012", saldo=3000.00)
- **Nowe nazwisko**: "Nowak"
- **Nowy PESEL**: 987654321

### DAOTest (8 testów) @Tag("model", "dao", "baza")
- **Klienci**: "Jan Kowalski", "Klient 1", "Klient 2", "Klient 3"
- **ID kart**: 1, 2 (niezależne blokady)

### InwentarzTest (10 testów)
- **Klienci**: nr=1 (imię="Jan"), nr=2 (imię="Anna"), nr=3 (imię="Piotr")
- **Karta**: ID=100, PIN="1234", saldo=1000.00

### ModelTest (12 testów) @Tag("model", "biznesowa")
- **Karta**: ID=100, PIN="1234", saldo=1000.00
- **Aktualizacja salda**: -100.00 (wynik: 900.00)
- **Nieistniejąca karta**: ID=999

### FabrykaKlientaTest (4 testy)
- **Klient 1**: "Jan", "Kowalski", PESEL=123456789
- **Klient 2**: "Anna", "Nowak", PESEL=222222222
- **Klient 3**: "Piotr", "Wiśniewski", PESEL=333333333
- **Klient 4**: "Maria", "Kowalczyk", PESEL=987654321

### ZablokowanaKartaTest (7 testów)
- **Karta bazowa**: ID=1, PIN="1234", saldo=1000.00
- **Data blokady**: "2024-01-01"
- **Próba zmiany salda**: +100.00 (nie zmienia)

## Testy Kontroler

### KontrolerKlientaTest (10 testów) @Tag("kontroler", "klient")
- **Wypłata**: 150.00 PLN (saldo: 1000.00 → 850.00)
- **Niewystarczające saldo**: 2000.00 PLN (saldo: 1000.00, nie zmienia się)
- **Maksymalna kwota**: 5000.00 PLN (saldo zwiększone do 10000.00 → 5000.00)
- **Przekroczona maksymalna**: 6000.00 PLN (saldo: 10000.00, nie zmienia się)
- **Niepoprawny numer karty**: "abc"

### KontrolerAdministratoraTest (3 testy)
- **ID bankomatu**: 1

### WeryfikacjaTozsamosciTest (6 testów) @Tag("kontroler", "weryfikacja", "bezpieczenstwo")
- **Poprawny PIN**: "1234"
- **Niepoprawny PIN**: "0000"
- **Maksymalna liczba prób**: 5 niepoprawnych prób → blokada karty
- **Reset licznika**: po 2 niepoprawnych próbach

### WyplataGotowkiTest (9 testów) @Tag("kontroler", "wyplata", "saldo")
- **Poprawna wypłata**: 150.00 PLN (saldo: 1000.00 → 850.00)
- **Niewystarczające saldo**: 2000.00 PLN
- **Kwota zero**: 0.00 PLN
- **Kwota ujemna**: -100.00 PLN
- **Maksymalna kwota**: 5000.00 PLN (saldo zwiększone do 10000.00)
- **Przekroczona maksymalna**: 6000.00 PLN
- **Nieistniejąca karta**: ID=999, kwota=100.00

### MonitorowanieBezpieczenstwaTest (8 testów) @Tag("kontroler", "bezpieczenstwo", "monitoring")
- **Obrazy**: "obraz_bezpieczny", "obraz_zagrożenie"
- **ID bankomatu**: 1
- **Sekwencja monitoringu**: ["obraz_bezpieczny", "obraz_bezpieczny", "obraz_zagrożenie"]

### ZablokowanieKartyTest (2 testy)
- **Karta**: ID=100, PIN="1234", saldo=1000.00
- **Nieistniejąca karta**: ID=999

### ZdalneBlokowanieBankomatuTest (2 testy)
- **ID bankomatu**: 1

## Testy z Mockowaniem (Zadanie 2)

### WyplataGotowkiMockTest (8 testów) @Tag("kontroler", "mock", "wyplata")
Używa Mockito do symulacji IModel.
- **@Mock**: mockModel (IModel)
- **@InjectMocks**: WyplataGotowki
- **when().thenReturn()**: sprawdzSaldo zwraca BigDecimal
- **when().thenThrow()**: symulacja wyjątku RuntimeException
- **doNothing().when()**: aktualizujSaldo, zarejestrujZdarzenie
- **verify()**, **times()**, **never()**, **atLeast()**, **atMost()**, **atMostOnce()**: weryfikacja wywołań
- **InOrder**: sprawdzenie kolejności wywołań

### WeryfikacjaTozsamosciMockTest (8 testów) @Tag("kontroler", "mock", "weryfikacja", "bezpieczenstwo")
Używa Mockito do symulacji IModel i IStrategiaZabezpieczenia.
- **@Mock**: mockModel (IModel), mockStrategia (IStrategiaZabezpieczenia)
- **@InjectMocks**: WeryfikacjaTozsamosci z wstrzykniętą symulacją strategii
- **when().thenReturn()**: sprawdzPin zwraca boolean
- **when().thenThrow()**: symulacja wyjątku połączenia
- **doNothing().when()**: wykonajReakcje
- **verify()**, **never()**, **atMost()**: weryfikacja wywołań
- **InOrder**: sprawdzenie kolejności wywołań

### MonitorowanieBezpieczenstwaMockTest (8 testów) @Tag("kontroler", "mock", "bezpieczenstwo", "monitoring")
Używa Mockito do symulacji IModel.
- **@Mock**: mockModel (IModel)
- **@InjectMocks**: MonitorowanieBezpieczenstwa
- **doNothing().when()**: zablokujBankomat, zarejestrujZdarzenie
- **doThrow().when()**: symulacja wyjątku przy rejestracji zdarzenia
- **verify()**, **times()**, **atLeast()**: weryfikacja wywołań
- **InOrder**: sprawdzenie kolejności wywołań

## Zestawy Testów (Zadanie 3)

### SuiteModel
- **@Suite**: zestaw testów
- **@SuiteDisplayName**: "Zestaw testów warstwy encji (Model)"
- **@SelectPackages**: "Model"
- Uruchamia wszystkie testy z pakietu Model

### SuiteKontroler
- **@Suite**: zestaw testów
- **@SuiteDisplayName**: "Zestaw testów warstwy kontroli (Kontroler)"
- **@SelectPackages**: "Kontroler"
- Uruchamia wszystkie testy z pakietu Kontroler

### SuiteBezpieczenstwo
- **@Suite**: zestaw testów
- **@SuiteDisplayName**: "Zestaw testów bezpieczeństwa (bez mocków)"
- **@SelectPackages**: {"Model", "Kontroler"}
- **@IncludeTags**: "bezpieczenstwo"
- **@ExcludeTags**: "mock"
- Praktyczne zastosowanie: testowanie funkcji bezpieczeństwa bez symulacji

### SuiteMock
- **@Suite**: zestaw testów
- **@SuiteDisplayName**: "Zestaw testów z symulacją (Mockito)"
- **@SelectPackages**: {"Model", "Kontroler"}
- **@IncludeTags**: "mock"
- **@ExcludeTags**: "saldo"
- Praktyczne zastosowanie: szybkie testy izolowane od rzeczywistych zależności

## Scenariusze Testowe

### Wypłata Gotówki
1. **Poprawna wypłata**: PIN="1234", kwota=150.00, saldo=1000.00 → 850.00
2. **Niepoprawny PIN**: PIN="0000", kwota=150.00, saldo nie zmienia się
3. **Niewystarczające saldo**: PIN="1234", kwota=2000.00, saldo=1000.00 (nie zmienia się)
4. **Maksymalna kwota**: PIN="1234", kwota=5000.00, saldo=10000.00 → 5000.00
5. **Przekroczona maksymalna**: PIN="1234", kwota=6000.00, saldo=10000.00 (nie zmienia się)

### Weryfikacja PIN
1. **Poprawny PIN**: "1234" → true
2. **Niepoprawny PIN**: "0000" → false
3. **5 niepoprawnych prób**: karta zablokowana
4. **Reset licznika**: po poprawnym PIN lub wywołaniu resetujLicznik()

### Bezpieczeństwo
1. **Monitoring**: wykrywa "obraz_zagrożenie" → blokuje bankomat
2. **Blokada karty**: po 5 niepoprawnych próbach PIN
3. **Zdalne blokowanie**: administrator blokuje bankomat ID=1

## Użyte Tagi

- **model**: testy warstwy encji
- **kontroler**: testy warstwy kontroli
- **karta**: testy klasy Karta
- **saldo**: testy operujące na saldzie
- **dao**: testy klasy DAO
- **baza**: testy związane z bazą danych
- **biznesowa**: testy logiki biznesowej
- **klient**: testy obsługi klienta
- **weryfikacja**: testy weryfikacji tożsamości
- **bezpieczenstwo**: testy funkcji bezpieczeństwa
- **wyplata**: testy wypłaty gotówki
- **monitoring**: testy monitorowania
- **mock**: testy z użyciem symulacji (Mockito)

