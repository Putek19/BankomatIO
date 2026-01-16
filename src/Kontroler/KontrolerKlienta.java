package Kontroler;
import Model.IModel;
import java.math.BigDecimal;

public class KontrolerKlienta implements IKontrolerKlienta {
	private IModel _model;
	private WeryfikacjaTozsamosci _weryfikacja;
	private WyplataGotowki _wyplata;
	private int _aktualnaKartaId = -1;

	public KontrolerKlienta(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
		this._weryfikacja = new WeryfikacjaTozsamosci(aModel);
		this._wyplata = new WyplataGotowki(aModel);
		_weryfikacja.ustawStrategie(new ZablokowanieKarty(aModel));
	}

	public void wyplataGotowki() {
		if (_aktualnaKartaId < 0) {
			System.out.println("Błąd: Brak zweryfikowanej karty");
			return;
		}
		try {
			BigDecimal saldo = _model.sprawdzSaldo(_aktualnaKartaId);
			System.out.println("Aktualne saldo: " + saldo);
			BigDecimal kwotaWyplaty = new BigDecimal("100.00");
			_wyplata.ustawKarte(_aktualnaKartaId);
			_wyplata.ustawKwote(kwotaWyplaty);
			boolean sukces = _wyplata.realizujWyplate();
			if (sukces) {
				System.out.println("Wypłata gotówki w kwocie " + kwotaWyplaty + " zakończona sukcesem");
			} else {
				System.out.println("Wypłata gotówki nie powiodła się");
			}
		} catch (IllegalStateException e) {
			System.out.println("Błąd: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Błąd: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Nieoczekiwany błąd podczas wypłaty: " + e.getMessage());
		}
	}

	public void weryfikacjaTozsamosci() {
		try {
			int testKartaId = 1;
			String testPin = "1234";
			_weryfikacja.ustawKarte(testKartaId);
			boolean wynik = _weryfikacja.weryfikujPin(testPin);
			if (wynik) {
				_aktualnaKartaId = testKartaId;
				System.out.println("Weryfikacja PIN zakończona sukcesem");
			} else {
				System.out.println("Weryfikacja PIN nie powiodła się");
			}
		} catch (Exception e) {
			System.out.println("Błąd podczas weryfikacji: " + e.getMessage());
		}
	}

	public void sprawdzenieStanuKonta() {
		if (_aktualnaKartaId < 0) {
			System.out.println("Błąd: Brak zweryfikowanej karty");
			return;
		}
		try {
			BigDecimal saldo = _model.sprawdzSaldo(_aktualnaKartaId);
			System.out.println("Aktualne saldo konta: " + saldo);
		} catch (IllegalStateException e) {
			System.out.println("Błąd: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("Błąd: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Nieoczekiwany błąd podczas sprawdzania salda: " + e.getMessage());
		}
	}

	public void ustawKarte(int aKartaId) {
		this._aktualnaKartaId = aKartaId;
		_weryfikacja.ustawKarte(aKartaId);
		_wyplata.ustawKarte(aKartaId);
	}
}