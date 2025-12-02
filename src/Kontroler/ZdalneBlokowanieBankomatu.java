package Kontroler;
import Model.IModel;

public class ZdalneBlokowanieBankomatu extends IStrategiaZabezpieczenia {

	public ZdalneBlokowanieBankomatu(IModel aModel) {
		this._model = aModel;
	}

	public void wykonajReakcje(int aIdBankomatu) {
		throw new UnsupportedOperationException();
	}
}