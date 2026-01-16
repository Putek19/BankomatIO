package Model;
import java.math.BigDecimal;

public abstract class KartaDekorator implements IKarta {
	protected IKarta _karta;

	public KartaDekorator(IKarta aKarta) {
		if (aKarta == null) {
			throw new IllegalArgumentException("Karta nie może być null");
		}
		this._karta = aKarta;
	}

	@Override
	public int dajId() {
		return _karta.dajId();
	}

	@Override
	public boolean sprawdzPin(String aPin) {
		return _karta.sprawdzPin(aPin);
	}

	@Override
	public BigDecimal pobierzSaldo() {
		return _karta.pobierzSaldo();
	}

	@Override
	public void zmienSaldo(BigDecimal aKwota) {
		_karta.zmienSaldo(aKwota);
	}

	@Override
	public boolean czyZablokowana() {
		return _karta.czyZablokowana();
	}
}