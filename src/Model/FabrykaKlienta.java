package Model;

public class FabrykaKlienta implements IFabrykaKlienta {

	public Klient stworzKontoKlienta(Formularz aDaneFormularza) {
		if (aDaneFormularza == null) {
			return null;
		}
		// Tymczasowa prosta implementacja: numer klienta na podstawie PESEL
		int nrKlienta = aDaneFormularza.getPesel();
		Klient klient = new Klient(nrKlienta, aDaneFormularza.getImie());
		return klient;
	}
}