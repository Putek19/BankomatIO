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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("WyplataGotowkiTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("wyplata")
@Tag("saldo")
public class WyplataGotowkiTest {
	private WyplataGotowki wyplata;
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
		wyplata = new WyplataGotowki(model);
		klient = new Klient(1, "Jan");
		karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
	}

	@AfterEach
	public void tearDown() {
		wyplata = null;
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
		// Jeśli (given): wypłata została utworzona w setUp()
		// Gdy (when): sprawdzenie stanu wypłaty
		// Wtedy (then): wypłata powinna być zainicjalizowana z kwotą 0
		assertNotNull(wyplata);
		assertEquals(0.0, wyplata.dajKwote(), 0.001);
	}

	@Test
	@Order(2)
	@DisplayName("UstawKwote")
	public void testUstawKwote() {
		// Jeśli (given): kwota do ustawienia
		double kwota = 150.0;
		// Gdy (when): ustawienie kwoty wypłaty
		wyplata.ustawKwote(kwota);
		// Wtedy (then): kwota powinna być ustawiona poprawnie
		assertEquals(kwota, wyplata.dajKwote(), 0.001);
	}

	@Test
	@Order(3)
	@DisplayName("RealizujWyplate - PoprawneDane")
	public void testRealizujWyplate_PoprawneDane() {
		// Jeśli (given): karta z wystarczającym saldem i kwota do wypłaty
		double kwota = 150.0;
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): realizacja wypłaty
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się udać i saldo powinno być zmniejszone
		assertTrue(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(4)
	@DisplayName("RealizujWyplate - NiewystarczajaceSaldo")
	public void testRealizujWyplate_NiewystarczajaceSaldo() {
		// Jeśli (given): karta z niewystarczającym saldem
		double kwota = 2000.0;
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próba zrealizowaniać wypłatę
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się nie udać, saldo bez zmian
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(5)
	@DisplayName("RealizujWyplate - KwotaZero")
	public void testRealizujWyplate_KwotaZero() {
		// Jeśli (given): kwota wypłaty ustawiona na zero
		wyplata.ustawKwote(0.0);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próba zrealizowaniać wypłatę
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się nie udać, saldo bez zmian
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(6)
	@DisplayName("RealizujWyplate - KwotaUjemna")
	public void testRealizujWyplate_KwotaUjemna() {
		// Jeśli (given): ujemna kwota wypłaty
		wyplata.ustawKwote(-100.0);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próba zrealizowaniać wypłatę
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się nie udać, saldo bez zmian
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@ParameterizedTest
	@Order(7)
	@CsvSource({ "5000.0,10000.00,5000.00", "3000.0,5000.00,2000.00", "1000.0,2000.00,1000.00" })
	@DisplayName("RealizujWyplate - MaksymalnaKwota")
	public void testRealizujWyplate_MaksymalnaKwota(double kwota, String saldoStr, String oczekiwaneStr) {
		// Jeśli (given): karta z wysokim saldem i maksymalne kwoty wypłaty
		BigDecimal saldoPrzed = new BigDecimal(saldoStr);
		karta.zmienSaldo(saldoPrzed.subtract(new BigDecimal("1000.00")));
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzedTest = model.sprawdzSaldo(100);
		// Gdy (when): realizacja wypłaty maksymalnej kwoty
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się udać i saldo być zgodne z oczekiwanym
		assertTrue(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwane = new BigDecimal(oczekiwaneStr);
		assertEquals(oczekiwane, saldoPo);
	}

	@Test
	@Order(8)
	@DisplayName("RealizujWyplate - PrzekroczonaMaksymalnaKwota")
	public void testRealizujWyplate_PrzekroczonaMaksymalnaKwota() {
		// Jeśli (given): kwota przekraczająca maksymalną dozwoloną kwotę
		double kwota = 6000.0;
		karta.zmienSaldo(new BigDecimal("9000.00"));
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		// Gdy (when): próba zrealizowaniać wypłatę
		boolean wynik = wyplata.realizujWyplate(100);
		// Wtedy (then): wypłata powinna się nie udać, saldo bez zmian
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(9)
	@DisplayName("RealizujWyplate - NieistniejacaKarta")
	public void testRealizujWyplate_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		wyplata.ustawKwote(100.0);
		// Gdy (when): próba zrealizowaniać wypłatę
		boolean wynik = wyplata.realizujWyplate(999);
		// Wtedy (then): wypłata powinna się nie udać
		assertFalse(wynik);
	}
}
