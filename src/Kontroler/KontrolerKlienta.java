package Kontroler;
import Model.IModel;

public class KontrolerKlienta implements IKontrolerKlienta {
	private IModel _model;
    private WeryfikacjaTozsamosci _weryfikacja;

	public KontrolerKlienta(IModel aModel) {
		this._model = aModel;
        this._weryfikacja = new WeryfikacjaTozsamosci(aModel);
	}

	
	public void wyplataGotowki() {
		System.out.println("Wywołano szkielet operacji: wyplataGotowki().");
        
	}

	public void weryfikacjaTozsamosci() {
		System.out.println("Wywołano szkielet operacji: weryfikacjaTozsamosci().");
        boolean wynik = _weryfikacja.weryfikujPin("0000");
        System.out.println("Wynik weryfikacji PIN (wartość testowa): " + wynik);
	}

	public void sprawdzenieStanuKonta() {
		System.out.println("Wywołano szkielet operacji: sprawdzenieStanuKonta().");
        
	}
	

}