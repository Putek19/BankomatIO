package Model;
import java.math.BigDecimal;

public class ZablokowanaKarta extends KartaDekorator {
	private String _dataBlokady;

	public ZablokowanaKarta(IKarta aKarta, String aData) {
		super(aKarta);
		this._dataBlokady = aData;
	}

	@Override
	public boolean czyZablokowana() {
		return true;
	}

	@Override
	public boolean sprawdzPin(String aPin) {
		return false;
	}

	public String getDataBlokady() {
		return _dataBlokady;
	}
}