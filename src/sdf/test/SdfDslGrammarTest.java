package sdf.test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jjtraveler.VisitFailure;

import org.junit.Test;

import aterm.ATerm;

import sdf.SdfDSL;
import sdf.util.GrammarDebugPrinter;

import de.tud.stg.parlex.ast.IAbstractNode;
import de.tud.stg.parlex.core.IGrammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import de.tud.stg.tigerseye.eclipse.core.builder.transformers.Context;
import de.tud.stg.tigerseye.eclipse.core.builder.transformers.ast.KeywordChainingTransformation;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.GrammarBuilder;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.UnicodeLookupTable;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.aterm.ATermBuilder;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.aterm.PrettyGroovyCodePrinter;
import de.tud.stg.tigerseye.test.TestUtils;

import junit.framework.TestCase;

public class SdfDslGrammarTest extends TestCase {

	@Test
	public void testGeneratedGrammar() {
		
		UnicodeLookupTable ult = TestUtils.getDefaultLookupTable();
		GrammarBuilder gb = new GrammarBuilder(ult);
		
		IGrammar<String> grammar = gb.buildGrammar(SdfDSL.class);
		
		try {
			FileOutputStream fos = new FileOutputStream("src/sdf/test/SdfDslGrammar.html");
			GrammarDebugPrinter gdp = new GrammarDebugPrinter(grammar, fos);
			gdp.printGrammar();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String testInput = "module LeesPlank\n"+
			"imports Test\n" +
			"exports\n" +
			"context-free start-symbols LeesPlank\n" +
			"sorts Aap Noot Mies LeesPlank\n" +
			"lexical syntax\n" +
			"\"aap\"			-> Aap\n" +
			"\"noot\"			-> Noot\n" +
			"\"mies\"			-> Mies\n" + 
			"App Noot Mies	-> LeesPlank\n";
		
		EarleyParser parser = new EarleyParser(null, grammar);
		Chart chart = (Chart)parser.parse(testInput);
		
//		System.out.println(chart);
		
		Context context = new Context("SdfDslGrammarTest");
		context.addDSL("sdf", SdfDSL.class);
		
		IAbstractNode ast;
		ATermBuilder aTermBuilder;
		ATerm term;
		ast = chart.getAST();
		chart.nextAmbiguity();

		aTermBuilder = new ATermBuilder(ast);
		term = aTermBuilder.getATerm();

		term = new KeywordChainingTransformation().transform(gb.getMethodOptions(), term);

		PrettyGroovyCodePrinter prettyPrinter = new PrettyGroovyCodePrinter();

		try {
			term.accept(prettyPrinter);
		} catch (VisitFailure e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		prettyPrinter.write(out);			
		String s = new String(out.toByteArray());
		
		System.out.println();
		System.out.println();
		System.out.println(s);
		
	}
}
