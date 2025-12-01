package Model;
import java.math.BigDecimal;

public class Karta implements IKarta {
	private int _id;
	private String _pin;
	private boolean _zablokowana;

	public Karta(int aId, String aPin, BigDecimal aSaldo) {
		throw new UnsupportedOperationException();
	}

	public int dajId() {
		throw new UnsupportedOperationException();
	}

	public BigDecimal pobierzSaldo() {
		throw new UnsupportedOperationException();
	}

	public void zmienSaldo(BigDecimal aKwota) {
		throw new UnsupportedOperationException();
	}

	public boolean czyZablokowana() {
		throw new UnsupportedOperationException();
	}

	public boolean sprawdzPin(String aPin) {
		throw new UnsupportedOperationException();
	}
}