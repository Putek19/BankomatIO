import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.IncludePackages;

@Suite
@SuiteDisplayName("Suite Model - Testy warstwy encji")
@SelectPackages("Model")
@IncludePackages("Model")
public class SuiteModel {
}
