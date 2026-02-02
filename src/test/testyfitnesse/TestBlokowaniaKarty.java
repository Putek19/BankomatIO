package testyfitnesse;

import fit.ColumnFixture;

public class TestBlokowaniaKarty extends ColumnFixture {
	public int idKarty;
	
	public void setIdKarty(int value) {
		idKarty = value;
	}
	
	public boolean zablokujKarte() {
		boolean czyZablokowanaPrzed = sprawdzCzyKartaZablokowana();
		SetUp.model.zablokujKarte(idKarty);
		boolean czyZablokowanaPo = sprawdzCzyKartaZablokowana();
		return !czyZablokowanaPrzed && czyZablokowanaPo;
	}
	
	public boolean sprawdzCzyKartaZablokowana() {
		for (Model.IKlient klient : SetUp.inwentarz.pobierzWszystkichKlientow()) {
			if (klient != null) {
				Model.IKarta karta = klient.pobierzKarte(idKarty);
				if (karta != null) {
					return karta.czyZablokowana();
				}
			}
		}
		return false;
	}
	
	public boolean sprawdzCzyMoznaWyplacicZKarty() {
		if (sprawdzCzyKartaZablokowana()) {
			return false;
		}
		return SetUp.model.sprawdzSaldo(idKarty).doubleValue() > 0;
	}
}
