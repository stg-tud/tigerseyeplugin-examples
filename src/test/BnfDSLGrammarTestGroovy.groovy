package test;

import junit.framework.TestCase;

import org.junit.*;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.popart.builder.test.dsls.*;



/*
 * BNF Test Grammar:
 *
	math		::= { expression }
	expression	::= plus | minus
	plus		::= number "+" number
	minus		::= number "-" number
	number		::= digit { digit }
 *
 */

class BnfDSLGrammarTestGroovy extends TestCase {

	private Grammar grammar;
	
	@Before
	public void setUp() {
		BnfDSL dsl = new BnfDSL();
		
		Closure program = {
			syntax([
				rule(
					identifierFromLetters(letters("math")),
					expression([termFromFactors([factorFromExpressionInBraces(
						expression([termFromFactors([factorFromIdentifier(identifierFromLetters(letters("expression")))] as BnfDSL.Factor[])] as BnfDSL.Term[])
					)] as BnfDSL.Factor[])] as BnfDSL.Term[])
				)
			] as BnfDSL.Rule[])
		}
		
		program.delegate = dsl
		program()

	}
	
	@Test
	public void testFoo() {
		System.out.println(grammar.toString());
		assertEquals(5, grammar.getRules().size());
	}
	
	private BnfDSL.Letter[] letters(String str) {
		BnfDSL.Letter[] arr = new BnfDSL.Letter[str.length()];
		for (int i = 0; i < str.length(); i++) {
			arr[i] = new BnfDSL.Letter(str.substring(i, i+1));
		}
		return arr;
	}


}
