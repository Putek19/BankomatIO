package Kontroler;
import Model.IModel;
import java.math.BigDecimal;

public class KontrolerKlienta implements IKontrolerKlienta {
	private IModel _model;
	private WeryfikacjaTozsamosci _weryfikacja;

	public KontrolerKlienta(IModel aModel) {
		_model = aModel;
		_weryfikacja = new WeryfikacjaTozsamosci(aModel);
	}

	public void wyplataGotowki(String aNumerKarty, String aPin, double aKwota) {
		try {
			int idKarty = Integer.parseInt(aNumerKarty);
			if (_model.sprawdzPin(idKarty, aPin)) {
				BigDecimal saldo = _model.sprawdzSaldo(idKarty);
				BigDecimal kwotaWyplaty = new BigDecimal(aKwota);
				if (saldo.compareTo(kwotaWyplaty) >= 0) {
					_model.aktualizujSaldo(idKarty, kwotaWyplaty.negate());
					_model.zarejestrujZdarzenie("Wypłata gotówki: " + aKwota + " z karty: " + aNumerKarty);
				} else {
					_model.zarejestrujZdarzenie("Próba wypłaty przy niewystarczającym saldzie");
				}
			} else {
				_model.zarejestrujZdarzenie("Błędny PIN dla karty: " + aNumerKarty);
			}
		} catch (NumberFormatException e) {
			_model.zarejestrujZdarzenie("Błędny numer karty: " + aNumerKarty);
		}
	}

	public void weryfikacjaTozsamosci() {
		_weryfikacja.weryfikujPin("");
	}

	public void sprawdzenieStanuKonta() {
		_model.zarejestrujZdarzenie("Sprawdzenie stanu konta");
	}

	public void wyplataGotowki() {
		_model.zarejestrujZdarzenie("Rozpoczęcie procesu wypłaty gotówki");
	}
}