package Model;

import java.util.ArrayList;
import java.util.List;

public class DAO implements IDAO {
	private List<String> tabelaKlientow = new ArrayList<>();

	public DAO() {
		
		
	}

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie) {
		
		tabelaKlientow.add(aZdarzenie);
	}

	public String znajdzKlienta(int aNrKlienta) {
		if (aNrKlienta < 0 || aNrKlienta >= tabelaKlientow.size()) {
			return null;
		}
		return tabelaKlientow.get(aNrKlienta);
	}

	public int dodajKlienta(String aKlient) {
		tabelaKlientow.add(aKlient);
		return tabelaKlientow.size() - 1;
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