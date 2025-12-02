package Model;

public class Formularz {
	private String _imie;
	private String _nazwisko;
	private int _pesel;

	public Formularz(String aImie, String aNazwisko, int aPesel) {
		this._imie = aImie;
		this._nazwisko = aNazwisko;
		this._pesel = aPesel;
	}

	public String getImie() {
		return _imie;
	}

	public String getNazwisko() {
		return _nazwisko;
	}

	public int getPesel() {
		return _pesel;
	}
}