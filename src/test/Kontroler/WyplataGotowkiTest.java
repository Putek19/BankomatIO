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
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("WyplataGotowkiTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("wyplata")
public class WyplataGotowkiTest {
	private WyplataGotowki wyplata;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy WyplataGotowki");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy WyplataGotowki");
	}

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
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(wyplata);
		assertEquals(0.0, wyplata.dajKwote(), 0.001);
	}

	@Test
	@Order(2)
	@DisplayName("UstawKwote")
	@Tag("kwota")
	public void testUstawKwote() {
		double kwota = 150.0;
		wyplata.ustawKwote(kwota);
		assertEquals(kwota, wyplata.dajKwote(), 0.001);
	}

	@Test
	@Order(3)
	@DisplayName("RealizujWyplate - PoprawneDane")
	@Tag("realizacja")
	public void testRealizujWyplate_PoprawneDane() {
		double kwota = 150.0;
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertTrue(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwaneSaldo = saldoPrzed.subtract(new BigDecimal(kwota));
		assertEquals(oczekiwaneSaldo, saldoPo);
	}

	@Test
	@Order(4)
	@DisplayName("RealizujWyplate - NiewystarczajaceSaldo")
	@Tag("realizacja")
	@Tag("saldo")
	public void testRealizujWyplate_NiewystarczajaceSaldo() {
		double kwota = 2000.0;
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(5)
	@DisplayName("RealizujWyplate - KwotaZero")
	@Tag("realizacja")
	@Tag("walidacja")
	public void testRealizujWyplate_KwotaZero() {
		wyplata.ustawKwote(0.0);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(6)
	@DisplayName("RealizujWyplate - KwotaUjemna")
	@Tag("realizacja")
	@Tag("walidacja")
	public void testRealizujWyplate_KwotaUjemna() {
		wyplata.ustawKwote(-100.0);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@ParameterizedTest
	@Order(7)
	@CsvSource({"5000.0,10000.00,5000.00", "3000.0,5000.00,2000.00", "1000.0,2000.00,1000.00"})
	@DisplayName("RealizujWyplate - MaksymalnaKwota")
	@Tag("realizacja")
	@Tag("limit")
	@Tag("parametryzowany")
	public void testRealizujWyplate_MaksymalnaKwota(double kwota, String saldoStr, String oczekiwaneStr) {
		BigDecimal saldoPrzed = new BigDecimal(saldoStr);
		karta.zmienSaldo(saldoPrzed.subtract(new BigDecimal("1000.00")));
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzedTest = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertTrue(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		BigDecimal oczekiwane = new BigDecimal(oczekiwaneStr);
		assertEquals(oczekiwane, saldoPo);
	}

	@Test
	@Order(8)
	@DisplayName("RealizujWyplate - PrzekroczonaMaksymalnaKwota")
	@Tag("realizacja")
	@Tag("limit")
	public void testRealizujWyplate_PrzekroczonaMaksymalnaKwota() {
		double kwota = 6000.0;
		karta.zmienSaldo(new BigDecimal("9000.00"));
		wyplata.ustawKwote(kwota);
		BigDecimal saldoPrzed = model.sprawdzSaldo(100);
		boolean wynik = wyplata.realizujWyplate(100);
		assertFalse(wynik);
		BigDecimal saldoPo = model.sprawdzSaldo(100);
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(9)
	@DisplayName("RealizujWyplate - NieistniejacaKarta")
	@Tag("realizacja")
	public void testRealizujWyplate_NieistniejacaKarta() {
		wyplata.ustawKwote(100.0);
		boolean wynik = wyplata.realizujWyplate(999);
		assertFalse(wynik);
	}
}
