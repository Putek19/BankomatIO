package testyfitnesse;

import fit.ColumnFixture;
import java.math.BigDecimal;

public class TestWyplatyGotowki extends ColumnFixture {
	public int idKarty;
	public String pin;
	public double kwotaWyplaty;
	
	public void setIdKarty(int value) {
		idKarty = value;
	}
	
	public void setPin(String value) {
		pin = value;
	}
	
	public void setKwotaWyplaty(double value) {
		kwotaWyplaty = value;
	}
	
	public boolean wykonajWyplateGotowki() {
		int stanPrzed = pobierzLiczbeTransakcji();
		BigDecimal saldoPrzed = SetUp.model.sprawdzSaldo(idKarty);
		
		boolean weryfikacja = SetUp.kontrolerKlienta.weryfikacjaTozsamosci(idKarty, pin);
		if (!weryfikacja) {
			return false;
		}
		
		SetUp.wyplataGotowki.ustawKwote(kwotaWyplaty);
		boolean wynik = SetUp.wyplataGotowki.realizujWyplate(idKarty);
		
		int stanPo = pobierzLiczbeTransakcji();
		
		return wynik && stanPrzed != stanPo;
	}
	
	public double sprawdzSaldoPoWyplacie() {
		BigDecimal saldo = SetUp.model.sprawdzSaldo(idKarty);
		return saldo.doubleValue();
	}
	
	private int pobierzLiczbeTransakcji() {
		return SetUp.dao.pobierzLiczbeZdarzen();
	}
	
	public boolean sprawdzCzyKartaNieZablokowana() {
		for (Model.IKlient klient : SetUp.inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				Model.IKarta karta = klient.pobierzKarte(idKarty);
				if (karta != null) {
					return !karta.czyZablokowana();
				}
			}
		}
		return false;
	}
}
