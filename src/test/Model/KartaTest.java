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
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("KartaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
		assertNotNull(karta);
		assertEquals(ID_KARTY, karta.dajId());
		assertEquals(SALDO_POCZATKOWE, karta.pobierzSaldo());
		assertFalse(karta.czyZablokowana());
	}

	@Test
	@Order(2)
	@DisplayName("SprawdzPin - PoprawnyPin")
	public void testSprawdzPin_PoprawnyPin() {
		assertTrue(karta.sprawdzPin(PIN));
	}

	@ParameterizedTest
	@Order(3)
	@ValueSource(strings = {"0000", "1111", "9999", ""})
	@DisplayName("SprawdzPin - NiepoprawnyPin")
	public void testSprawdzPin_NiepoprawnyPin(String niepoprawnyPin) {
		assertFalse(karta.sprawdzPin(niepoprawnyPin));
	}

	@Test
	@Order(4)
	@DisplayName("SprawdzPin - NullPin")
	public void testSprawdzPin_NullPin() {
		assertFalse(karta.sprawdzPin(null));
	}

	@ParameterizedTest
	@Order(5)
	@CsvSource({"100.00,1100.00", "50.00,1050.00", "200.00,1200.00"})
	@DisplayName("ZmienSaldo - DodatniaKwota")
	public void testZmienSaldo_DodatniaKwota(String kwotaStr, String oczekiwaneStr) {
		BigDecimal kwota = new BigDecimal(kwotaStr);
		BigDecimal oczekiwane = new BigDecimal(oczekiwaneStr);
		karta.zmienSaldo(kwota);
		assertEquals(oczekiwane, karta.pobierzSaldo());
	}

	@Test
	@Order(6)
	@DisplayName("ZmienSaldo - UjemnaKwota")
	public void testZmienSaldo_UjemnaKwota() {
		BigDecimal kwota = new BigDecimal("-100.00");
		karta.zmienSaldo(kwota);
		BigDecimal oczekiwaneSaldo = SALDO_POCZATKOWE.add(kwota);
		assertEquals(oczekiwaneSaldo, karta.pobierzSaldo());
	}

	@Test
	@Order(7)
	@DisplayName("ZmienSaldo - ZablokowanaKarta")
	public void testZmienSaldo_ZablokowanaKarta() {
		karta.ustawZablokowana(true);
		BigDecimal kwota = new BigDecimal("100.00");
		BigDecimal saldoPrzed = karta.pobierzSaldo();
		karta.zmienSaldo(kwota);
		assertEquals(saldoPrzed, karta.pobierzSaldo());
	}

	@Test
	@Order(8)
	@DisplayName("UstawZablokowana")
	public void testUstawZablokowana() {
		karta.ustawZablokowana(true);
		assertTrue(karta.czyZablokowana());
		karta.ustawZablokowana(false);
		assertFalse(karta.czyZablokowana());
	}

	@Test
	@Order(9)
	@DisplayName("SprawdzPin - ZablokowanaKarta")
	public void testSprawdzPin_ZablokowanaKarta() {
		karta.ustawZablokowana(true);
		assertFalse(karta.sprawdzPin(PIN));
	}
}
