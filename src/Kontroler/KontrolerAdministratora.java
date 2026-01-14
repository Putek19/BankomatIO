package Kontroler;

import Model.IModel;

public class KontrolerAdministratora implements IKontrolerAdminstratora {
	private IModel _model;
	private MonitorowanieBezpieczenstwa _monitorowanie;

	public KontrolerAdministratora(IModel aModel) {
		_model = aModel;
		_monitorowanie = new MonitorowanieBezpieczenstwa(aModel);
	}

	public void monitorowanieBezpieczenstwa() {
		_monitorowanie.rozpocznijMonitoring();
	}

	public void zdalneBlokowanieBankomatu() {
		_model.zablokujBankomat();
		_model.zarejestrujZdarzenie("Zdalne zablokowanie bankomatu przez administratora");
	}

	public void zarzadzanieGotowka() {
		_model.zarejestrujZdarzenie("Zarządzanie gotówką w bankomacie");
	}
}