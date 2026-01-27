package Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

@DisplayName("FabrykaKlientaTest")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FabrykaKlientaTest {
	private FabrykaKlienta fabryka;

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
	public void testStworzKontoKlienta() {
		// Jeśli (given): formularz z danymi klienta
		Formularz formularz = new Formularz("Jan", "Kowalski", 123456789);
		// Gdy (when): tworzenie kontao klienta
		IKlient klient = fabryka.stworzKontoKlienta(formularz);
		// Wtedy (then): klient powinien być utworzony z poprawnymi danymi
		assertNotNull(klient);
		assertEquals("Jan", klient.dajImie());
		assertEquals("Kowalski", klient.dajNazwisko());
		assertEquals(123456789, klient.dajPesel());
		assertTrue(klient.dajNrKlienta() > 0);
	}

	@Test
	@Order(2)
	@DisplayName("StworzKontoKlienta - NullFormularz")
	public void testStworzKontoKlienta_NullFormularz() {
		// Jeśli (given): formularz o wartości null
		// Gdy (when): próba stworzyć konto z null
		IKlient klient = fabryka.stworzKontoKlienta(null);
		// Wtedy (then): wynik powinien być null
		assertNull(klient);
	}

	@Test
	@Order(3)
	@DisplayName("StworzKontoKlienta - UnikalneNumery")
	public void testStworzKontoKlienta_UnikalneNumery() {
		// Jeśli (given): trzy różne formularze klientów
		Formularz form1 = new Formularz("Jan", "Kowalski", 111111111);
		Formularz form2 = new Formularz("Anna", "Nowak", 222222222);
		Formularz form3 = new Formularz("Piotr", "Wiśniewski", 333333333);
		// Gdy (when): tworzenie trzy konta klientów
		IKlient klient1 = fabryka.stworzKontoKlienta(form1);
		IKlient klient2 = fabryka.stworzKontoKlienta(form2);
		IKlient klient3 = fabryka.stworzKontoKlienta(form3);
		// Wtedy (then): każdy klient powinien mieć unikalny numer
		assertNotEquals(klient1.dajNrKlienta(), klient2.dajNrKlienta());
		assertNotEquals(klient2.dajNrKlienta(), klient3.dajNrKlienta());
		assertNotEquals(klient1.dajNrKlienta(), klient3.dajNrKlienta());
	}

	@ParameterizedTest
	@Order(4)
	@MethodSource("dostarczDaneKlientow")
	@DisplayName("StworzKontoKlienta - PoprawneDane")
	public void testStworzKontoKlienta_PoprawneDane(String imie, String nazwisko, int pesel) {
		// Jeśli (given): formularz z parametryzowanymi danymi klienta
		Formularz formularz = new Formularz(imie, nazwisko, pesel);
		// Gdy (when): tworzenie kontao klienta
		IKlient klient = fabryka.stworzKontoKlienta(formularz);
		// Wtedy (then): klient powinien być utworzony z poprawnymi danymi
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
