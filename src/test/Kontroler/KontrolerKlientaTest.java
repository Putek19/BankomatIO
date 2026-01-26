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

@DisplayName("KontrolerKlientaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("klient")
public class KontrolerKlientaTest {
	private KontrolerKlienta kontroler;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy KontrolerKlienta");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy KontrolerKlienta");
	}

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		kontroler = new KontrolerKlienta(model);
		klient = new Klient(1, "Jan");
		karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
	}

	@AfterEach
	public void tearDown() {
		kontroler = null;
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
		assertNotNull(kontroler);
	}

	@Test
	@Order(2)
	@DisplayName("WeryfikacjaTozsamosci - PoprawnyPin")
	@Tag("weryfikacja")
	@Tag("pin")
	public void testWeryfikacjaTozsamosci_PoprawnyPin() {
		boolean wynik = kontroler.weryfikacjaTozsamosci(100, "1234");
		assertTrue(wynik);
	}

	@Test
	@Order(3)
	@DisplayName("WeryfikacjaTozsamosci - NiepoprawnyPin")
	@Tag("weryfikacja")
	@Tag("pin")
	public void testWeryfikacjaTozsamosci_NiepoprawnyPin() {
		boolean wynik = kontroler.weryfikacjaTozsamosci(100, "0000");
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("WeryfikacjaTozsamosci - NieistniejacaKarta")
	@Tag("weryfikacja")
	public void testWeryfikacjaTozsamosci_NieistniejacaKarta() {
		boolean wynik = kontroler.weryfikacjaTozsamosci(999, "1234");
		assertFalse(wynik);
	}

	@Test
	@Order(5)
	@DisplayName("WyplataGotowki - PoprawneDane")
	@Tag("wyplata")
	public void testWyplataGotowki_PoprawneDane() {
		double kwota = 150.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		kontroler.wyplataGotowki("100", "1234", kwota);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(6)
	@DisplayName("WyplataGotowki - NiepoprawnyPin")
	@Tag("wyplata")
	@Tag("pin")
	public void testWyplataGotowki_NiepoprawnyPin() {
		double kwota = 150.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		kontroler.wyplataGotowki("100", "0000", kwota);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(7)
	@DisplayName("WyplataGotowki - NiepoprawnyNumerKarty")
	@Tag("wyplata")
	public void testWyplataGotowki_NiepoprawnyNumerKarty() {
		assertDoesNotThrow(() -> kontroler.wyplataGotowki("abc", "1234", 100.0));
	}

	@Test
	@Order(8)
	@DisplayName("WyplataGotowki - NiewystarczajaceSaldo")
	@Tag("wyplata")
	@Tag("saldo")
	public void testWyplataGotowki_NiewystarczajaceSaldo() {
		double kwota = 2000.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		kontroler.wyplataGotowki("100", "1234", kwota);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(9)
	@DisplayName("WyplataGotowki - MaksymalnaKwota")
	@Tag("wyplata")
	@Tag("limit")
	public void testWyplataGotowki_MaksymalnaKwota() {
		double kwota = 5000.0;
		BigDecimal saldoPrzed = new BigDecimal("10000.00");
		karta.zmienSaldo(new BigDecimal("9000.00"));
		kontroler.wyplataGotowki("100", "1234", kwota);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(10)
	@DisplayName("WyplataGotowki - PrzekroczonaMaksymalnaKwota")
	@Tag("wyplata")
	@Tag("limit")
	public void testWyplataGotowki_PrzekroczonaMaksymalnaKwota() {
		double kwota = 6000.0;
		BigDecimal saldoPrzed = new BigDecimal("10000.00");
		karta.zmienSaldo(new BigDecimal("9000.00"));
		kontroler.wyplataGotowki("100", "1234", kwota);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(11)
	@DisplayName("SprawdzenieStanuKonta")
	@Tag("konto")
	public void testSprawdzenieStanuKonta() {
		assertDoesNotThrow(() -> kontroler.sprawdzenieStanuKonta());
	}

	@Test
	@Order(12)
	@DisplayName("WyplataGotowki - BezParametrow")
	@Tag("wyplata")
	public void testWyplataGotowki_BezParametrow() {
		assertDoesNotThrow(() -> kontroler.wyplataGotowki());
	}
}
