package Model;

public class Klient {
	private int _nrKlienta;
	private String _imie;
	private String _nazwisko;
	private int _pesel;
	private IKarta[] _karty;

	public Klient(int aNr, String aImie) {
		throw new UnsupportedOperationException();
	}

	public void dodajKarte(IKarta aK) {
		throw new UnsupportedOperationException();
	}

	public IKarta pobierzKarte(int aId) {
		throw new UnsupportedOperationException();
	}
}