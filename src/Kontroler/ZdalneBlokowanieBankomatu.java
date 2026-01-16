package Kontroler;
import Model.IModel;

public class ZdalneBlokowanieBankomatu extends IStrategiaZabezpieczenia {

	public ZdalneBlokowanieBankomatu(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
	}

	@Override
	public void wykonajReakcje(int aIdBankomatu) {
		_model.zablokujBankomat();
		_model.zarejestrujZdarzenie("Zdalne zablokowanie bankomatu - ID: " + aIdBankomatu);
	}
}