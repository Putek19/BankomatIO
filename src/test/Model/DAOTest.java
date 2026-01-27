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

@DisplayName("DAOTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("model")
@Tag("dao")
@Tag("baza")
public class DAOTest {
	private DAO dao;

	@BeforeEach
	public void setUp() {
		dao = new DAO();
	}

	@AfterEach
	public void tearDown() {
		dao = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		// Jeśli (given): DAO został utworzony w setUp()
		// Gdy (when): sprawdzamy instancję DAO
		// Wtedy (then): DAO nie powinien być null
		assertNotNull(dao);
	}

	@Test
	@Order(2)
	@DisplayName("DodajWpisDoRejestruZdarzen")
	public void testDodajWpisDoRejestruZdarzen() {
		// Jeśli (given): zdarzenie do dodania
		String zdarzenie = "Test zdarzenie";
		// Gdy (when): dodajemy wpis do rejestru zdarzeń
		dao.dodajWpisDoRejestruZdarzen(zdarzenie);
		// Wtedy (then): kolejne dodanie nie powinno wyrzucić wyjątku
		assertDoesNotThrow(() -> dao.dodajWpisDoRejestruZdarzen("Kolejne zdarzenie"));
	}

	@Test
	@Order(3)
	@DisplayName("ZnajdzKlienta - Nieistniejacy")
	public void testZnajdzKlienta_Nieistniejacy() {
		// Jeśli (given): pusta baza klientów
		// Gdy (when): szukamy nieistniejącego klienta
		// Wtedy (then): wynik powinien być null
		assertNull(dao.znajdzKlienta(999));
	}

	@Test
	@Order(4)
	@DisplayName("DodajKlienta")
	public void testDodajKlienta() {
		// Jeśli (given): dane nowego klienta
		String daneKlienta = "Jan Kowalski";
		// Gdy (when): dodajemy klienta do bazy
		int nrKlienta = dao.dodajKlienta(daneKlienta);
		// Wtedy (then): klient powinien otrzymać numer większy od 0
		assertTrue(nrKlienta > 0);
		// Wtedy (then): powinniśmy móc znaleźć klienta po numerze
		String znaleziony = dao.znajdzKlienta(nrKlienta);
		assertEquals(daneKlienta, znaleziony);
	}

	@Test
	@Order(5)
	@DisplayName("DodajKlienta - Wielokrotne")
	public void testDodajKlienta_Wielokrotne() {
		// Jeśli (given): trzech różnych klientów do dodania
		// Gdy (when): dodajemy trzech klientów
		int nr1 = dao.dodajKlienta("Klient 1");
		int nr2 = dao.dodajKlienta("Klient 2");
		int nr3 = dao.dodajKlienta("Klient 3");
		// Wtedy (then): każdy klient powinien otrzymać unikalny numer
		assertNotEquals(nr1, nr2);
		assertNotEquals(nr2, nr3);
		// Wtedy (then): wszystkich klientów można znaleźć po ich numerach
		assertEquals("Klient 1", dao.znajdzKlienta(nr1));
		assertEquals("Klient 2", dao.znajdzKlienta(nr2));
		assertEquals("Klient 3", dao.znajdzKlienta(nr3));
	}

	@Test
	@Order(6)
	@DisplayName("UsunKlienta")
	public void testUsunKlienta() {
		// Jeśli (given): klient dodany do bazy
		int nrKlienta = dao.dodajKlienta("Klient do usunięcia");
		assertNotNull(dao.znajdzKlienta(nrKlienta));
		// Gdy (when): usuwamy klienta
		dao.usunKlienta(nrKlienta);
		// Wtedy (then): klient nie powinien być znaleziony
		assertNull(dao.znajdzKlienta(nrKlienta));
	}

	@Test
	@Order(7)
	@DisplayName("UsunKlienta - Nieistniejacy")
	public void testUsunKlienta_Nieistniejacy() {
		// Jeśli (given): pusta baza klientów
		// Gdy (when): próbujemy usunąć nieistniejącego klienta
		// Wtedy (then): nie powinien zostać wyrzucony wyjątek
		assertDoesNotThrow(() -> dao.usunKlienta(999));
	}

	@Test
	@Order(8)
	@DisplayName("EdytujKlienta")
	public void testEdytujKlienta() {
		// Jeśli (given): klient dodany do bazy
		int nrKlienta = dao.dodajKlienta("Klient");
		// Gdy (when): edytujemy klienta
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> dao.edytujKlienta(nrKlienta));
	}

	@Test
	@Order(9)
	@DisplayName("ZmianaBlokadyKarty")
	public void testZmianaBlokadyKarty() {
		// Jeśli (given): identyfikator karty
		int idKarty = 1;
		// Gdy (when): zmieniamy blokadę karty po raz pierwszy
		boolean stan1 = dao.zmianaBlokadyKarty(idKarty);
		// Wtedy (then): karta powinna być zablokowana
		assertTrue(stan1);
		// Gdy (when): zmieniamy blokadę karty po raz drugi
		boolean stan2 = dao.zmianaBlokadyKarty(idKarty);
		// Wtedy (then): karta powinna być odblokowana
		assertFalse(stan2);
		// Gdy (when): zmieniamy blokadę karty po raz trzeci
		boolean stan3 = dao.zmianaBlokadyKarty(idKarty);
		// Wtedy (then): karta powinna być znowu zablokowana
		assertTrue(stan3);
	}

	@Test
	@Order(10)
	@DisplayName("ZmianaBlokadyKarty - RozneKarty")
	public void testZmianaBlokadyKarty_RozneKarty() {
		// Jeśli (given): dwie różne karty
		// Gdy (when): blokujemy pierwszą kartę
		boolean stan1 = dao.zmianaBlokadyKarty(1);
		// Gdy (when): blokujemy drugą kartę
		boolean stan2 = dao.zmianaBlokadyKarty(2);
		// Wtedy (then): obie karty powinny być zablokowane
		assertTrue(stan1);
		assertTrue(stan2);
		// Gdy (when): odblokowujemy pierwszą kartę
		boolean stan1_2 = dao.zmianaBlokadyKarty(1);
		// Wtedy (then): pierwsza karta powinna być odblokowana
		assertFalse(stan1_2);
		// Gdy (when): odblokowujemy drugą kartę
		boolean stan2_2 = dao.zmianaBlokadyKarty(2);
		// Wtedy (then): druga karta powinna być odblokowana
		assertFalse(stan2_2);
	}
}
