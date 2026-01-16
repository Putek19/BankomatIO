package Model;
import java.math.BigDecimal;

public class Model implements IModel {
	private Inwentarz _inwentarz;
	private IDAO _dao;
	private int _aktualnaKartaId = -1;

	public Model(Inwentarz aInwentarz, IDAO aDao) {
		if (aInwentarz == null || aDao == null) {
			throw new IllegalArgumentException("Inwentarz i DAO nie mogą być null");
		}
		this._inwentarz = aInwentarz;
		this._dao = aDao;
	}

	@Override
	public void zarejestrujZdarzenie(String aOpis) {
		if (aOpis != null) {
			_dao.dodajWpisDoRejestruZdarzen(aOpis);
		}
	}

	public String pobierzDaneKarty(int aId) {
		IKarta karta = _inwentarz.znajdzKarte(aId);
		if (karta != null) {
			Klient klient = znajdzKlientaPoKarcie(aId);
			if (klient != null) {
				return "Klient: " + klient.getImie() + " " + klient.getNazwisko() + ", Karta ID: " + aId;
			}
			return "Karta ID: " + aId;
		}
		return null;
	}

	public boolean sprawdzPin(int aId, String aPin) {
		if (_inwentarz.czyZablokowany()) {
			zarejestrujZdarzenie("Próba użycia zablokowanego bankomatu - Karta ID: " + aId);
			return false;
		}
		IKarta karta = _inwentarz.znajdzKarte(aId);
		if (karta == null) {
			zarejestrujZdarzenie("Próba użycia nieistniejącej karty - ID: " + aId);
			return false;
		}
		if (karta.czyZablokowana()) {
			zarejestrujZdarzenie("Próba użycia zablokowanej karty - ID: " + aId);
			return false;
		}
		boolean wynik = karta.sprawdzPin(aPin);
		if (wynik) {
			_aktualnaKartaId = aId;
			zarejestrujZdarzenie("Pomyślna weryfikacja PIN - Karta ID: " + aId);
		} else {
			zarejestrujZdarzenie("Błędna weryfikacja PIN - Karta ID: " + aId);
		}
		return wynik;
	}

	public BigDecimal sprawdzSaldo(int aId) {
		if (_inwentarz.czyZablokowany()) {
			throw new IllegalStateException("Bankomat jest zablokowany");
		}
		IKarta karta = _inwentarz.znajdzKarte(aId);
		if (karta == null) {
			throw new IllegalArgumentException("Karta o ID " + aId + " nie istnieje");
		}
		if (karta.czyZablokowana()) {
			throw new IllegalStateException("Karta jest zablokowana");
		}
		BigDecimal saldo = karta.pobierzSaldo();
		if (saldo == null) {
			return BigDecimal.ZERO;
		}
		zarejestrujZdarzenie("Sprawdzenie salda - Karta ID: " + aId + ", Saldo: " + saldo);
		return saldo;
	}

	public void aktualizujSaldo(int aId, BigDecimal aKwota) {
		if (_inwentarz.czyZablokowany()) {
			throw new IllegalStateException("Bankomat jest zablokowany");
		}
		if (aKwota == null) {
			throw new IllegalArgumentException("Kwota nie może być null");
		}
		IKarta karta = _inwentarz.znajdzKarte(aId);
		if (karta == null) {
			throw new IllegalArgumentException("Karta o ID " + aId + " nie istnieje");
		}
		if (karta.czyZablokowana()) {
			throw new IllegalStateException("Karta jest zablokowana");
		}
		BigDecimal saldo = karta.pobierzSaldo();
		if (saldo == null) {
			saldo = BigDecimal.ZERO;
		}
		BigDecimal noweSaldo = saldo.add(aKwota);
		if (noweSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalStateException("Niewystarczające środki na koncie");
		}
		karta.zmienSaldo(aKwota);
		zarejestrujZdarzenie("Aktualizacja salda - Karta ID: " + aId + ", Zmiana: " + aKwota + ", Nowe saldo: " + noweSaldo);
	}

	public void zablokujKarte(int aId) {
		IKarta karta = _inwentarz.znajdzKarte(aId);
		if (karta != null) {
			_inwentarz.zablokujKarte(aId);
			zarejestrujZdarzenie("Zablokowanie karty - ID: " + aId);
		}
	}

	public void zablokujBankomat() {
		_inwentarz.zablokujBankomat();
		zarejestrujZdarzenie("Zablokowanie bankomatu");
	}

	public void usuniecieKlienta(int aNrKlienta) {
		_inwentarz.usunKlienta(aNrKlienta);
		zarejestrujZdarzenie("Usunięcie klienta - Nr: " + aNrKlienta);
	}

	public void usuniecieKarty(int aIdKarty) {
		IKarta karta = _inwentarz.znajdzKarte(aIdKarty);
		if (karta != null) {
			Klient klient = znajdzKlientaPoKarcie(aIdKarty);
			if (klient != null) {
				zarejestrujZdarzenie("Usunięcie karty - ID: " + aIdKarty + " z konta klienta: " + klient.getNrKlienta());
			}
		}
	}

	private Klient znajdzKlientaPoKarcie(int aIdKarty) {
		return _inwentarz.znajdzKlientaPoKarcie(aIdKarty);
	}

	public int getAktualnaKartaId() {
		return _aktualnaKartaId;
	}

	public void resetujAktualnaKarta() {
		_aktualnaKartaId = -1;
	}
}