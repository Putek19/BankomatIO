package test;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.ExcludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("Zestaw testów z symulacją (Mockito)")
@SelectPackages({ "Model", "Kontroler" })
@IncludeTags("mock")
@ExcludeTags("saldo")
public class SuiteMock {

}
