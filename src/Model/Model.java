package Model;
import java.math.BigDecimal;

public class Model implements IModel {
	private Inwentarz _inwentarz;
	private IDAO _dao;
	private boolean _bankomatZablokowany;

	public Model(Inwentarz aInwentarz, IDAO aDao) {
		_inwentarz = aInwentarz;
		_dao = aDao;
		_bankomatZablokowany = false;
	}

	public void zarejestrujZdarzenie(String aOpis) {
		_dao.dodajWpisDoRejestruZdarzen(aOpis);
	}

	public String pobierzDaneKarty(int aId) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aId);
				if (karta != null) {
					return "Karta ID: " + aId + ", Saldo: " + karta.pobierzSaldo();
				}
			}
		}
		return null;
	}

	public boolean sprawdzPin(int aId, String aPin) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aId);
				if (karta != null) {
					return karta.sprawdzPin(aPin);
				}
			}
		}
		return false;
	}

	public BigDecimal sprawdzSaldo(int aId) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aId);
				if (karta != null) {
					return karta.pobierzSaldo();
				}
			}
		}
		return BigDecimal.ZERO;
	}

	public void aktualizujSaldo(int aId, BigDecimal aKwota) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aId);
				if (karta != null) {
					karta.zmienSaldo(aKwota);
					zarejestrujZdarzenie("Zmiana salda karty " + aId + " o kwotę: " + aKwota);
					return;
				}
			}
		}
	}

	public void zablokujKarte(int aId) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aId);
				if (karta != null && karta instanceof Karta) {
					((Karta) karta).ustawZablokowana(true);
					_inwentarz.zablokujKarte(aId);
					zarejestrujZdarzenie("Zablokowano kartę: " + aId);
					return;
				}
			}
		}
	}

	public void zablokujBankomat() {
		_bankomatZablokowany = true;
		zarejestrujZdarzenie("Bankomat został zablokowany");
	}

	public void usuniecieKlienta(int aNrKlienta) {
		_inwentarz.usunKlienta(aNrKlienta);
		zarejestrujZdarzenie("Usunięto klienta: " + aNrKlienta);
	}

	public void usuniecieKarty(int aIdKarty) {
		for (IKlient klient : _inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				IKarta karta = klient.pobierzKarte(aIdKarty);
				if (karta != null) {
					// Usunięcie karty z listy klienta
					zarejestrujZdarzenie("Usunięto kartę: " + aIdKarty);
					return;
				}
			}
		}
	}

	public boolean czyBankomatZablokowany() {
		return _bankomatZablokowany;
	}
}