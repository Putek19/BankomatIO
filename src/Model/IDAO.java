package Model;

import java.util.List;

public interface IDAO {

	public void dodajWpisDoRejestruZdarzen(String aZdarzenie);

	public String znajdzKlienta(int aNrKlienta);

	public int dodajKlienta(String aKlient);

	public void edytujKlienta(int aNrKlienta);

	public void usunKlienta(int aNrKlienta);

	public boolean zmianaBlokadyKarty(int aIdKarty);

	public int pobierzLiczbeZdarzen();

	public List<String> pobierzRejestrZdarzen();
}