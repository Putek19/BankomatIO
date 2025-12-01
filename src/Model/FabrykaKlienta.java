package Model;

public class FabrykaKlienta implements IFabrykaKlienta {
	private static int _nastepnyNrKlienta = 1;

	public Klient stworzKontoKlienta(Formularz aDaneFormularza) {
		if (aDaneFormularza == null) {
			return null;
		}
		int nrKlienta = _nastepnyNrKlienta++;
		Klient klient = new Klient(nrKlienta, aDaneFormularza.dajImie());
		klient.ustawNazwisko(aDaneFormularza.dajNazwisko());
		klient.ustawPesel(aDaneFormularza.dajPesel());
		return klient;
	}
}