package Kontroler;

import Model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MonitorowanieBezpieczenstwaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("bezpieczenstwo")
@Tag("monitoring")
public class MonitorowanieBezpieczenstwaTest {
	private MonitorowanieBezpieczenstwa monitoring;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		monitoring = new MonitorowanieBezpieczenstwa(model);
	}

	@AfterEach
	public void tearDown() {
		monitoring = null;
		model = null;
		inwentarz = null;
		dao = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		// Jeśli (given): monitoring został utworzony w setUp()
		// Gdy (when): sprawdzenie stanu monitoringu
		// Wtedy (then): monitoring powinien być zainicjalizowany jako nieaktywny
		assertNotNull(monitoring);
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(2)
	@DisplayName("RozpocznijMonitoring")
	public void testRozpocznijMonitoring() {
		// Jeśli (given): monitoring nieaktywny
		// Gdy (when): rozpoczęcie monitoringu
		monitoring.rozpocznijMonitoring();
		// Wtedy (then): ponowne rozpoczęcie nie powinno wyrzucić wyjątku
		assertDoesNotThrow(() -> monitoring.rozpocznijMonitoring());
	}

	@Test
	@Order(3)
	@DisplayName("AnalizaObrazu - Bezpieczny")
	public void testAnalizaObrazu_Bezpieczny() {
		// Jeśli (given): obraz oznaczony jako bezpieczny
		// Gdy (when): analiza obrazu
		boolean wynik = monitoring.analizaObrazu("obraz_bezpieczny");
		// Wtedy (then): nie powinno zostać wykryte zagrożenie
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("AnalizaObrazu - Zagrozenie")
	public void testAnalizaObrazu_Zagrozenie() {
		// Jeśli (given): obraz oznaczony jako zagrożenie
		// Gdy (when): analiza obrazu
		boolean wynik = monitoring.analizaObrazu("obraz_zagrożenie");
		// Wtedy (then): powinno zostać wykryte zagrożenie
		assertTrue(wynik);
	}

	@ParameterizedTest
	@Order(5)
	@ValueSource(strings = { "", "null", "test", "inne" })
	@DisplayName("AnalizaObrazu - Niepoprawne")
	public void testAnalizaObrazu_Niepoprawne(String obraz) {
		// Jeśli (given): niepoprawne nazwy obrazów
		// Gdy (when): analiza niepoprawny obraz
		boolean wynik = monitoring.analizaObrazu(obraz);
		// Wtedy (then): nie powinno zostać wykryte zagrożenie
		assertFalse(wynik);
	}

	@Test
	@Order(6)
	@DisplayName("AnalizaObrazu - Null")
	public void testAnalizaObrazu_Null() {
		// Jeśli (given): obraz o wartości null
		// Gdy (when): analiza null
		boolean wynik = monitoring.analizaObrazu(null);
		// Wtedy (then): nie powinno zostać wykryte zagrożenie
		assertFalse(wynik);
	}

	@Test
	@Order(7)
	@DisplayName("RozpocznijMonitoring - WykrycieZagrozenia")
	public void testRozpocznijMonitoring_WykrycieZagrozenia() {
		// Jeśli (given): bankomat niezablokowany
		assertFalse(model.czyBankomatZablokowany());
		// Gdy (when): rozpoczęcie monitoringu (który wykrywa zagrożenie)
		monitoring.rozpocznijMonitoring();
		// Wtedy (then): bankomat powinien zostać zablokowany
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(8)
	@DisplayName("ZatrzymajMonitoring")
	public void testZatrzymajMonitoring() {
		// Jeśli (given): monitoring aktywny
		monitoring.rozpocznijMonitoring();
		// Gdy (when): zatrzymanie monitoringu
		monitoring.zatrzymajMonitoring();
		// Wtedy (then): monitoring powinien być nieaktywny
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(9)
	@DisplayName("ObsluzZagrozenie")
	public void testObsluzZagrozenie() {
		// Jeśli (given): bankomat niezablokowany i monitoring aktywny
		assertFalse(model.czyBankomatZablokowany());
		// Gdy (when): obsługa zagrożeniaie
		monitoring.obsluzZagrozenie(1);
		// Wtedy (then): bankomat powinien być zablokowany, monitoring nieaktywny
		assertTrue(model.czyBankomatZablokowany());
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(10)
	@DisplayName("CzyMonitoringAktywny")
	public void testCzyMonitoringAktywny() {
		// Jeśli (given): monitoring nieaktywny
		assertFalse(monitoring.czyMonitoringAktywny());
		// Gdy (when): rozpoczęcie monitoringu i sprawdzenie status
		monitoring.rozpocznijMonitoring();
		// Wtedy (then): sprawdzenie statusu nie powinno wyrzucić wyjątku
		assertDoesNotThrow(() -> monitoring.czyMonitoringAktywny());
	}
}
