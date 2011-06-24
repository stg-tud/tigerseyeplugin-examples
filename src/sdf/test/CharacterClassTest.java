package sdf.test;

import org.junit.Before;
import org.junit.Test;

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
import junit.framework.TestCase;

/**
 * 
 * This test case checks the implementation of character classes, in particular
 * the character class operators.
 * 
 * SDF:
 * <code>
 * module CharClassTest
 * exports
 * context-free start-symbols List
 * sorts OctDigit HexDigit DEF OctNumber HexNumber DecNumber Number Item List
 * 
 * lexical syntax
 * [0-9] / [8-9]		-> OctDigit
 * [0-9] \/ [a-zA-Z]	-> HexDigit
 * [a-f] /\ [d-q]		-> DEF
 * "0" OctDigit*		-> OctNumber
 * "0x" HexDigit+		-> HexNumber
 * [1-9] [0-9]*			-> DecNumber
 * 
 * context-free syntax
 * OctNumber			-> Number
 * HexNumber			-> Number
 * DecNumber			-> Number
 * 
 * Number				-> Item
 * DEF					-> Item
 * 
 * {Item ","}*			-> List
 * 
 * 
 * </code>
 * 
 * 
 * @author Pablo Hoch
 *
 */
public class CharacterClassTest extends TestCase {
	SdfDSL sdf;
	Grammar grammar;

	@Before
	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[]{
				sdf.sortSymbol("OctDigit"),
				sdf.sortSymbol("HexDigit"),
				sdf.sortSymbol("DEF"),
				sdf.sortSymbol("OctNumber"),
				sdf.sortSymbol("HexNumber"),
				sdf.sortSymbol("DecNumber"),
				sdf.sortSymbol("Number"),
				sdf.sortSymbol("Item"),
				sdf.sortSymbol("List")
		});
		
		Syntax lexSyntax = sdf.lexicalSyntax(new Production[]{
				sdf.production(new Symbol[]{
						sdf.characterClassDifference(sdf.characterClassSymbol("0-9"), sdf.characterClassSymbol("8-9"))
				}, sdf.sortSymbol("OctDigit")),
				sdf.production(new Symbol[]{
						sdf.characterClassUnion(sdf.characterClassSymbol("0-9"), sdf.characterClassSymbol("a-zA-Z"))
				}, sdf.sortSymbol("HexDigit")),
				sdf.production(new Symbol[]{
						sdf.characterClassIntersection(sdf.characterClassSymbol("a-f"), sdf.characterClassSymbol("d-q"))
				}, sdf.sortSymbol("DEF")),
				sdf.production(new Symbol[]{
						sdf.caseSensitiveLiteralSymbol("0"),
						sdf.repetitionSymbolAtLeastZero(sdf.sortSymbol("OctDigit"))
				}, sdf.sortSymbol("OctNumber")),
				sdf.production(new Symbol[]{
						sdf.caseSensitiveLiteralSymbol("0x"),
						sdf.repetitionSymbolAtLeastOnce(sdf.sortSymbol("HexDigit"))
				}, sdf.sortSymbol("HexNumber")),
				sdf.production(new Symbol[]{
						sdf.characterClassSymbol("[1-9]"),
						sdf.repetitionSymbolAtLeastZero(sdf.characterClassSymbol("[0-9]"))
				}, sdf.sortSymbol("DecNumber")),
		});
		
		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[]{
				sdf.production(new Symbol[]{
						sdf.sortSymbol("OctNumber"),
				}, sdf.sortSymbol("Number")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("HexNumber"),
				}, sdf.sortSymbol("Number")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("DecNumber"),
				}, sdf.sortSymbol("Number")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("Number"),
				}, sdf.sortSymbol("Item")),
				sdf.production(new Symbol[]{
						sdf.sortSymbol("DEF"),
				}, sdf.sortSymbol("Item")),
				sdf.production(new Symbol[]{
						sdf.listSymbolAtLeastZero(sdf.sortSymbol("Item"), sdf.caseSensitiveLiteralSymbol(","))
				}, sdf.sortSymbol("List")),
		});
		
		StartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[]{
				sdf.sortSymbol("List")
		});
		
		Exports exports = sdf.exports(new GrammarElement[]{
			startSymbols,
			sorts,
			lexSyntax,
			cfSyntax
		});
		
		Module module = sdf.moduleWithoutParameters(new ModuleId("CharClassTest"), new Imports[]{}, new ExportOrHiddenSection[]{ exports });
		
		grammar = sdf.getGrammar("CharClassTest");
	}
	
	@Test
	public void testGrammar() {
		System.out.println("== CharacterClassTest: Grammar ==");
		System.out.println(grammar.toString());
		System.out.println();
	}
	
	@Test
	public void testEarleyParserWithExpr1() {
		System.out.println("== CharacterClassTest: Parser ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("123,0755,0xcaFE123,d,f");
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		assertTrue(chart.isValidParse());
		
		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
		System.out.println();
	}
}
