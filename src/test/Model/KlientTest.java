package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("KlientTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KlientTest {
	private Klient klient;
	private static final int NR_KLIENTA = 1;
	private static final String IMIE = "Jan";
	private static final String NAZWISKO = "Kowalski";
	private static final int PESEL = 123456789;

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
	public void testKonstruktor() {
		// Jeśli (given): klient został utworzony w setUp()
		// Gdy (when): sprawdzamy stan klienta
		// Wtedy (then): klient powinien być poprawnie zainicjalizowany
		assertNotNull(klient);
		assertEquals(NR_KLIENTA, klient.dajNrKlienta());
		assertEquals(IMIE, klient.dajImie());
	}

	@Test
	@Order(2)
	@DisplayName("UstawNazwisko")
	public void testUstawNazwisko() {
		// Jeśli (given): nowe nazwisko do ustawienia
		String noweNazwisko = "Nowak";
		// Gdy (when): ustawiamy nowe nazwisko
		klient.ustawNazwisko(noweNazwisko);
		// Wtedy (then): nazwisko powinno być zmienione
		assertEquals(noweNazwisko, klient.dajNazwisko());
	}

	@Test
	@Order(3)
	@DisplayName("UstawPesel")
	public void testUstawPesel() {
		// Jeśli (given): nowy PESEL do ustawienia
		int nowyPesel = 987654321;
		// Gdy (when): ustawiamy nowy PESEL
		klient.ustawPesel(nowyPesel);
		// Wtedy (then): PESEL powinien być zmieniony
		assertEquals(nowyPesel, klient.dajPesel());
	}

	@Test
	@Order(4)
	@DisplayName("DodajKarte")
	public void testDodajKarte() {
		// Jeśli (given): nowa karta do dodania
		IKarta karta = new Karta(1, "1234", new BigDecimal("1000.00"));
		// Gdy (when): dodajemy kartę do klienta
		klient.dodajKarte(karta);
		// Wtedy (then): karta powinna być dostępna dla klienta
		IKarta pobranaKarta = klient.pobierzKarte(1);
		assertNotNull(pobranaKarta);
		assertEquals(1, pobranaKarta.dajId());
	}

	@Test
	@Order(5)
	@DisplayName("DodajKarte - Null")
	public void testDodajKarte_Null() {
		// Jeśli (given): próba dodania karty o wartości null
		// Gdy (when): dodajemy null jako kartę
		klient.dodajKarte(null);
		// Wtedy (then): nie powinniśmy znaleźć żadnej karty
		assertNull(klient.pobierzKarte(999));
	}

	@Test
	@Order(6)
	@DisplayName("PobierzKarte - IstniejacaKarta")
	public void testPobierzKarte_IstniejacaKarta() {
		// Jeśli (given): klient z dwiema dodanymi kartami
		IKarta karta1 = new Karta(1, "1234", new BigDecimal("1000.00"));
		IKarta karta2 = new Karta(2, "5678", new BigDecimal("2000.00"));
		klient.dodajKarte(karta1);
		klient.dodajKarte(karta2);
		// Gdy (when): pobieramy drugą kartę
		IKarta pobrana = klient.pobierzKarte(2);
		// Wtedy (then): powinna zostać zwrócona poprawna karta
		assertNotNull(pobrana);
		assertEquals(2, pobrana.dajId());
	}

	@Test
	@Order(7)
	@DisplayName("PobierzKarte - NieistniejacaKarta")
	public void testPobierzKarte_NieistniejacaKarta() {
		// Jeśli (given): klient bez kart
		// Gdy (when): próbujemy pobrać nieistniejącą kartę
		// Wtedy (then): wynik powinien być null
		assertNull(klient.pobierzKarte(999));
	}

	@ParameterizedTest
	@Order(8)
	@CsvSource({"1,1234,1000.00", "2,5678,2000.00", "3,9012,3000.00"})
	@DisplayName("WielokrotneKarty")
	public void testWielokrotneKarty(int idKarty, String pin, String saldo) {
		// Jeśli (given): karta z różnymi parametrami
		IKarta karta = new Karta(idKarty, pin, new BigDecimal(saldo));
		// Gdy (when): dodajemy kartę do klienta
		klient.dodajKarte(karta);
		// Wtedy (then): karta powinna być dostępna i poprawnie zidentyfikowana
		IKarta pobrana = klient.pobierzKarte(idKarty);
		assertNotNull(pobrana);
		assertEquals(idKarty, pobrana.dajId());
	}
}
