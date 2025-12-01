package Kontroler;
import Model.IModel;
import java.math.BigDecimal;

public abstract class WyplataGotowki {
	private IModel _model;
	private double _kwota;
	private static final double MAKSYMALNA_KWOTA_WYPLATY = 5000.0;

	public WyplataGotowki(IModel aModel) {
		_model = aModel;
		_kwota = 0.0;
	}

	public void realizujWyplate() {
		if (sprawdzanieSaldaiGotowki() && zatwierdzenieWyplaty()) {
			_model.zarejestrujZdarzenie("Zrealizowano wypłatę gotówki: " + _kwota);
		} else {
			_model.zarejestrujZdarzenie("Nie udało się zrealizować wypłaty gotówki: " + _kwota);
		}
	}

	private boolean sprawdzanieSaldaiGotowki() {
		if (_kwota <= 0) {
			return false;
		}
		if (_kwota > MAKSYMALNA_KWOTA_WYPLATY) {
			_model.zarejestrujZdarzenie("Przekroczono maksymalną kwotę wypłaty");
			return false;
		}
		return true;
	}

	private boolean zatwierdzenieWyplaty() {
		// Symulacja zatwierdzenia wypłaty
		return true;
	}

	public void ustawKwote(double aKwota) {
		_kwota = aKwota;
	}

	public double dajKwote() {
		return _kwota;
	}
}