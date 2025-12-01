package Model;
import java.math.BigDecimal;

public class Model implements IModel {
	private Inwentarz _inwentarz;
	private IDAO _dao;

	public Model(Inwentarz aInwentarz, IDAO aDao) {
		this._inwentarz = aInwentarz;
		this._dao = aDao;
	}

	@Override
	public void zarejestrujZdarzenie(String aOpis) {
		_dao.dodajWpisDoRejestruZdarzen(aOpis);
	}

	public String pobierzDaneKarty(int aId) {
		return _dao.znajdzKlienta(aId);
	}

	public boolean sprawdzPin(int aId, String aPin) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal sprawdzSaldo(int aId) {
		throw new UnsupportedOperationException();
	}

	public void aktualizujSaldo(int aId, BigDecimal aKwota) {
		throw new UnsupportedOperationException();
	}

	public void zablokujKarte(int aId) {
		throw new UnsupportedOperationException();
	}

	public void zablokujBankomat() {
		throw new UnsupportedOperationException();
	}

	public void usuniecieKlienta(int aNrKlienta) {
		throw new UnsupportedOperationException();
	}

	public void usuniecieKarty(int aIdKarty) {
		throw new UnsupportedOperationException();
	}
}