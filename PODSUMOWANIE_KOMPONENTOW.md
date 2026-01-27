
## Warstwa Model

### Interfejsy
- **IModel** - główny interfejs modelu
- **IDAO** - interfejs dostępu do danych
- **IKlient** - interfejs klienta
- **IKarta** - interfejs karty
- **IFabrykaKlienta** - interfejs fabryki klientów

### Klasy Implementujące
- **Model** - główna klasa modelu, implementuje IModel
- **DAO** - implementacja dostępu do danych, implementuje IDAO
- **Klient** - implementacja klienta, implementuje IKlient
- **Karta** - podstawowa implementacja karty, implementuje IKarta
- **Inwentarz** - zarządzanie kolekcją klientów
- **FabrykaKlienta** - fabryka tworząca klientów (wzorzec Factory Method)
- **Formularz** - klasa pomocnicza do przechowywania danych formularza

### Wzorce Projektowe
- **KartaDekorator** - abstrakcyjna klasa dekoratora (wzorzec Decorator)
- **ZablokowanaKarta** - konkretny dekorator dla zablokowanych kart

## Warstwa Kontroler

### Interfejsy
- **IKontrolerKlienta** - interfejs kontrolera klienta
- **IKontrolerAdminstratora** - interfejs kontrolera administratora
- **IStrategiaZabezpieczenia** - abstrakcyjna klasa strategii zabezpieczeń

### Klasy Implementujące
- **KontrolerKlienta** - kontroler operacji klienta, implementuje IKontrolerKlienta
- **KontrolerAdministratora** - kontroler operacji administratora, implementuje IKontrolerAdminstratora
- **WeryfikacjaTozsamosci** - weryfikacja PIN z licznikiem prób
- **WyplataGotowki** - logika wypłaty gotówki
- **MonitorowanieBezpieczenstwa** - monitoring bezpieczeństwa bankomatu
- **ZablokowanieKarty** - strategia blokowania karty (wzorzec Strategy)
- **ZdalneBlokowanieBankomatu** - strategia zdalnego blokowania bankomatu

### Klasa Główna
- **SystemBankomatu** - klasa main z przykładowym użyciem systemu

## Funkcjonalności

### Operacje Klienta
- Weryfikacja tożsamości (PIN)
- Wypłata gotówki
- Sprawdzanie salda
- Blokada karty po 5 nieudanych próbach PIN

### Operacje Administratora
- Monitorowanie bezpieczeństwa
- Zdalne blokowanie bankomatu
- Zarządzanie gotówką

### Bezpieczeństwo
- Weryfikacja PIN z limitem prób
- Automatyczna blokada karty
- Monitoring bezpieczeństwa z wykrywaniem zagrożeń
- Zdalne blokowanie bankomatu

### Wzorce Projektowe
- **Factory Method** - FabrykaKlienta
- **Decorator** - KartaDekorator, ZablokowanaKarta
- **Strategy** - IStrategiaZabezpieczenia i implementacje
- **Adapter** - DAO jako adapter do danych

## Testy JUnit 5

Utworzono kompleksowe testy jednostkowe dla wszystkich komponentów:

### Testy Model
- `KartaTest` - testy karty (PIN, saldo, blokada)
- `KlientTest` - testy klienta i zarządzania kartami
- `DAOTest` - testy dostępu do danych
- `InwentarzTest` - testy zarządzania klientami
- `ModelTest` - testy głównego modelu
- `FabrykaKlientaTest` - testy fabryki klientów
- `ZablokowanaKartaTest` - testy dekoratora zablokowanej karty

### Testy Kontroler (Przypadki użycia)
- `KontrolerKlientaTest` - testy kontrolera klienta
- `KontrolerAdministratoraTest` - testy kontrolera administratora
- `WeryfikacjaTozsamosciTest` + `WeryfikacjaTozsamosciMockTest` - testy weryfikacji PIN
- `WyplataGotowkiTest` + `WyplataGotowkiMockTest` - testy wypłaty gotówki
- `MonitorowanieBezpieczenstwaTest` + `MonitorowanieBezpieczenstwaMockTest` - testy monitoringu
- `ZablokowanieKartyTest` - testy blokowania karty
- `ZdalneBlokowanieBankomatuTest` - testy zdalnego blokowania

### Zestawy testów (Test Suites)
- `SuiteModel` - wszystkie testy warstwy Model
- `SuiteKontroler` - wszystkie testy warstwy Kontroler
- `SuiteMock` - tylko testy z Mockito
- `SuiteBezpieczenstwo` - tylko testy bezpieczeństwa (bez mocków)




