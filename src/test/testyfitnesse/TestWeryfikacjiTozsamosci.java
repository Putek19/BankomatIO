package testyfitnesse;

import fit.ColumnFixture;

public class TestWeryfikacjiTozsamosci extends ColumnFixture {
	public int idKarty;
	public String pin;
	
	public void setIdKarty(int value) {
		idKarty = value;
	}
	
	public void setPin(String value) {
		pin = value;
	}
	
	public boolean weryfikujTozsamosc() {
		return SetUp.weryfikacjaTozsamosci.weryfikujPin(idKarty, pin);
	}
	
	public boolean sprawdzCzyKartaJestAktywna() {
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
	
	public double sprawdzSaldoKarty() {
		return SetUp.model.sprawdzSaldo(idKarty).doubleValue();
	}
}
