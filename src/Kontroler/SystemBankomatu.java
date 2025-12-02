package Kontroler;

import Model.*;

public class SystemBankomatu {

	public static void main(String[] Args) {
		// Inicjalizacja systemu
		DAO dao = new DAO();
		Inwentarz inwentarz = new Inwentarz(dao);
		Model model = new Model(inwentarz, dao);
		
		// Utworzenie przykładowego klienta
		Formularz formularz = new Formularz("Jan", "Kowalski", 123456789);
		FabrykaKlienta fabryka = new FabrykaKlienta();
		IKlient klient = fabryka.stworzKontoKlienta(formularz);
		inwentarz.dodajKlienta(klient);
		inwentarz.dajKlienta(1);
		
		// Utworzenie przykładowej karty
		Karta karta = new Karta(1, "1234", new java.math.BigDecimal("1000.00"));
		klient.dodajKarte(karta);
		
		// Inicjalizacja kontrolerów
		KontrolerKlienta kontrolerKlienta = new KontrolerKlienta(model);
		KontrolerAdministratora kontrolerAdministratora = new KontrolerAdministratora(model);
		
		// Przykładowe operacje
		System.out.println("System bankomatu uruchomiony");
		System.out.println("Saldo karty: " + model.sprawdzSaldo(1));
		
		// Test wypłaty
		kontrolerKlienta.wyplataGotowki("1", "1234", 150.0);
		System.out.println("Saldo po wypłacie: " + model.sprawdzSaldo(1));
		
		// Test monitorowania
		kontrolerAdministratora.monitorowanieBezpieczenstwa();
	}
}