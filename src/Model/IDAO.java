package Model;

public interface IDAO {

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie);

	public String znajdzKlienta(int aNrKlienta);

	public int dodajKlienta(String aKlient);

	public void edytujKlienta(int aNrKlienta);

	public void usunKlienta(int aNrKlienta);

	public boolean zmianaBlokadyKarty(int aIdKarty);
}