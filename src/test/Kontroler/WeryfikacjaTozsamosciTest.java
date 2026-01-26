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
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("WeryfikacjaTozsamosciTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("weryfikacja")
public class WeryfikacjaTozsamosciTest {
	private WeryfikacjaTozsamosci weryfikacja;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy WeryfikacjaTozsamosci");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy WeryfikacjaTozsamosci");
	}

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		weryfikacja = new WeryfikacjaTozsamosci(model);
		weryfikacja.ustawStrategie(new ZablokowanieKarty(model));
		klient = new Klient(1, "Jan");
		karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
	}

	@AfterEach
	public void tearDown() {
		weryfikacja = null;
		model = null;
		inwentarz = null;
		dao = null;
		klient = null;
		karta = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(weryfikacja);
	}

	@Test
	@Order(2)
	@DisplayName("WeryfikujPin - PoprawnyPin")
	@Tag("pin")
	public void testWeryfikujPin_PoprawnyPin() {
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		assertTrue(wynik);
	}

	@Test
	@Order(3)
	@DisplayName("WeryfikujPin - NiepoprawnyPin")
	@Tag("pin")
	public void testWeryfikujPin_NiepoprawnyPin() {
		boolean wynik = weryfikacja.weryfikujPin(100, "0000");
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("WeryfikujPin - PoprawnyPinResetujeLicznik")
	@Tag("pin")
	@Tag("licznik")
	public void testWeryfikujPin_PoprawnyPinResetujeLicznik() {
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		assertTrue(wynik);
		boolean wynik2 = weryfikacja.weryfikujPin(100, "0000");
		assertFalse(wynik2);
	}

	@Test
	@Order(5)
	@DisplayName("WeryfikujPin - MaksymalnaLiczbaProb")
	@Tag("pin")
	@Tag("blokada")
	public void testWeryfikujPin_MaksymalnaLiczbaProb() {
		for (int i = 0; i < 5; i++) {
			weryfikacja.weryfikujPin(100, "0000");
		}
		assertTrue(karta.czyZablokowana());
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		assertFalse(wynik);
	}

	@Test
	@Order(6)
	@DisplayName("ResetujLicznik")
	@Tag("licznik")
	public void testResetujLicznik() {
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.resetujLicznik();
		for (int i = 0; i < 5; i++) {
			weryfikacja.weryfikujPin(100, "0000");
		}
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(7)
	@DisplayName("UstawStrategie")
	@Tag("strategia")
	public void testUstawStrategie() {
		IStrategiaZabezpieczenia strategia = new ZablokowanieKarty(model);
		assertDoesNotThrow(() -> weryfikacja.ustawStrategie(strategia));
	}
}
