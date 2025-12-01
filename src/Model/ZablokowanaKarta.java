package Model;
import java.math.BigDecimal;

public class ZablokowanaKarta extends KartaDekorator {
	private String _dataBlokady;

	public ZablokowanaKarta(IKarta aKarta, String aData) {
		super(aKarta);
		_dataBlokady = aData;
	}

	public boolean czyZablokowana() {
		return true;
	}

	public int dajId() {
		return _karta.dajId();
	}

	public boolean sprawdzPin(String aPin) {
		return false; // Zablokowana karta nie może weryfikować PIN
	}

	public BigDecimal pobierzSaldo() {
		return _karta.pobierzSaldo();
	}

	public void zmienSaldo(BigDecimal aKwota) {
		// Zablokowana karta nie może zmieniać salda
	}

	public String dajDateBlokady() {
		return _dataBlokady;
	}
}