package Kontroler;

import Model.IModel;

public class MonitorowanieBezpieczenstwa {
	private IModel _model;
	private boolean _monitoringAktywny;
	private ZdalneBlokowanieBankomatu _blokada;

	public MonitorowanieBezpieczenstwa(IModel aModel) {
		_model = aModel;
		_monitoringAktywny = false;
		_blokada = null;
	}

	public void rozpocznijMonitoring() {
	
		_monitoringAktywny = true;
		_model.zarejestrujZdarzenie("Monitorowanie rozpoczęte");

		String[] klatki = { "obraz_bezpieczny", "obraz_bezpieczny", "obraz_zagrożenie" };
		int idBankomatu = 1;

		for (String klatka : klatki) {
			if (!_monitoringAktywny) {
				break;
			}
			boolean wynikAnalizy = analizaObrazu(klatka);
			if (wynikAnalizy) {
				_model.zarejestrujZdarzenie("WYKRYTO ZAGROŻENIE");
				_blokada = new ZdalneBlokowanieBankomatu(_model);
				_blokada.wykonajReakcje(idBankomatu);
				_monitoringAktywny = false;
				break;
			}
		}
	}

	public boolean analizaObrazu(String aStrumien) {

		boolean wykrytoZagrozenie = false;

		if (aStrumien != null && !aStrumien.isEmpty()) {

			_model.zarejestrujZdarzenie("Analiza obrazu: " + aStrumien);
			if (aStrumien.contains("zagrożenie")) {
				wykrytoZagrozenie = true;
			}
		}
		return wykrytoZagrozenie;
	}

	public void obsluzZagrozenie(int aIdBankomatu) {
		_model.zarejestrujZdarzenie("WYKRYTO ZAGROŻENIE");
		_blokada = new ZdalneBlokowanieBankomatu(_model);
		_blokada.wykonajReakcje(aIdBankomatu);
		_monitoringAktywny = false;
	}

	public void zatrzymajMonitoring() {
		_monitoringAktywny = false;
		_model.zarejestrujZdarzenie("Zatrzymano monitoring bezpieczeństwa");
	}

	public boolean czyMonitoringAktywny() {
		return _monitoringAktywny;
	}
}