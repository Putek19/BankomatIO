# 📚 ŚCIĄGAWKA JUNIT 5 & MOCKITO - System Bankomatu

## 🎯 Spis treści
1. [Biblioteki i wersje](#biblioteki-i-wersje)
2. [JUnit 5 - Podstawy](#junit-5---podstawy)
3. [Mockito - Symulacje](#mockito---symulacje)
4. [Adnotacje](#adnotacje)
5. [Asercje](#asercje)
6. [Testy parametryzowane](#testy-parametryzowane)
7. [Zestawy testów](#zestawy-testów)
8. [Struktura testów](#struktura-testów)
9. [Wzorce i dobre praktyki](#wzorce-i-dobre-praktyki)

---

## 📦 Biblioteki i wersje

### JUnit 5 (Jupiter)
- **Wersja**: 5.10.0
- **Co robi**: Framework do testowania jednostkowego w Javie
- **Użycie**: Tworzenie i uruchamianie testów jednostkowych
- **Artefakt**: `org.junit.jupiter:junit-jupiter`

### JUnit Platform Suite
- **Wersja**: 1.10.0
- **Co robi**: Umożliwia grupowanie testów w zestawy (suites)
- **Użycie**: Tworzenie zestawów testów na podstawie pakietów lub tagów
- **Artefakty**: 
  - `junit-platform-suite-api` - API do definiowania zestawów
  - `junit-platform-suite-engine` - Silnik wykonujący zestawy

### Mockito
- **Wersja**: 5.8.0
- **Co robi**: Framework do tworzenia obiektów testowych (mocków/atrap)
- **Użycie**: Symulacja zależności w testach izolowanych
- **Artefakty**:
  - `mockito-core` - Podstawowa funkcjonalność
  - `mockito-junit-jupiter` - Integracja z JUnit 5

### Biblioteki pomocnicze (Mockito)
- **byte-buddy** (1.14.11) - Manipulacja bytecode dla Mockito
- **objenesis** (3.3) - Tworzenie obiektów bez konstruktorów

---

## 🧪 JUnit 5 - Podstawy

### Struktura klasy testowej

```java
@DisplayName("NazwaTestu")                    // Czytelna nazwa testu
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)  // Kolejność wykonania
@Tag("kategoria")                             // Tagowanie testów
public class NazwaKlasyTest {
    
    private ObiekDoTestowania obiekt;
    
    @BeforeEach                               // Przed każdym testem
    public void setUp() {
        obiekt = new ObiekDoTestowania();
    }
    
    @AfterEach                                // Po każdym teście
    public void tearDown() {
        obiekt = null;
    }
    
    @Test                                     // Oznacza metodę testową
    @Order(1)                                 // Kolejność wykonania
    @DisplayName("Opis testu")
    public void testMetoda() {
        // Jeśli (given) - przygotowanie danych
        // Gdy (when) - wykonanie operacji
        // Wtedy (then) - asercje
    }
}
```

### Adnotacje cyklu życia

| Adnotacja | Kiedy | Użycie w projekcie |
|-----------|-------|-------------------|
| `@BeforeEach` | Przed każdym testem | Inicjalizacja obiektów testowych |
| `@AfterEach` | Po każdym teście | Czyszczenie zasobów |
| `@BeforeAll` | Raz przed wszystkimi testami | Brak w projekcie |
| `@AfterAll` | Raz po wszystkich testach | Brak w projekcie |

### Przykład z projektu (DAOTest)

```java
@BeforeEach
public void setUp() {
    dao = new DAO();  // Świeży obiekt przed każdym testem
}

@AfterEach
public void tearDown() {
    dao = null;  // Czyszczenie po teście
}
```

---

## 🎭 Mockito - Symulacje

### Podstawowe koncepcje

**Mock (Atrapa)** - sztuczny obiekt, który:
- Symuluje zachowanie rzeczywistego obiektu
- Nie wykonuje prawdziwej logiki
- Pozwala na kontrolę i weryfikację wywołań

### Tworzenie mocków

#### Metoda 1: Adnotacje (zalecana w projekcie)

```java
@Mock
private IModel mockModel;  // Automatyczny mock

@InjectMocks
private WyplataGotowki wyplata;  // Automatyczne wstrzykiwanie mocków

private AutoCloseable closeable;

@BeforeEach
public void setUp() {
    closeable = MockitoAnnotations.openMocks(this);  // Inicjalizacja
}

@AfterEach
public void tearDown() throws Exception {
    closeable.close();  // Czyszczenie
}
```

#### Metoda 2: Bezpośrednie tworzenie

```java
IModel mockModel = mock(IModel.class);  // Ręczne utworzenie mocka
```

### Definiowanie zachowania mocków

#### 1. Metody zwracające wartość (when().thenReturn())

```java
// Zwracanie konkretnej wartości
when(mockModel.sprawdzSaldo(100)).thenReturn(new BigDecimal("1000.00"));

// Zwracanie wartości dla dowolnego parametru
when(mockModel.sprawdzPin(anyInt(), anyString())).thenReturn(true);
```

#### 2. Symulacja wyjątków (when().thenThrow())

```java
// Rzucenie wyjątku
when(mockModel.sprawdzSaldo(100))
    .thenThrow(new RuntimeException("Błąd bazy danych"));
```

#### 3. Metody void (doNothing().when())

```java
// Metoda void nic nie robi (domyślne)
doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

// Metoda void rzuca wyjątek
doThrow(new RuntimeException("Błąd"))
    .when(mockModel)
    .zarejestrujZdarzenie(anyString());
```

### Weryfikacja wywołań

#### Podstawowa weryfikacja

```java
// Czy metoda została wywołana?
verify(mockModel).sprawdzSaldo(100);

// Czy metoda została wywołana z dowolnymi parametrami?
verify(mockModel).sprawdzSaldo(anyInt());
```

#### Weryfikacja liczby wywołań

| Metoda | Znaczenie | Przykład |
|--------|-----------|----------|
| `times(n)` | Dokładnie n razy | `verify(mock, times(3)).metoda()` |
| `never()` | Ani razu | `verify(mock, never()).metoda()` |
| `atLeast(n)` | Minimum n razy | `verify(mock, atLeast(2)).metoda()` |
| `atLeastOnce()` | Minimum raz | `verify(mock, atLeastOnce()).metoda()` |
| `atMost(n)` | Maksimum n razy | `verify(mock, atMost(5)).metoda()` |
| `atMostOnce()` | Maksimum raz | `verify(mock, atMostOnce()).metoda()` |

```java
// Przykłady z projektu
verify(mockModel, times(1)).zablokujBankomat();
verify(mockModel, never()).aktualizujSaldo(anyInt(), any());
verify(mockModel, atLeast(3)).zarejestrujZdarzenie(anyString());
```

#### Weryfikacja kolejności wywołań (InOrder)

```java
InOrder inOrder = inOrder(mockModel);
inOrder.verify(mockModel).sprawdzSaldo(idKarty);
inOrder.verify(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
inOrder.verify(mockModel).zarejestrujZdarzenie(contains("Zrealizowano"));
```

### Matchery argumentów

| Matcher | Znaczenie | Przykład |
|---------|-----------|----------|
| `any()` | Dowolna wartość | `any(BigDecimal.class)` |
| `anyInt()` | Dowolny int | `anyInt()` |
| `anyString()` | Dowolny String | `anyString()` |
| `eq(wartość)` | Równe wartości | `eq(100)` |
| `contains(tekst)` | Zawiera tekst | `contains("Zrealizowano")` |

```java
// Przykład kombinacji
verify(mockModel).aktualizujSaldo(eq(100), any(BigDecimal.class));
```

### Kompletny przykład z projektu

```java
@Test
@DisplayName("RealizujWyplate - PoprawneDane z mockiem")
public void testRealizujWyplate_PoprawneDane() {
    // Jeśli (given): określenie zachowania symulacji
    int idKarty = 100;
    double kwota = 150.0;
    BigDecimal saldo = new BigDecimal("1000.00");
    
    when(mockModel.sprawdzSaldo(idKarty)).thenReturn(saldo);
    doNothing().when(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
    doNothing().when(mockModel).zarejestrujZdarzenie(anyString());
    
    // Gdy (when): wykonanie testowanej operacji
    wyplata.ustawKwote(kwota);
    boolean wynik = wyplata.realizujWyplate(idKarty);
    
    // Wtedy (then): sprawdzenie użycia symulacji i asercje
    assertTrue(wynik);
    verify(mockModel).sprawdzSaldo(idKarty);
    verify(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
    verify(mockModel, times(1)).zarejestrujZdarzenie(contains("Zrealizowano"));
}
```

---

## 📝 Adnotacje

### Adnotacje JUnit 5

| Adnotacja | Zastosowanie | Przykład |
|-----------|--------------|----------|
| `@Test` | Oznacza metodę testową | `@Test public void testMetoda()` |
| `@DisplayName` | Czytelna nazwa testu | `@DisplayName("Test dodawania klienta")` |
| `@Order(n)` | Kolejność wykonania | `@Order(1)` |
| `@Tag("nazwa")` | Tagowanie testu | `@Tag("model")` |
| `@BeforeEach` | Przed każdym testem | Setup danych |
| `@AfterEach` | Po każdym teście | Czyszczenie |
| `@ParameterizedTest` | Test parametryzowany | Zobacz [Testy parametryzowane](#testy-parametryzowane) |
| `@TestMethodOrder` | Określa kolejność | `@TestMethodOrder(MethodOrderer.OrderAnnotation.class)` |

### Adnotacje Mockito

| Adnotacja | Zastosowanie | Przykład |
|-----------|--------------|----------|
| `@Mock` | Tworzy mock obiektu | `@Mock private IModel mockModel;` |
| `@InjectMocks` | Wstrzykuje mocki do obiektu | `@InjectMocks private WyplataGotowki wyplata;` |

### Adnotacje Suite (zestawy testów)

| Adnotacja | Zastosowanie | Przykład |
|-----------|--------------|----------|
| `@Suite` | Oznacza zestaw testów | `@Suite` |
| `@SuiteDisplayName` | Nazwa zestawu | `@SuiteDisplayName("Testy Model")` |
| `@SelectPackages` | Wybór pakietów | `@SelectPackages("Model")` |
| `@IncludeTags` | Włączone tagi | `@IncludeTags("mock")` |
| `@ExcludeTags` | Wykluczone tagi | `@ExcludeTags("saldo")` |

---

## ✅ Asercje

### Podstawowe asercje JUnit 5

```java
// Import
import static org.junit.jupiter.api.Assertions.*;
```

| Asercja | Zastosowanie | Przykład z projektu |
|---------|--------------|-------------------|
| `assertTrue(bool)` | Sprawdza czy true | `assertTrue(wynik)` |
| `assertFalse(bool)` | Sprawdza czy false | `assertFalse(karta.czyZablokowana())` |
| `assertEquals(expected, actual)` | Porównuje wartości | `assertEquals(1000, saldo)` |
| `assertNotEquals(a, b)` | Sprawdza czy różne | `assertNotEquals(nr1, nr2)` |
| `assertNull(obj)` | Sprawdza czy null | `assertNull(dao.znajdzKlienta(999))` |
| `assertNotNull(obj)` | Sprawdza czy nie null | `assertNotNull(karta)` |
| `assertThrows(Exception.class, lambda)` | Oczekuje wyjątku | `assertThrows(RuntimeException.class, () -> wyplata.realizuj())` |
| `assertDoesNotThrow(lambda)` | Nie oczekuje wyjątku | `assertDoesNotThrow(() -> dao.usun())` |

### Przykłady z projektu

```java
// KartaTest - różne asercje
assertNotNull(karta);                           // Obiekt istnieje
assertEquals(ID_KARTY, karta.dajId());         // Równość wartości
assertTrue(karta.sprawdzPin(PIN));             // Warunek true
assertFalse(karta.czyZablokowana());           // Warunek false

// DAOTest - asercje kolekcji
assertEquals("Klient 1", dao.znajdzKlienta(nr1));
assertNotEquals(nr1, nr2);                      // Różne wartości

// Wyjątki
assertThrows(RuntimeException.class, () -> 
    wyplata.realizujWyplate(idKarty));
```

---

## 🔄 Testy parametryzowane

### @ValueSource - jedna wartość

```java
@ParameterizedTest
@ValueSource(strings = { "0000", "1111", "9999", "" })
@DisplayName("SprawdzPin - NiepoprawnyPin")
public void testSprawdzPin_NiepoprawnyPin(String niepoprawnyPin) {
    assertFalse(karta.sprawdzPin(niepoprawnyPin));
}
```

**Zastosowanie**: Test tej samej logiki dla różnych wartości jednego parametru

**Dostępne typy**: 
- `strings` - String[]
- `ints` - int[]
- `longs` - long[]
- `doubles` - double[]
- `booleans` - boolean[]

### @CsvSource - wiele wartości

```java
@ParameterizedTest
@CsvSource({ 
    "100.00,1100.00", 
    "50.00,1050.00", 
    "200.00,1200.00" 
})
@DisplayName("ZmienSaldo - DodatniaKwota")
public void testZmienSaldo_DodatniaKwota(String kwotaStr, String oczekiwaneStr) {
    BigDecimal kwota = new BigDecimal(kwotaStr);
    BigDecimal oczekiwane = new BigDecimal(oczekiwaneStr);
    karta.zmienSaldo(kwota);
    assertEquals(oczekiwane, karta.pobierzSaldo());
}
```

**Zastosowanie**: Test z wieloma parametrami wejściowymi i oczekiwanym wynikiem

**Format**: `"param1,param2,param3"`

### Inne źródła parametrów (w projekcie niewykorzystane)

- `@MethodSource` - metoda dostarczająca parametry
- `@FieldSource` - pole z parametrami
- `@EnumSource` - wartości z enuma

---

## 📦 Zestawy testów (Test Suites)

### Struktura zestawu testów

```java
@Suite
@SuiteDisplayName("Nazwa zestawu")
@SelectPackages("NazwaPakietu")
public class SuiteNazwa {
    // Klasa może być pusta
}
```

### Zestawy w projekcie

#### 1. SuiteModel - Testy warstwy encji

```java
@Suite
@SuiteDisplayName("Zestaw testów warstwy encji (Model)")
@SelectPackages("Model")
public class SuiteModel {
}
```

**Uruchamia**: Wszystkie testy z pakietu Model (DAO, Karta, Klient, etc.)

#### 2. SuiteKontroler - Testy warstwy kontroli

```java
@Suite
@SuiteDisplayName("Zestaw testów warstwy kontroli (Kontroler)")
@SelectPackages("Kontroler")
public class SuiteKontroler {
}
```

**Uruchamia**: Wszystkie testy z pakietu Kontroler

#### 3. SuiteBezpieczenstwo - Testy bezpieczeństwa bez mocków

```java
@Suite
@SuiteDisplayName("Zestaw testów bezpieczeństwa (bez mocków)")
@SelectPackages({"Model", "Kontroler"})
@IncludeTags("bezpieczenstwo")
@ExcludeTags("mock")
public class SuiteBezpieczenstwo {
}
```

**Uruchamia**: Tylko testy z tagiem "bezpieczenstwo", wykluczając testy z "mock"

**Zastosowanie**: Testowanie funkcji bezpieczeństwa na rzeczywistych obiektach

#### 4. SuiteMock - Testy z Mockito

```java
@Suite
@SuiteDisplayName("Zestaw testów z symulacją (Mockito)")
@SelectPackages({"Model", "Kontroler"})
@IncludeTags("mock")
@ExcludeTags("saldo")
public class SuiteMock {
}
```

**Uruchamia**: Tylko testy z tagiem "mock", wykluczając testy z "saldo"

**Zastosowanie**: Szybkie testy izolowane od rzeczywistych zależności

### Tagi używane w projekcie

| Tag | Znaczenie | Przykład użycia |
|-----|-----------|----------------|
| `model` | Testy warstwy encji | DAOTest, KartaTest |
| `kontroler` | Testy warstwy kontroli | WyplataGotowkiTest |
| `mock` | Testy z Mockito | WyplataGotowkiMockTest |
| `karta` | Testy klasy Karta | KartaTest |
| `saldo` | Testy operacji na saldzie | KartaTest |
| `dao` | Testy DAO | DAOTest |
| `baza` | Testy bazy danych | DAOTest |
| `biznesowa` | Testy logiki biznesowej | ModelTest |
| `klient` | Testy obsługi klienta | KontrolerKlientaTest |
| `weryfikacja` | Testy weryfikacji | WeryfikacjaTozsamosciTest |
| `bezpieczenstwo` | Testy bezpieczeństwa | MonitorowanieBezpieczenstwaTest |
| `wyplata` | Testy wypłaty | WyplataGotowkiTest |
| `monitoring` | Testy monitoringu | MonitorowanieBezpieczenstwaTest |

---

## 🏗️ Struktura testów

### Wzorzec Given-When-Then (Jeśli-Gdy-Wtedy)

Każdy test w projekcie używa tego wzorca:

```java
@Test
@DisplayName("Opis testu")
public void testNazwa() {
    // Jeśli (given): przygotowanie danych testowych
    int idKarty = 100;
    double kwota = 150.0;
    
    // Gdy (when): wykonanie testowanej operacji
    boolean wynik = wyplata.realizujWyplate(idKarty);
    
    // Wtedy (then): sprawdzenie wyników (asercje)
    assertTrue(wynik);
    assertEquals(850.0, saldo);
}
```

### Kolejność testowania

**Zasada**: Od niezależnych do zależnych

1. **Testy warstwy encji (Model)**
   - Operacje niezależne (konstruktory, gettery, settery)
   - Operacje podstawowe (dodawanie, usuwanie)
   - Operacje złożone (walidacja, transformacje)

2. **Testy warstwy kontroli (Kontroler)**
   - Operacje elementarne (pojedyncze akcje)
   - Operacje złożone (przypadki użycia)

### Przykładowa kolejność w DAOTest

```java
@Order(1) - testKonstruktor()           // Najprostszy
@Order(2) - testDodajWpisDoRejestruZdarzen()
@Order(3) - testZnajdzKlienta_Nieistniejacy()
@Order(4) - testDodajKlienta()          // Bazowy
@Order(5) - testDodajKlienta_Wielokrotne()  // Rozszerzony
@Order(6) - testUsunKlienta()           // Używa dodajKlienta
@Order(7) - testUsunKlienta_Nieistniejacy()
@Order(8) - testEdytujKlienta()
@Order(9) - testZmianaBlokadyKarty()
@Order(10) - testZmianaBlokadyKarty_RozneKarty()
```

---

## 💡 Wzorce i dobre praktyki

### 1. Nazywanie testów

**Konwencja**: `test[MetodaTestowana]_[Scenariusz]`

```java
testRealizujWyplate_PoprawneDane()
testRealizujWyplate_NiewystarczajaceSaldo()
testRealizujWyplate_KwotaZero()
```

**@DisplayName** dla czytelności:

```java
@DisplayName("RealizujWyplate - PoprawneDane z mockiem")
```

### 2. Struktura klasy testowej

```java
// 1. Adnotacje klasy
@DisplayName("NazwaTestu")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kategoria")

// 2. Pola testowe
private ObiekTestowany obiekt;
private AutoCloseable closeable;  // Dla Mockito

// 3. Setup i teardown
@BeforeEach
@AfterEach

// 4. Testy w kolejności logicznej
@Test @Order(1)
@Test @Order(2)
```

### 3. Czyszczenie zasobów

```java
@AfterEach
public void tearDown() throws Exception {
    if (closeable != null) {
        closeable.close();  // Mockito cleanup
    }
    obiekt = null;  // Null assignment
}
```

### 4. Testy z mockami - kompletny wzorzec

```java
public class KlasaMockTest {
    @Mock
    private Zaleznosc mockZaleznosc;
    
    @InjectMocks
    private KlasaTestowana obiekt;
    
    private AutoCloseable closeable;
    
    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        obiekt = null;
    }
    
    @Test
    public void testMetoda() {
        // Given: definiuj zachowanie mocków
        when(mockZaleznosc.metoda()).thenReturn(wartosc);
        
        // When: wykonaj operację
        wynik = obiekt.testowanaMetoda();
        
        // Then: weryfikuj wywołania i wynik
        verify(mockZaleznosc).metoda();
        assertTrue(wynik);
    }
}
```

### 5. Testowanie wyjątków

```java
// Oczekiwanie wyjątku
assertThrows(RuntimeException.class, () -> 
    obiekt.metodaRzucajacaWyjatek()
);

// Weryfikacja wywołania przed wyjątkiem
assertThrows(RuntimeException.class, () -> wyplata.realizuj());
verify(mockModel).sprawdzSaldo(100);
```

### 6. Dane testowe jako stałe

```java
private static final int ID_KARTY = 1;
private static final String PIN = "1234";
private static final BigDecimal SALDO_POCZATKOWE = new BigDecimal("1000.00");

@BeforeEach
public void setUp() {
    karta = new Karta(ID_KARTY, PIN, SALDO_POCZATKOWE);
}
```

### 7. Weryfikacja kolejności operacji

```java
InOrder inOrder = inOrder(mockModel);
inOrder.verify(mockModel).operacja1();
inOrder.verify(mockModel).operacja2();
inOrder.verify(mockModel).operacja3();
```

### 8. Grupowanie testów tagami

```java
@Tag("model")      // Warstwa
@Tag("karta")      // Klasa
@Tag("saldo")      // Funkcjonalność
@Tag("bezpieczenstwo")  // Aspekt
```

---

## 🔧 Uruchamianie testów

### Z linii poleceń

```bash
# Wszystkie testy
cd src
./run-tests.sh

# Maven
mvn test

# Konkretna klasa
mvn test -Dtest=KartaTest

# Konkretny test
mvn test -Dtest=KartaTest#testKonstruktor
```

### Z IDE

- **IntelliJ IDEA**: Prawy przycisk na klasie/metodzie → Run
- **Eclipse**: Prawy przycik → Run As → JUnit Test

### Zestawy testów

```bash
# Uruchomienie zestawu
mvn test -Dtest=SuiteMock
mvn test -Dtest=SuiteModel
mvn test -Dtest=SuiteBezpieczenstwo
```

---

## 📊 Podsumowanie projektu

### Statystyki testów

| Kategoria | Liczba testów |
|-----------|---------------|
| Testy Model | ~50 |
| Testy Kontroler | ~35 |
| Testy z Mockito | ~24 |
| Razem | ~85+ testów |

### Pokrycie funkcjonalności

- ✅ Testy jednostkowe klas encji (Model)
- ✅ Testy jednostkowe kontrolerów (Kontroler)
- ✅ Testy z mockowaniem zależności (Mockito)
- ✅ Testy parametryzowane (@ValueSource, @CsvSource)
- ✅ Zestawy testów (@Suite)
- ✅ Tagowanie testów (@Tag)
- ✅ Komentarze Given-When-Then

### Najważniejsze klasy testowe

#### Bez mocków:
- `DAOTest` - 10 testów (operacje bazowe DAO)
- `KartaTest` - 9 testów (w tym parametryzowane)
- `InwentarzTest` - 10 testów
- `ModelTest` - 12 testów
- `WyplataGotowkiTest` - 9 testów

#### Z mockami:
- `WyplataGotowkiMockTest` - 8 testów (symulacja IModel)
- `WeryfikacjaTozsamosciMockTest` - 8 testów (symulacja IModel i strategii)
- `MonitorowanieBezpieczenstwaMockTest` - 8 testów (symulacja IModel)

---

## 🎓 Kluczowe koncepcje

### Kiedy używać mocków?

✅ **TAK**:
- Testowana klasa zależy od trudnodostępnych zasobów (baza danych, API)
- Chcesz przetestować obsługę błędów
- Zależności są niestabilne lub wolne
- Chcesz izolować testowaną logikę

❌ **NIE**:
- Testowanie prostych klas encji (POJO)
- Klasy bez zależności
- Gdy mock byłby bardziej skomplikowany niż rzeczywisty obiekt

### JUnit vs Mockito

| Aspekt | JUnit 5 | Mockito |
|--------|---------|---------|
| Rola | Framework testowy | Tworzenie mocków |
| Główne zadanie | Uruchamianie testów, asercje | Symulacja zależności |
| Używany do | Struktury testów | Izolacji testów |
| Przykłady | @Test, assertEquals() | @Mock, verify() |

---

## 📖 Dodatkowe zasoby

### Dokumentacja
- **JUnit 5**: https://junit.org/junit5/docs/current/user-guide/
- **Mockito**: https://javadoc.io/doc/org.mockito/mockito-core/latest/

### Materiały w projekcie
- `OPIS_TESTOW.md` - Szczegółowy opis wszystkich testów
- `testy_fuc_instr` - Instrukcje do zadań testowych

---

**Autor**: System Bankomatu - Projekt IO  
**Data**: 2026-01-27  
**Wersja**: 1.0
