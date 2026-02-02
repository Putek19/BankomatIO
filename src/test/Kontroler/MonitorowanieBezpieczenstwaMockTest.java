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

@DisplayName("MonitorowanieBezpieczenstwaMockTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("mock")
@Tag("bezpieczenstwo")
@Tag("monitoring")
public class MonitorowanieBezpieczenstwaMockTest {

    // Symulacja obiektu IModel, od którego zależy MonitorowanieBezpieczenstwa
    @Mock
    private IModel mockModel;

    // Obiekt testowany z wstrzykniętą symulacją
    @InjectMocks
    private MonitorowanieBezpieczenstwa monitoring;

    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        // Jeśli (given): inicjalizacja symulacji
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
        monitoring = null;
    }

    @Test
    @Order(1)
    @DisplayName("AnalizaObrazu - Bezpieczny z mockiem")
    public void testAnalizaObrazu_Bezpieczny() {
        // Jeśli (given): określenie zachowania symulacji
        String obraz = "obraz_bezpieczny";
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        boolean wynik = monitoring.analizaObrazu(obraz);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel).zarejestrujZdarzenie(contains("Analiza obrazu"));
    }

    @Test
    @Order(2)
    @DisplayName("AnalizaObrazu - Zagrozenie z mockiem")
    public void testAnalizaObrazu_Zagrozenie() {
        // Jeśli (given): określenie zachowania symulacji
        String obraz = "obraz_zagrożenie";
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        boolean wynik = monitoring.analizaObrazu(obraz);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertTrue(wynik);
        verify(mockModel).zarejestrujZdarzenie(contains("Analiza obrazu"));
    }

    @Test
    @Order(3)
    @DisplayName("AnalizaObrazu - Null")
    public void testAnalizaObrazu_Null() {
        // Jeśli (given): obraz null

        // Gdy (when): wykonanie testowanej operacjii
        boolean wynik = monitoring.analizaObrazu(null);

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(wynik);
        verify(mockModel, never()).zarejestrujZdarzenie(anyString());
    }

    @Test
    @Order(4)
    @DisplayName("ObsluzZagrozenie - BlokadaBankomatu z mockiem")
    public void testObsluzZagrozenie_BlokadaBankomatu() {
        // Jeśli (given): określenie zachowania symulacji
        int idBankomatu = 1;
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());
        doNothing().when(mockModel).zablokujBankomat();

        // Gdy (when): wykonanie testowanej operacjii
        monitoring.obsluzZagrozenie(idBankomatu);

        // Wtedy (then): sprawdzenie kolejności wywołań i asercje
        InOrder inOrder = inOrder(mockModel);
        inOrder.verify(mockModel).zarejestrujZdarzenie(contains("ZAGROŻENIE"));
        inOrder.verify(mockModel).zablokujBankomat();
        assertFalse(monitoring.czyMonitoringAktywny());
    }

    @Test
    @Order(5)
    @DisplayName("ZatrzymajMonitoring z mockiem")
    public void testZatrzymajMonitoring() {
        // Jeśli (given): określenie zachowania symulacji
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when): wykonanie testowanej operacjii
        monitoring.zatrzymajMonitoring();

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        assertFalse(monitoring.czyMonitoringAktywny());
        verify(mockModel).zarejestrujZdarzenie(contains("Zatrzymano"));
    }

    @Test
    @Order(6)
    @DisplayName("RozpocznijMonitoring - WykrycieZagrozenia z mockiem")
    public void testRozpocznijMonitoring_WykrycieZagrozenia() {
        // Jeśli (given): określenie zachowania symulacji
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());
        doNothing().when(mockModel).zablokujBankomat();

        // Gdy (when): wykonanie testowanej operacjii
        monitoring.rozpocznijMonitoring();

        // Wtedy (then): sprawdzenie użycia symulacji i asercje
        verify(mockModel).zarejestrujZdarzenie(contains("Monitorowanie rozpoczęte"));
        verify(mockModel).zarejestrujZdarzenie(contains("ZAGROŻENIE"));
        verify(mockModel).zablokujBankomat();
        assertFalse(monitoring.czyMonitoringAktywny());
    }

    @Test
    @Order(7)
    @DisplayName("AnalizaObrazu - Wyjątek z thenThrow")
    public void testAnalizaObrazu_Wyjatek() {
        // Jeśli (given): symulacja wyrzuca wyjątek
        String obraz = "obraz_test";
        doThrow(new RuntimeException("Błąd zapisu")).when(mockModel).zarejestrujZdarzenie(anyString());

        // Gdy (when) i Wtedy (then): oczekujemy wyjątku
        assertThrows(RuntimeException.class, () -> monitoring.analizaObrazu(obraz));
        verify(mockModel, times(1)).zarejestrujZdarzenie(anyString());
    }

    @Test
    @Order(8)
    @DisplayName("RozpocznijMonitoring - weryfikacja times")
    public void testRozpocznijMonitoring_Times() {
        // Jeśli (given): określenie zachowania symulacji
        doNothing().when(mockModel).zarejestrujZdarzenie(anyString());
        doNothing().when(mockModel).zablokujBankomat();

        // Gdy (when): wykonanie testowanej operacjii
        monitoring.rozpocznijMonitoring();

        // Wtedy (then): sprawdzenie ilości wywołań
        // Rozpoczęcie + 3 analizy obrazu + wykrycie zagrożenia + blokada bankomatu
        verify(mockModel, atLeast(3)).zarejestrujZdarzenie(anyString());
        verify(mockModel, times(1)).zablokujBankomat();
    }
}
