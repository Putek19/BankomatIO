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

@DisplayName("ZablokowanieKartyTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("blokada")
@Tag("strategia")
public class ZablokowanieKartyTest {
	private ZablokowanieKarty zablokowanie;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;
	private IKlient klient;
	private Karta karta;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy ZablokowanieKarty");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy ZablokowanieKarty");
	}

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		zablokowanie = new ZablokowanieKarty(model);
		klient = new Klient(1, "Jan");
		karta = new Karta(100, "1234", new BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		inwentarz.dodajKlienta(klient);
	}

	@AfterEach
	public void tearDown() {
		zablokowanie = null;
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
		assertNotNull(zablokowanie);
	}

	@Test
	@Order(2)
	@DisplayName("WykonajReakcje")
	@Tag("reakcja")
	public void testWykonajReakcje() {
		assertFalse(karta.czyZablokowana());
		zablokowanie.wykonajReakcje(100);
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(3)
	@DisplayName("WykonajReakcje - NieistniejacaKarta")
	@Tag("reakcja")
	public void testWykonajReakcje_NieistniejacaKarta() {
		assertDoesNotThrow(() -> zablokowanie.wykonajReakcje(999));
	}
}
