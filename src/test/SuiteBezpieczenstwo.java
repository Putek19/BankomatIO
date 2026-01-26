import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;

@Suite
@SuiteDisplayName("Suite Bezpieczenstwo - Testy mechanizmów bezpieczeństwa")
@SelectPackages({"Model", "Kontroler"})
@IncludeTags({"bezpieczenstwo", "blokada"})
@ExcludeTags("parametryzowany")
public class SuiteBezpieczenstwo {
}
