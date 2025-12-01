package Kontroler;

import Model.DAO;
import Model.IDAO;
import Model.IModel;
import Model.Inwentarz;
import Model.Model;

public class SystemBankomatu {

    public static void main(String[] args) {
        System.out.println("=== START SYSTEMU BANKOMATU (SYMULACJA) ===\n");

        // 1. Inicjalizacja warstwy Modelu (Symulacja Bazy Danych)
        IDAO dao = new DAO();
        Inwentarz inwentarz = new Inwentarz(dao);
        IModel model = new Model(inwentarz, dao);

        // 2. Inicjalizacja warstwy Kontrolera
        IKontrolerKlienta kontrolerKlienta = new KontrolerKlienta(model);

        // 3. SCENARIUSZ A: Poprawna weryfikacja
        System.out.println("\n--- [SCENARIUSZ A] Poprawne logowanie ---");
        kontrolerKlienta.weryfikacjaTozsamosci(); // W środku zasymulowany poprawny PIN

        // 4. SCENARIUSZ B: Atak (Błędne PIN-y -> Blokada)
        System.out.println("\n--- [SCENARIUSZ B] Próba ataku (Wielokrotny błąd) ---");
        // Symulujemy sytuację, gdzie kontroler wymusza błędy
        // W prawdziwej aplikacji byłaby to pętla w UI, tu symulujemy wywołanie metody "błędnej"
        ((KontrolerKlienta) kontrolerKlienta).symulujAtakNaPin();

        // 5. Koniec
        System.out.println("\n=== KONIEC SYSTEMU ===");
    }
}