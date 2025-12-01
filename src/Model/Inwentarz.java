package Model;

import java.util.ArrayList;
import java.util.List;

public class Inwentarz {
	private IDAO _dao;
	private List<Klient> _klienci;

	public Inwentarz(IDAO aDao) {
		_dao = aDao;
		_klienci = new ArrayList<Klient>();
	}

	public Klient dajKlienta(int aNrKlienta) {
		for (Klient klient : _klienci) {
			if (klient != null && klient.dajNrKlienta() == aNrKlienta) {
				return klient;
			}
		}
		return null;
	}

	public void usunKlienta(int aNrKlienta) {
		Klient klient = dajKlienta(aNrKlienta);
		if (klient != null) {
			_klienci.remove(klient);
			_dao.usunKlienta(aNrKlienta);
		}
	}

	public void zablokujKarte(int aIdKarty) {
		_dao.zmianaBlokadyKarty(aIdKarty);
		for (Klient klient : _klienci) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aIdKarty);
				if (karta != null) {
					// Karta zostanie zablokowana przez model
				}
			}
		}
	}

	public List<Klient> pobierzWszystkichKlientow() {
		return _klienci;
	}

	public void dodajKlienta(Klient aKlient) {
		if (aKlient != null) {
			_klienci.add(aKlient);
		}
	}
}