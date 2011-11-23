package sdf.test;

import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aterm.ATerm;

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
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

/**
 * 
 * Simple test case that uses a syntax definition consisting of one module. The
 * grammar recognizes arithmetic expressions containing numbers and +-
 * operators. This version uses production attributes.
 * 
 * <p>
 * SDF:
 * 
 * <pre>
 * {@code
 * module SimpleArithExprWithAttributes
 * exports
 * context-free start-symbols Expr
 * sorts Expr Number
 * 
 * lexical syntax
 * [0-9]+				-> Number
 * [ ]+					-> LAYOUT
 * 
 * context-free syntax
 * Expr "+" Expr		-> Expr {left}
 * Expr "+" Expr		-> Expr {cons("add")}			%% to test production merging
 * Expr "-" Expr		-> Expr {left}
 * Number				-> Expr {custom("test",123)}	%% to test custom aterm attributes
 * }
 * </pre>
 * 
 * 
 * @author Pablo Hoch
 * 
 */
public class SimpleArithExprWithAttributesTest {
	SdfDSL sdf;
	GeneratedGrammar generatedGrammar;
	
	private static final String MAIN_MODULE_NAME = "SimpleArithExprWithAttributes";

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(
				new SortSymbol[] {
						sdf.sortSymbol("Expr"),
						sdf.sortSymbol("Number")
				});

		Syntax lexSyntax = sdf.lexicalSyntax(new Production[] { sdf.production(
				new Symbol[] { sdf.repetitionSymbolAtLeastOnce(sdf
						.characterClassSymbol("0-9")) }, sdf
						.sortSymbol("Number")),
		// sdf.production(new Symbol[]{
		// sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol(" ")) },
		// sdf.sortSymbol("LAYOUT")),
				});

		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[] {
				sdf.productionWithAttributes(
						new Symbol[] { sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol("+"),
								sdf.sortSymbol("Expr") },
						sdf.sortSymbol("Expr"),
						new ATerm[] {
							sdf.customATerm("left")
						}),
				sdf.productionWithAttributes(
						new Symbol[] { sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol("+"),
								sdf.sortSymbol("Expr") },
								sdf.sortSymbol("Expr"),
						new ATerm[] {
							sdf.customATerm("cons(\"add\")")
						}),
				sdf.productionWithAttributes(
						new Symbol[] { sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol("-"),
								sdf.sortSymbol("Expr") },
						sdf.sortSymbol("Expr"),
						new ATerm[] {
							sdf.customATerm("left")
						}),
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Number")
						},
						sdf.sortSymbol("Expr"),
						new ATerm[] {
							sdf.customATerm("custom(\"test\",123)")
						}), });

		StartSymbols startSymbols = sdf
				.contextFreeStartSymbols(new Symbol[] { sdf.sortSymbol("Expr") });

		Exports exports = sdf.exports(new GrammarElement[] { startSymbols,
				sorts, lexSyntax, cfSyntax });

		Module module = sdf.moduleWithoutParameters(new ModuleId(
				MAIN_MODULE_NAME), new Imports[] {},
				new ExportOrHiddenSection[] { exports });

		generatedGrammar = sdf.getGrammar(MAIN_MODULE_NAME);
	}

	@Test
	public void testGrammar() {
		Grammar grammar = generatedGrammar.getGrammar();
		System.out.println("== SimpleArithExprWithAttributesTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
		
		sdf.printGeneratedGrammarHTML(MAIN_MODULE_NAME, "src/sdf/test/SimpleArithExprWithAttributesTest.html");
	}

	@Test
	public void testEarleyParserWithExpr1() {
		System.out.println("== SimpleArithExprWithAttributesTest: Parser ==");
		ParseResult result = sdf.parseString(MAIN_MODULE_NAME, "2+3+4");
		
		assertTrue(result.isValid());

		System.out.println("AST:");
		System.out.println(result.getParseTree());
		System.out.println();
	}
}
