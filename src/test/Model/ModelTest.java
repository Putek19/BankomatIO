package Model;

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

@DisplayName("ModelTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("fasada")
@Tag("model")
public class ModelTest {
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy Model");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy Model");
	}

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
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(model);
		assertFalse(model.czyBankomatZablokowany());
	}

	@Test
	@Order(2)
	@DisplayName("SprawdzSaldo - IstniejacaKarta")
	@Tag("saldo")
	public void testSprawdzSaldo_IstniejacaKarta() {
		BigDecimal saldo = model.sprawdzSaldo(100);
		assertEquals(new BigDecimal("1000.00"), saldo);
	}

	@Test
	@Order(3)
	@DisplayName("SprawdzSaldo - NieistniejacaKarta")
	@Tag("saldo")
	public void testSprawdzSaldo_NieistniejacaKarta() {
		BigDecimal saldo = model.sprawdzSaldo(999);
		assertEquals(BigDecimal.ZERO, saldo);
	}

	@Test
	@Order(4)
	@DisplayName("SprawdzPin - PoprawnyPin")
	@Tag("pin")
	public void testSprawdzPin_PoprawnyPin() {
		assertTrue(model.sprawdzPin(100, "1234"));
	}

	@Test
	@Order(5)
	@DisplayName("SprawdzPin - NiepoprawnyPin")
	@Tag("pin")
	public void testSprawdzPin_NiepoprawnyPin() {
		assertFalse(model.sprawdzPin(100, "0000"));
	}

	@Test
	@Order(6)
	@DisplayName("SprawdzPin - NieistniejacaKarta")
	@Tag("pin")
	public void testSprawdzPin_NieistniejacaKarta() {
		assertFalse(model.sprawdzPin(999, "1234"));
	}

	@Test
	@Order(7)
	@DisplayName("AktualizujSaldo")
	@Tag("saldo")
	public void testAktualizujSaldo() {
		BigDecimal kwota = new BigDecimal("-100.00");
		model.aktualizujSaldo(100, kwota);
		BigDecimal noweSaldo = model.sprawdzSaldo(100);
		assertEquals(new BigDecimal("900.00"), noweSaldo);
	}

	@Test
	@Order(8)
	@DisplayName("AktualizujSaldo - NieistniejacaKarta")
	@Tag("saldo")
	public void testAktualizujSaldo_NieistniejacaKarta() {
		BigDecimal kwota = new BigDecimal("100.00");
		assertDoesNotThrow(() -> model.aktualizujSaldo(999, kwota));
	}

	@Test
	@Order(9)
	@DisplayName("ZablokujKarte")
	@Tag("blokada")
	public void testZablokujKarte() {
		assertFalse(karta.czyZablokowana());
		model.zablokujKarte(100);
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(10)
	@DisplayName("ZablokujKarte - NieistniejacaKarta")
	@Tag("blokada")
	public void testZablokujKarte_NieistniejacaKarta() {
		assertDoesNotThrow(() -> model.zablokujKarte(999));
	}

	@Test
	@Order(11)
	@DisplayName("ZablokujBankomat")
	@Tag("blokada")
	@Tag("bezpieczenstwo")
	public void testZablokujBankomat() {
		assertFalse(model.czyBankomatZablokowany());
		model.zablokujBankomat();
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(12)
	@DisplayName("ZarejestrujZdarzenie")
	@Tag("rejestr")
	public void testZarejestrujZdarzenie() {
		String zdarzenie = "Test zdarzenie";
		assertDoesNotThrow(() -> model.zarejestrujZdarzenie(zdarzenie));
	}

	@Test
	@Order(13)
	@DisplayName("PobierzDaneKarty")
	@Tag("getter")
	public void testPobierzDaneKarty() {
		String dane = model.pobierzDaneKarty(100);
		assertNotNull(dane);
		assertTrue(dane.contains("100"));
		assertTrue(dane.contains("1000.00"));
	}

	@Test
	@Order(14)
	@DisplayName("PobierzDaneKarty - NieistniejacaKarta")
	@Tag("getter")
	public void testPobierzDaneKarty_NieistniejacaKarta() {
		String dane = model.pobierzDaneKarty(999);
		assertNull(dane);
	}

	@Test
	@Order(15)
	@DisplayName("UsuniecieKlienta")
	@Tag("klient")
	public void testUsuniecieKlienta() {
		assertNotNull(inwentarz.dajKlienta(1));
		model.usuniecieKlienta(1);
		assertNull(inwentarz.dajKlienta(1));
	}

	@Test
	@Order(16)
	@DisplayName("UsuniecieKarty")
	@Tag("karta")
	public void testUsuniecieKarty() {
		assertDoesNotThrow(() -> model.usuniecieKarty(100));
	}
}
