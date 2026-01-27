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

@DisplayName("KontrolerAdministratoraTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KontrolerAdministratoraTest {
	private KontrolerAdministratora kontroler;
	private Model model;
	private Inwentarz inwentarz;
	private DAO dao;

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
	public void testKonstruktor() {
		// Jeśli (given): kontroler administratora został utworzony w setUp()
		// Gdy (when): sprawdzamy instancję kontrolera
		// Wtedy (then): kontroler nie powinien być null
		assertNotNull(kontroler);
	}

	@Test
	@Order(2)
	@DisplayName("MonitorowanieBezpieczenstwa")
	public void testMonitorowanieBezpieczenstwa() {
		// Jeśli (given): kontroler administratora z dostępem do monitoringu
		// Gdy (when): uruchamiamy monitorowanie bezpieczeństwa
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> kontroler.monitorowanieBezpieczenstwa());
	}

	@Test
	@Order(3)
	@DisplayName("ZdalneBlokowanieBankomatu")
	public void testZdalneBlokowanieBankomatu() {
		// Jeśli (given): bankomat niezablokowany
		assertFalse(model.czyBankomatZablokowany());
		// Gdy (when): administrator zdalnie blokuje bankomat
		kontroler.zdalneBlokowanieBankomatu();
		// Wtedy (then): bankomat powinien zostać zablokowany
		assertTrue(model.czyBankomatZablokowany());
	}

	@Test
	@Order(4)
	@DisplayName("ZarzadzanieGotowka")
	public void testZarzadzanieGotowka() {
		// Jeśli (given): kontroler administratora z dostępem do zarządzania gotówką
		// Gdy (when): wykonujemy zarządzanie gotówką
		// Wtedy (then): operacja nie powinna wyrzucić wyjątku
		assertDoesNotThrow(() -> kontroler.zarzadzanieGotowka());
	}
}
