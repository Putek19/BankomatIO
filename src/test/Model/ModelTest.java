package Model;

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

@DisplayName("ModelTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("model")
@Tag("biznesowa")
public class ModelTest {
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
		klient = new Klient(1, "Jan");
		karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
	}

	@AfterEach
	public void tearDown() {
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
		// Jeśli (given): model został utworzony w setUp()
		// Gdy (when): sprawdzenie stanu modelu
		// Wtedy (then): model powinien być zainicjalizowany, bankomat niezablokowany
		assertNotNull(model);
		assertFalse(model.czyBankomatZablokowany());
	}

	@Test
	@Order(2)
	@DisplayName("SprawdzSaldo - IstniejacaKarta")
	public void testSprawdzSaldo_IstniejacaKarta() {
		// Jeśli (given): karta z saldem w systemie
		// Gdy (when): sprawdzenie salda karty
		BigDecimal saldo = model.sprawdzSaldo(100);
		// Wtedy (then): saldo powinno być zgodne z oczekiwanym
		assertEquals(new BigDecimal("1000.00"), saldo);
	}

	@Test
	@Order(3)
	@DisplayName("SprawdzSaldo - NieistniejacaKarta")
	public void testSprawdzSaldo_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): sprawdzenie salda nieistniejącej karty
		BigDecimal saldo = model.sprawdzSaldo(999);
		// Wtedy (then): saldo powinno być zero
		assertEquals(BigDecimal.ZERO, saldo);
	}

	@Test
	@Order(4)
	@DisplayName("SprawdzPin - PoprawnyPin")
	public void testSprawdzPin_PoprawnyPin() {
		// Jeśli (given): karta z poprawnym PIN-em
		// Gdy (when): sprawdzenie poprawny PIN
		// Wtedy (then): sprawdzenie powinno zwrócić true
		assertTrue(model.sprawdzPin(100, "1234"));
	}

	@Test
	@Order(5)
	@DisplayName("SprawdzPin - NiepoprawnyPin")
	public void testSprawdzPin_NiepoprawnyPin() {
		// Jeśli (given): karta z ustalonym PIN-em
		// Gdy (when): sprawdzenie niepoprawny PIN
		// Wtedy (then): sprawdzenie powinno zwrócić false
		assertFalse(model.sprawdzPin(100, "0000"));
	}

	@Test
	@Order(6)
	@DisplayName("SprawdzPin - NieistniejacaKarta")
	public void testSprawdzPin_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): sprawdzenie PIN-u nieistniejącej karty
		// Wtedy (then): sprawdzenie powinno zwrócić false
		assertFalse(model.sprawdzPin(999, "1234"));
	}

	@Test
	@Order(7)
	@DisplayName("AktualizujSaldo")
	public void testAktualizujSaldo() {
		// Jeśli (given): karta z początkowym saldem
		BigDecimal kwota = new BigDecimal("-100.00");
		// Gdy (when): aktualizacja saldao o ujemną kwotę
		model.aktualizujSaldo(100, kwota);
		// Wtedy (then): saldo powinno być zmniejszone
		BigDecimal noweSaldo = model.sprawdzSaldo(100);
		assertEquals(new BigDecimal("900.00"), noweSaldo);
	}

	@Test
	@Order(8)
	@DisplayName("AktualizujSaldo - NieistniejacaKarta")
	public void testAktualizujSaldo_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta i kwota do aktualizacji
		BigDecimal kwota = new BigDecimal("100.00");
		// Gdy (when): próba zaktualizować saldo nieistniejącej karty
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> model.aktualizujSaldo(999, kwota));
	}

	@Test
	@Order(9)
	@DisplayName("ZablokujKarte")
	public void testZablokujKarte() {
		// Jeśli (given): karta niezablokowana w systemie
		assertFalse(karta.czyZablokowana());
		// Gdy (when): zablokowanie kartyyę przez model
		model.zablokujKarte(100);
		// Wtedy (then): karta powinna być zablokowana
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(10)
	@DisplayName("ZablokujKarte - NieistniejacaKarta")
	public void testZablokujKarte_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): próba zablokować nieistniejącą kartę
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> model.zablokujKarte(999));
	}

	@Test
	@Order(11)
	@DisplayName("ZablokujBankomat")
	public void testZablokujBankomat() {
		// Jeśli (given): bankomat niezablokowany
		assertFalse(model.czyBankomatZablokowany());
		// Gdy (when): zablokowanie bankomatu
		model.zablokujBankomat();
		// Wtedy (then): bankomat powinien być zablokowany
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(12)
	@DisplayName("ZarejestrujZdarzenie")
	public void testZarejestrujZdarzenie() {
		// Jeśli (given): zdarzenie do zarejestrowania
		String zdarzenie = "Test zdarzenie";
		// Gdy (when): rejestracja zdarzeniaie
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> model.zarejestrujZdarzenie(zdarzenie));
	}

	@Test
	@Order(13)
	@DisplayName("PobierzDaneKarty")
	public void testPobierzDaneKarty() {
		// Jeśli (given): karta z danymi w systemie
		// Gdy (when): pobranie danyche karty
		String dane = model.pobierzDaneKarty(100);
		// Wtedy (then): dane powinny zawierać ID i saldo karty
		assertNotNull(dane);
		assertTrue(dane.contains("100"));
		assertTrue(dane.contains("1000.00"));
	}

	@Test
	@Order(14)
	@DisplayName("PobierzDaneKarty - NieistniejacaKarta")
	public void testPobierzDaneKarty_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): próba pobrać dane nieistniejącej karty
		String dane = model.pobierzDaneKarty(999);
		// Wtedy (then): wynik powinien być null
		assertNull(dane);
	}

	@Test
	@Order(15)
	@DisplayName("UsuniecieKlienta")
	public void testUsuniecieKlienta() {
		// Jeśli (given): klient w systemie
		assertNotNull(inwentarz.dajKlienta(1));
		// Gdy (when): usunięcie klienta
		model.usuniecieKlienta(1);
		// Wtedy (then): klient nie powinien być dostępny
		assertNull(inwentarz.dajKlienta(1));
	}

	@Test
	@Order(16)
	@DisplayName("UsuniecieKarty")
	public void testUsuniecieKarty() {
		// Jeśli (given): karta w systemie
		// Gdy (when): usunięcie kartyę
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> model.usuniecieKarty(100));
	}
}
