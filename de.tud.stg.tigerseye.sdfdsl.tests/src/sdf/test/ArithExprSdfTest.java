package sdf.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import sdf.GeneratedGrammar;
import sdf.ParseResult;
import sdf.SdfDSL;
import sdf.model.ExportOrHiddenSection;
import sdf.model.Exports;
import sdf.model.GrammarElement;
import sdf.model.Imports;
import sdf.model.Module;
import sdf.model.ModuleId;
import sdf.model.Production;
import sdf.model.SortSymbol;
import sdf.model.Sorts;
import sdf.model.StartSymbols;
import sdf.model.Symbol;
import sdf.model.Syntax;
import de.tud.stg.parlex.core.Grammar;

/**
 * 
 * Simple test case that uses a syntax definition consisting of one module. The
 * grammar recognizes arithmetic expressions containing numbers, +-/* operators
 * and brackets.
 * 
 * <p>
 * SDF:
 * 
 * <pre>
 * {@code
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
 * }
 * </pre>
 * 
 * 
 * @author Pablo Hoch
 * 
 */
public class ArithExprSdfTest {
	private static final String MAIN_MODULE_NAME = "ArithExpr";
	private SdfDSL sdf;
	private GeneratedGrammar generatedGrammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[] {
				sdf.sortSymbol("Expr"), sdf.sortSymbol("Number"),
				sdf.sortSymbol("Term"), sdf.sortSymbol("Factor") });

		Syntax lexSyntax = sdf.lexicalSyntax(new Production[] {
				sdf.production(new Symbol[] { sdf
						.repetitionSymbolAtLeastOnce(sdf
								.characterClassSymbol("0-9")) }, sdf
						.sortSymbol("Number")),
				sdf.production(new Symbol[] { sdf
						.repetitionSymbolAtLeastOnce(sdf
								.characterClassSymbol(" ")) }, sdf
						.sortSymbol("LAYOUT")), });

		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[] {
				sdf.production(
						new Symbol[] { sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol("+"),
								sdf.sortSymbol("Term") },
						sdf.sortSymbol("Expr")),
				sdf.production(
						new Symbol[] { sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol("-"),
								sdf.sortSymbol("Term") },
						sdf.sortSymbol("Expr")),
				sdf.production(new Symbol[] { sdf.sortSymbol("Term") },
						sdf.sortSymbol("Expr")),

				sdf.production(
						new Symbol[] { sdf.sortSymbol("Term"),
								sdf.caseSensitiveLiteralSymbol("*"),
								sdf.sortSymbol("Factor") },
						sdf.sortSymbol("Term")),
				sdf.production(
						new Symbol[] { sdf.sortSymbol("Term"),
								sdf.caseSensitiveLiteralSymbol("/"),
								sdf.sortSymbol("Factor") },
						sdf.sortSymbol("Term")),
				sdf.production(new Symbol[] { sdf.sortSymbol("Factor") },
						sdf.sortSymbol("Term")),

				sdf.production(new Symbol[] { sdf.sortSymbol("Number") },
						sdf.sortSymbol("Factor")),
				sdf.production(
						new Symbol[] { sdf.caseSensitiveLiteralSymbol("("),
								sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol(")"), },
						sdf.sortSymbol("Factor")), });

		StartSymbols startSymbols = sdf
				.contextFreeStartSymbols(new Symbol[] { sdf.sortSymbol("Expr") });

		Exports exports = sdf.exports(new GrammarElement[] { startSymbols,
				sorts, lexSyntax, cfSyntax });

		Module module = sdf.moduleWithoutParameters(new ModuleId(MAIN_MODULE_NAME),
				new Imports[] {}, new ExportOrHiddenSection[] { exports });

		generatedGrammar = sdf.getGrammar(MAIN_MODULE_NAME);
	}

	@Test
	public void testGrammar() {
		Grammar grammar = generatedGrammar.getGrammar();
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
		ParseResult result = sdf.parseString(MAIN_MODULE_NAME, "2+3*5");
		
		assertTrue(result.isValid());

		System.out.println("AST:");
		System.out.println(result.getParseTree());
		System.out.println();
	}

	@Test
	public void testEarleyParserWithExpr2() {
		System.out.println("== ArithExprSdfTest: Parser: 4 + 8 * (15 / (16+23)) - 42 ==");
		ParseResult result = sdf.parseString(MAIN_MODULE_NAME, "4 + 8 * (15 / (16+23)) - 42");
		
		assertTrue(result.isValid());

		System.out.println("AST:");
		System.out.println(result.getParseTree());
		System.out.println();
	}
}
