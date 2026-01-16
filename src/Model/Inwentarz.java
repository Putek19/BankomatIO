package Model;

public class Inwentarz {
	private IDAO _dao;
	private Klient[] _klienci = null;
	private boolean _zablokowany = false;

	public Inwentarz(IDAO aDao) {
		if (aDao == null) {
			throw new IllegalArgumentException("DAO nie może być null");
		}
		this._dao = aDao;
		this._klienci = new Klient[10];
	}

	public Klient dajKlienta(int aNrKlienta) {
		for (Klient k : _klienci) {
			if (k != null && k.getNrKlienta() == aNrKlienta) {
				return k;
			}
		}
		return null;
	}

	public void dodajKlienta(Klient klient) {
		if (klient == null) {
			return;
		}
		for (int i = 0; i < _klienci.length; i++) {
			if (_klienci[i] == null) {
				_klienci[i] = klient;
				return;
			}
		}
	}

	public void usunKlienta(int aNrKlienta) {
		for (int i = 0; i < _klienci.length; i++) {
			if (_klienci[i] != null && _klienci[i].getNrKlienta() == aNrKlienta) {
				_klienci[i] = null;
				_dao.usunKlienta(aNrKlienta);
				return;
			}
		}
	}

	public IKarta znajdzKarte(int aIdKarty) {
		for (Klient k : _klienci) {
			if (k != null) {
				IKarta karta = k.pobierzKarte(aIdKarty);
				if (karta != null) {
					return karta;
				}
			}
		}
		return null;
	}

	public void zablokujKarte(int aIdKarty) {
		_dao.zmianaBlokadyKarty(aIdKarty);
		IKarta karta = znajdzKarte(aIdKarty);
		if (karta != null && karta instanceof Karta) {
			((Karta) karta).zablokuj();
		}
	}

	public void zablokujBankomat() {
		_zablokowany = true;
	}

	public boolean czyZablokowany() {
		return _zablokowany;
	}

	public void odblokujBankomat() {
		_zablokowany = false;
	}

	public Klient znajdzKlientaPoKarcie(int aIdKarty) {
		for (Klient k : _klienci) {
			if (k != null && k.pobierzKarte(aIdKarty) != null) {
				return k;
			}
		}
		return null;
	}
}