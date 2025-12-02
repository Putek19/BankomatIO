package Kontroler;
import Model.IModel;

public abstract class WyplataGotowki {
	private IModel _model;
	private double _kwota;

	public WyplataGotowki(IModel aModel) {
		throw new UnsupportedOperationException();
	}

	public void realizujWyplate() {
		throw new UnsupportedOperationException();
	}

	private boolean sprawdzanieSaldaiGotowki() {
		return _model.sprawdzSaldo(0).doubleValue() >= _kwota;
	}

	private boolean zatwierdzenieWyplaty() {
		return true;
	}
}