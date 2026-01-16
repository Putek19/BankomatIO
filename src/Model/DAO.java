package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO implements IDAO {
	private List<String> tabelaKlientow = new ArrayList<>();
	private Map<Integer, IKarta> karty = new HashMap<>();
	private Map<Integer, Boolean> blokadyKart = new HashMap<>();
	private List<String> rejestrZdarzen = new ArrayList<>();

	public DAO() {
	}

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie) {
		if (aZdarzenie != null) {
			rejestrZdarzen.add(aZdarzenie);
		}
	}

	public String znajdzKlienta(int aNrKlienta) {
		if (aNrKlienta < 0 || aNrKlienta >= tabelaKlientow.size()) {
			return null;
		}
		return tabelaKlientow.get(aNrKlienta);
	}

	public int dodajKlienta(String aKlient) {
		if (aKlient != null) {
			tabelaKlientow.add(aKlient);
			return tabelaKlientow.size() - 1;
		}
		return -1;
	}

	public void edytujKlienta(int aNrKlienta) {
		if (aNrKlienta >= 0 && aNrKlienta < tabelaKlientow.size()) {
			String klient = tabelaKlientow.get(aNrKlienta);
			if (klient != null) {
				tabelaKlientow.set(aNrKlienta, klient);
			}
		}
	}

	public void usunKlienta(int aNrKlienta) {
		if (aNrKlienta >= 0 && aNrKlienta < tabelaKlientow.size()) {
			tabelaKlientow.set(aNrKlienta, null);
		}
	}

	public boolean zmianaBlokadyKarty(int aIdKarty) {
		if (karty.containsKey(aIdKarty)) {
			Boolean obecnyStatus = blokadyKart.get(aIdKarty);
			boolean nowyStatus = obecnyStatus == null || !obecnyStatus;
			blokadyKart.put(aIdKarty, nowyStatus);
			return nowyStatus;
		}
		return false;
	}

	public void dodajKarte(IKarta karta) {
		if (karta != null) {
			karty.put(karta.dajId(), karta);
			blokadyKart.put(karta.dajId(), karta.czyZablokowana());
		}
	}

	public IKarta znajdzKarte(int aIdKarty) {
		return karty.get(aIdKarty);
	}

	public boolean czyKartaZablokowana(int aIdKarty) {
		Boolean status = blokadyKart.get(aIdKarty);
		return status != null && status;
	}

	public void ustawBlokadeKarty(int aIdKarty, boolean zablokowana) {
		blokadyKart.put(aIdKarty, zablokowana);
	}
}