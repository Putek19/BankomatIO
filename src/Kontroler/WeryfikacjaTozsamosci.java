package Kontroler;
import Model.IModel;

public class WeryfikacjaTozsamosci {
	private IModel _model;
	private int _licznikProb;
	private IStrategiaZabezpieczenia _strategia;
	private int _aktualnaKartaId = -1;

	public WeryfikacjaTozsamosci(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
		this._licznikProb = 0;
	}

	public boolean weryfikujPin(int aKartaId, String aPin) {
		if (aPin == null || aPin.isEmpty()) {
			return false;
		}
		if (aKartaId != _aktualnaKartaId) {
			_licznikProb = 0;
			_aktualnaKartaId = aKartaId;
		}
		boolean wynik = _model.sprawdzPin(aKartaId, aPin);
		if (!wynik) {
			_licznikProb++;
			if (_licznikProb > 3 && _strategia != null) {
				wykonajZabezpieczenie(aKartaId);
			}
		} else {
			_licznikProb = 0;
		}
		return wynik;
	}

	public boolean weryfikujPin(String aPin) {
		if (_aktualnaKartaId < 0) {
			return false;
		}
		return weryfikujPin(_aktualnaKartaId, aPin);
	}

	public void ustawStrategie(IStrategiaZabezpieczenia aS) {
		this._strategia = aS;
	}

	public void ustawKarte(int aKartaId) {
		this._aktualnaKartaId = aKartaId;
		this._licznikProb = 0;
	}

	public void resetujLicznik() {
		_licznikProb = 0;
	}

	private void wykonajZabezpieczenie(int aKartaId) {
		if (_strategia != null) {
			_strategia.wykonajReakcje(aKartaId);
		}
	}
}