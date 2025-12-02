package Kontroler;
import Model.IModel;

public class WeryfikacjaTozsamosci {
	private IModel _model;
	private int _licznikProb;
	private IStrategiaZabezpieczenia _strategia;
	public IStrategiaZabezpieczenia _unnamed_IStrategiaZabezpieczenia_;

	public WeryfikacjaTozsamosci(IModel aModel) {
		this._model = aModel;
	}

	public boolean weryfikujPin(String aPin) {
		System.out.println("Szkielet weryfikacji PIN – zawsze zwraca false");
        _licznikProb++;
        if (_licznikProb > 3 && _strategia != null) {
            wykonajZabezpieczenie();
        }
        return false;
    }

	public void ustawStrategie(IStrategiaZabezpieczenia aS) {
		this._strategia = aS;
	}

	private void wykonajZabezpieczenie() {
		System.out.println("Wywołano szkielet zabezpieczenia weryfikacji tożsamości.");
        if (_strategia != null) {
            _strategia.wykonajReakcje(0); // identyfikator obiektu
        }
    }
}