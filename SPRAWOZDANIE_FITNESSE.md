# Sprawozdanie - Testowanie akceptacyjne realizacji przypadków użycia

## Autorzy
- System Bankomatu - BankomatIO

## Temat
Testowanie akceptacyjne realizacji przypadków użycia z użyciem frameworku FitNesse

---

## Lista zmian w kodzie źródłowym

### 1. Zmiany w pliku `pom.xml`
Dodano zależność FitNesse:
```xml
<dependency>
    <groupId>org.fitnesse</groupId>
    <artifactId>fitnesse</artifactId>
    <version>20230503</version>
    <scope>test</scope>
</dependency>
```

### 2. Zmiany w klasie `Model/Inwentarz.java`
Dodano metody sprawdzające stan warstwy encji:
- `int ileKlientow()` - zwraca liczbę klientów w systemie
- `IKlient pobierzOstatniegoDodanegoKlienta()` - zwraca ostatnio dodanego klienta

### 3. Zmiany w klasie `Model/Klient.java`
Dodano metody:
- `int ileKart()` - zwraca liczbę kart klienta
- `List<IKarta> pobierzWszystkieKarty()` - zwraca wszystkie karty klienta

### 4. Zmiany w klasie `Model/Model.java`
Dodano metody:
- `void odblokujBankomat()` - odblokowanie bankomatu
- `Inwentarz pobierzInwentarz()` - dostęp do inwentarza dla testów

### 5. Zmiany w klasie `Model/DAO.java`
Dodano metody:
- `int pobierzLiczbeZdarzen()` - zwraca liczbę zarejestrowanych zdarzeń
- `List<String> pobierzRejestrZdarzen()` - zwraca pełny rejestr zdarzeń

### 6. Zmiany w interfejsie `Model/IDAO.java`
Dodano deklaracje nowych metod do interfejsu.

---

## Kod klas testujących FitNesse

### testyfitnesse/SetUp.java

```java
package testyfitnesse;

import fit.Fixture;
import Model.*;
import Kontroler.*;
import java.math.BigDecimal;

public class SetUp extends Fixture {
	public static Model model;
	public static Inwentarz inwentarz;
	public static DAO dao;
	public static KontrolerKlienta kontrolerKlienta;
	public static KontrolerAdministratora kontrolerAdministratora;
	public static WyplataGotowki wyplataGotowki;
	public static WeryfikacjaTozsamosci weryfikacjaTozsamosci;
	public static MonitorowanieBezpieczenstwa monitorowanieBezpieczenstwa;
	public static FabrykaKlienta fabrykaKlienta;
	
	public SetUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		
		kontrolerKlienta = new KontrolerKlienta(model);
		kontrolerAdministratora = new KontrolerAdministratora(model);
		
		wyplataGotowki = new WyplataGotowki(model);
		weryfikacjaTozsamosci = new WeryfikacjaTozsamosci(model);
		monitorowanieBezpieczenstwa = new MonitorowanieBezpieczenstwa(model);
		
		fabrykaKlienta = new FabrykaKlienta();
		
		przygotujDaneTestowe();
	}
	
	private void przygotujDaneTestowe() {
		IKlient klient1 = new Klient(1, "Jan");
		klient1.ustawNazwisko("Kowalski");
		klient1.ustawPesel(12345678);
		IKarta karta1 = new Karta(1001, "1234", new BigDecimal("1000.00"));
		klient1.dodajKarte(karta1);
		inwentarz.dodajKlienta(klient1);
		
		IKlient klient2 = new Klient(2, "Anna");
		klient2.ustawNazwisko("Nowak");
		klient2.ustawPesel(87654321);
		IKarta karta2 = new Karta(1002, "5678", new BigDecimal("500.00"));
		klient2.dodajKarte(karta2);
		inwentarz.dodajKlienta(klient2);
		
		IKlient klient3 = new Klient(3, "Piotr");
		klient3.ustawNazwisko("Wiśniewski");
		klient3.ustawPesel(11223344);
		IKarta karta3 = new Karta(1003, "9999", new BigDecimal("2500.00"));
		klient3.dodajKarte(karta3);
		inwentarz.dodajKlienta(klient3);
	}
}
```

### testyfitnesse/TestWyplatyGotowki.java

