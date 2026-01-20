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
		assertNotNull(blokada);
	}

	@Test
	@Order(2)
	@DisplayName("WykonajReakcje")
	public void testWykonajReakcje() {
		assertFalse(model.czyBankomatZablokowany());
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(3)
	@DisplayName("WykonajReakcje - Wielokrotne")
	public void testWykonajReakcje_Wielokrotne() {
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
	}
}
