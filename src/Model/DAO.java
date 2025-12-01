package Model;

import java.util.ArrayList;
import java.util.List;

public class DAO implements IDAO {
	private List<String> tabelaKlientow = new ArrayList<>();

	public DAO() {
		tabelaKlientow.add("1;Jan;Kowalski;12345678901;100;1234;5000.00;false");
        tabelaKlientow.add("2;Anna;Nowak;98765432109;101;0000;200.00;true");
		
	}

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie) {
		System.out.println("[BAZA DANYCH LOG] Zapisano zdarzenie: " + aZdarzenie);
		
	}

	public String znajdzKlienta(int aNrKlienta) {
		System.out.println("[DAO] Szukam w bazie klienta o ID: " + aNrKlienta);
        for (String rekord : tabelaKlientow) {
            if (rekord.startsWith(aNrKlienta + ";")) {
                return rekord;
            }
        }
        return null;
		
	}

	public int dodajKlienta(String aKlient) {
		throw new UnsupportedOperationException();
	}

	public void edytujKlienta(int aNrKlienta) {
		throw new UnsupportedOperationException();
	}

	public void usunKlienta(int aNrKlienta) {
		throw new UnsupportedOperationException();
	}

	public boolean zmianaBlokadyKarty(int aIdKarty) {
		throw new UnsupportedOperationException();
	}
}