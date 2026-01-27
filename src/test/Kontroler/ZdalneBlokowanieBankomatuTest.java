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

@DisplayName("ZdalneBlokowanieBankomatuTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZdalneBlokowanieBankomatuTest {
	private ZdalneBlokowanieBankomatu blokada;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		blokada = new ZdalneBlokowanieBankomatu(model);
	}

	@AfterEach
	public void tearDown() {
		blokada = null;
		model = null;
		inwentarz = null;
		dao = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		// Jeśli (given): blokada została utworzona w setUp()
		// Gdy (when): sprawdzenie instancji blokady
		// Wtedy (then): blokada nie powinna być null
		assertNotNull(blokada);
	}

	@Test
	@Order(2)
	@DisplayName("WykonajReakcje")
	public void testWykonajReakcje() {
		// Jeśli (given): bankomat niezablokowany
		assertFalse(model.czyBankomatZablokowany());
		// Gdy (when): wykonanie reakcjię zdalnego blokowania
		blokada.wykonajReakcje(1);
		// Wtedy (then): bankomat powinien zostać zablokowany
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(3)
	@DisplayName("WykonajReakcje - Wielokrotne")
	public void testWykonajReakcje_Wielokrotne() {
		// Jeśli (given): bankomat niezablokowany
		// Gdy (when): wykonanie reakcjię zdalnego blokowania pierwszy raz
		blokada.wykonajReakcje(1);
		// Wtedy (then): bankomat powinien być zablokowany
		assertTrue(model.czyBankomatZablokowany());
		// Gdy (when): wykonanie reakcjię ponownie
		blokada.wykonajReakcje(1);
		// Wtedy (then): bankomat powinien pozostać zablokowany
		assertTrue(model.czyBankomatZablokowany());
	}
}
