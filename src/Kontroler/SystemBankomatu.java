package Kontroler;

import Model.DAO;
import Model.IDAO;
import Model.IModel;
import Model.Inwentarz;
import Model.Model;
import Model.Klient;
import Model.Karta;
import Model.IKarta;
import java.math.BigDecimal;

public class SystemBankomatu {

    public static void main(String[] args) {
        try {
            IDAO dao = new DAO();
            Inwentarz inwentarz = new Inwentarz(dao);
            IModel model = new Model(inwentarz, dao);

            inicjalizujDaneTestowe(inwentarz, dao);

            IKontrolerKlienta kontrolerKlienta = new KontrolerKlienta(model);
            IKontrolerAdminstratora kontrolerAdministratora = new KontrolerAdministratora(model);

            System.out.println("START TESTOWEGO DZIAŁANIA SYSTEMU BANKOMATU");

            System.out.println("\nWeryfikacja tożsamości klienta");
            kontrolerKlienta.weryfikacjaTozsamosci();

            System.out.println("\nSprawdzenie stanu konta");
            kontrolerKlienta.sprawdzenieStanuKonta();

            System.out.println("\nWypłata gotówki");
            kontrolerKlienta.wyplataGotowki();

            System.out.println("\nZdalne blokowanie bankomatu (administrator)");
            kontrolerAdministratora.zdalneBlokowanieBankomatu();

            System.out.println("\nMonitorowanie bezpieczeństwa (administrator)");
            kontrolerAdministratora.monitorowanieBezpieczenstwa();

            System.out.println("\nKONIEC TESTU");
        } catch (Exception e) {
            System.out.println("Błąd krytyczny: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inicjalizujDaneTestowe(Inwentarz inwentarz, IDAO dao) {
        Klient klient1 = new Klient(1, "Jan");
        IKarta karta1 = new Karta(1, "1234", new BigDecimal("1000.00"));
        klient1.dodajKarte(karta1);
        inwentarz.dodajKlienta(klient1);
        dao.dodajKarte(karta1);

        Klient klient2 = new Klient(2, "Anna");
        IKarta karta2 = new Karta(2, "5678", new BigDecimal("500.00"));
        klient2.dodajKarte(karta2);
        inwentarz.dodajKlienta(klient2);
        dao.dodajKarte(karta2);
    }
}