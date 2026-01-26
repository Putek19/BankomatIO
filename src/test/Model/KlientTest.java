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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("KlientTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("encja")
@Tag("model")
public class KlientTest {
	private Klient klient;
	private static final int NR_KLIENTA = 1;
	private static final String IMIE = "Jan";
	private static final String NAZWISKO = "Kowalski";
	private static final int PESEL = 123456789;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy Klient");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy Klient");
	}

	@BeforeEach
	public void setUp() {
		klient = new Klient(NR_KLIENTA, IMIE);
		klient.ustawNazwisko(NAZWISKO);
		klient.ustawPesel(PESEL);
	}

	@AfterEach
	public void tearDown() {
		klient = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(klient);
		assertEquals(NR_KLIENTA, klient.dajNrKlienta());
		assertEquals(IMIE, klient.dajImie());
	}

	@Test
	@Order(2)
	@DisplayName("UstawNazwisko")
	@Tag("setter")
	public void testUstawNazwisko() {
		String noweNazwisko = "Nowak";
		klient.ustawNazwisko(noweNazwisko);
		assertEquals(noweNazwisko, klient.dajNazwisko());
	}

	@Test
	@Order(3)
	@DisplayName("UstawPesel")
	@Tag("setter")
	public void testUstawPesel() {
		int nowyPesel = 987654321;
		klient.ustawPesel(nowyPesel);
		assertEquals(nowyPesel, klient.dajPesel());
	}

	@Test
	@Order(4)
	@DisplayName("DodajKarte")
	@Tag("karta")
	public void testDodajKarte() {
		IKarta karta = new Karta(1, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		IKarta pobranaKarta = klient.pobierzKarte(1);
		assertNotNull(pobranaKarta);
		assertEquals(1, pobranaKarta.dajId());
	}

	@Test
	@Order(5)
	@DisplayName("DodajKarte - Null")
	@Tag("karta")
	public void testDodajKarte_Null() {
		klient.dodajKarte(null);
		assertNull(klient.pobierzKarte(999));
	}

	@Test
	@Order(6)
	@DisplayName("PobierzKarte - IstniejacaKarta")
	@Tag("karta")
	public void testPobierzKarte_IstniejacaKarta() {
		IKarta karta1 = new Karta(1, "1234", new BigDecimal("1000.00"));
		IKarta karta2 = new Karta(2, "5678", new BigDecimal("2000.00"));
		klient.dodajKarte(karta1);
		klient.dodajKarte(karta2);
		IKarta pobrana = klient.pobierzKarte(2);
		assertNotNull(pobrana);
		assertEquals(2, pobrana.dajId());
	}

	@Test
	@Order(7)
	@DisplayName("PobierzKarte - NieistniejacaKarta")
	@Tag("karta")
	public void testPobierzKarte_NieistniejacaKarta() {
		IKarta pobrana = klient.pobierzKarte(999);
		assertNull(pobrana);
	}

	@ParameterizedTest
	@Order(8)
	@CsvSource({"1,1234,1000.00", "2,5678,2000.00", "3,9012,3000.00"})
	@DisplayName("WielokrotneKarty")
	@Tag("karta")
	@Tag("parametryzowany")
	public void testWielokrotneKarty(int idKarty, String pin, String saldo) {
		IKarta karta = new Karta(idKarty, pin, new BigDecimal(saldo));
		klient.dodajKarte(karta);
		IKarta pobrana = klient.pobierzKarte(idKarty);
		assertNotNull(pobrana);
		assertEquals(idKarty, pobrana.dajId());
	}
}
