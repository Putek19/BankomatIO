package Model;

public class FabrykaKlienta implements IFabrykaKlienta {
	private static int _nastepnyNrKlienta = 1;

	public IKlient stworzKontoKlienta(Formularz aDaneFormularza) {
		if (aDaneFormularza == null) {
			return null;
		}
		int nrKlienta = _nastepnyNrKlienta++;
		IKlient klient = new Klient(nrKlienta, aDaneFormularza.dajImie());
		klient.ustawNazwisko(aDaneFormularza.dajNazwisko());
		klient.ustawPesel(aDaneFormularza.dajPesel());
		return klient;
	}
}