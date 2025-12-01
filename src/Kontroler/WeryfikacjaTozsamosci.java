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

	public boolean weryfikujPin(String aPin) {
		// Weryfikacja PIN 
		_licznikProb++;
		if (_licznikProb >= MAKSYMALNA_LICZBA_PROB) {
			wykonajZabezpieczenie();
			return false;
		}
		return true;
	}

	public void ustawStrategie(IStrategiaZabezpieczenia aS) {
		_strategia = aS;
		_unnamed_IStrategiaZabezpieczenia_ = aS;
	}

	private void wykonajZabezpieczenie() {
		if (_strategia != null) {
			_strategia.wykonajReakcje(0); // ID karty - w rzeczywistości powinno być przekazane
		}
		_model.zarejestrujZdarzenie("Przekroczono maksymalną liczbę prób weryfikacji PIN");
	}

	public void resetujLicznik() {
		_licznikProb = 0;
	}
}