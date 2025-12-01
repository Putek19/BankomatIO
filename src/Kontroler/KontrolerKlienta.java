package Kontroler;
import Model.IModel;

public class KontrolerKlienta implements IKontrolerKlienta {
	private IModel _model;

	public KontrolerKlienta(IModel aModel) {
		this._model = aModel;
	}

	@Override
	public void wyplataGotowki() {
		String symulowanyPinUzytkownika = "1234";
        int idKarty = 1; // Zakładamy, że karta nr 1 jest w czytniku

        System.out.println("[KONTROLER] Użytkownik wprowadził PIN: " + symulowanyPinUzytkownika);

        WeryfikacjaTozsamosci worker = new WeryfikacjaTozsamosci(_model);
        boolean wynik = worker.weryfikujPin(symulowanyPinUzytkownika);

        if (wynik) {
            System.out.println("[WIDOK] Zalogowano pomyślnie.");
        } else {
            System.out.println("[WIDOK] Błąd logowania.");
        }
	}

	public void weryfikacjaTozsamosci() {
		
	}

	public void sprawdzenieStanuKonta() {
		throw new UnsupportedOperationException();
	}
	public void symulujAtakNaPin() {
        WeryfikacjaTozsamosci worker = new WeryfikacjaTozsamosci(_model);
        int idKarty = 1;
        String blednyPin = "0000";

        // Symulujemy 5 błędnych prób z rzędu
        for (int i = 0; i < 6; i++) {
            System.out.println("[KONTROLER] Próba " + (i+1) + ": Wpisano PIN " + blednyPin);
            worker.weryfikujPin(blednyPin);
        }
    }

}