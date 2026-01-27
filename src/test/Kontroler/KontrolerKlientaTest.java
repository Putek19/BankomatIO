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
	public void testKonstruktor() {
		// Jeśli (given): kontroler został utworzony w setUp()
		// Gdy (when): sprawdzamy instancję kontrolera
		// Wtedy (then): kontroler nie powinien być null
		assertNotNull(kontroler);
	}

	@Test
	@Order(2)
	@DisplayName("WeryfikacjaTozsamosci - PoprawnyPin")
	public void testWeryfikacjaTozsamosci_PoprawnyPin() {
		// Jeśli (given): karta z poprawnym PIN-em
		// Gdy (when): weryfikujemy tożsamość z poprawnym PIN-em
		boolean wynik = kontroler.weryfikacjaTozsamosci(100, "1234");
		// Wtedy (then): weryfikacja powinna się udać
		assertTrue(wynik);
	}

	@Test
	@Order(3)
	@DisplayName("WeryfikacjaTozsamosci - NiepoprawnyPin")
	public void testWeryfikacjaTozsamosci_NiepoprawnyPin() {
		// Jeśli (given): karta z ustalonym PIN-em
		// Gdy (when): weryfikujemy tożsamość z niepoprawnym PIN-em
		boolean wynik = kontroler.weryfikacjaTozsamosci(100, "0000");
		// Wtedy (then): weryfikacja powinna się nie udać
		assertFalse(wynik);
	}

	@Test
	@Order(4)
	@DisplayName("WeryfikacjaTozsamosci - NieistniejacaKarta")
	public void testWeryfikacjaTozsamosci_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): próbujemy weryfikować tożsamość nieistniejącej karty
		boolean wynik = kontroler.weryfikacjaTozsamosci(999, "1234");
		// Wtedy (then): weryfikacja powinna się nie udać
		assertFalse(wynik);
	}

	@Test
	@Order(5)
	@DisplayName("WyplataGotowki - PoprawneDane")
	public void testWyplataGotowki_PoprawneDane() {
		// Jeśli (given): karta z wystarczającym saldem i poprawne dane
		double kwota = 150.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): realizujemy wypłatę gotówki
		kontroler.wyplataGotowki("100", "1234", kwota);
		// Wtedy (then): saldo powinno zostać zmniejszone o kwotę wypłaty
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(6)
	@DisplayName("WyplataGotowki - NiepoprawnyPin")
	public void testWyplataGotowki_NiepoprawnyPin() {
		// Jeśli (given): karta z ustalonym PIN-em i niepoprawny PIN
		double kwota = 150.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próbujemy zrealizować wypłatę z niepoprawnym PIN-em
		kontroler.wyplataGotowki("100", "0000", kwota);
		// Wtedy (then): saldo nie powinno się zmienić
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(7)
	@DisplayName("WyplataGotowki - NiepoprawnyNumerKarty")
	public void testWyplataGotowki_NiepoprawnyNumerKarty() {
		// Jeśli (given): niepoprawny numer karty (tekst zamiast liczby)
		// Gdy (when): próbujemy zrealizować wypłatę
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> kontroler.wyplataGotowki("abc", "1234", 100.0));
	}

	@Test
	@Order(8)
	@DisplayName("WyplataGotowki - NiewystarczajaceSaldo")
	public void testWyplataGotowki_NiewystarczajaceSaldo() {
		// Jeśli (given): karta z niewystarczającym saldem
		double kwota = 2000.0;
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próbujemy zrealizować wypłatę
		kontroler.wyplataGotowki("100", "1234", kwota);
		// Wtedy (then): saldo nie powinno się zmienić
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(9)
	@DisplayName("WyplataGotowki - MaksymalnaKwota")
	public void testWyplataGotowki_MaksymalnaKwota() {
		// Jeśli (given): karta z wysokim saldem i maksymalna kwota wypłaty
		double kwota = 5000.0;
		BigDecimal saldoPrzed = new BigDecimal("10000.00");
		karta.zmienSaldo(new BigDecimal("9000.00"));
		// Gdy (when): realizujemy wypłatę maksymalnej kwoty
		kontroler.wyplataGotowki("100", "1234", kwota);
		// Wtedy (then): saldo powinno być pomniejszone o maksymalną kwotę
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(10)
	@DisplayName("WyplataGotowki - PrzekroczonaMaksymalnaKwota")
	public void testWyplataGotowki_PrzekroczonaMaksymalnaKwota() {
		// Jeśli (given): kwota przekraczająca maksymalną dozwoloną kwotę
		double kwota = 6000.0;
		BigDecimal saldoPrzed = new BigDecimal("10000.00");
		karta.zmienSaldo(new BigDecimal("9000.00"));
		// Gdy (when): próbujemy zrealizować wypłatę
		kontroler.wyplataGotowki("100", "1234", kwota);
		// Wtedy (then): saldo nie powinno się zmienić
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(11)
	@DisplayName("SprawdzenieStanuKonta")
	public void testSprawdzenieStanuKonta() {
		// Jeśli (given): kontroler klienta z dostępem do konta
		// Gdy (when): sprawdzamy stan konta
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> kontroler.sprawdzenieStanuKonta());
	}

	@Test
	@Order(12)
	@DisplayName("WyplataGotowki - BezParametrow")
	public void testWyplataGotowki_BezParametrow() {
		// Jeśli (given): kontroler klienta
		// Gdy (when): wywołujemy wypłatę gotówki bez parametrów
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> kontroler.wyplataGotowki());
	}
}
