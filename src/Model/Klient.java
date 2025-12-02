package Model;

public class Klient {
	private int _nrKlienta;
	private String _imie;
	private String _nazwisko;
	private int _pesel;
	private IKarta[] _karty;

	public Klient(int aNr, String aImie) {
		this._nrKlienta = aNr;
		this._imie = aImie;
		this._nazwisko = "";
		this._pesel = 0;
		this._karty = new IKarta[10];
	}

	public int getNrKlienta() {
		return _nrKlienta;
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

	public void dodajKarte(IKarta aK) {
		if (aK == null) {
			return;
		}
		for (int i = 0; i < _karty.length; i++) {
			if (_karty[i] == null) {
				_karty[i] = aK;
				return;
			}
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
}