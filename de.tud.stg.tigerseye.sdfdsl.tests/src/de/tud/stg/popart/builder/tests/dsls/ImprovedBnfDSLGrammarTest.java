package de.tud.stg.popart.builder.tests.dsls;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.core.Rule;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import de.tud.stg.popart.builder.test.dsls.ImprovedBnfDSL;

/*
 * BNF Test Grammar:
 * 
	math		::= { expression }
	expression	::= plus | minus
	plus		::= number "+" number
	minus		::= number "-" number
	number		::= digit { digit }
	TODO The class under test seems to e not finished.
 *
 */

//public class ImprovedBnfDSLGrammarTest extends TestCase {
//	
//	private Grammar grammar;
//	
//	private ImprovedBnfDSL.Rule startRule;
//
//	@Before
//	public void setUp() {
//		ImprovedBnfDSL dsl = new ImprovedBnfDSL();
//		
//        // math		::= { expression }
//		ImprovedBnfDSL.Expression mathRHS = dsl.expression(new ImprovedBnfDSL.Term[]{
//				dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//						dsl.factorFromExpressionInBraces(
//							dsl.expression(new ImprovedBnfDSL.Term[]{
//									dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//											dsl.factorFromIdentifier(identifier("expression"))
//									})
//							})	
//						)
//				})
//		});
//		ImprovedBnfDSL.Rule mathRule = dsl.rule(identifier("math"), mathRHS);
//		
//		startRule = mathRule;
//		
//        // expression	::= plus | minus
//		ImprovedBnfDSL.Expression expressionRHS = dsl.expression(new ImprovedBnfDSL.Term[]{
//			dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//					dsl.factorFromIdentifier(identifier("plus"))
//			}),
//			dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//					dsl.factorFromIdentifier(identifier("minus"))
//			}),
//		});
//		ImprovedBnfDSL.Rule expressionRule = dsl.rule(identifier("expression"), expressionRHS);
//
//        // plus		::= number "+" number
//		ImprovedBnfDSL.Expression plusRHS = dsl.expression(new ImprovedBnfDSL.Term[]{
//			dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//					dsl.factorFromIdentifier(identifier("number")),
//					dsl.factorFromQuotedSymbol(new ImprovedBnfDSL.QuotedSymbol("+")),
//					dsl.factorFromIdentifier(identifier("number"))
//			})	
//		});
//		ImprovedBnfDSL.Rule plusRule = dsl.rule(identifier("plus"), plusRHS);
//		
//        // minus		::= number "-" number
//		ImprovedBnfDSL.Expression minusRHS = dsl.expression(new ImprovedBnfDSL.Term[]{
//				dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//					dsl.factorFromIdentifier(identifier("number")),
//					dsl.factorFromQuotedSymbol(new ImprovedBnfDSL.QuotedSymbol("-")),
//					dsl.factorFromIdentifier(identifier("number"))
//			})	
//		});
//		ImprovedBnfDSL.Rule minusRule = dsl.rule(identifier("minus"), minusRHS);
//		
//        // number		::= digit { digit }
//		ImprovedBnfDSL.Expression numberRHS = dsl.expression(new ImprovedBnfDSL.Term[]{
//				dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//						//dsl.factorFromIdentifier(identifier("digit")),
//						dsl.factorFromQuotedSymbol(new ImprovedBnfDSL.QuotedSymbol("digit")),
//						dsl.factorFromExpressionInBraces(
//							dsl.expression(new ImprovedBnfDSL.Term[]{
//									dsl.termFromFactors(new ImprovedBnfDSL.Factor[]{
//											//dsl.factorFromIdentifier(identifier("digit"))
//											dsl.factorFromQuotedSymbol(new ImprovedBnfDSL.QuotedSymbol("digit"))
//									})
//							})	
//						)
//				})
//		});
//		ImprovedBnfDSL.Rule numberRule = dsl.rule(identifier("number"), numberRHS);
//		
//		ImprovedBnfDSL.Syntax syntax = dsl.syntax(new ImprovedBnfDSL.Rule[]{
//			mathRule, expressionRule, plusRule, minusRule, numberRule
//		});
//		
//		grammar = syntax.getGrammar();
//	}
//	
//	@Test
//	public void testFoo() {
//		System.out.println("== ImprovedBnfDSLGrammarTest == ");
//		System.out.println(grammar.toString());
//		
//	}
//	
//	@Test
//	public void testEarleyParserWithImprovedBnfDSL() {
//		System.out.println(grammar.toString());
//		EarleyParser parser = new EarleyParser(grammar);
//		
//		
//		Chart chart = (Chart) parser.parse("digit+digit+digit");
//		chart.rparse((Rule)grammar.getStartRule()); 
//		System.out.println(chart.toString());
//		assertTrue("No valid parser",chart.isValidParse());
//	}
//	
//	
//	private ImprovedBnfDSL.Identifier identifier(String str) {
//		return new ImprovedBnfDSL.Identifier(str);
//	}
//	
//
//	
//}
