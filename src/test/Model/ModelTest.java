package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("ModelTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
		assertNotNull(model);
		assertFalse(model.czyBankomatZablokowany());
	}

	@Test
	@Order(2)
	@DisplayName("SprawdzSaldo - IstniejacaKarta")
	public void testSprawdzSaldo_IstniejacaKarta() {
		BigDecimal saldo = model.sprawdzSaldo(100);
		assertEquals(new BigDecimal("1000.00"), saldo);
	}

	@Test
	@Order(3)
	@DisplayName("SprawdzSaldo - NieistniejacaKarta")
	public void testSprawdzSaldo_NieistniejacaKarta() {
		BigDecimal saldo = model.sprawdzSaldo(999);
		assertEquals(BigDecimal.ZERO, saldo);
	}

	@Test
	@Order(4)
	@DisplayName("SprawdzPin - PoprawnyPin")
	public void testSprawdzPin_PoprawnyPin() {
		assertTrue(model.sprawdzPin(100, "1234"));
	}

	@Test
	@Order(5)
	@DisplayName("SprawdzPin - NiepoprawnyPin")
	public void testSprawdzPin_NiepoprawnyPin() {
		assertFalse(model.sprawdzPin(100, "0000"));
	}

	@Test
	@Order(6)
	@DisplayName("SprawdzPin - NieistniejacaKarta")
	public void testSprawdzPin_NieistniejacaKarta() {
		assertFalse(model.sprawdzPin(999, "1234"));
	}

	@Test
	@Order(7)
	@DisplayName("AktualizujSaldo")
	public void testAktualizujSaldo() {
		BigDecimal kwota = new BigDecimal("-100.00");
		model.aktualizujSaldo(100, kwota);
		BigDecimal noweSaldo = model.sprawdzSaldo(100);
		assertEquals(new BigDecimal("900.00"), noweSaldo);
	}

	@Test
	@Order(8)
	@DisplayName("AktualizujSaldo - NieistniejacaKarta")
	public void testAktualizujSaldo_NieistniejacaKarta() {
		BigDecimal kwota = new BigDecimal("100.00");
		assertDoesNotThrow(() -> model.aktualizujSaldo(999, kwota));
	}

	@Test
	@Order(9)
	@DisplayName("ZablokujKarte")
	public void testZablokujKarte() {
		assertFalse(karta.czyZablokowana());
		model.zablokujKarte(100);
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(10)
	@DisplayName("ZablokujKarte - NieistniejacaKarta")
	public void testZablokujKarte_NieistniejacaKarta() {
		assertDoesNotThrow(() -> model.zablokujKarte(999));
	}

	@Test
	@Order(11)
	@DisplayName("ZablokujBankomat")
	public void testZablokujBankomat() {
		assertFalse(model.czyBankomatZablokowany());
		model.zablokujBankomat();
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(12)
	@DisplayName("ZarejestrujZdarzenie")
	public void testZarejestrujZdarzenie() {
		String zdarzenie = "Test zdarzenie";
		assertDoesNotThrow(() -> model.zarejestrujZdarzenie(zdarzenie));
	}

	@Test
	@Order(13)
	@DisplayName("PobierzDaneKarty")
	public void testPobierzDaneKarty() {
		String dane = model.pobierzDaneKarty(100);
		assertNotNull(dane);
		assertTrue(dane.contains("100"));
		assertTrue(dane.contains("1000.00"));
	}

	@Test
	@Order(14)
	@DisplayName("PobierzDaneKarty - NieistniejacaKarta")
	public void testPobierzDaneKarty_NieistniejacaKarta() {
		String dane = model.pobierzDaneKarty(999);
		assertNull(dane);
	}

	@Test
	@Order(15)
	@DisplayName("UsuniecieKlienta")
	public void testUsuniecieKlienta() {
		assertNotNull(inwentarz.dajKlienta(1));
		model.usuniecieKlienta(1);
		assertNull(inwentarz.dajKlienta(1));
	}

	@Test
	@Order(16)
	@DisplayName("UsuniecieKarty")
	public void testUsuniecieKarty() {
		assertDoesNotThrow(() -> model.usuniecieKarty(100));
	}
}
