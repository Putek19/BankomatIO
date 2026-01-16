package Kontroler;
import Model.IModel;

public class KontrolerAdministratora implements IKontrolerAdminstratora {
	private IModel _model;
	private MonitorowanieBezpieczenstwa _monitoring;
	private ZdalneBlokowanieBankomatu _zdalneBlokowanie;

	public KontrolerAdministratora(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
		this._monitoring = new MonitorowanieBezpieczenstwa(aModel);
		this._zdalneBlokowanie = new ZdalneBlokowanieBankomatu(aModel);
	}

	public void monitorowanieBezpieczenstwa() {
		try {
			_monitoring.rozpocznijMonitoring();
			System.out.println("Monitoring bezpieczeństwa został uruchomiony");
			_monitoring.przeanalizujZdarzenie("Przegląd systemu bezpieczeństwa");
		} catch (Exception e) {
			System.out.println("Błąd podczas uruchamiania monitoringu: " + e.getMessage());
		}
	}

	public void zdalneBlokowanieBankomatu() {
		try {
			_zdalneBlokowanie.wykonajReakcje(1);
			System.out.println("Bankomat został zdalnie zablokowany");
		} catch (Exception e) {
			System.out.println("Błąd podczas blokowania bankomatu: " + e.getMessage());
		}
	}

	public void zarzadzanieGotowka() {
		try {
			_model.zarejestrujZdarzenie("Administrator: Przegląd zarządzania gotówką");
			System.out.println("Operacja zarządzania gotówką została zarejestrowana");
		} catch (Exception e) {
			System.out.println("Błąd podczas zarządzania gotówką: " + e.getMessage());
		}
	}
}