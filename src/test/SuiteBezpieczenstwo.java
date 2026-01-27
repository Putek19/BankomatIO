package test;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Zestaw testów bezpieczeństwa - testy oznaczone tagiem "bezpieczenstwo".
 * Wyklucza testy z tagiem "mock" (testy z symulacją).
 * 
 * Zastosowanie praktyczne: uruchamianie testów związanych z bezpieczeństwem
 * bez testów wykorzystujących mockowanie, które mogą nie odzwierciedlać
 * rzeczywistego zachowania systemu.
 */
@Suite
@SuiteDisplayName("Zestaw testów bezpieczeństwa (bez mocków)")
@SelectPackages({ "Model", "Kontroler" })
@IncludeTags("bezpieczenstwo")
@ExcludeTags("mock")
public class SuiteBezpieczenstwo {
    // Zestaw testów bezpieczeństwa
    // Uruchamia wszystkie testy z tagiem "bezpieczenstwo"
    // Wyklucza testy z tagiem "mock"
}
