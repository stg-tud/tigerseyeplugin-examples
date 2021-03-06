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
 * Outputs the grammar generated by the GrammarBuilder for the SdfDSL class.
 * Also tries to transform a sample input file using the DSL.
 * 
 * @author Pablo Hoch
 * 
 */
public class SdfDslGrammarTest {

	private static final Logger logger = LoggerFactory
			.getLogger(SdfDslGrammarTest.class);

	private final UnicodeLookupTable ult = TransformationUtils.getDefaultLookupTable();

	@Test
	public void testGeneratedGrammar() {

		GrammarBuilder gb = new GrammarBuilder(ult);

		List<Class<? extends DSL>> classList = new ArrayList<Class<? extends DSL>>();
		classList.add(SdfDSL.class);
		IGrammar<String> grammar = gb.buildGrammar(classList);
		// IGrammar<String> grammar = gb.buildGrammar(SdfDSL.class);

		try {
			FileOutputStream fos = new FileOutputStream(
					"src/sdf/test/SdfDslGrammar.html");
			GrammarDebugPrinter gdp = new GrammarDebugPrinter(grammar, fos);
			gdp.printGrammar();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("test");

		try {
			// InputStream inputStream =
			// SdfDslGrammarTest.class.getResourceAsStream("resources/SdfDslGrammarTest.input");
			InputStream inputStream = new FileInputStream(
					"src/sdf/test/resources/SdfDslGrammarTest.input");
			InputStreamReader reader = new InputStreamReader(inputStream);
			String testInput = IOUtils.getStringFromReader(reader);
			inputStream.close();

			String s = transform(testInput, SdfDSL.class);
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String transform(String input, Class<? extends DSL>... classes)
			throws VisitFailure {
		TestDSLTransformation trans = new TestDSLTransformation(ult,
				new PrettyGroovyCodePrinterFactory());
		List<Class<? extends DSL>> classList = Arrays.asList(classes);

		return trans.performTransformation(input, classList);
	}
}
