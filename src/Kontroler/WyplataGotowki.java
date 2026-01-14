package Kontroler;

import Model.IModel;
import java.math.BigDecimal;

public class WyplataGotowki {
	private IModel _model;
	private double _kwota;
	private static final double MAKSYMALNA_KWOTA_WYPLATY = 5000.0;

	public WyplataGotowki(IModel aModel) {
		_model = aModel;
		_kwota = 0.0;
	}

	public boolean realizujWyplate(int aIdKarty) {
		if (sprawdzanieSaldaiGotowki() && zatwierdzenieWyplaty()) {
			BigDecimal saldo = _model.sprawdzSaldo(aIdKarty);
			BigDecimal kwotaWyplaty = new BigDecimal(_kwota);

			if (saldo.compareTo(kwotaWyplaty) >= 0) {
				_model.aktualizujSaldo(aIdKarty, kwotaWyplaty.negate());
				_model.zarejestrujZdarzenie("Zrealizowano wypłatę gotówki: " + _kwota + " z karty: " + aIdKarty);
				return true;
			} else {
				_model.zarejestrujZdarzenie("Próba wypłaty przy niewystarczającym saldzie: " + _kwota);
				return false;
			}
		} else {
			_model.zarejestrujZdarzenie("Nie udało się zrealizować wypłaty gotówki: " + _kwota);
			return false;
		}
	}

	private boolean sprawdzanieSaldaiGotowki() {
		if (_kwota <= 0) {
			_model.zarejestrujZdarzenie("Próba wypłaty z niewystarczającą kwotą");
			return false;
		}
		if (_kwota > MAKSYMALNA_KWOTA_WYPLATY) {
			_model.zarejestrujZdarzenie("Przekroczono maksymalną kwotę wypłaty");
			return false;
		}
		return true;
	}

	private boolean zatwierdzenieWyplaty() {
		
		return true;
	}

	public void ustawKwote(double aKwota) {
		_kwota = aKwota;
	}

	public double dajKwote() {
		return _kwota;
	}
}