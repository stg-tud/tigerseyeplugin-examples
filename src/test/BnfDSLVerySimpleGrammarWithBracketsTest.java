package test;

import junit.framework.TestCase;

import org.junit.*;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import de.tud.stg.popart.builder.test.dsls.*;

/*
 * BNF Test Grammar:
 * 
	FOO	::= "a" {"a"} "b"
 *
 */

public class BnfDSLVerySimpleGrammarWithBracketsTest extends TestCase {
	
	private Grammar grammar;

	@Before
	public void setUp() {
		BnfDSL dsl = new BnfDSL();
		
		BnfDSL.Expression fooRHS = dsl.expression(new BnfDSL.Term[]{
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("a")})),
					dsl.factorFromExpressionInBraces(
						dsl.expression(new BnfDSL.Term[]{
								dsl.termFromFactors(new BnfDSL.Factor[]{
										dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("a")})),
								})
						})	
					),
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("b")})),
			})
		});
		
		BnfDSL.Rule fooRule = dsl.rule(dsl.identifierFromLetters(letters("FOO")), fooRHS);
		
		BnfDSL.Syntax syntax = dsl.syntax(new BnfDSL.Rule[]{
			fooRule
		});
		
		grammar = syntax.getGrammar();
	}
	
	@Test
	public void testFoo() {
		System.out.println("== BnfDSLVerySimpleGrammarWithBracketsTest == ");
		System.out.println(grammar.toString());
		
	}
	
	@Test
	public void testEarleyParserWithVerySimpleWithBracketsGrammar() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("aaab");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
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
