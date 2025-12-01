package Kontroler;
import Model.IModel;

public class ZdalneBlokowanieBankomatu extends IStrategiaZabezpieczenia {

	public ZdalneBlokowanieBankomatu(IModel aModel) {
		super(aModel);
	}

	public void wykonajReakcje(int aIdBankomatu) {
		_model.zablokujBankomat();
		_model.zarejestrujZdarzenie("Zdalne zablokowanie bankomatu: " + aIdBankomatu);
	}
}