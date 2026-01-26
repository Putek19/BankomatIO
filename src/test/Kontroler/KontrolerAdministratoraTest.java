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

@DisplayName("KontrolerAdministratoraTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("kontroler")
@Tag("administrator")
public class KontrolerAdministratoraTest {
	private KontrolerAdministratora kontroler;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy KontrolerAdministratora");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy KontrolerAdministratora");
	}

	@BeforeEach
	public void setUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		kontroler = new KontrolerAdministratora(model);
	}

	@AfterEach
	public void tearDown() {
		kontroler = null;
		model = null;
		inwentarz = null;
		dao = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	@Tag("konstruktor")
	public void testKonstruktor() {
		assertNotNull(kontroler);
	}

	@Test
	@Order(2)
	@DisplayName("MonitorowanieBezpieczenstwa")
	@Tag("bezpieczenstwo")
	public void testMonitorowanieBezpieczenstwa() {
		assertDoesNotThrow(() -> kontroler.monitorowanieBezpieczenstwa());
	}

	@Test
	@Order(3)
	@DisplayName("ZdalneBlokowanieBankomatu")
	@Tag("blokada")
	@Tag("bezpieczenstwo")
	public void testZdalneBlokowanieBankomatu() {
		assertFalse(model.czyBankomatZablokowany());
		kontroler.zdalneBlokowanieBankomatu();
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(4)
	@DisplayName("ZarzadzanieGotowka")
	@Tag("gotowka")
	public void testZarzadzanieGotowka() {
		assertDoesNotThrow(() -> kontroler.zarzadzanieGotowka());
	}
}
