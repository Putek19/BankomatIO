package Model;

import java.math.BigDecimal;

public abstract class KartaDekorator implements IKarta {
	protected IKarta _karta;

	public KartaDekorator(IKarta aKarta) {
		_karta = aKarta;
	}

	public int dajId() {
		return _karta.dajId();
	}

	public boolean sprawdzPin(String aPin) {
		return _karta.sprawdzPin(aPin);
	}

	public BigDecimal pobierzSaldo() {
		return _karta.pobierzSaldo();
	}

	public void zmienSaldo(BigDecimal aKwota) {
		_karta.zmienSaldo(aKwota);
	}

	public boolean czyZablokowana() {
		return _karta.czyZablokowana();
	}
}