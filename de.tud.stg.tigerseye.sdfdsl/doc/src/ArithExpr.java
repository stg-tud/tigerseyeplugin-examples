import sdf.SdfDSL;
import sdf.model.*;
import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

public class ArithExprSdfTest {
	SdfDSL sdf;
	Grammar grammar;

	public void setUp() {
		sdf = new SdfDSL();

		Sorts sorts = sdf.sortsDeclaration(new SortSymbol[] {
				sdf.sortSymbol("Expr"), sdf.sortSymbol("Number"),
				sdf.sortSymbol("Term"), sdf.sortSymbol("Factor") });

		Syntax lexSyntax = sdf.lexicalSyntax(new Production[] {
				sdf.production(new Symbol[] {
						sdf.repetitionSymbolAtLeastOnce(
								sdf.characterClassSymbol("0-9")) },
						sdf.sortSymbol("Number")),
				sdf.production(new Symbol[] {
						sdf.repetitionSymbolAtLeastOnce(
								sdf.characterClassSymbol(" ")) },
						sdf.sortSymbol("LAYOUT")), });

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

		Module module = sdf.moduleWithoutParameters(new ModuleId("ArithExpr"),
				new Imports[] {}, new ExportOrHiddenSection[] { exports });

		grammar = sdf.getGrammar("ArithExpr");
	}

	public void testEarleyParserWithExpr1() {
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse("2+3*5");
		chart.rparse((de.tud.stg.parlex.core.Rule) grammar.getStartRule());

		System.out.println("AST:");
		System.out.println(chart.getAST().toString());
	}
}
