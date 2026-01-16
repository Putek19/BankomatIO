package Model;
import java.math.BigDecimal;

public class Karta implements IKarta {
	private int _id;
	private String _pin;
	private boolean _zablokowana;
	private BigDecimal _saldo;

	public Karta(int aId, String aPin, BigDecimal aSaldo) {
		this._id = aId;
		this._pin = aPin;
		this._saldo = aSaldo;
		this._zablokowana = false;
	}

	public int dajId() {
		return _id;
	}

	public BigDecimal pobierzSaldo() {
		return _saldo;
	}

	public void zmienSaldo(BigDecimal aKwota) {
		if (aKwota == null) {
			return;
		}
		if (_saldo == null) {
			_saldo = aKwota;
		} else {
			_saldo = _saldo.add(aKwota);
		}
	}

	public boolean czyZablokowana() {
		return _zablokowana;
	}

	public void zablokuj() {
		_zablokowana = true;
	}

	public void odblokuj() {
		_zablokowana = false;
	}

	public boolean sprawdzPin(String aPin) {
		if (aPin == null || _zablokowana) {
			return false;
		}
		return _pin.equals(aPin);
	}
}