```java
package testyfitnesse;

import fit.ColumnFixture;
import java.math.BigDecimal;

public class TestWyplatyGotowki extends ColumnFixture {
	public int idKarty;
	public String pin;
	public double kwotaWyplaty;
	
	public boolean wykonajWyplateGotowki() {
		int stanPrzed = pobierzLiczbeTransakcji();
		BigDecimal saldoPrzed = SetUp.model.sprawdzSaldo(idKarty);
		
		boolean weryfikacja = SetUp.kontrolerKlienta.weryfikacjaTozsamosci(idKarty, pin);
		if (!weryfikacja) {
			return false;
		}
		
		SetUp.wyplataGotowki.ustawKwote(kwotaWyplaty);
		boolean wynik = SetUp.wyplataGotowki.realizujWyplate(idKarty);
		
		int stanPo = pobierzLiczbeTransakcji();
		
		return wynik && stanPrzed != stanPo;
	}
	
	public double sprawdzSaldoPoWyplacie() {
		BigDecimal saldo = SetUp.model.sprawdzSaldo(idKarty);
		return saldo.doubleValue();
	}
	
	private int pobierzLiczbeTransakcji() {
		return SetUp.dao.pobierzLiczbeZdarzen();
	}
	
	public boolean sprawdzCzyKartaNieZablokowana() {
		for (var klient : SetUp.inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				var karta = klient.pobierzKarte(idKarty);
				if (karta != null) {
					return !karta.czyZablokowana();
				}
			}
		}
		return false;
	}
}
```

### testyfitnesse/TestWeryfikacjiTozsamosci.java

```java
package testyfitnesse;

import fit.ColumnFixture;

public class TestWeryfikacjiTozsamosci extends ColumnFixture {
	public int idKarty;
	public String pin;
	
	public boolean weryfikujTozsamosc() {
		return SetUp.weryfikacjaTozsamosci.weryfikujPin(idKarty, pin);
	}
	
	public boolean sprawdzCzyKartaJestAktywna() {
		for (var klient : SetUp.inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				var karta = klient.pobierzKarte(idKarty);
				if (karta != null) {
					return !karta.czyZablokowana();
				}
			}
		}
		return false;
	}
	
	public double sprawdzSaldoKarty() {
		return SetUp.model.sprawdzSaldo(idKarty).doubleValue();
	}
}
```

### testyfitnesse/TestMonitorowaniaBezpieczenstwa.java

```java
package testyfitnesse;

import fit.ColumnFixture;

public class TestMonitorowaniaBezpieczenstwa extends ColumnFixture {
	public String strumienObrazow;
	
	public boolean analizujObraz() {
		return SetUp.monitorowanieBezpieczenstwa.analizaObrazu(strumienObrazow);
	}
	
	public boolean uruchomMonitoring() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.monitorowanieBezpieczenstwa.rozpocznijMonitoring();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
	
	public boolean sprawdzCzyMonitoringAktywny() {
		return SetUp.monitorowanieBezpieczenstwa.czyMonitoringAktywny();
	}
	
	public boolean sprawdzCzyBankomatZablokowany() {
		return SetUp.model.czyBankomatZablokowany();
	}
}
```

### testyfitnesse/TestZdalnegoBlokowania.java

```java
package testyfitnesse;

import fit.ColumnFixture;

public class TestZdalnegoBlokowania extends ColumnFixture {
	
	public boolean zablokujBankomatZdalnie() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.kontrolerAdministratora.zdalneBlokowanieBankomatu();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
	
	public boolean sprawdzCzyBankomatZablokowany() {
		return SetUp.model.czyBankomatZablokowany();
	}
	
	public boolean odblokujBankomat() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.model.odblokujBankomat();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
}
```

### testyfitnesse/TestBlokowaniaKarty.java

