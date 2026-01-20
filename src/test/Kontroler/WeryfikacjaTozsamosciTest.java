package Kontroler;

import Model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("WeryfikacjaTozsamosciTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WeryfikacjaTozsamosciTest {
	private WeryfikacjaTozsamosci weryfikacja;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

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
	public void testKonstruktor() {
		assertNotNull(weryfikacja);
	}

	@Test
	@Order(2)
	@DisplayName("WeryfikujPin - PoprawnyPin")
	public void testWeryfikujPin_PoprawnyPin() {
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		assertTrue(wynik);
	}

	@Test
	@Order(3)
	@DisplayName("WeryfikujPin - NiepoprawnyPin")
	public void testWeryfikujPin_NiepoprawnyPin() {
		boolean wynik = weryfikacja.weryfikujPin(100, "0000");
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("WeryfikujPin - PoprawnyPinResetujeLicznik")
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
	public void testUstawStrategie() {
		IStrategiaZabezpieczenia strategia = new ZablokowanieKarty(model);
		weryfikacja.ustawStrategie(strategia);
		assertDoesNotThrow(() -> weryfikacja.ustawStrategie(strategia));
	}
}
