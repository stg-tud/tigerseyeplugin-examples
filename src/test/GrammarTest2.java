package test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import de.tud.stg.parlex.core.*;
import de.tud.stg.parlex.core.groupcategories.IntegerCategory;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

/*
 * BNF Test Grammar:
 * 
	expression	::= plus | minus
	plus		::= number "+" number
	minus		::= number "-" number
	number		::= "digit" { "digit" }
 *
 * Rules:
 * 
 * S			-> EXPRESSION
 * EXPRESSION	-> PLUS
 * EXPRESSION	-> MINUS
 * PLUS			-> NUMBER "+" NUMBER
 * MINUS		-> NUMBER "-" NUMBER
 * NUMBER		-> "digit"
 * NUMBER		-> NUMBER "digit"
 */

public class GrammarTest2 extends TestCase {

	Grammar grammar;
	Category S, EXPRESSION, PLUS, MINUS, NUMBER, digit, plusSign, minusSign;
	Rule rEXPRESSION1, rEXPRESSION2, rPLUS, rMINUS, rNUMBER1, rNUMBER2, rS;
	
	@Before
	public void setUp() {
		grammar = new Grammar();
		
		S = new Category("S", false);
		EXPRESSION = new Category("EXPRESSION", false);
		PLUS = new Category("PLUS", false);
		MINUS = new Category("MINUS", false);
		NUMBER = new Category("NUMBER", false);
		//NUMBER = new IntegerCategory();
		digit = new Category("digit", true);
		plusSign = new Category("+", true);
		minusSign = new Category("-", true);
		
		rS = new Rule(S, EXPRESSION);
		rEXPRESSION1 = new Rule(EXPRESSION, PLUS);
		rEXPRESSION2 = new Rule(EXPRESSION, MINUS);
		rPLUS = new Rule(PLUS, NUMBER, plusSign, NUMBER);
		rMINUS = new Rule(MINUS, NUMBER, minusSign, NUMBER);
		rNUMBER1 = new Rule(NUMBER, digit);
		rNUMBER2 = new Rule(NUMBER, NUMBER, digit);
		
		grammar.addCategories(S, EXPRESSION, PLUS, MINUS, NUMBER, digit, plusSign, minusSign);
		grammar.addRules(rEXPRESSION1, rEXPRESSION2, rPLUS, rMINUS, rNUMBER1, rNUMBER2, rS);
		grammar.setStartRule(rS);
	}
	
	@Test
	public void testGrammar2() {
		assertEquals(rS, grammar.getStartRule());
		assertEquals(8, grammar.getCategories().size());
		assertEquals(7, grammar.getRules().size());
		
		System.out.println("== GrammarTest2 == ");
		System.out.println(grammar.toString());
	}
	
	@Test
	public void testEarleyParserGrammar2() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("digit+digitdigit");
		chart.rparse(this.rS);
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
	}
	
}