```java
package testyfitnesse;

import fit.ColumnFixture;

public class TestBlokowaniaKarty extends ColumnFixture {
	public int idKarty;
	
	public boolean zablokujKarte() {
		boolean czyZablokowanaPrzed = sprawdzCzyKartaZablokowana();
		SetUp.model.zablokujKarte(idKarty);
		boolean czyZablokowanaPo = sprawdzCzyKartaZablokowana();
		return !czyZablokowanaPrzed && czyZablokowanaPo;
	}
	
	public boolean sprawdzCzyKartaZablokowana() {
		for (var klient : SetUp.inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				var karta = klient.pobierzKarte(idKarty);
				if (karta != null) {
					return karta.czyZablokowana();
				}
			}
		}
		return false;
	}
	
	public boolean sprawdzCzyMoznaWyplacicZKarty() {
		if (sprawdzCzyKartaZablokowana()) {
			return false;
		}
		return SetUp.model.sprawdzSaldo(idKarty).doubleValue() > 0;
	}
}
```

---

## Strony testujące FitNesse

### Strona SetUp

```
!|testyfitnesse.SetUp|
```

### Strona TestWyplatyGotowki

```
!|testyfitnesse.TestWyplatyGotowki|
|idKarty|pin |kwotaWyplaty|wykonajWyplateGotowki?|sprawdzSaldoPoWyplacie?|sprawdzCzyKartaNieZablokowana?|
|1001   |1234|100.00      |true                   |900.0                   |true                              |
|1001   |1234|200.00      |true                   |700.0                   |true                              |
|1002   |5678|50.00       |true                   |450.0                   |true                              |
|1001   |9999|100.00      |false                  |700.0                   |true                              |
|1003   |9999|6000.00     |false                  |2500.0                  |true                              |
```

### Strona TestWeryfikacjiTozsamosci

```
!|testyfitnesse.TestWeryfikacjiTozsamosci|
|idKarty|pin |weryfikujTozsamosc?|sprawdzCzyKartaJestAktywna?|sprawdzSaldoKarty?|
|1001   |1234|true               |true                        |1000.0            |
|1002   |5678|true               |true                        |500.0             |
|1001   |0000|false              |true                        |1000.0            |
|1003   |1234|false              |true                        |2500.0            |
|1003   |9999|true               |true                        |2500.0            |
```

### Strona TestMonitorowaniaBezpieczenstwa

```
!|testyfitnesse.TestMonitorowaniaBezpieczenstwa|
|strumienObrazow  |analizujObraz?|uruchomMonitoring?|sprawdzCzyBankomatZablokowany?|
|obraz_bezpieczny |false         |false             |false                         |
|obraz_zagrożenie |true          |true              |true                          |
```

### Strona TestZdalnegoBlokowania

```
!|testyfitnesse.TestZdalnegoBlokowania|
|zablokujBankomatZdalnie?|sprawdzCzyBankomatZablokowany?|odblokujBankomat?|
|true                     |true                          |true             |
|true                     |true                          |true             |
```

### Strona TestBlokowaniaKarty

```
!|testyfitnesse.TestBlokowaniaKarty|
|idKarty|zablokujKarte?|sprawdzCzyKartaZablokowana?|sprawdzCzyMoznaWyplacicZKarty?|
|1001   |true          |true                        |false                          |
|1002   |true          |true                        |false                          |
|1003   |true          |true                        |false                          |
```

---

## Instrukcja uruchomienia

### Sposób 1: Użycie skryptu (ZALECANE)
```bash
./uruchom-fitnesse-simple.sh
```

### Sposób 2: Ręcznie
```bash
# Kompilacja
mvn clean compile test-compile

# Pobieranie zależności
mvn dependency:copy-dependencies -DincludeScope=test -DoutputDirectory=target/fitnesse-libs

# Uruchomienie FitNesse
java -cp "target/fitnesse-libs/*" fitnesseMain.FitNesseMain -p 8080

# Otwarcie przeglądarki
open http://localhost:8080
```

## Testowane przypadki użycia

1. **Wypłata gotówki** - Test realizacji pełnego procesu wypłaty z weryfikacją PIN i sprawdzeniem salda
2. **Weryfikacja tożsamości** - Test weryfikacji PIN karty
3. **Monitorowanie bezpieczeństwa** - Test analizy obrazu i automatycznego blokowania przy zagrożeniu
4. **Zdalne blokowanie bankomatu** - Test zdalnego blokowania przez administratora
5. **Blokowanie karty** - Test blokowania karty i uniemożliwienia wypłat

Wszystkie testy obejmują zarówno poprawne, jak i niepoprawne scenariusze wykonania.
