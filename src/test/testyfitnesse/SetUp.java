package testyfitnesse;

import fit.Fixture;
import Model.*;
import Kontroler.*;
import java.math.BigDecimal;

public class SetUp extends Fixture {
	public static Model model;
	public static Inwentarz inwentarz;
	public static DAO dao;
	public static KontrolerKlienta kontrolerKlienta;
	public static KontrolerAdministratora kontrolerAdministratora;
	public static WyplataGotowki wyplataGotowki;
	public static WeryfikacjaTozsamosci weryfikacjaTozsamosci;
	public static MonitorowanieBezpieczenstwa monitorowanieBezpieczenstwa;
	public static FabrykaKlienta fabrykaKlienta;
	
	public SetUp() {
		dao = new DAO();
		inwentarz = new Inwentarz(dao);
		model = new Model(inwentarz, dao);
		
		kontrolerKlienta = new KontrolerKlienta(model);
		kontrolerAdministratora = new KontrolerAdministratora(model);
		
		wyplataGotowki = new WyplataGotowki(model);
		weryfikacjaTozsamosci = new WeryfikacjaTozsamosci(model);
		monitorowanieBezpieczenstwa = new MonitorowanieBezpieczenstwa(model);
		
		fabrykaKlienta = new FabrykaKlienta();
		
		przygotujDaneTestowe();
	}
	
	private void przygotujDaneTestowe() {
		IKlient klient1 = new Klient(1, "Jan");
		klient1.ustawNazwisko("Kowalski");
		klient1.ustawPesel(12345678);
		IKarta karta1 = new Karta(1001, "1234", new BigDecimal("1000.00"));
		klient1.dodajKarte(karta1);
		inwentarz.dodajKlienta(klient1);
		
		IKlient klient2 = new Klient(2, "Anna");
		klient2.ustawNazwisko("Nowak");
		klient2.ustawPesel(87654321);
		IKarta karta2 = new Karta(1002, "5678", new BigDecimal("500.00"));
		klient2.dodajKarte(karta2);
		inwentarz.dodajKlienta(klient2);
		
		IKlient klient3 = new Klient(3, "Piotr");
		klient3.ustawNazwisko("Wiśniewski");
		klient3.ustawPesel(11223344);
		IKarta karta3 = new Karta(1003, "9999", new BigDecimal("2500.00"));
		klient3.dodajKarte(karta3);
		inwentarz.dodajKlienta(klient3);
	}
}
