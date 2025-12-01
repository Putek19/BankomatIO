package Kontroler;

import Model.IModel;


public class MonitorowanieBezpieczenstwa {
	private IModel _model;
	private boolean _monitoringAktywny;

	public MonitorowanieBezpieczenstwa(IModel aModel) {
		_model = aModel;
		_monitoringAktywny = false;
	}

	public void rozpocznijMonitoring() {
		_monitoringAktywny = true;
		_model.zarejestrujZdarzenie("Rozpoczęto monitoring bezpieczeństwa");
	}

	private boolean analizaObrazu(String aStrumien) {
		// Symulacja analizy obrazu z kamery
		if (aStrumien != null && !aStrumien.isEmpty()) {
			_model.zarejestrujZdarzenie("Analiza obrazu: " + aStrumien);
			return true;
		}
		return false;
	}

	public void zatrzymajMonitoring() {
		_monitoringAktywny = false;
		_model.zarejestrujZdarzenie("Zatrzymano monitoring bezpieczeństwa");
	}
}