package sdf.test;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sdf.SdfDSL;
import sdf.model.ContextFreeStartSymbols;
import sdf.model.ExportOrHiddenSection;
import sdf.model.Exports;
import sdf.model.GrammarElement;
import sdf.model.Imports;
import sdf.model.LexicalSyntax;
import sdf.model.Module;
import sdf.model.ModuleId;
import sdf.model.Production;
import sdf.model.SortSymbol;
import sdf.model.Sorts;
import sdf.model.Symbol;
import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

/**
 * A very simple test case using a simple grammar that only accepts one specific
 * string. The example is taken from the SDF documentation.
 * 
 * <pre>
 * {@code
 *  module LeesPlank
 * 
 *  exports
 *  context-free start-symbols LeesPlank
 *  sorts Aap Noot Mies LeesPlank
 *  lexical syntax
 *  "aap"         -> Aap
 *  "noot"        -> Noot
 *  "mies"        -> Mies
 *  Aap Noot Mies -> LeesPlank
 * }
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a
 *      href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#example.simplelex">Source
 *      of the example</a>
 * 
 */
public class SimpleSdfTest {

	SdfDSL sdf;
	Grammar grammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[] {
				sdf.sortSymbol("Aap"), sdf.sortSymbol("Noot"),
				sdf.sortSymbol("Mies"), sdf.sortSymbol("LeesPlank") });

		LexicalSyntax lexSyntax = sdf
				.lexicalSyntax(new Production[] {
						sdf.production(new Symbol[] { sdf
								.caseSensitiveLiteralSymbol("aap") }, sdf
								.sortSymbol("Aap")),
						sdf.production(new Symbol[] { sdf
								.caseSensitiveLiteralSymbol("noot") }, sdf
								.sortSymbol("Noot")),
						sdf.production(new Symbol[] { sdf
								.caseSensitiveLiteralSymbol("mies") }, sdf
								.sortSymbol("Mies")),
						sdf.production(
								new Symbol[] { sdf.sortSymbol("Aap"),
										sdf.sortSymbol("Noot"),
										sdf.sortSymbol("Mies") },
								sdf.sortSymbol("LeesPlank")),
				// sdf.production(new Symbol[]{
				// sdf.caseInsensitiveLiteralSymbol(" ") },
				// sdf.sortSymbol("LAYOUT")),
				});

		ContextFreeStartSymbols startSymbols = sdf
				.contextFreeStartSymbols(new Symbol[] { sdf
						.sortSymbol("LeesPlank") });

		Exports exports = sdf.exports(new GrammarElement[] { startSymbols,
				sorts, lexSyntax });

		Module module = sdf.moduleWithoutParameters(new ModuleId("LeesPlank"),
				new Imports[] {}, new ExportOrHiddenSection[] { exports });

		grammar = sdf.getGrammar("LeesPlank");
	}

	@Test
	public void testGrammar() {
		System.out.println("== SimpleSdfTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
	}

	@Test
	public void testEarleyParserWithBnfDSL() {
		System.out.println("== SimpleSdfTest: Parser ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("aapnootmies");
		chart.rparse((de.tud.stg.parlex.core.Rule) grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());

		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
