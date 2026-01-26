package Model;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

@DisplayName("FabrykaKlientaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("fabryka")
@Tag("model")
public class FabrykaKlientaTest {
	private FabrykaKlienta fabryka;

	@BeforeAll
	public static void setUpBeforeClass() {
		System.out.println("Rozpoczęcie testów klasy FabrykaKlienta");
	}

	@AfterAll
	public static void tearDownAfterClass() {
		System.out.println("Zakończenie testów klasy FabrykaKlienta");
	}

	@BeforeEach
	public void setUp() {
		fabryka = new FabrykaKlienta();
	}

	@AfterEach
	public void tearDown() {
		fabryka = null;
	}

	@Test
	@Order(1)
	@DisplayName("StworzKontoKlienta")
	@Tag("tworzenie")
	public void testStworzKontoKlienta() {
		Formularz formularz = new Formularz("Jan", "Kowalski", 123456789);
		IKlient klient = fabryka.stworzKontoKlienta(formularz);
		assertNotNull(klient);
		assertEquals("Jan", klient.dajImie());
		assertEquals("Kowalski", klient.dajNazwisko());
		assertEquals(123456789, klient.dajPesel());
		assertTrue(klient.dajNrKlienta() > 0);
	}

	@Test
	@Order(2)
	@DisplayName("StworzKontoKlienta - NullFormularz")
	@Tag("tworzenie")
	public void testStworzKontoKlienta_NullFormularz() {
		IKlient klient = fabryka.stworzKontoKlienta(null);
		assertNull(klient);
	}

	@Test
	@Order(3)
	@DisplayName("StworzKontoKlienta - UnikalneNumery")
	@Tag("tworzenie")
	public void testStworzKontoKlienta_UnikalneNumery() {
		Formularz form1 = new Formularz("Jan", "Kowalski", 111111111);
		Formularz form2 = new Formularz("Anna", "Nowak", 222222222);
		Formularz form3 = new Formularz("Piotr", "Wiśniewski", 333333333);
		IKlient klient1 = fabryka.stworzKontoKlienta(form1);
		IKlient klient2 = fabryka.stworzKontoKlienta(form2);
		IKlient klient3 = fabryka.stworzKontoKlienta(form3);
		assertNotEquals(klient1.dajNrKlienta(), klient2.dajNrKlienta());
		assertNotEquals(klient2.dajNrKlienta(), klient3.dajNrKlienta());
		assertNotEquals(klient1.dajNrKlienta(), klient3.dajNrKlienta());
	}

	@ParameterizedTest
	@Order(4)
	@MethodSource("dostarczDaneKlientow")
	@DisplayName("StworzKontoKlienta - PoprawneDane")
	@Tag("tworzenie")
	@Tag("parametryzowany")
	public void testStworzKontoKlienta_PoprawneDane(String imie, String nazwisko, int pesel) {
		Formularz formularz = new Formularz(imie, nazwisko, pesel);
		IKlient klient = fabryka.stworzKontoKlienta(formularz);
		assertNotNull(klient);
		assertEquals(imie, klient.dajImie());
		assertEquals(nazwisko, klient.dajNazwisko());
		assertEquals(pesel, klient.dajPesel());
	}

	static Stream<org.junit.jupiter.params.provider.Arguments> dostarczDaneKlientow() {
		return Stream.of(
			org.junit.jupiter.params.provider.Arguments.of("Maria", "Kowalczyk", 987654321),
			org.junit.jupiter.params.provider.Arguments.of("Tomasz", "Nowak", 111222333),
			org.junit.jupiter.params.provider.Arguments.of("Anna", "Kowalska", 444555666)
		);
	}
}
