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

		// Test wypłaty (zawiera PU01)
		System.out.println("--- TEST: Wypłata Gotówki (z PU01) ---");
		kontrolerKlienta.wyplataGotowki("1", "1234", 150.0);
		java.math.BigDecimal oczekiwaneSaldo = new java.math.BigDecimal("850.00");
		java.math.BigDecimal aktualneSaldo = model.sprawdzSaldo(1);

		System.out.println("Saldo po wypłacie: " + aktualneSaldo);
		if (aktualneSaldo.compareTo(oczekiwaneSaldo) == 0) {
			System.out.println("[SUKCES] Nowa logika wypłaty działa poprawnie.");
		} else {
			System.out.println("[BŁĄD] Logika wypłaty nie zadziałała poprawnie. Oczekiwano: " + oczekiwaneSaldo);
		}

		// Test Weryfikacji Tożsamości (PU01)
		System.out.println("\n--- TEST PU01: Weryfikacja Tożsamości ---");
		System.out.println("Próba poprawna:");
		kontrolerKlienta.weryfikacjaTozsamosci(1, "1234");

		System.out.println("Próba błędna (x5) - blokada:");
		for (int i = 0; i < 6; i++) {
			kontrolerKlienta.weryfikacjaTozsamosci(1, "0000");
		}
		System.out.println("Czy karta zablokowana? " + karta.czyZablokowana());

		// Test Monitorowania Bezpieczeństwa (PU08)
		System.out.println("\n--- TEST PU08: Monitorowanie Bezpieczeństwa ---");
		kontrolerAdministratora.monitorowanieBezpieczenstwa();
	}
}