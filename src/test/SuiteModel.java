package test;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Zestaw testów klas warstwy encji (Model).
 * Zawiera wszystkie testy z pakietu Model.
 */
@Suite
@SuiteDisplayName("Zestaw testów warstwy encji (Model)")
@SelectPackages("Model")
public class SuiteModel {
    // Zestaw testów warstwy encji (Model)
    // Uruchamia wszystkie testy z pakietu Model
}
