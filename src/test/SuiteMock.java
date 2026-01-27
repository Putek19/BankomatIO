package test;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Zestaw testów z użyciem symulacji (mockowania).
 * Testy oznaczone tagiem "mock" - wykorzystują Mockito.
 * Wyklucza testy z tagiem "saldo" (testy operujące na rzeczywistym saldzie).
 * 
 * Zastosowanie praktyczne: uruchamianie tylko testów z mockowaniem,
 * które są szybsze i izolowane od rzeczywistych zależności.
 * Wykluczenie testów saldo pozwala skupić się na testach logiki biznesowej.
 */
@Suite
@SuiteDisplayName("Zestaw testów z symulacją (Mockito)")
@SelectPackages({ "Model", "Kontroler" })
@IncludeTags("mock")
@ExcludeTags("saldo")
public class SuiteMock {
    // Zestaw testów z symulacją (mockowanie)
    // Uruchamia wszystkie testy z tagiem "mock"
    // Wyklucza testy z tagiem "saldo"
}
