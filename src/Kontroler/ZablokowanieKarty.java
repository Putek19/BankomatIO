package Kontroler;
import Model.IModel;

public class ZablokowanieKarty extends IStrategiaZabezpieczenia {

	public ZablokowanieKarty(IModel aModel) {
		this._model = aModel;
	}

	public void wykonajReakcje(int aIdKarty) {
		throw new UnsupportedOperationException();
	}
}