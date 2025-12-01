package Model;
import java.math.BigDecimal;

public class Karta implements IKarta {
	private int _id;
	private String _pin;
	private boolean _zablokowana;
	private BigDecimal _saldo;

	public Karta(int aId, String aPin, BigDecimal aSaldo) {
		_id = aId;
		_pin = aPin;
		_saldo = aSaldo;
		_zablokowana = false;
	}

	public int dajId() {
		return _id;
	}

	public BigDecimal pobierzSaldo() {
		return _saldo;
	}

	public void zmienSaldo(BigDecimal aKwota) {
		if (!_zablokowana) {
			_saldo = _saldo.add(aKwota);
		}
	}

	public boolean czyZablokowana() {
		return _zablokowana;
	}

	public void ustawZablokowana(boolean aZablokowana) {
		_zablokowana = aZablokowana;
	}

	public boolean sprawdzPin(String aPin) {
		if (_zablokowana) {
			return false;
		}
		return _pin != null && _pin.equals(aPin);
	}
}