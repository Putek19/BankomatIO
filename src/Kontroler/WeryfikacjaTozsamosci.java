package Kontroler;

import Model.IModel;

public class WeryfikacjaTozsamosci {
	private IModel _model;
	private int _licznikProb;
	private IStrategiaZabezpieczenia _strategia;
	public IStrategiaZabezpieczenia _unnamed_IStrategiaZabezpieczenia_;
	private static final int MAKSYMALNA_LICZBA_PROB = 5;

	public WeryfikacjaTozsamosci(IModel aModel) {
		_model = aModel;
		_licznikProb = 0;
		_strategia = null;
	}

	public boolean weryfikujPin(int aIdKarty, String aPin) {
		// Weryfikacja PIN
		_licznikProb++;
		if (_licznikProb >= MAKSYMALNA_LICZBA_PROB) {
			wykonajZabezpieczenie(aIdKarty);
			return false;
		}

		boolean czyPoprawny = _model.sprawdzPin(aIdKarty, aPin);
		if (czyPoprawny) {
			resetujLicznik();
			_model.zarejestrujZdarzenie("Pomyślna weryfikacja tożsamości dla karty: " + aIdKarty);
			return true;
		} else {
			_model.zarejestrujZdarzenie("Nieudana weryfikacja tożsamości dla karty: " + aIdKarty);
			return false;
		}
	}

	public void ustawStrategie(IStrategiaZabezpieczenia aS) {
		_strategia = aS;
		_unnamed_IStrategiaZabezpieczenia_ = aS;
	}

	private void wykonajZabezpieczenie(int aIdKarty) {
		if (_strategia != null) {
			_strategia.wykonajReakcje(aIdKarty);
		}
		_model.zarejestrujZdarzenie("Przekroczono maksymalną liczbę prób weryfikacji PIN dla karty: " + aIdKarty);
	}

	public void resetujLicznik() {
		_licznikProb = 0;
	}
}