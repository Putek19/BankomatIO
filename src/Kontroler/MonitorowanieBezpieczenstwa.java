package Kontroler;

import Model.IModel;

public class MonitorowanieBezpieczenstwa {
	private IModel _model;
	private boolean _aktywny;

	public MonitorowanieBezpieczenstwa(IModel aModel) {
		if (aModel == null) {
			throw new IllegalArgumentException("Model nie może być null");
		}
		this._model = aModel;
		this._aktywny = false;
	}

	public void rozpocznijMonitoring() {
		_aktywny = true;
		_model.zarejestrujZdarzenie("Rozpoczęto monitoring bezpieczeństwa");
	}

	public void zatrzymajMonitoring() {
		_aktywny = false;
		_model.zarejestrujZdarzenie("Zatrzymano monitoring bezpieczeństwa");
	}

	public boolean czyAktywny() {
		return _aktywny;
	}

	private boolean analizaObrazu(String aStrumien) {
		if (aStrumien == null || aStrumien.isEmpty()) {
			return false;
		}
		return true;
	}

	public void przeanalizujZdarzenie(String opis) {
		if (_aktywny && opis != null) {
			_model.zarejestrujZdarzenie("Monitoring: " + opis);
		}
	}
}