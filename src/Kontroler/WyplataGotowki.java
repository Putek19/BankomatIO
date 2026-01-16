package Kontroler;
import Model.IModel;
import java.math.BigDecimal;

public class WyplataGotowki {
	private IModel _model;
	private BigDecimal _kwota;
	private int _kartaId;

	public WyplataGotowki(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
		this._kwota = BigDecimal.ZERO;
		this._kartaId = -1;
	}

	public void ustawKarte(int aKartaId) {
		this._kartaId = aKartaId;
	}

	public void ustawKwote(BigDecimal aKwota) {
		if (aKwota == null || aKwota.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Kwota musi być większa od zera");
		}
		this._kwota = aKwota;
	}

	public boolean realizujWyplate() {
		if (_kartaId < 0) {
			throw new IllegalStateException("Nie wybrano karty");
		}
		if (_kwota == null || _kwota.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Kwota musi być większa od zera");
		}
		if (!sprawdzanieSaldaiGotowki()) {
			_model.zarejestrujZdarzenie("Próba wypłaty bez wystarczających środków - Karta ID: " + _kartaId + ", Kwota: " + _kwota);
			return false;
		}
		if (!zatwierdzenieWyplaty()) {
			return false;
		}
		try {
			BigDecimal kwotaUjemna = _kwota.negate();
			_model.aktualizujSaldo(_kartaId, kwotaUjemna);
			_model.zarejestrujZdarzenie("Wypłata gotówki - Karta ID: " + _kartaId + ", Kwota: " + _kwota);
			return true;
		} catch (Exception e) {
			_model.zarejestrujZdarzenie("Błąd podczas wypłaty - Karta ID: " + _kartaId + ", Błąd: " + e.getMessage());
			return false;
		}
	}

	private boolean sprawdzanieSaldaiGotowki() {
		try {
			BigDecimal saldo = _model.sprawdzSaldo(_kartaId);
			if (saldo == null) {
				return false;
			}
			return saldo.compareTo(_kwota) >= 0;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean zatwierdzenieWyplaty() {
		return true;
	}
}