package Kontroler;

import Model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MonitorowanieBezpieczenstwaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("bezpieczenstwo")
public class MonitorowanieBezpieczenstwaTest {
	private MonitorowanieBezpieczenstwa monitoring;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy MonitorowanieBezpieczenstwa");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy MonitorowanieBezpieczenstwa");
	}

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
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(monitoring);
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(2)
	@DisplayName("RozpocznijMonitoring")
	@Tag("monitoring")
	public void testRozpocznijMonitoring() {
		monitoring.rozpocznijMonitoring();
		assertDoesNotThrow(() -> monitoring.rozpocznijMonitoring());
	}

	@Test
	@Order(3)
	@DisplayName("AnalizaObrazu - Bezpieczny")
	@Tag("analiza")
	public void testAnalizaObrazu_Bezpieczny() {
		boolean wynik = monitoring.analizaObrazu("obraz_bezpieczny");
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("AnalizaObrazu - Zagrozenie")
	@Tag("analiza")
	@Tag("zagrozenie")
	public void testAnalizaObrazu_Zagrozenie() {
		boolean wynik = monitoring.analizaObrazu("obraz_zagrożenie");
		assertTrue(wynik);
	}

	@ParameterizedTest
	@Order(5)
	@ValueSource(strings = {"", "null", "test", "inne"})
	@DisplayName("AnalizaObrazu - Niepoprawne")
	@Tag("analiza")
	@Tag("parametryzowany")
	public void testAnalizaObrazu_Niepoprawne(String obraz) {
		boolean wynik = monitoring.analizaObrazu(obraz);
		assertFalse(wynik);
	}

	@Test
	@Order(6)
	@DisplayName("AnalizaObrazu - Null")
	@Tag("analiza")
	public void testAnalizaObrazu_Null() {
		boolean wynik = monitoring.analizaObrazu(null);
		assertFalse(wynik);
	}

	@Test
	@Order(7)
	@DisplayName("RozpocznijMonitoring - WykrycieZagrozenia")
	@Tag("monitoring")
	@Tag("zagrozenie")
	public void testRozpocznijMonitoring_WykrycieZagrozenia() {
		assertFalse(model.czyBankomatZablokowany());
		monitoring.rozpocznijMonitoring();
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(8)
	@DisplayName("ZatrzymajMonitoring")
	@Tag("monitoring")
	public void testZatrzymajMonitoring() {
		monitoring.rozpocznijMonitoring();
		monitoring.zatrzymajMonitoring();
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(9)
	@DisplayName("ObsluzZagrozenie")
	@Tag("zagrozenie")
	public void testObsluzZagrozenie() {
		assertFalse(model.czyBankomatZablokowany());
		monitoring.obsluzZagrozenie(1);
		assertTrue(model.czyBankomatZablokowany());
		assertFalse(monitoring.czyMonitoringAktywny());
	}

	@Test
	@Order(10)
	@DisplayName("CzyMonitoringAktywny")
	@Tag("monitoring")
	public void testCzyMonitoringAktywny() {
		assertFalse(monitoring.czyMonitoringAktywny());
		monitoring.rozpocznijMonitoring();
		assertDoesNotThrow(() -> monitoring.czyMonitoringAktywny());
	}
}
