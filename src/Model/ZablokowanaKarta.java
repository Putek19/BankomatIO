package Model;
import java.math.BigDecimal;

public class ZablokowanaKarta extends KartaDekorator {
	private String _dataBlokady;

	public ZablokowanaKarta(IKarta aKarta, String aData) {
		super(aKarta);
		throw new UnsupportedOperationException();
	}

	public boolean czyZablokowana() {
		throw new UnsupportedOperationException();
	}

	public int dajId() {
		throw new UnsupportedOperationException();
	}

	public boolean sprawdzPin(String aPin) {
		throw new UnsupportedOperationException();
	}

	public BigDecimal pobierzSaldo() {
		throw new UnsupportedOperationException();
	}

	public void zmienSaldo(BigDecimal aKwota) {
		throw new UnsupportedOperationException();
	}
}