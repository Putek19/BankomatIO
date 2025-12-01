package Model;

public interface IKlient {

	public void dodajKarte(IKarta aK);

	public IKarta pobierzKarte(int aId);

	public int dajNrKlienta();

	public String dajImie();

	public String dajNazwisko();

	public void ustawNazwisko(String aNazwisko);

	public int dajPesel();

	public void ustawPesel(int aPesel);
}
