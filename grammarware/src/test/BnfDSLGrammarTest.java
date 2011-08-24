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
	math		::= { expression }
	expression	::= plus | minus
	plus		::= number "+" number
	minus		::= number "-" number
	number		::= "digit" { "digit" }
 *
 */

public class BnfDSLGrammarTest extends TestCase {
	
	private Grammar grammar;

	@Before
	public void setUp() {
		BnfDSL dsl = new BnfDSL();
		
		BnfDSL.Expression mathRHS = dsl.expression(new BnfDSL.Term[]{
				dsl.termFromFactors(new BnfDSL.Factor[]{
						dsl.factorFromExpressionInBraces(
							dsl.expression(new BnfDSL.Term[]{
									dsl.termFromFactors(new BnfDSL.Factor[]{
											dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("expression")))
									})
							})	
						)
				})
		});
		BnfDSL.Rule mathRule = dsl.rule(dsl.identifierFromLetters(letters("math")), mathRHS);
		
		BnfDSL.Expression expressionRHS = dsl.expression(new BnfDSL.Term[]{
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("plus")))
			}),
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("minus")))
			}),
		});
		BnfDSL.Rule expressionRule = dsl.rule(dsl.identifierFromLetters(letters("expression")), expressionRHS);
		
		BnfDSL.Expression plusRHS = dsl.expression(new BnfDSL.Term[]{
			dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("number"))),
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("+")})),
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("number")))
			})	
		});
		BnfDSL.Rule plusRule = dsl.rule(dsl.identifierFromLetters(letters("plus")), plusRHS);
		
		BnfDSL.Expression minusRHS = dsl.expression(new BnfDSL.Term[]{
				dsl.termFromFactors(new BnfDSL.Factor[]{
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("number"))),
					dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("-")})),
					dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("number")))
			})	
		});
		BnfDSL.Rule minusRule = dsl.rule(dsl.identifierFromLetters(letters("minus")), minusRHS);
		
		BnfDSL.Expression numberRHS = dsl.expression(new BnfDSL.Term[]{
				dsl.termFromFactors(new BnfDSL.Factor[]{
						//dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("digit"))),
						dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("digit")})),
						dsl.factorFromExpressionInBraces(
							dsl.expression(new BnfDSL.Term[]{
									dsl.termFromFactors(new BnfDSL.Factor[]{
											//dsl.factorFromIdentifier(dsl.identifierFromLetters(letters("digit")))
											dsl.factorFromQuotedSymbol(dsl.quotedSymbolFromAnyCharacters(new BnfDSL.AnyCharacter[]{new BnfDSL.AnyCharacter("digit")}))
									})
							})	
						)
				})
		});
		BnfDSL.Rule numberRule = dsl.rule(dsl.identifierFromLetters(letters("number")), numberRHS);
		
		BnfDSL.Syntax syntax = dsl.syntax(new BnfDSL.Rule[]{
			mathRule, expressionRule, plusRule, minusRule, numberRule
		});
		
		grammar = syntax.getGrammar();
	}
	
	@Test
	public void testFoo() {
		System.out.println("== BnfDSLGrammarTest == ");
		System.out.println(grammar.toString());
		
	}
	
	@Test
	public void testEarleyParserWithBnfDSL() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("digitdigit+digitdigit");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
	}
	
	private BnfDSL.Letter[] letters(String str) {
		BnfDSL.Letter[] arr = new BnfDSL.Letter[str.length()];
		for (int i = 0; i < str.length(); i++) {
			arr[i] = new BnfDSL.Letter(str.substring(i, i+1));
		}
		return arr;
	}
	
}
