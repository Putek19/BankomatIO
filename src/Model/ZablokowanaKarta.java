package Model;
import java.math.BigDecimal;

public class ZablokowanaKarta extends KartaDekorator {
	private String _dataBlokady;

	public ZablokowanaKarta(IKarta aKarta, String aData) {
		super(aKarta);
		this._dataBlokady = aData;
	}

	public boolean czyZablokowana() {
		return true;
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
}