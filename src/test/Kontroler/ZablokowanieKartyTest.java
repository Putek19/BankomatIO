package Kontroler;

import Model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("ZablokowanieKartyTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZablokowanieKartyTest {
	private ZablokowanieKarty zablokowanie;
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
	public void testKonstruktor() {
		// Jeśli (given): zablokowanie karty zostało utworzone w setUp()
		// Gdy (when): sprawdzamy instancję zablokowania
		// Wtedy (then): zablokowanie nie powinno być null
		assertNotNull(zablokowanie);
	}

	@Test
	@Order(2)
	@DisplayName("WykonajReakcje")
	public void testWykonajReakcje() {
		// Jeśli (given): karta niezablokowana
		assertFalse(karta.czyZablokowana());
		// Gdy (when): wykonujemy reakcję zabezpieczającą
		zablokowanie.wykonajReakcje(100);
		// Wtedy (then): karta powinna zostać zablokowana
		assertTrue(karta.czyZablokowana());
	}

	@Test
	@Order(3)
	@DisplayName("WykonajReakcje - NieistniejacaKarta")
	public void testWykonajReakcje_NieistniejacaKarta() {
		// Jeśli (given): nieistniejąca karta
		// Gdy (when): próbujemy wykonać reakcję na nieistniejącej karcie
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> zablokowanie.wykonajReakcje(999));
	}
}
