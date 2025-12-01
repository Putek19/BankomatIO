package Model;

import java.util.ArrayList;
import java.util.List;

public class Klient {
	private int _nrKlienta;
	private String _imie;
	private String _nazwisko;
	private int _pesel;
	private List<IKarta> _karty;

	public Klient(int aNr, String aImie) {
		_nrKlienta = aNr;
		_imie = aImie;
		_karty = new ArrayList<IKarta>();
	}

	public void dodajKarte(IKarta aK) {
		if (aK != null) {
			_karty.add(aK);
		}
	}

	public IKarta pobierzKarte(int aId) {
		for (IKarta karta : _karty) {
			if (karta != null && karta.dajId() == aId) {
				return karta;
			}
		}
		return null;
	}

	public int dajNrKlienta() {
		return _nrKlienta;
	}

	public String dajImie() {
		return _imie;
	}

	public String dajNazwisko() {
		return _nazwisko;
	}

	public void ustawNazwisko(String aNazwisko) {
		_nazwisko = aNazwisko;
	}

	public int dajPesel() {
		return _pesel;
	}

	public void ustawPesel(int aPesel) {
		_pesel = aPesel;
	}
}