package Kontroler;

import Model.DAO;
import Model.IDAO;
import Model.IModel;
import Model.Inwentarz;
import Model.Model;

public class SystemBankomatu {

    public static void main(String[] args) {
        // 1) Przygotowanie danych i podstawowych obiektów modelu
        IDAO dao = new DAO();
        Inwentarz inwentarz = new Inwentarz(dao);
        IModel model = new Model(inwentarz, dao);

        // 2) Utworzenie kontrolerów warstwy kontroli
        IKontrolerKlienta kontrolerKlienta = new KontrolerKlienta(model);
        IKontrolerAdminstratora kontrolerAdministratora = new KontrolerAdministratora(model);

        System.out.println("START TESTOWEGO DZIAŁANIA SYSTEMU BANKOMATU");

        // 3) Testowe wywołanie operacji przypadku użycia – weryfikacja tożsamości
        System.out.println("\nWeryfikacja tożsamości klienta");
        try {
            kontrolerKlienta.weryfikacjaTozsamosci();
        } catch (UnsupportedOperationException ex) {
            System.out.println("Operacja weryfikacji tożsamości nie jest jeszcze zaimplementowana.");
        }

        // 4) Testowe wywołanie operacji – sprawdzenie stanu konta
        System.out.println("\nSprawdzenie stanu konta");
        try {
            kontrolerKlienta.sprawdzenieStanuKonta();
        } catch (UnsupportedOperationException ex) {
            System.out.println("Operacja sprawdzenia stanu konta nie jest jeszcze zaimplementowana.");
        }

        // 5) Testowe wywołanie operacji administratora – zdalne blokowanie bankomatu
        System.out.println("\nZdalne blokowanie bankomatu (administrator)");
        try {
            kontrolerAdministratora.zdalneBlokowanieBankomatu();
        } catch (UnsupportedOperationException ex) {
            System.out.println("Operacja zdalnego blokowania bankomatu nie jest jeszcze zaimplementowana.");
        }

        System.out.println("\nKONIEC TESTU");
    }
}