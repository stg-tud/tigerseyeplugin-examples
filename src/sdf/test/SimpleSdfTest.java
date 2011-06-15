package sdf.test;

import junit.framework.TestCase;
import org.junit.*;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

import sdf.SdfDSL;
import sdf.model.*;

/*
 module LeesPlank

 exports
 context-free start-symbols LeesPlank
 sorts Aap Noot Mies LeesPlank
 lexical syntax
 "aap"         -> Aap
 "noot"        -> Noot
 "mies"        -> Mies
 Aap Noot Mies -> LeesPlank
 */

public class SimpleSdfTest extends TestCase {
	
	SdfDSL sdf;
	Grammar grammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sorts(new SortSymbol[]{
				sdf.sortSymbol("Aap"),
				sdf.sortSymbol("Noot"),
				sdf.sortSymbol("Mies"),
				sdf.sortSymbol("LeesPlank")
		});
		
		LexicalSyntax lexSyntax = sdf.lexicalSyntax(new Production[]{
				sdf.production(new Symbol[]{ sdf.caseSensitiveliteralSymbol("aap") }, sdf.sortSymbol("Aap")),
				sdf.production(new Symbol[]{ sdf.caseSensitiveliteralSymbol("noot") }, sdf.sortSymbol("Noot")),
				sdf.production(new Symbol[]{ sdf.caseSensitiveliteralSymbol("mies") }, sdf.sortSymbol("Mies")),
				sdf.production(new Symbol[]{ sdf.sortSymbol("Aap"), sdf.sortSymbol("Noot"), sdf.sortSymbol("Mies") }, sdf.sortSymbol("LeesPlank")),
//				sdf.production(new Symbol[]{ sdf.caseInsensitiveLiteralSymbol(" ") }, sdf.sortSymbol("LAYOUT")),
		});
		
		ContextFreeStartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[]{
				sdf.sortSymbol("LeesPlank")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			startSymbols,
			sorts,
			lexSyntax
		});
		
		Module module = sdf.moduleWithoutParameters("LeesPlank", new Imports[]{}, new ExportOrHiddenSection[]{ exports });
		
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
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
