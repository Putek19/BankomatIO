package Kontroler;
import Model.IModel;

public class WeryfikacjaTozsamosci {
	private IModel _model;
	private int _licznikProb;
	private IStrategiaZabezpieczenia _strategia;
	public IStrategiaZabezpieczenia _unnamed_IStrategiaZabezpieczenia_;

	public WeryfikacjaTozsamosci(IModel aModel) {
		this._model = aModel;
	}

	public boolean weryfikujPin(String aPin) {
		System.out.println("--- [PU01] Rozpoczęto Weryfikację Tożsamości ---");
        
        // Symulacja: Prawdziwy PIN to "1234"
        String poprawnyPin = "1234"; 

        if (aPin.equals(poprawnyPin)) {
            System.out.println("--- [PU01] PIN poprawny. Dostęp przyznany. ---");
            _licznikProb = 0;
            return true;
        } else {
            _licznikProb++;
            System.out.println("--- [PU01] Błąd! Niepoprawny PIN. Próba: " + _licznikProb + "/5 ---");
            
            if (_licznikProb >= 5) {
                wykonajZabezpieczenie();
            }
            return false;
        }
    }

	public void ustawStrategie(IStrategiaZabezpieczenia aS) {
		this._strategia = aS;
	}

	private void wykonajZabezpieczenie() {
		System.out.println("!!! WYKRYTO ZAGROŻENIE !!!");
        
        if (_strategia == null) {
            
            _strategia = new ZablokowanieKarty(_model); 
        }
        _strategia.wykonajReakcje(100); 
	}
}