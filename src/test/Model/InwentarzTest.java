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
import java.util.List;

@DisplayName("InwentarzTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InwentarzTest {
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
	}

	@AfterEach
	public void tearDown() {
		inwentarz = null;
		dao = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		assertNotNull(inwentarz);
	}

	@Test
	@Order(2)
	@DisplayName("DodajKlienta")
	public void testDodajKlienta() {
		IKlient klient = new Klient(1, "Jan");
		inwentarz.dodajKlienta(klient);
		IKlient pobrany = inwentarz.dajKlienta(1);
		assertNotNull(pobrany);
		assertEquals(1, pobrany.dajNrKlienta());
	}

	@Test
	@Order(3)
	@DisplayName("DodajKlienta - Null")
	public void testDodajKlienta_Null() {
		assertDoesNotThrow(() -> inwentarz.dodajKlienta(null));
	}

	@Test
	@Order(4)
	@DisplayName("DajKlienta - Istniejacy")
	public void testDajKlienta_Istniejacy() {
		IKlient klient1 = new Klient(1, "Jan");
		IKlient klient2 = new Klient(2, "Anna");
		inwentarz.dodajKlienta(klient1);
		inwentarz.dodajKlienta(klient2);
		IKlient pobrany = inwentarz.dajKlienta(2);
		assertNotNull(pobrany);
		assertEquals(2, pobrany.dajNrKlienta());
		assertEquals("Anna", pobrany.dajImie());
	}

	@Test
	@Order(5)
	@DisplayName("DajKlienta - Nieistniejacy")
	public void testDajKlienta_Nieistniejacy() {
		assertNull(inwentarz.dajKlienta(999));
	}

	@Test
	@Order(6)
	@DisplayName("UsunKlienta")
	public void testUsunKlienta() {
		IKlient klient = new Klient(1, "Jan");
		inwentarz.dodajKlienta(klient);
		assertNotNull(inwentarz.dajKlienta(1));
		inwentarz.usunKlienta(1);
		assertNull(inwentarz.dajKlienta(1));
	}

	@Test
	@Order(7)
	@DisplayName("UsunKlienta - Nieistniejacy")
	public void testUsunKlienta_Nieistniejacy() {
		assertDoesNotThrow(() -> inwentarz.usunKlienta(999));
	}

	@Test
	@Order(8)
	@DisplayName("PobierzWszystkichKlientow")
	public void testPobierzWszystkichKlientow() {
		IKlient klient1 = new Klient(1, "Jan");
		IKlient klient2 = new Klient(2, "Anna");
		IKlient klient3 = new Klient(3, "Piotr");
		inwentarz.dodajKlienta(klient1);
		inwentarz.dodajKlienta(klient2);
		inwentarz.dodajKlienta(klient3);
		List<IKlient> klienci = inwentarz.pobierzWszystkichKlientow();
		assertNotNull(klienci);
		assertEquals(3, klienci.size());
	}

	@Test
	@Order(9)
	@DisplayName("PobierzWszystkichKlientow - Pusty")
	public void testPobierzWszystkichKlientow_Pusty() {
		List<IKlient> klienci = inwentarz.pobierzWszystkichKlientow();
		assertNotNull(klienci);
		assertTrue(klienci.isEmpty());
	}

	@Test
	@Order(10)
	@DisplayName("ZablokujKarte")
	public void testZablokujKarte() {
		IKlient klient = new Klient(1, "Jan");
		IKarta karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
		inwentarz.zablokujKarte(100);
		boolean stan = dao.zmianaBlokadyKarty(100);
		assertFalse(stan);
	}
}
