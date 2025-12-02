package Model;

public abstract class KartaDekorator implements IKarta {
	protected IKarta _karta;

	public KartaDekorator(IKarta aKarta) {
		this._karta = aKarta;
	}

	public int dajId() {
		return _karta.dajId();
	}

	public boolean sprawdzPin(String aPin) {
		return _karta.sprawdzPin(aPin);
	}
}