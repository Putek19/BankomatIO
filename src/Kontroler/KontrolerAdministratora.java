package Kontroler;
import Model.IModel;

public class KontrolerAdministratora implements IKontrolerAdminstratora {
	private IModel _model;

	public KontrolerAdministratora(IModel aModel) {
		this._model = aModel;
	}

	public void monitorowanieBezpieczenstwa() {
		System.out.println("Wywołano szkielet operacji: monitorowanieBezpieczenstwa().");
        
	}

	public void zdalneBlokowanieBankomatu() {
		System.out.println("Wywołano szkielet operacji: zdalneBlokowanieBankomatu().");
        
	}

	public void zarzadzanieGotowka() {
		System.out.println("Wywołano szkielet operacji: zarzadzanieGotowka().");
        
	}
}