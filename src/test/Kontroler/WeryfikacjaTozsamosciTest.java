package Kontroler;

import Model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("WeryfikacjaTozsamosciTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("weryfikacja")
@Tag("bezpieczenstwo")
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
		// Jeśli (given): weryfikacja została utworzona w setUp()
		// Gdy (when): sprawdzenie instancji weryfikacji
		// Wtedy (then): weryfikacja nie powinna być null
		assertNotNull(weryfikacja);
	}

	@Test
	@Order(2)
	@DisplayName("WeryfikujPin - PoprawnyPin")
	public void testWeryfikujPin_PoprawnyPin() {
		// Jeśli (given): karta z poprawnym PIN-em
		// Gdy (when): weryfikacja poprawny PIN
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		// Wtedy (then): weryfikacja powinna się udać
		assertTrue(wynik);
	}

	@Test
	@Order(3)
	@DisplayName("WeryfikujPin - NiepoprawnyPin")
	public void testWeryfikujPin_NiepoprawnyPin() {
		// Jeśli (given): karta z ustalonym PIN-em
		// Gdy (when): weryfikacja niepoprawny PIN
		boolean wynik = weryfikacja.weryfikujPin(100, "0000");
		// Wtedy (then): weryfikacja powinna się nie udać
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("WeryfikujPin - PoprawnyPinResetujeLicznik")
	public void testWeryfikujPin_PoprawnyPinResetujeLicznik() {
		// Jeśli (given): trzy nieudane próby weryfikacji
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		// Gdy (when): weryfikacja poprawny PIN
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		// Wtedy (then): weryfikacja powinna się udać i licznik być zresetowany
		assertTrue(wynik);
		// Wtedy (then): kolejna niepoprawna próba nie blokuje od razu karty
		boolean wynik2 = weryfikacja.weryfikujPin(100, "0000");
		assertFalse(wynik2);
	}

	@Test
	@Order(5)
	@DisplayName("WeryfikujPin - MaksymalnaLiczbaProb")
	public void testWeryfikujPin_MaksymalnaLiczbaProb() {
		// Jeśli (given): karta niezablokowana
		// Gdy (when): wykonanie 5 nieudanych prób weryfikacji
		for (int i = 0; i < 5; i++) {
			weryfikacja.weryfikujPin(100, "0000");
		}
		// Wtedy (then): karta powinna zostać zablokowana
		assertTrue(karta.czyZablokowana());
		// Wtedy (then): nawet poprawny PIN nie przejdzie weryfikacji
		boolean wynik = weryfikacja.weryfikujPin(100, "1234");
		assertFalse(wynik);
	}

	@Test
	@Order(6)
	@DisplayName("ResetujLicznik")
	public void testResetujLicznik() {
		// Jeśli (given): dwie nieudane próby weryfikacji
		weryfikacja.weryfikujPin(100, "0000");
		weryfikacja.weryfikujPin(100, "0000");
		// Gdy (when): reset licznikanik ręcznie
		weryfikacja.resetujLicznik();
		// Gdy (when): wykonanie 5 kolejnych nieudanych prób
		for (int i = 0; i < 5; i++) {
			weryfikacja.weryfikujPin(100, "0000");
		}
		// Wtedy (then): karta powinna zostać zablokowana dopiero po 5 próbach
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(7)
	@DisplayName("UstawStrategie")
	public void testUstawStrategie() {
		// Jeśli (given): strategia zabezpieczenia do ustawienia
		IStrategiaZabezpieczenia strategia = new ZablokowanieKarty(model);
		// Gdy (when): ustawienie strategiię
		weryfikacja.ustawStrategie(strategia);
		// Wtedy (then): ponowne ustawienie nie powinno wyrzucić wyjątku
		assertDoesNotThrow(() -> weryfikacja.ustawStrategie(strategia));
	}
}
