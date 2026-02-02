package test;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
@Suite
@SuiteDisplayName("Zestaw testów bezpieczeństwa (bez mocków)")
@SelectPackages({ "Model", "Kontroler" })
@IncludeTags("bezpieczenstwo")
@ExcludeTags("mock")
public class SuiteBezpieczenstwo {

}
