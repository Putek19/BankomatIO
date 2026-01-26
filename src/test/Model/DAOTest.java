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

@DisplayName("DAOTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("dao")
@Tag("model")
public class DAOTest {
	private DAO dao;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy DAO");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy DAO");
	}

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
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(dao);
	}

	@Test
	@Order(2)
	@DisplayName("DodajWpisDoRejestruZdarzen")
	@Tag("rejestr")
	public void testDodajWpisDoRejestruZdarzen() {
		String zdarzenie = "Test zdarzenie";
		dao.dodajWpisDoRejestruZdarzen(zdarzenie);
		assertDoesNotThrow(() -> dao.dodajWpisDoRejestruZdarzen("Kolejne zdarzenie"));
	}

	@Test
	@Order(3)
	@DisplayName("ZnajdzKlienta - Nieistniejacy")
	@Tag("klient")
	public void testZnajdzKlienta_Nieistniejacy() {
		String znaleziony = dao.znajdzKlienta(999);
		assertNull(znaleziony);
	}

	@Test
	@Order(4)
	@DisplayName("DodajKlienta")
	@Tag("klient")
	public void testDodajKlienta() {
		String daneKlienta = "Jan Kowalski";
		int nrKlienta = dao.dodajKlienta(daneKlienta);
		assertTrue(nrKlienta > 0);
		String znaleziony = dao.znajdzKlienta(nrKlienta);
		assertEquals(daneKlienta, znaleziony);
	}

	@Test
	@Order(5)
	@DisplayName("DodajKlienta - Wielokrotne")
	@Tag("klient")
	public void testDodajKlienta_Wielokrotne() {
		int nr1 = dao.dodajKlienta("Klient 1");
		int nr2 = dao.dodajKlienta("Klient 2");
		int nr3 = dao.dodajKlienta("Klient 3");
		assertNotEquals(nr1, nr2);
		assertNotEquals(nr2, nr3);
		assertEquals("Klient 1", dao.znajdzKlienta(nr1));
		assertEquals("Klient 2", dao.znajdzKlienta(nr2));
		assertEquals("Klient 3", dao.znajdzKlienta(nr3));
	}

	@Test
	@Order(6)
	@DisplayName("UsunKlienta")
	@Tag("klient")
	public void testUsunKlienta() {
		int nrKlienta = dao.dodajKlienta("Klient do usunięcia");
		assertNotNull(dao.znajdzKlienta(nrKlienta));
		dao.usunKlienta(nrKlienta);
		assertNull(dao.znajdzKlienta(nrKlienta));
	}

	@Test
	@Order(7)
	@DisplayName("UsunKlienta - Nieistniejacy")
	@Tag("klient")
	public void testUsunKlienta_Nieistniejacy() {
		assertDoesNotThrow(() -> dao.usunKlienta(999));
	}

	@Test
	@Order(8)
	@DisplayName("EdytujKlienta")
	@Tag("klient")
	public void testEdytujKlienta() {
		int nrKlienta = dao.dodajKlienta("Klient");
		assertDoesNotThrow(() -> dao.edytujKlienta(nrKlienta));
	}

	@Test
	@Order(9)
	@DisplayName("ZmianaBlokadyKarty")
	@Tag("blokada")
	public void testZmianaBlokadyKarty() {
		int idKarty = 1;
		boolean stan1 = dao.zmianaBlokadyKarty(idKarty);
		boolean stan2 = dao.zmianaBlokadyKarty(idKarty);
		boolean stan3 = dao.zmianaBlokadyKarty(idKarty);
		assertTrue(stan1);
		assertFalse(stan2);
		assertTrue(stan3);
	}

	@Test
	@Order(10)
	@DisplayName("ZmianaBlokadyKarty - RozneKarty")
	@Tag("blokada")
	public void testZmianaBlokadyKarty_RozneKarty() {
		boolean stan1 = dao.zmianaBlokadyKarty(1);
		boolean stan2 = dao.zmianaBlokadyKarty(2);
		assertTrue(stan1);
		assertTrue(stan2);
		boolean stan1_2 = dao.zmianaBlokadyKarty(1);
		assertFalse(stan1_2);
		boolean stan2_2 = dao.zmianaBlokadyKarty(2);
		assertFalse(stan2_2);
	}
}
