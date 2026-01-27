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
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.InOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

/**
 * Testy jednostkowe klasy WyplataGotowki z użyciem Mockito.
 * Testowane operacje zależą od IModel, który jest symulowany.
 */
@DisplayName("WyplataGotowkiMockTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
@Tag("wyplata")
public class WyplataGotowkiMockTest {

    // Symulacja obiektu IModel, od którego zależy WyplataGotowki
    @Mock
    private IModel mockModel;

    // Obiekt testowany z wstrzykniętą symulacją
    @InjectMocks
    private WyplataGotowki wyplata;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        // Jeśli (given): inicjalizacja symulacji
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        wyplata = null;
    }

    @Test
    @Order(1)
    @DisplayName("RealizujWyplate - PoprawneDane z mockiem")
    public void testRealizujWyplate_PoprawneDane() {
        // Jeśli (given): określenie zachowania symulacji
        int idKarty = 100;
        double kwota = 150.0;
        BigDecimal saldo = new BigDecimal("1000.00");

        when(mockModel.sprawdzSaldo(idKarty)).thenReturn(saldo);
        doNothing().when(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertTrue(wynik);
        verify(mockModel).sprawdzSaldo(idKarty);
        verify(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
        verify(mockModel, times(1)).zarejestrujZdarzenie(contains("Zrealizowano"));
    }

    @Test
    @Order(2)
    @DisplayName("RealizujWyplate - NiewystarczajaceSaldo z mockiem")
    public void testRealizujWyplate_NiewystarczajaceSaldo() {
        // Jeśli (given): saldo niewystarczające
        int idKarty = 100;
        double kwota = 2000.0;
        BigDecimal saldo = new BigDecimal("1000.00");

        when(mockModel.sprawdzSaldo(idKarty)).thenReturn(saldo);
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel).sprawdzSaldo(idKarty);
        verify(mockModel, never()).aktualizujSaldo(anyInt(), any(BigDecimal.class));
        verify(mockModel).zarejestrujZdarzenie(contains("niewystarczającym"));
    }

    @Test
    @Order(3)
    @DisplayName("RealizujWyplate - KwotaZero z mockiem")
    public void testRealizujWyplate_KwotaZero() {
        // Jeśli (given): kwota zero
        int idKarty = 100;
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(0.0);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel, never()).sprawdzSaldo(anyInt());
        verify(mockModel, atLeastOnce()).zarejestrujZdarzenie(anyString());
    }

    @Test
    @Order(4)
    @DisplayName("RealizujWyplate - KwotaUjemna z mockiem")
    public void testRealizujWyplate_KwotaUjemna() {
        // Jeśli (given): kwota ujemna
        int idKarty = 100;
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(-100.0);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel, never()).aktualizujSaldo(anyInt(), any(BigDecimal.class));
        verify(mockModel, atLeast(1)).zarejestrujZdarzenie(anyString());
    }

    @Test
    @Order(5)
    @DisplayName("RealizujWyplate - PrzekroczonaMaksymalnaKwota z mockiem")
    public void testRealizujWyplate_PrzekroczonaMaksymalnaKwota() {
        // Jeśli (given): przekroczenie maksymalnej kwoty
        int idKarty = 100;
        double kwota = 6000.0;
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel, never()).sprawdzSaldo(anyInt());
        verify(mockModel, atMost(2)).zarejestrujZdarzenie(anyString());
    }

    @Test
    @Order(6)
    @DisplayName("RealizujWyplate - Kolejność wywołań z InOrder")
    public void testRealizujWyplate_Kolejnosc() {
        // Jeśli (given): poprawne dane
        int idKarty = 100;
        double kwota = 150.0;
        BigDecimal saldo = new BigDecimal("1000.00");

        when(mockModel.sprawdzSaldo(idKarty)).thenReturn(saldo);
        doNothing().when(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);
        boolean wynik = wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie kolejności użycia symulacji
        InOrder inOrder = inOrder(mockModel);
        inOrder.verify(mockModel).sprawdzSaldo(idKarty);
        inOrder.verify(mockModel).aktualizujSaldo(eq(idKarty), any(BigDecimal.class));
        inOrder.verify(mockModel).zarejestrujZdarzenie(contains("Zrealizowano"));
        assertTrue(wynik);
    }

    @Test
    @Order(7)
    @DisplayName("RealizujWyplate - Wyjątek przy sprawdzaniu salda")
    public void testRealizujWyplate_WyjatekPrzySaldzie() {
        // Jeśli (given): symulacja wyrzuca wyjątek
        int idKarty = 100;
        double kwota = 150.0;

        when(mockModel.sprawdzSaldo(idKarty)).thenThrow(new RuntimeException("Błąd bazy danych"));
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);

        // Wtedy (then): oczekujemy wyjątku
        assertThrows(RuntimeException.class, () -> wyplata.realizujWyplate(idKarty));
        verify(mockModel).sprawdzSaldo(idKarty);
    }

    @Test
    @Order(8)
    @DisplayName("RealizujWyplate - atMostOnce weryfikacja")
    public void testRealizujWyplate_AtMostOnce() {
        // Jeśli (given): poprawne dane
        int idKarty = 100;
        double kwota = 100.0;
        BigDecimal saldo = new BigDecimal("500.00");

        when(mockModel.sprawdzSaldo(idKarty)).thenReturn(saldo);
        doNothing().when(mockModel).aktualizujSaldo(anyInt(), any(BigDecimal.class));
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        wyplata.ustawKwote(kwota);
        wyplata.realizujWyplate(idKarty);

        // Wtedy (then): sprawdzenie użycia symulacji atMostOnce
        verify(mockModel, atMostOnce()).sprawdzSaldo(idKarty);
        verify(mockModel, atMostOnce()).aktualizujSaldo(anyInt(), any(BigDecimal.class));
    }
}
