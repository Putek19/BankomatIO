package Kontroler;

public interface IKontrolerKlienta {

	public void wyplataGotowki(String aNumerKarty, String aPin, double aKwota);

	public void wyplataGotowki();

	public boolean weryfikacjaTozsamosci(int aIdKarty, String aPin);

	public void sprawdzenieStanuKonta();
}