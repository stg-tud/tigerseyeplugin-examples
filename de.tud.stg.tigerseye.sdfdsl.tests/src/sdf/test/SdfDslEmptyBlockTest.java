package sdf.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jjtraveler.VisitFailure;

import org.apache.bsf.util.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sdf.SdfDSL;
import sdf.util.GrammarDebugPrinter;
import de.tud.stg.parlex.core.IGrammar;
import de.tud.stg.tigerseye.dslsupport.DSL;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.GrammarBuilder;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.UnicodeLookupTable;
import de.tud.stg.tigerseye.test.PrettyGroovyCodePrinterFactory;
import de.tud.stg.tigerseye.test.TestDSLTransformation;
import de.tud.stg.tigerseye.test.TransformationUtils;

/**
 * Simple test case that uses a very simple input file with an empty sdf block.
 * This should not cause an exception during transformation.
 * This test case previously raised an exception in the Earley parser due to
 * incorrect handling of water rules.
 * 
 * @author Pablo Hoch
 *
 */
public class SdfDslEmptyBlockTest {

	private static final Logger logger = LoggerFactory
			.getLogger(SdfDslGrammarTest.class);

	private final UnicodeLookupTable ult = TransformationUtils.getDefaultLookupTable();

	@Test
	public void testGeneratedGrammar() throws VisitFailure {

		GrammarBuilder gb = new GrammarBuilder(ult);

		List<Class<? extends DSL>> classList = new ArrayList<Class<? extends DSL>>();
		classList.add(SdfDSL.class);
		IGrammar<String> grammar = gb.buildGrammar(classList);
		
		String testInput = "sdf(name:'Fail'){}";
		
		String s = transform(testInput, SdfDSL.class);
		System.out.println(s);

	}

	private String transform(String input, Class<? extends DSL>... classes)
			throws VisitFailure {
		TestDSLTransformation trans = new TestDSLTransformation(ult,
				new PrettyGroovyCodePrinterFactory());
		List<Class<? extends DSL>> classList = Arrays.asList(classes);

		return trans.performTransformation(input, classList);
	}
}
