package Kontroler;
import Model.IModel;

public class ZablokowanieKarty extends IStrategiaZabezpieczenia {

	public ZablokowanieKarty(IModel aModel) {
		super(aModel);
	}

	public void wykonajReakcje(int aIdKarty) {
		_model.zablokujKarte(aIdKarty);
		_model.zarejestrujZdarzenie("Zablokowano kartę jako reakcję zabezpieczenia: " + aIdKarty);
	}
}