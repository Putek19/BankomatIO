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

@DisplayName("ZdalneBlokowanieBankomatuTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("blokada")
@Tag("bezpieczenstwo")
@Tag("strategia")
public class ZdalneBlokowanieBankomatuTest {
	private ZdalneBlokowanieBankomatu blokada;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy ZdalneBlokowanieBankomatu");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy ZdalneBlokowanieBankomatu");
	}

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
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(blokada);
	}

	@Test
	@Order(2)
	@DisplayName("WykonajReakcje")
	@Tag("reakcja")
	public void testWykonajReakcje() {
		assertFalse(model.czyBankomatZablokowany());
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(3)
	@DisplayName("WykonajReakcje - Wielokrotne")
	@Tag("reakcja")
	public void testWykonajReakcje_Wielokrotne() {
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
		blokada.wykonajReakcje(1);
		assertTrue(model.czyBankomatZablokowany());
	}
}
