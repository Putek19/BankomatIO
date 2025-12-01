package Kontroler;
import Model.IModel;

public abstract class IStrategiaZabezpieczenia {
	protected IModel _model;

	public void wykonajReakcje(int aIdObiektu) {
		throw new UnsupportedOperationException();
	}
}