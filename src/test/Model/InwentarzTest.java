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
		// Jeśli (given): inwentarz został utworzony w setUp()
		// Gdy (when): sprawdzenie instancji inwentarza
		// Wtedy (then): inwentarz nie powinien być null
		assertNotNull(inwentarz);
	}

	@Test
	@Order(2)
	@DisplayName("DodajKlienta")
	public void testDodajKlienta() {
		// Jeśli (given): nowy klient do dodania
		IKlient klient = new Klient(1, "Jan");
		// Gdy (when): dodanie klienta do inwentarza
		inwentarz.dodajKlienta(klient);
		// Wtedy (then): klient powinien być dostępny w inwentarzu
		IKlient pobrany = inwentarz.dajKlienta(1);
		assertNotNull(pobrany);
		assertEquals(1, pobrany.dajNrKlienta());
	}

	@Test
	@Order(3)
	@DisplayName("DodajKlienta - Null")
	public void testDodajKlienta_Null() {
		// Jeśli (given): próba dodania klienta o wartości null
		// Gdy (when): dodanie null jako klienta
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> inwentarz.dodajKlienta(null));
	}

	@Test
	@Order(4)
	@DisplayName("DajKlienta - Istniejacy")
	public void testDajKlienta_Istniejacy() {
		// Jeśli (given): dwóch klientów dodanych do inwentarza
		IKlient klient1 = new Klient(1, "Jan");
		IKlient klient2 = new Klient(2, "Anna");
		inwentarz.dodajKlienta(klient1);
		inwentarz.dodajKlienta(klient2);
		// Gdy (when): pobranie drugiejiego klienta
		IKlient pobrany = inwentarz.dajKlienta(2);
		// Wtedy (then): powinien zostać zwrócony poprawny klient
		assertNotNull(pobrany);
		assertEquals(2, pobrany.dajNrKlienta());
		assertEquals("Anna", pobrany.dajImie());
	}

	@Test
	@Order(5)
	@DisplayName("DajKlienta - Nieistniejacy")
	public void testDajKlienta_Nieistniejacy() {
		// Jeśli (given): pusty inwentarz
		// Gdy (when): próba pobrać nieistniejącego klienta
		// Wtedy (then): wynik powinien być null
		assertNull(inwentarz.dajKlienta(999));
	}

	@Test
	@Order(6)
	@DisplayName("UsunKlienta")
	public void testUsunKlienta() {
		// Jeśli (given): klient dodany do inwentarza
		IKlient klient = new Klient(1, "Jan");
		inwentarz.dodajKlienta(klient);
		assertNotNull(inwentarz.dajKlienta(1));
		// Gdy (when): usunięcie klienta
		inwentarz.usunKlienta(1);
		// Wtedy (then): klient nie powinien być dostępny
		assertNull(inwentarz.dajKlienta(1));
	}

	@Test
	@Order(7)
	@DisplayName("UsunKlienta - Nieistniejacy")
	public void testUsunKlienta_Nieistniejacy() {
		// Jeśli (given): pusty inwentarz
		// Gdy (when): próba usunięcia nieistniejącego klienta
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> inwentarz.usunKlienta(999));
	}

	@Test
	@Order(8)
	@DisplayName("PobierzWszystkichKlientow")
	public void testPobierzWszystkichKlientow() {
		// Jeśli (given): trzech klientów dodanych do inwentarza
		IKlient klient1 = new Klient(1, "Jan");
		IKlient klient2 = new Klient(2, "Anna");
		IKlient klient3 = new Klient(3, "Piotr");
		inwentarz.dodajKlienta(klient1);
		inwentarz.dodajKlienta(klient2);
		inwentarz.dodajKlienta(klient3);
		// Gdy (when): pobranie wszystkich klientówów
		List<IKlient> klienci = inwentarz.pobierzWszystkichKlientow();
		// Wtedy (then): lista powinna zawierać 3 klientów
		assertNotNull(klienci);
		assertEquals(3, klienci.size());
	}

	@Test
	@Order(9)
	@DisplayName("PobierzWszystkichKlientow - Pusty")
	public void testPobierzWszystkichKlientow_Pusty() {
		// Jeśli (given): pusty inwentarz
		// Gdy (when): pobranie wszystkich klientówów
		List<IKlient> klienci = inwentarz.pobierzWszystkichKlientow();
		// Wtedy (then): lista powinna być pusta ale nie null
		assertNotNull(klienci);
		assertTrue(klienci.isEmpty());
	}

	@Test
	@Order(10)
	@DisplayName("ZablokujKarte")
	public void testZablokujKarte() {
		// Jeśli (given): klient z kartą dodany do inwentarza
		IKlient klient = new Klient(1, "Jan");
		IKarta karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
		// Gdy (when): zablokowanie kartyyę przez inwentarz
		inwentarz.zablokujKarte(100);
		// Wtedy (then): karta powinna być zablokowana
		boolean stan = dao.zmianaBlokadyKarty(100);
		assertFalse(stan);
	}
}
