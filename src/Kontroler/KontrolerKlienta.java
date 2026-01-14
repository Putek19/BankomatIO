package Kontroler;

import Model.IModel;
import java.math.BigDecimal;

public class KontrolerKlienta implements IKontrolerKlienta {
	private IModel _model;
	private WeryfikacjaTozsamosci _weryfikacja;
	private WyplataGotowki _wyplata;

	public KontrolerKlienta(IModel aModel) {
		_model = aModel;
		_weryfikacja = new WeryfikacjaTozsamosci(aModel);
		_wyplata = new WyplataGotowki(aModel);
		_weryfikacja.ustawStrategie(new ZablokowanieKarty(aModel));
	}

	public void wyplataGotowki(String aNumerKarty, String aPin, double aKwota) {
		try {
			int idKarty = Integer.parseInt(aNumerKarty);
			if (weryfikacjaTozsamosci(idKarty, aPin)) {
				_wyplata.ustawKwote(aKwota);
				_wyplata.realizujWyplate(idKarty);
			} 
		} catch (NumberFormatException e) {
			_model.zarejestrujZdarzenie("Błędny numer karty: " + aNumerKarty);
		}
	}

	public boolean weryfikacjaTozsamosci(int aIdKarty, String aPin) {
		return _weryfikacja.weryfikujPin(aIdKarty, aPin);
	}

	public void sprawdzenieStanuKonta() {
		_model.zarejestrujZdarzenie("Sprawdzenie stanu konta");
	}

	public void wyplataGotowki() {
		_model.zarejestrujZdarzenie("Rozpoczęcie procesu wypłaty gotówki");
	}
}