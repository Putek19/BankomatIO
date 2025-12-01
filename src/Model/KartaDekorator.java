package Model;

public abstract class KartaDekorator implements IKarta {
	protected IKarta _karta;

	public KartaDekorator(IKarta aKarta) {
		throw new UnsupportedOperationException();
	}

	public int dajId() {
		throw new UnsupportedOperationException();
	}

	public boolean sprawdzPin(String aPin) {
		throw new UnsupportedOperationException();
	}
}