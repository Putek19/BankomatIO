package Kontroler;
import Model.IModel;

public class ZablokowanieKarty extends IStrategiaZabezpieczenia {

	public ZablokowanieKarty(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
	}

	@Override
	public void wykonajReakcje(int aIdKarty) {
		if (aIdKarty < 0) {
			return;
		}
		_model.zablokujKarte(aIdKarty);
		_model.zarejestrujZdarzenie("Automatyczne zablokowanie karty po przekroczeniu limitu prób PIN - ID: " + aIdKarty);
	}
}