package sdf.test.treeviewer;

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
import treeviewer.tree.aterms.ATermTreeBuilder;
import treeviewer.tree.parlex.ParseTreeBuilder;
import treeviewer.ui.MultiTreeViewer;
import aterm.ATerm;
import de.tud.stg.parlex.ast.IAbstractNode;

public class SimpleCons {
	private SdfDSL sdf;
	private GeneratedGrammar generatedGrammar;
	
	private static final String MAIN_MODULE_NAME = "SimpleCons";
	
	public SimpleCons() {
		setUp();
	}
	
	private void setUp() {
		sdf = new SdfDSL();
		
		/* Grammar:
				Id          -> Var  {cons("Var")}
				Var         -> Exp 
				IntConst    -> Exp  {cons("Int")}
				"-" Exp     -> Exp  {cons("Uminus")}
				Exp "*" Exp -> Exp  {cons("Times")}
				Exp "+" Exp -> Exp  {cons("Plus")}
				Exp "-" Exp -> Exp  {cons("Minus")}
				Exp "=" Exp -> Exp  {cons("Eq")}
				Exp ">" Exp -> Exp  {cons("Gt")}
				"(" Exp ")" -> Exp
				
			This example grammar is taken from:
			http://hydra.nixos.org/build/1451061/download/1/manual/chunk-chapter/tutorial-parsing.html#id1302182
		 */

		Sorts sorts = sdf.sortsDeclaration(
				new SortSymbol[] {
						sdf.sortSymbol("Id"),
						sdf.sortSymbol("Var"),
						sdf.sortSymbol("IntConst"),
						sdf.sortSymbol("Exp")
				});

		Syntax lexSyntax = sdf.lexicalSyntax(new Production[] {

				sdf.production(new Symbol[] {
						sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol("0-9"))
				}, sdf.sortSymbol("IntConst")),
				
//				sdf.productionWithAttributes(new Symbol[] {
//						sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol("0-9"))
//				}, sdf.sortSymbol("IntConst"),
//				new ATerm[]{sdf.consATerm("IntLiteral")}),
						
				sdf.production(new Symbol[] {
						sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol("[a-zA-Z]"))
				}, sdf.sortSymbol("Id")),

				sdf.production(new Symbol[]{
						sdf.repetitionSymbolAtLeastOnce(sdf.characterClassSymbol(" "))
				}, sdf.sortSymbol("LAYOUT")),
		});

		Syntax cfSyntax = sdf.contextFreeSyntax(new Production[] {
//				Id          -> Var  {cons("Var")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Id")
						},
						sdf.sortSymbol("Var"),
						new ATerm[] {
							sdf.consATerm("Var")
						}
				),
				
//				Var         -> Exp 
				sdf.production(
						new Symbol[] {
								sdf.sortSymbol("Var")
						},
						sdf.sortSymbol("Exp")
				),
				
//				IntConst    -> Exp  {cons("Int")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("IntConst")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Int")
						}
				),
				
//				"-" Exp     -> Exp  {cons("Uminus")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.caseSensitiveLiteralSymbol("-"),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Uminus")
						}
				),
				
//				Exp "*" Exp -> Exp  {cons("Times")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol("*"),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Times")
						}
				),
				
//				Exp "+" Exp -> Exp  {cons("Plus")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol("+"),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Plus")
//							sdf.customATerm("test")
						}
				),
				
//				Exp "-" Exp -> Exp  {cons("Minus")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol("-"),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Minus")
						}
				),
				
//				Exp "=" Exp -> Exp  {cons("Eq")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol("="),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Eq")
						}
				),
				
//				Exp ">" Exp -> Exp  {cons("Gt")}
				sdf.productionWithAttributes(
						new Symbol[] {
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol(">"),
								sdf.sortSymbol("Exp")
						},
						sdf.sortSymbol("Exp"),
						new ATerm[] {
							sdf.consATerm("Gt")
						}
				),
				
//				"(" Exp ")" -> Exp
				sdf.production(
						new Symbol[] {
								sdf.caseSensitiveLiteralSymbol("("),
								sdf.sortSymbol("Exp"),
								sdf.caseSensitiveLiteralSymbol(")"),
						},
						sdf.sortSymbol("Exp")
				),
				
		});

		StartSymbols startSymbols = sdf.contextFreeStartSymbols(new Symbol[] {
				sdf.sortSymbol("Exp")
		});

		Exports exports = sdf.exports(new GrammarElement[] {
				startSymbols, sorts, lexSyntax, cfSyntax
		});

		Module module = sdf.moduleWithoutParameters(
				new ModuleId(MAIN_MODULE_NAME),
				new Imports[] {},
				new ExportOrHiddenSection[] { exports }
		);

		generatedGrammar = sdf.getGrammar(MAIN_MODULE_NAME);
		
		sdf.printGeneratedGrammarHTML(MAIN_MODULE_NAME, "src/sdf/test/treeviewer/SimpleCons.html");
	}
	
	public void parse(String input) {
		ParseResult result = sdf.parseString(MAIN_MODULE_NAME, input);
		
		System.out.println(result.getParseChart());
		
		IAbstractNode parseTree = result.getParseTree();
		
		System.out.println(parseTree);
		
		ATerm atermTree = result.getConsTree();
		
		System.out.println("ATerm: " + atermTree);
		
		MultiTreeViewer tv = new MultiTreeViewer();
		tv.addTree("Parse Tree", new ParseTreeBuilder(parseTree));
		tv.addTree("ATerm Tree", new ATermTreeBuilder(atermTree));
		
		tv.show(true);
	}
	
	public static void main(String[] args) {
		MultiTreeViewer.initializeGui();
		SimpleCons c = new SimpleCons();
		c.parse("(bar+n)*12345");
	}

}
