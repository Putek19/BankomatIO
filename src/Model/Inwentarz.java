package Model;

import java.util.ArrayList;
import java.util.List;

public class Inwentarz {
	private IDAO _dao;
	private List<IKlient> _klienci;

	public Inwentarz(IDAO aDao) {
		_dao = aDao;
		_klienci = new ArrayList<IKlient>();
	}

	public IKlient dajKlienta(int aNrKlienta) {
		for (IKlient klient : _klienci) {
			if (klient != null && klient.dajNrKlienta() == aNrKlienta) {
				System.out.println("Znaleziono klienta: " + klient.dajNrKlienta()+ klient.dajImie()+ klient.dajNazwisko()+ klient.dajPesel());
				return klient;
			}
		}
		return null;
	}

	public void usunKlienta(int aNrKlienta) {
		IKlient klient = dajKlienta(aNrKlienta);
		if (klient != null) {
			_klienci.remove(klient);
			_dao.usunKlienta(aNrKlienta);
		}
	}

	public void zablokujKarte(int aIdKarty) {
		_dao.zmianaBlokadyKarty(aIdKarty);
		for (IKlient klient : _klienci) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aIdKarty);
				if (karta != null) {
					// Karta zostanie zablokowana przez model
				}
			}
		}
	}

	public List<IKlient> pobierzWszystkichKlientow() {
		return _klienci;
	}

	public void dodajKlienta(IKlient aKlient) {
		if (aKlient != null) {
			_klienci.add(aKlient);
		}
	}
}