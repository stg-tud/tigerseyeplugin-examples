package de.tud.stg.popart.builder.tests.dsls.sdf;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import de.tud.stg.popart.builder.test.dsls.sdf.BnfDSL;

public class BnfDSLVerySimpleGrammarTest  {
	
	private Grammar grammar;

	@Before
	public void setUp() {
		BnfDSL dsl = new BnfDSL();
		
		BnfDSL.Expression fooRHS = dsl.expression(new BnfDSL.Term[]{
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("BAR"))),
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("b")})),
			})	
		});
		
		BnfDSL.Rule fooRule = dsl.rule(dsl.identifierFromLetters(letters("FOO")), fooRHS);
		
		BnfDSL.Expression barRHS = dsl.expression(new BnfDSL.Term[]{
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("a")})),
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("BAR"))),
			}),
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("a")})),
			}),
		});
		BnfDSL.Rule barRule = dsl.rule(dsl.identifierFromLetters(letters("BAR")), barRHS);
		
		BnfDSL.Syntax syntax = dsl.syntax(new BnfDSL.Rule[]{
			fooRule, barRule
		});
		Grammar grammar2 = syntax.getGrammar();
		grammar = grammar2;
	}
	
	@Test
	public void testEarleyParserWithVerySimpleGrammar() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("aaab");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
//		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
//		System.out.println("AST:");
//		System.out.println(chart.getAST().toString());
	}
	
	@Test
	public void testInvalidParse1() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("aa");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		assertFalse(chart.isValidParse());
	}
	
	@Test
	public void testInvalidParse2() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("b");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		assertFalse(chart.isValidParse());
	}
	
	private BnfDSL.Letter[] letters(String str) {
		BnfDSL.Letter[] arr = new BnfDSL.Letter[str.length()];
		for (int i = 0; i < str.length(); i++) {
			arr[i] = new BnfDSL.Letter(str.substring(i, i+1));
		}
		return arr;
	}
	
}
