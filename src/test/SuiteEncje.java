import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;

@Suite
@SuiteDisplayName("Suite Encje - Testy encji danych bez blokad")
@SelectPackages({"Model", "Kontroler"})
@IncludeTags("encja")
@ExcludeTags("blokada")
public class SuiteEncje {
}
