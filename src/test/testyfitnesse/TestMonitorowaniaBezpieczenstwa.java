package testyfitnesse;

import fit.ColumnFixture;

public class TestMonitorowaniaBezpieczenstwa extends ColumnFixture {
	public String strumienObrazow;
	
	public void setStrumienObrazow(String value) {
		strumienObrazow = value;
	}
	
	public boolean analizujObraz() {
		return SetUp.monitorowanieBezpieczenstwa.analizaObrazu(strumienObrazow);
	}
	
	public boolean analizujIZareaguj() {
		boolean wykrytoZagrozenie = SetUp.monitorowanieBezpieczenstwa.analizaObrazu(strumienObrazow);
		if (wykrytoZagrozenie) {
			SetUp.monitorowanieBezpieczenstwa.obsluzZagrozenie(1);
		}
		return wykrytoZagrozenie;
	}
	
	public boolean uruchomMonitoring() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.monitorowanieBezpieczenstwa.rozpocznijMonitoring();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
	
	public boolean sprawdzCzyMonitoringAktywny() {
		return SetUp.monitorowanieBezpieczenstwa.czyMonitoringAktywny();
	}
	
	public boolean sprawdzCzyBankomatZablokowany() {
		return SetUp.model.czyBankomatZablokowany();
	}
}
