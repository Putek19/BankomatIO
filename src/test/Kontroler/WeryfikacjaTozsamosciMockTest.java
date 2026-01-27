package Kontroler;

import Model.IModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.InOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testy jednostkowe klasy WeryfikacjaTozsamosci z użyciem Mockito.
 * Testowane operacje zależą od IModel, który jest symulowany.
 * Strategia zabezpieczenia używa konkretnej implementacji ZablokowanieKarty
 * z wstrzykniętym mockiem IModel.
 */
@DisplayName("WeryfikacjaTozsamosciMockTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
@Tag("weryfikacja")
@Tag("bezpieczenstwo")
public class WeryfikacjaTozsamosciMockTest {

    // Symulacja obiektu IModel, od którego zależy WeryfikacjaTozsamosci
    @Mock
    private IModel mockModel;

    // Obiekt testowany
    private WeryfikacjaTozsamosci weryfikacja;

    // Strategia zabezpieczenia z wstrzykniętym mockiem
    private ZablokowanieKarty strategia;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        // Jeśli (given): inicjalizacja symulacji
        closeable = MockitoAnnotations.openMocks(this);
        // Tworzymy obiekt testowany ręcznie z mockiem IModel
        weryfikacja = new WeryfikacjaTozsamosci(mockModel);
        // Strategia używa tego samego mocka IModel
        strategia = new ZablokowanieKarty(mockModel);
        weryfikacja.ustawStrategie(strategia);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        weryfikacja = null;
        strategia = null;
    }

    @Test
    @Order(1)
    @DisplayName("WeryfikujPin - PoprawnyPin z mockiem")
    public void testWeryfikujPin_PoprawnyPin() {
        // Jeśli (given): określenie zachowania symulacji
        int idKarty = 100;
        String pin = "1234";

        when(mockModel.sprawdzPin(idKarty, pin)).thenReturn(true);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacji
        boolean wynik = weryfikacja.weryfikujPin(idKarty, pin);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertTrue(wynik);
        verify(mockModel).sprawdzPin(idKarty, pin);
        verify(mockModel).zarejestrujZdarzenie(contains("Pomyślna"));
    }

    @Test
    @Order(2)
    @DisplayName("WeryfikujPin - NiepoprawnyPin z mockiem")
    public void testWeryfikujPin_NiepoprawnyPin() {
        // Jeśli (given): określenie zachowania symulacji
        int idKarty = 100;
        String pin = "0000";

        when(mockModel.sprawdzPin(idKarty, pin)).thenReturn(false);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacji
        boolean wynik = weryfikacja.weryfikujPin(idKarty, pin);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel).sprawdzPin(idKarty, pin);
        verify(mockModel).zarejestrujZdarzenie(contains("Nieudana"));
    }

    @Test
    @Order(3)
    @DisplayName("WeryfikujPin - Blokada po 5 próbach z mockiem")
    public void testWeryfikujPin_BlokadaPo5Probach() {
        // Jeśli (given): określenie zachowania symulacji
        int idKarty = 100;
        String pin = "0000";

        when(mockModel.sprawdzPin(idKarty, pin)).thenReturn(false);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());
        doNothing().when(mockModel).zablokujKarte(idKarty);

        // Gdy (when): wykonanie 5 niepoprawnych prób
        for (int i = 0; i < 5; i++) {
            weryfikacja.weryfikujPin(idKarty, pin);
        }

        // Wtedy (then): sprawdzenie wykonania zabezpieczenia (blokada karty)
        verify(mockModel, times(1)).zablokujKarte(idKarty);
        verify(mockModel, atLeast(4)).sprawdzPin(idKarty, pin);
        verify(mockModel).zarejestrujZdarzenie(contains("Przekroczono"));
    }

    @Test
    @Order(4)
    @DisplayName("WeryfikujPin - Reset licznika z mockiem")
    public void testWeryfikujPin_ResetLicznika() {
        // Jeśli (given): dwie nieudane próby, potem udana
        int idKarty = 100;
        String niepoprawnyPin = "0000";
        String poprawnyPin = "1234";

        when(mockModel.sprawdzPin(idKarty, niepoprawnyPin)).thenReturn(false);
        when(mockModel.sprawdzPin(idKarty, poprawnyPin)).thenReturn(true);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): 2 nieudane próby, potem udana
        weryfikacja.weryfikujPin(idKarty, niepoprawnyPin);
        weryfikacja.weryfikujPin(idKarty, niepoprawnyPin);
        boolean wynik = weryfikacja.weryfikujPin(idKarty, poprawnyPin);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertTrue(wynik);
        verify(mockModel, times(2)).sprawdzPin(idKarty, niepoprawnyPin);
        verify(mockModel, times(1)).sprawdzPin(idKarty, poprawnyPin);
        verify(mockModel, never()).zablokujKarte(anyInt());
    }

    @Test
    @Order(5)
    @DisplayName("WeryfikujPin - Kolejność wywołań z InOrder")
    public void testWeryfikujPin_Kolejnosc() {
        // Jeśli (given): poprawne dane
        int idKarty = 100;
        String pin = "1234";

        when(mockModel.sprawdzPin(idKarty, pin)).thenReturn(true);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacji
        boolean wynik = weryfikacja.weryfikujPin(idKarty, pin);

        // Wtedy (then): sprawdzenie kolejności użycia symulacji
        InOrder inOrder = inOrder(mockModel);
        inOrder.verify(mockModel).sprawdzPin(idKarty, pin);
        inOrder.verify(mockModel).zarejestrujZdarzenie(anyString());
        assertTrue(wynik);
    }

    @Test
    @Order(6)
    @DisplayName("WeryfikujPin - Wyjątek z thenThrow")
    public void testWeryfikujPin_Wyjatek() {
        // Jeśli (given): symulacja wyrzuca wyjątek
        int idKarty = 100;
        String pin = "1234";

        when(mockModel.sprawdzPin(idKarty, pin)).thenThrow(new RuntimeException("Błąd połączenia"));

        // Gdy (when) i Wtedy (then): oczekujemy wyjątku
        assertThrows(RuntimeException.class, () -> weryfikacja.weryfikujPin(idKarty, pin));
        verify(mockModel).sprawdzPin(idKarty, pin);
    }

    @Test
    @Order(7)
    @DisplayName("UstawStrategie")
    public void testUstawStrategie() {
        // Jeśli (given): nowa strategia z mockiem
        ZablokowanieKarty nowaStrategia = new ZablokowanieKarty(mockModel);

        // Gdy (when): ustawienie strategii
        assertDoesNotThrow(() -> weryfikacja.ustawStrategie(nowaStrategia));

        // Wtedy (then): sprawdzenie że strategia została ustawiona
        assertNotNull(weryfikacja._unnamed_IStrategiaZabezpieczenia_);
    }

    @Test
    @Order(8)
    @DisplayName("ResetujLicznik - weryfikacja z atMost")
    public void testResetujLicznik() {
        // Jeśli (given): 3 nieudane próby
        int idKarty = 100;
        String pin = "0000";

        when(mockModel.sprawdzPin(idKarty, pin)).thenReturn(false);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): 3 nieudane próby, reset, 3 kolejne
        weryfikacja.weryfikujPin(idKarty, pin);
        weryfikacja.weryfikujPin(idKarty, pin);
        weryfikacja.weryfikujPin(idKarty, pin);
        weryfikacja.resetujLicznik();
        weryfikacja.weryfikujPin(idKarty, pin);
        weryfikacja.weryfikujPin(idKarty, pin);

        // Wtedy (then): sprawdzenie że zabezpieczenie nie zostało wykonane
        verify(mockModel, never()).zablokujKarte(anyInt());
        verify(mockModel, atMost(5)).sprawdzPin(anyInt(), anyString());
    }
}
