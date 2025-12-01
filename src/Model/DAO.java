package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO implements IDAO {
	private List<String> _rejestrZdarzen;
	private Map<Integer, String> _klienci;
	private Map<Integer, Boolean> _blokadyKart;
	private int _nastepnyNrKlienta;

	public DAO() {
		_rejestrZdarzen = new ArrayList<String>();
		_klienci = new HashMap<Integer, String>();
		_blokadyKart = new HashMap<Integer, Boolean>();
		_nastepnyNrKlienta = 1;
	}

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie) {
		_rejestrZdarzen.add(aZdarzenie);
	}

	public String znajdzKlienta(int aNrKlienta) {
		return _klienci.get(aNrKlienta);
	}

	public int dodajKlienta(String aKlient) {
		int nrKlienta = _nastepnyNrKlienta++;
		_klienci.put(nrKlienta, aKlient);
		return nrKlienta;
	}

	public void edytujKlienta(int aNrKlienta) {
		// Implementacja edycji klienta
		if (_klienci.containsKey(aNrKlienta)) {
			// Logika edycji - w tym przypadku pozostawiamy bez zmian
		}
	}

	public void usunKlienta(int aNrKlienta) {
		_klienci.remove(aNrKlienta);
	}

	public boolean zmianaBlokadyKarty(int aIdKarty) {
		Boolean obecnyStan = _blokadyKart.get(aIdKarty);
		boolean nowyStan = (obecnyStan == null) ? true : !obecnyStan;
		_blokadyKart.put(aIdKarty, nowyStan);
		return nowyStan;
	}
}