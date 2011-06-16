package sdf.test;

import org.junit.Before;
import org.junit.Test;

import sdf.SdfDSL;
import sdf.model.ExportOrHiddenSection;
import sdf.model.Exports;
import sdf.model.GrammarElement;
import sdf.model.Hiddens;
import sdf.model.Import;
import sdf.model.Imports;
import sdf.model.Module;
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
 * This test case uses a syntax definition using multiple modules and tests imports including renaming.
 * 
 * @author Pablo Hoch
 *
 */
public class ImportTest extends TestCase {
	SdfDSL sdf;
	Grammar grammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		setUpNumbersModule();
		setUpWhitespaceModule();
		setUpArithExpr2Module();
		setUpSimpleLangModule();
		
		grammar = sdf.getGrammar("SimpleLang", true);
	}
	
	/**
	 * Defines the test/Numbers module
	 * 
	 *  module test/Numbers
	 *  exports
	 *  	sorts Integer
	 *  	lexical syntax
	 *  		[0-9]+	-> Integer
	 *  hiddens
	 *  	lexical start-symbols Integer
	 */
	private Module setUpNumbersModule() {
		Sorts sorts = sdf.sorts(new SortSymbol[]{
				sdf.sortSymbol("Integer"),
		});
		
		Syntax lexSyntax = sdf.lexicalSyntax(new Production[]{
				sdf.production(new Symbol[]{ sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol("0-9")) }, sdf.sortSymbol("Integer")),
		});
		
		StartSymbols startSymbols = sdf.lexicalStartSymbols(new Symbol[]{
				sdf.sortSymbol("Integer")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			sorts,
			lexSyntax,
		});
		
		Hiddens hiddens = sdf.hiddens(new GrammarElement[]{
				startSymbols
		});
		
		return sdf.moduleWithoutParameters("test/Numbers", new Imports[]{}, new ExportOrHiddenSection[]{ exports, hiddens });
	}
	
	/**
	 * Defines the test/Whitespace module
	 * 
	 *  module test/Numbers
	 *  exports
	 *  	lexical syntax
	 *  		[ ]+	-> LAYOUT
	 */
	private Module setUpWhitespaceModule() {
		Syntax lexSyntax = sdf.lexicalSyntax(new Production[]{
				sdf.production(new Symbol[]{ sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol(" ")) }, sdf.sortSymbol("LAYOUT")),
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			lexSyntax,
		});
		
		return sdf.moduleWithoutParameters("test/Whitespace", new Imports[]{}, new ExportOrHiddenSection[]{ exports });
	}

	/**
	 * Defines the ArithExpr2 module
	 * 
	 * module ArithExpr2
	 * imports test/Numbers[ Integer => Number ]
	 * imports test/Whitespace
	 * exports
	 * sorts Expr Term Factor
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
	 * hiddens
	 * context-free start-symbols Expr
	 */
	private Module setUpArithExpr2Module() {
		Sorts sorts = sdf.sorts(new SortSymbol[]{
				sdf.sortSymbol("Expr"),
				sdf.sortSymbol("Term"),
				sdf.sortSymbol("Factor")
		});
		
		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[]{
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Expr"), sdf.caseSensitiveliteralSymbol("+"), sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Expr"), sdf.caseSensitiveliteralSymbol("-"), sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term")
				}, sdf.sortSymbol("Expr")),
				
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term"), sdf.caseSensitiveliteralSymbol("*"), sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Term"), sdf.caseSensitiveliteralSymbol("/"), sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Factor")
				}, sdf.sortSymbol("Term")),
				
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Number")
				}, sdf.sortSymbol("Factor")),
				sdf.production(new Symbol[]{
						sdf.caseSensitiveliteralSymbol("("), sdf.sortSymbol("Expr"), sdf.caseSensitiveliteralSymbol(")"),
				}, sdf.sortSymbol("Factor")),
		});
		
		StartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[]{
				sdf.sortSymbol("Expr")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			sorts,
			cfSyntax
		});
		
		Hiddens hiddens = sdf.hiddens(new GrammarElement[]{
				startSymbols
		});
		
		// no support in SdfDSL yet
		Import numbersImport = new Import("test/Numbers");
		numbersImport.getRenamings().put(sdf.sortSymbol("Integer"), sdf.sortSymbol("Number"));
		
		Imports imports = sdf.imports(new Import[]{
//				sdf.importModuleWithoutParameters("test/Numbers"),
				numbersImport,
				sdf.importModuleWithoutParameters("test/Whitespace"),
		});
		
		return sdf.moduleWithoutParameters("ArithExpr2", new Imports[]{ imports }, new ExportOrHiddenSection[]{ exports, hiddens });
	}
	
	/**
	 * Defines the SimplePlang module
	 * 
	 * module SimpleLang
	 * imports ArithExpr2
	 * exports
	 * sorts Program Stmt
	 * 
	 * context-free syntax
	 * "print" Expr		-> Stmt
	 * "exit"			-> Stmt
	 * 					-> Stmt
	 * 
	 * {Stmt ";"}*		-> Program
	 * 
	 * hiddens
	 * context-free start-symbols Program
	 */
	private Module setUpSimpleLangModule() {
		Sorts sorts = sdf.sorts(new SortSymbol[]{
				sdf.sortSymbol("Stmt"),
				sdf.sortSymbol("Program"),
		});
		
		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[]{
				sdf.production(new Symbol[]{
						sdf.caseSensitiveliteralSymbol("print"), sdf.sortSymbol("Expr")
				}, sdf.sortSymbol("Stmt")),
				sdf.production(new Symbol[]{
						sdf.caseSensitiveliteralSymbol("exit")
				}, sdf.sortSymbol("Stmt")),
				sdf.production(new Symbol[]{
						
				}, sdf.sortSymbol("Stmt")),
				
				sdf.production(new Symbol[]{
						sdf.listSymbolAtLeastZero(sdf.sortSymbol("Stmt"), sdf.caseSensitiveliteralSymbol(";"))
				}, sdf.sortSymbol("Program")),
		});
		
		StartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[]{
				sdf.sortSymbol("Program")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			sorts,
			cfSyntax
		});
		
		Hiddens hiddens = sdf.hiddens(new GrammarElement[]{
				startSymbols
		});
		
		Imports imports = sdf.imports(new Import[]{
				sdf.importModuleWithoutParameters("ArithExpr2"),
		});
		
		return sdf.moduleWithoutParameters("SimpleLang", new Imports[]{ imports }, new ExportOrHiddenSection[]{ exports, hiddens });
	}
	
	@Test
	public void testGrammar() {
		System.out.println("== ImportTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
		
		assertNotNull("start rule null", grammar.getStartRule());
		assertTrue("no categories", grammar.getCategories().size() > 0);
		assertTrue("no rules", grammar.getRules().size() > 0);
	}
	
	@Test
	public void testEarleyParserWithExpr1() {
		System.out.println("== ImportTest: Parser: print 2+3*5 ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("print 2+3*5");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
	
	@Test
	public void testEarleyParserWithExpr2() {
		System.out.println("== ImportTest: Parser: print 4 + 8 * (15 / (16+23)) - 42; print 1+2; exit ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("print 4 + 8 * (15 / (16+23)) - 42; print 1+2; exit");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
