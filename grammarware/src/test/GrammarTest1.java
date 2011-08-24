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
	math		::= { expression }
	expression	::= plus | minus
	plus		::= number "+" number
	minus		::= number "-" number
	number		::= "digit" { "digit" }
 *
 * Rules:
 * 
 * S			-> MATH
 * MATH			-> ε
 * MATH			-> MATH EXPRESSION
 * EXPRESSION	-> PLUS
 * EXPRESSION	-> MINUS
 * PLUS			-> NUMBER "+" NUMBER
 * MINUS		-> NUMBER "-" NUMBER
 * NUMBER		-> "digit"
 * NUMBER		-> NUMBER "digit"
 */

public class GrammarTest1 extends TestCase {

	Grammar grammar;
	Category S, MATH, EXPRESSION, PLUS, MINUS, NUMBER, digit, plusSign, minusSign;
	Rule rMATH1, rMATH2, rEXPRESSION1, rEXPRESSION2, rPLUS, rMINUS, rNUMBER1, rNUMBER2, rS;
	
	@Before
	public void setUp() {
		grammar = new Grammar();
		
		S = new Category("S", false);
		MATH = new Category("MATH", false);
		EXPRESSION = new Category("EXPRESSION", false);
		PLUS = new Category("PLUS", false);
		MINUS = new Category("MINUS", false);
		NUMBER = new Category("NUMBER", false);
		//NUMBER = new IntegerCategory();
		digit = new Category("digit", true);
		plusSign = new Category("+", true);
		minusSign = new Category("-", true);
		
		rS = new Rule(S, MATH);
		rMATH1 = new Rule(MATH);
		rMATH2 = new Rule(MATH, MATH, EXPRESSION);
		rEXPRESSION1 = new Rule(EXPRESSION, PLUS);
		rEXPRESSION2 = new Rule(EXPRESSION, MINUS);
		rPLUS = new Rule(PLUS, NUMBER, plusSign, NUMBER);
		rMINUS = new Rule(MINUS, NUMBER, minusSign, NUMBER);
		rNUMBER1 = new Rule(NUMBER, digit);
		rNUMBER2 = new Rule(NUMBER, NUMBER, digit);
		
		grammar.addCategories(S, MATH, EXPRESSION, PLUS, MINUS, NUMBER, digit, plusSign, minusSign);
		grammar.addRules(rMATH1, rMATH2, rEXPRESSION1, rEXPRESSION2, rPLUS, rMINUS, rNUMBER1, rNUMBER2, rS);
		grammar.setStartRule(rS);
	}
	
	@Test
	public void testGrammar1() {
		assertEquals(rS, grammar.getStartRule());
		assertEquals(9, grammar.getCategories().size());
		assertEquals(9, grammar.getRules().size());
		
		System.out.println("== GrammarTest1 == ");
		System.out.println(grammar.toString());
	}
	
	@Test
	public void testEarleyParser1() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("digit+digitdigit");
		chart.rparse(this.rS);
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
	}
	
}
