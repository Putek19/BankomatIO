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

@DisplayName("ZablokowanaKartaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ZablokowanaKartaTest {
	private IKarta karta;
	private ZablokowanaKarta zablokowanaKarta;
	private static final int ID_KARTY = 1;
	private static final String PIN = "1234";
	private static final BigDecimal SALDO = new BigDecimal("1000.00");
	private static final String DATA_BLOKADY = "2024-01-01";

	@BeforeEach
	public void setUp() {
		karta = new Karta(ID_KARTY, PIN, SALDO);
		zablokowanaKarta = new ZablokowanaKarta(karta, DATA_BLOKADY);
	}

	@AfterEach
	public void tearDown() {
		zablokowanaKarta = null;
		karta = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		// Jeśli (given): zablokowana karta została utworzona w setUp()
		// Gdy (when): sprawdzenie instancji zablokowanej karty
		// Wtedy (then): karta nie powinna być null
		assertNotNull(zablokowanaKarta);
	}

	@Test
	@Order(2)
	@DisplayName("CzyZablokowana")
	public void testCzyZablokowana() {
		// Jeśli (given): zablokowana karta
		// Gdy (when): sprawdzenie czy karta jest zablokowana
		// Wtedy (then): karta powinna być zablokowana
		assertTrue(zablokowanaKarta.czyZablokowana());
	}

	@Test
	@Order(3)
	@DisplayName("SprawdzPin - ZawszeFalse")
	public void testSprawdzPin_ZawszeFalse() {
		// Jeśli (given): zablokowana karta
		// Gdy (when): sprawdzenie PIN-u (poprawny lub niepoprawny)
		// Wtedy (then): sprawdzenie zawsze powinno zwrócić false
		assertFalse(zablokowanaKarta.sprawdzPin(PIN));
		assertFalse(zablokowanaKarta.sprawdzPin("0000"));
	}

	@Test
	@Order(4)
	@DisplayName("ZmienSaldo - NieZmieniaSalda")
	public void testZmienSaldo_NieZmieniaSalda() {
		// Jeśli (given): zablokowana karta z początkowym saldem
		BigDecimal saldoPrzed = zablokowanaKarta.pobierzSaldo();
		// Gdy (when): próba zmiany saldo zablokowanej karty
		zablokowanaKarta.zmienSaldo(new BigDecimal("100.00"));
		// Wtedy (then): saldo nie powinno się zmienić
		BigDecimal saldoPo = zablokowanaKarta.pobierzSaldo();
		assertEquals(saldoPrzed, saldoPo);
	}

	@Test
	@Order(5)
	@DisplayName("PobierzSaldo")
	public void testPobierzSaldo() {
		// Jeśli (given): zablokowana karta z początkowym saldem
		// Gdy (when): pobranie saldao
		// Wtedy (then): saldo powinno być równe początkowemu saldowi
		assertEquals(SALDO, zablokowanaKarta.pobierzSaldo());
	}

	@Test
	@Order(6)
	@DisplayName("DajId")
	public void testDajId() {
		// Jeśli (given): zablokowana karta z określonym ID
		// Gdy (when): pobranie ID karty
		// Wtedy (then): ID powinno być zgodne z oczekiwanym
		assertEquals(ID_KARTY, zablokowanaKarta.dajId());
	}

	@Test
	@Order(7)
	@DisplayName("DajDateBlokady")
	public void testDajDateBlokady() {
		// Jeśli (given): zablokowana karta z datą blokady
		// Gdy (when): pobranie datyę blokady
		// Wtedy (then): data powinna być zgodna z oczekiwaną
		assertEquals(DATA_BLOKADY, zablokowanaKarta.dajDateBlokady());
	}
}
