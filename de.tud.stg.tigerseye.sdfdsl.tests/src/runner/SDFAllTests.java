package runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sdf.general.GrammarTest1;
import sdf.general.GrammarTest2;
import sdf.test.ArithExprSdfTest;
import sdf.test.CharacterClassTest;
import sdf.test.ImportTest;
import sdf.test.SdfDslGrammarTest;
import sdf.test.SimpleArithExprSdfTest;
import sdf.test.SimpleSdfTest;
import de.tud.stg.popart.builder.tests.dsls.sdf.BnfDSLGrammarTest;
import de.tud.stg.popart.builder.tests.dsls.sdf.BnfDSLSimplerGrammarTest;
import de.tud.stg.popart.builder.tests.dsls.sdf.BnfDSLVerySimpleGrammarTest;
import de.tud.stg.popart.builder.tests.dsls.sdf.BnfDSLVerySimpleGrammarWithBracketsTest;
import de.tud.stg.popart.builder.tests.dsls.sdf.ImprovedBnfDSLGrammarTest;

@RunWith(Suite.class)
@SuiteClasses({
	
	BnfDSLVerySimpleGrammarTest.class, //
	ImprovedBnfDSLGrammarTest.class, //
	BnfDSLVerySimpleGrammarWithBracketsTest.class, //
	BnfDSLSimplerGrammarTest.class, //
	BnfDSLGrammarTest.class, //
	CharacterClassTest.class, //
	ImportTest.class, //
	SimpleSdfTest.class, //
	SdfDslGrammarTest.class, //
	SimpleArithExprSdfTest.class, //
	ArithExprSdfTest.class, //
	GrammarTest1.class, //
	GrammarTest2.class, //

	
})
public class SDFAllTests {
}
