package sdf.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sdf.SdfDSL;
import sdf.model.ContextFreeStartSymbols;
import sdf.model.ContextFreeSyntax;
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
import sdf.model.StartSymbols;
import sdf.model.Symbol;
import sdf.model.Syntax;
import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import junit.framework.TestCase;

/**
 * 
 * Simple test case that uses a syntax definition consisting of one module. The grammar
 * recognizes arithmetic expressions containing numbers, +-/* operators and brackets.
 * 
 * SDF:
 * <code>
 * module ArithExpr
 * exports
 * context-free start-symbols Expr
 * sorts Expr Number Term Factor
 * 
 * lexical syntax
 * [0-9]+				-> Number
 * [ ]+					-> LAYOUT
 * 
 * context-free syntax
 * Expr "+" Term		-> Expr
 * Expr "-" Term		-> Expr
 * Term					-> Expr
 * 
 * Term "*" Factor		-> Term
 * Term "/" Factor		-> Term
 * Factor				-> Term
 * 
 * Number				-> Factor
 * "(" Expr ")"			-> Factor
 * 
 * 
 * </code>
 * 
 * 
 * @author Pablo Hoch
 *
 */
public class ArithExprSdfTest extends TestCase {
	SdfDSL sdf;
	Grammar grammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[]{
				sdf.sortSymbol("Expr"),
				sdf.sortSymbol("Number"),
				sdf.sortSymbol("Term"),
				sdf.sortSymbol("Factor")
		});
		
		Syntax lexSyntax = sdf.lexicalSyntax(new Production[]{
				sdf.production(new Symbol[]{ sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol("0-9")) }, sdf.sortSymbol("Number")),
				sdf.production(new Symbol[]{ sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol(" ")) }, sdf.sortSymbol("LAYOUT")),
		});
		
		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[]{
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Expr"), sdf.caseSensitiveLiteralSymbol("+"), sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Expr"), sdf.caseSensitiveLiteralSymbol("-"), sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term"), sdf.caseSensitiveLiteralSymbol("*"), sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term"), sdf.caseSensitiveLiteralSymbol("/"), sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Number")
				}, sdf.sortSymbol("Factor")),
				sdf.production(new Symbol[]{
						sdf.caseSensitiveLiteralSymbol("("), sdf.sortSymbol("Expr"), sdf.caseSensitiveLiteralSymbol(")"),
				}, sdf.sortSymbol("Factor")),
		});
		
		StartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[]{
				sdf.sortSymbol("Expr")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			startSymbols,
			sorts,
			lexSyntax,
			cfSyntax
		});
		
		Module module = sdf.moduleWithoutParameters(new ModuleId("ArithExpr"), new Imports[]{}, new ExportOrHiddenSection[]{ exports });
		
		grammar = sdf.getGrammar("ArithExpr");
	}
	
	@Test
	public void testGrammar() {
		System.out.println("== ArithExprSdfTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
		
		assertNotNull("start rule null", grammar.getStartRule());
		assertTrue("no categories", grammar.getCategories().size() > 0);
		assertTrue("no rules", grammar.getRules().size() > 0);
	}
	
	@Test
	public void testEarleyParserWithExpr1() {
		System.out.println("== ArithExprSdfTest: Parser: 2+3*5 ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("2+3*5");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
	
	@Test
	public void testEarleyParserWithExpr2() {
		System.out.println("== ArithExprSdfTest: Parser: 4 + 8 * (15 / (16+23)) - 42 ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("4 + 8 * (15 / (16+23)) - 42");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
