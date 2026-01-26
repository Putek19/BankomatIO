import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.IncludePackages;

@Suite
@SuiteDisplayName("Suite Kontroler - Testy warstwy kontroli")
@SelectPackages("Kontroler")
@IncludePackages("Kontroler")
public class SuiteKontroler {
}
