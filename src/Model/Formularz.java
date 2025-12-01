package Model;

public class Formularz {
	private String _imie;
	private String _nazwisko;
	private int _pesel;

	public Formularz(String aImie, String aNazwisko, int aPesel) {
		_imie = aImie;
		_nazwisko = aNazwisko;
		_pesel = aPesel;
	}

	public String dajImie() {
		return _imie;
	}

	public String dajNazwisko() {
		return _nazwisko;
	}

	public int dajPesel() {
		return _pesel;
	}
}