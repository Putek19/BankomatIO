package Kontroler;
import Model.IModel;

public abstract class IStrategiaZabezpieczenia {
	protected IModel _model;

	public IStrategiaZabezpieczenia(IModel aModel) {
		_model = aModel;
	}

	public void wykonajReakcje(int aIdObiektu) {
		_model.zarejestrujZdarzenie("Wykonano reakcję zabezpieczenia dla obiektu: " + aIdObiektu);
	}
}