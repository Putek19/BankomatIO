package Model;

public class Inwentarz {
	private IDAO _dao;
	private Klient[] _klienci = null;

	public Inwentarz(IDAO aDao) {
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

	public void usunKlienta(int aNrKlienta) {
		for (int i = 0; i < _klienci.length; i++) {
			if (_klienci[i] != null && _klienci[i].getNrKlienta() == aNrKlienta) {
				_klienci[i] = null;
				_dao.usunKlienta(aNrKlienta);
				return;
			}
		}
	}

	public void zablokujKarte(int aIdKarty) {
		_dao.zmianaBlokadyKarty(aIdKarty);
	}
}