package sdf.test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aterm.ATerm;

import sdf.SdfDSL;
import sdf.model.ExportOrHiddenSection;
import sdf.model.Exports;
import sdf.model.GrammarElement;
import sdf.model.Imports;
import sdf.model.Module;
import sdf.model.ModuleId;
import sdf.model.Priorities;
import sdf.model.Priority;
import sdf.model.PriorityGroup;
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
 * grammar recognizes arithmetic expressions containing numbers, +-/* operators
 * and brackets.
 * This version uses attributes and priorities.
 * 
 * <p>
 * SDF:
 * 
 * <pre>
 * {@code
 * module ArithExprWithPriorities
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
 * Expr "-" Expr		-> Expr {left}
 * Expr "*" Expr		-> Expr {left}
 * Expr "/" Expr		-> Expr {left}
 * Number				-> Expr
 * "(" Expr ")"			-> Expr
 * 
 * context-free priorities
 * &#123;
 *   Expr "*" Expr		-> Expr {left}
 *   Expr "/" Expr		-> Expr {left}
 * &#125; > &#123;
 *   Expr "+" Expr		-> Expr {left}
 *   Expr "-" Expr		-> Expr {left}
 * &#125;
 * 
 * }
 * </pre>
 * 
 * 
 * @author Pablo Hoch
 * 
 */
public class ArithExprWithPrioritiesTest {
	SdfDSL sdf;
	Grammar grammar;
	
	private static final String MAIN_MODULE_NAME = "ArithExprWithPriorities";

	@Before
	public void setUp() {
		sdf = new SdfDSL();
		
		ATerm[] leftAttribute = new ATerm[] { sdf.customATerm("left") };

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[] {
				sdf.sortSymbol("Expr"), sdf.sortSymbol("Number")});

		Syntax lexSyntax = sdf.lexicalSyntax(new Production[] {
				sdf.production(new Symbol[] { sdf
						.repetitionSymbolAtLeastOnce(sdf
								.characterClassSymbol("0-9")) }, sdf
						.sortSymbol("Number")),
				sdf.production(new Symbol[] { sdf
						.repetitionSymbolAtLeastOnce(sdf
								.characterClassSymbol(" ")) }, sdf
						.sortSymbol("LAYOUT")), });

		Production prodPlus = sdf.productionWithAttributes(
				new Symbol[] { sdf.sortSymbol("Expr"),
						sdf.caseSensitiveLiteralSymbol("+"),
						sdf.sortSymbol("Expr") },
				sdf.sortSymbol("Expr"),
				leftAttribute);
		
		Production prodMinus = sdf.productionWithAttributes(
				new Symbol[] { sdf.sortSymbol("Expr"),
						sdf.caseSensitiveLiteralSymbol("-"),
						sdf.sortSymbol("Expr") },
				sdf.sortSymbol("Expr"),
				leftAttribute);
		
		Production prodMult = sdf.productionWithAttributes(
				new Symbol[] { sdf.sortSymbol("Expr"),
						sdf.caseSensitiveLiteralSymbol("*"),
						sdf.sortSymbol("Expr") },
				sdf.sortSymbol("Expr"),
				leftAttribute);
		
		Production prodDiv = sdf.productionWithAttributes(
				new Symbol[] { sdf.sortSymbol("Expr"),
						sdf.caseSensitiveLiteralSymbol("/"),
						sdf.sortSymbol("Expr") },
				sdf.sortSymbol("Expr"),
				leftAttribute);
		
		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[] {
				prodPlus,
				prodMinus,
				prodMult,
				prodDiv,

				sdf.production(new Symbol[] { sdf.sortSymbol("Number") },
						sdf.sortSymbol("Expr")),
				sdf.production(
						new Symbol[] { sdf.caseSensitiveLiteralSymbol("("),
								sdf.sortSymbol("Expr"),
								sdf.caseSensitiveLiteralSymbol(")"), },
						sdf.sortSymbol("Expr")), });

		StartSymbols startSymbols = sdf
				.contextFreeStartSymbols(new Symbol[] { sdf.sortSymbol("Expr") });
		
		Priorities cfPriorities = sdf.contextFreePriorities(new Priority[] {
				sdf.priority(new PriorityGroup[]{
					sdf.priorityGroup(new Production[]{
							prodMult, prodDiv
					}),
					sdf.priorityGroup(new Production[]{
							prodPlus, prodMinus
					})
				})
		});

		Exports exports = sdf.exports(new GrammarElement[] {
				startSymbols,
				sorts,
				lexSyntax,
				cfSyntax,
				cfPriorities
		});

		Module module = sdf.moduleWithoutParameters(new ModuleId(MAIN_MODULE_NAME),
				new Imports[] {}, new ExportOrHiddenSection[] { exports });

		grammar = sdf.getGrammar(MAIN_MODULE_NAME);
	}

	@Test
	public void testGrammar() {
		System.out.println("== ArithExprWithPrioritiesTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
		
		sdf.printGeneratedGrammarHTML(MAIN_MODULE_NAME, "src/sdf/test/ArithExprWithPrioritiesTest.html");

		assertNotNull("start rule null", grammar.getStartRule());
		assertTrue("no categories", grammar.getCategories().size() > 0);
		assertTrue("no rules", grammar.getRules().size() > 0);
	}

	@Test
	public void testEarleyParserWithExpr1() {
		System.out.println("== ArithExprWithPrioritiesTest: Parser: 2+3*5 ==");
		EarleyParser parser = new EarleyParser(grammar);
		parser.detectUsedOracles();
		Chart chart = (Chart) parser.parse("2+3*5");
		chart.rparse((de.tud.stg.parlex.core.Rule) grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());

		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}

	@Test
	public void testEarleyParserWithExpr2() {
		System.out
				.println("== ArithExprWithPrioritiesTest: Parser: 4 + 8 * (15 / (16+23)) - 42 ==");
		EarleyParser parser = new EarleyParser(grammar);
		parser.detectUsedOracles();
		Chart chart = (Chart) parser.parse("4 + 8 * (15 / (16+23)) - 42");
		chart.rparse((de.tud.stg.parlex.core.Rule) grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());

		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
