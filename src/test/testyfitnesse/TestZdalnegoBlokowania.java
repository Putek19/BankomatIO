package testyfitnesse;

import fit.ColumnFixture;

public class TestZdalnegoBlokowania extends ColumnFixture {
	
	public boolean zablokujBankomatZdalnie() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.kontrolerAdministratora.zdalneBlokowanieBankomatu();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
	
	public boolean sprawdzCzyBankomatZablokowany() {
		return SetUp.model.czyBankomatZablokowany();
	}
	
	public boolean odblokujBankomat() {
		boolean stanPrzed = SetUp.model.czyBankomatZablokowany();
		SetUp.model.odblokujBankomat();
		boolean stanPo = SetUp.model.czyBankomatZablokowany();
		return stanPrzed != stanPo;
	}
}
