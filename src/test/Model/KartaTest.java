package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("KartaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("model")
@Tag("karta")
@Tag("saldo")
public class KartaTest {
	private Karta karta;
	private static final int ID_KARTY = 1;
	private static final String PIN = "1234";
	private static final BigDecimal SALDO_POCZATKOWE = new BigDecimal("1000.00");

	@BeforeEach
	public void setUp() {
		karta = new Karta(ID_KARTY, PIN, SALDO_POCZATKOWE);
	}

	@AfterEach
	public void tearDown() {
		karta = null;
	}

	@Test
	@Order(1)
	@DisplayName("Konstruktor")
	public void testKonstruktor() {
		// Jeśli (given): karta została utworzona w setUp()
		// Gdy (when): sprawdzamy stan karty
		// Wtedy (then): karta powinna być poprawnie zainicjalizowana
		assertNotNull(karta);
		assertEquals(ID_KARTY, karta.dajId());
		assertEquals(SALDO_POCZATKOWE, karta.pobierzSaldo());
		assertFalse(karta.czyZablokowana());
	}

	@Test
	@Order(2)
	@DisplayName("SprawdzPin - PoprawnyPin")
	public void testSprawdzPin_PoprawnyPin() {
		// Jeśli (given): karta z poprawnym PIN-em
		// Gdy (when): sprawdzamy PIN
		// Wtedy (then): sprawdzenie powinno zwrócić true
		assertTrue(karta.sprawdzPin(PIN));
	}

	@ParameterizedTest
	@Order(3)
	@ValueSource(strings = { "0000", "1111", "9999", "" })
	@DisplayName("SprawdzPin - NiepoprawnyPin")
	public void testSprawdzPin_NiepoprawnyPin(String niepoprawnyPin) {
		// Jeśli (given): karta z ustalonym PIN-em i niepoprawny PIN do sprawdzenia
		// Gdy (when): sprawdzamy niepoprawny PIN
		// Wtedy (then): sprawdzenie powinno zwrócić false
		assertFalse(karta.sprawdzPin(niepoprawnyPin));
	}

	@Test
	@Order(4)
	@DisplayName("SprawdzPin - NullPin")
	public void testSprawdzPin_NullPin() {
		// Jeśli (given): karta z ustalonym PIN-em
		// Gdy (when): sprawdzamy PIN o wartości null
		// Wtedy (then): sprawdzenie powinno zwrócić false
		assertFalse(karta.sprawdzPin(null));
	}

	@ParameterizedTest
	@Order(5)
	@CsvSource({ "100.00,1100.00", "50.00,1050.00", "200.00,1200.00" })
	@DisplayName("ZmienSaldo - DodatniaKwota")
	public void testZmienSaldo_DodatniaKwota(String kwotaStr, String oczekiwaneStr) {
		// Jeśli (given): karta z początkowym saldem i dodatnia kwota do dodania
		BigDecimal kwota = new BigDecimal(kwotaStr);
		BigDecimal oczekiwane = new BigDecimal(oczekiwaneStr);
		// Gdy (when): zmieniamy saldo o dodatnią kwotę
		karta.zmienSaldo(kwota);
		// Wtedy (then): saldo powinno być zwiększone o podaną kwotę
		assertEquals(oczekiwane, karta.pobierzSaldo());
	}

	@Test
	@Order(6)
	@DisplayName("ZmienSaldo - UjemnaKwota")
	public void testZmienSaldo_UjemnaKwota() {
		// Jeśli (given): karta z początkowym saldem i ujemna kwota
		BigDecimal kwota = new BigDecimal("-100.00");
		// Gdy (when): zmieniamy saldo o ujemną kwotę
		karta.zmienSaldo(kwota);
		// Wtedy (then): saldo powinno być zmniejszone o podaną kwotę
		BigDecimal oczekiwaneSaldo = SALDO_POCZATKOWE.add(kwota);
		assertEquals(oczekiwaneSaldo, karta.pobierzSaldo());
	}

	@Test
	@Order(7)
	@DisplayName("ZmienSaldo - ZablokowanaKarta")
	public void testZmienSaldo_ZablokowanaKarta() {
		// Jeśli (given): karta zablokowana i kwota do zmiany
		karta.ustawZablokowana(true);
		BigDecimal kwota = new BigDecimal("100.00");
		BigDecimal saldoPrzed = karta.pobierzSaldo();
		// Gdy (when): próbujemy zmienić saldo zablokowanej karty
		karta.zmienSaldo(kwota);
		// Wtedy (then): saldo nie powinno się zmienić
		assertEquals(saldoPrzed, karta.pobierzSaldo());
	}

	@Test
	@Order(8)
	@DisplayName("UstawZablokowana")
	public void testUstawZablokowana() {
		// Jeśli (given): karta niezablokowana
		// Gdy (when): blokujemy kartę
		karta.ustawZablokowana(true);
		// Wtedy (then): karta powinna być zablokowana
		assertTrue(karta.czyZablokowana());
		// Gdy (when): odblokowujemy kartę
		karta.ustawZablokowana(false);
		// Wtedy (then): karta powinna być odblokowana
		assertFalse(karta.czyZablokowana());
	}

	@Test
	@Order(9)
	@DisplayName("SprawdzPin - ZablokowanaKarta")
	public void testSprawdzPin_ZablokowanaKarta() {
		// Jeśli (given): karta zablokowana
		karta.ustawZablokowana(true);
		// Gdy (when): sprawdzamy PIN zablokowanej karty
		// Wtedy (then): sprawdzenie powinno zwrócić false
		assertFalse(karta.sprawdzPin(PIN));
	}
}
