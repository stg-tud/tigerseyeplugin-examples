package sdf.test.dsl.complex
import de.tud.stg.tigerseye.dslsupport.DSLInvoker;
import sdf.SdfDSL;

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

/*
 * This example is taken from the SDF Library
 * Changes made:
 * - Lexical/context-free restrictions removed (not supported by SDF DSL)
 * - a:[0-9]b:[0-9]c:[0-9] changed to a:[0-9] b:[0-9] c:[0-9] (space between symbols required)
 */

SdfDSL sdfInstance;

//DSLInvoker.eval([
//SdfDSL.class]){
new SdfDSL().sdf() {

	moduleWithoutParameters(
			new sdf.model.ModuleId("languages/aterm/syntax/ATerms"),
			[
				importsStatement(
				[
					importModuleWithoutParameters(
					new sdf.model.ModuleId("languages/aterm/syntax/IntCon")),
					importModuleWithoutParameters(
					new sdf.model.ModuleId("languages/aterm/syntax/RealCon")),
					importModuleWithoutParameters(
					new sdf.model.ModuleId("basic/StrCon")),
					importModuleWithoutParameters(
					new sdf.model.ModuleId("basic/IdentifierCon"))
				]
				as Import[])
			]
			as Imports[],
			[
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("AFun"),
						new sdf.model.SortSymbol("ATerm"),
						new sdf.model.SortSymbol("Annotation")
					]
					as SortSymbol[])),
					grammarElement(
					syntax(
					contextFreeSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							new sdf.model.SortSymbol("StrCon"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("AFun"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"quoted\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							symbol(
							new sdf.model.SortSymbol("IdCon"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("AFun"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"unquoted\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[]))),
					syntax(
					syntax(
					contextFreeSyntax(
					[
						productionWithAttributes(
						[
							new sdf.model.SortSymbol("IntCon")
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("ATerm")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"int\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							symbol(
							new sdf.model.SortSymbol("RealCon"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("ATerm"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"real\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("fun"),
							new sdf.model.SortSymbol("AFun"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("ATerm"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"fun\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("fun"),
							new sdf.model.SortSymbol("AFun")),
							symbol(
							caseSensitiveLiteralSymbol(
							"(")),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("args"),
							symbol(
							listSymbolAtLeastOnce(
							new sdf.model.SortSymbol("ATerm"),
							symbol(
							caseSensitiveLiteralSymbol(
							","))))),
							symbol(
							caseSensitiveLiteralSymbol(
							")"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("ATerm"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"appl\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							caseSensitiveLiteralSymbol(
							"<"),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("type"),
							new sdf.model.SortSymbol("ATerm")),
							symbol(
							caseSensitiveLiteralSymbol(
							">"))
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("ATerm")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"placeholder\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							symbol(
							caseSensitiveLiteralSymbol(
							"[")),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("elems"),
							symbol(
							listSymbolAtLeastZero(
							symbol(
							new sdf.model.SortSymbol("ATerm")),
							caseSensitiveLiteralSymbol(
							",")))),
							symbol(
							caseSensitiveLiteralSymbol(
							"]"))
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("ATerm")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"list\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("trm"),
							new sdf.model.SortSymbol("ATerm")),
							symbol(
							new sdf.model.SortSymbol("Annotation"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("ATerm"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"annotated\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[]))),
					syntax(
					syntax(
					contextFreeSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							caseSensitiveLiteralSymbol(
							"{")),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("annos"),
							symbol(
							listSymbolAtLeastOnce(
							new sdf.model.SortSymbol("ATerm"),
							caseSensitiveLiteralSymbol(
							",")))),
							symbol(
							caseSensitiveLiteralSymbol(
							"}"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("Annotation"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"default\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[])),
				exportOrHiddenSection(
				hiddens(
				[
					grammarElement(
					startSymbols(
					contextFreeStartSymbols(
					[
						symbol(
						new sdf.model.SortSymbol("ATerm"))
					]
					as Symbol[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])



	moduleWithoutParameters(
			new sdf.model.ModuleId("languages/aterm/syntax/IntCon"),
			[
				importsStatement(
				[
					importModuleWithoutParameters(
					new sdf.model.ModuleId("basic/Whitespace")),
					importModuleWithoutParameters(
					new sdf.model.ModuleId("basic/NatCon"))
				]
				as Import[])
			]
			as Imports[],
			[
				exportOrHiddenSection(
				hiddens(
				[
					grammarElement(
					startSymbols(
					contextFreeStartSymbols(
					[
						symbol(
						new sdf.model.SortSymbol("IntCon"))
					]
					as Symbol[])))
				]
				as GrammarElement[])),
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("IntCon")
					]
					as SortSymbol[])),
					grammarElement(
					syntax(
					contextFreeSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							new sdf.model.SortSymbol("NatCon"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("IntCon"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"natural\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("pos"),
							caseSensitiveLiteralSymbol(
							"+")),
							symbol(
							new sdf.model.SortSymbol("NatCon"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("IntCon"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"positive\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("neg"),
							caseSensitiveLiteralSymbol(
							"-")),
							new sdf.model.SortSymbol("NatCon")
						]
						as Symbol[],
						new sdf.model.SortSymbol("IntCon"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"negative\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])


	moduleWithoutParameters(
			new sdf.model.ModuleId("languages/aterm/syntax/RealCon"),
			[
				importsStatement(
				[
					importModuleWithoutParameters(
					new sdf.model.ModuleId("languages/aterm/syntax/IntCon"))
				]
				as Import[])
			]
			as Imports[],
			[
				exportOrHiddenSection(
				hiddens(
				[
					grammarElement(
					startSymbols(
					contextFreeStartSymbols(
					[
						symbol(
						new sdf.model.SortSymbol("RealCon"))
					]
					as Symbol[])))
				]
				as GrammarElement[])),
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("OptExp"),
						new sdf.model.SortSymbol("RealCon")
					]
					as SortSymbol[])),
					syntax(
					syntax(
					contextFreeSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							caseSensitiveLiteralSymbol(
							"e")),
							new sdf.model.SortSymbol("IntCon")
						]
						as Symbol[],
						new sdf.model.SortSymbol("OptExp"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"present\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						new sdf.model.SortSymbol("OptExp"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"absent\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("base"),
							symbol(
							new sdf.model.SortSymbol("IntCon"))),
							symbol(
							caseSensitiveLiteralSymbol(
							".")),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("decimal"),
							new sdf.model.SortSymbol("NatCon")),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("exp"),
							new sdf.model.SortSymbol("OptExp"))
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("RealCon")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"real-con\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])

	/* basic getModules()
	 */

	moduleWithoutParameters(
			new sdf.model.ModuleId("basic/Whitespace"),
			[
				exportOrHiddenSection(
				exports(
				[
					syntax(
					syntax(
					lexicalSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							characterClass(
							new sdf.model.CharacterClassSymbol("[\\ \\t\\n\\r]")))
						]
						as Symbol[],
						new sdf.model.SortSymbol("LAYOUT"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"whitespace\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])

	//	  context-free restrictions
	//		LAYOUT? -/- [\ \t\n\r]



	moduleWithoutParameters(
			new sdf.model.ModuleId("basic/NatCon"),
			[
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("NatCon")
					]
					as SortSymbol[])),
					syntax(
					syntax(
					lexicalSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							repetitionSymbolAtLeastOnce(
							symbol(
							new sdf.model.CharacterClassSymbol("[0-9]"))))
						]
						as Symbol[],
						new sdf.model.SortSymbol("NatCon"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"digits\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])

	//	lexical restrictions
	//
	//	 NatCon -/- [0-9]


	moduleWithoutParameters(
			new sdf.model.ModuleId("basic/StrCon"),
			[
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("StrCon"),
						new sdf.model.SortSymbol("StrChar")
					]
					as SortSymbol[])),
					grammarElement(
					syntax(
					lexicalSyntax(
					[
						productionWithAttributes(
						[
							symbol(
							caseSensitiveLiteralSymbol(
							"\\n"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("StrChar"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"newline\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							caseSensitiveLiteralSymbol(
							"\\t")
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("StrChar")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"tab\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							caseSensitiveLiteralSymbol(
							"\\\"")
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("StrChar")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"quote\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							symbol(
							caseSensitiveLiteralSymbol(
							"\\\\"))
						]
						as Symbol[],
						new sdf.model.SortSymbol("StrChar"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"backslash\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							caseSensitiveLiteralSymbol(
							"\\"),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("a"),
							symbol(
							new sdf.model.CharacterClassSymbol("[0-9]"))),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("b"),
							symbol(
							new sdf.model.CharacterClassSymbol("[0-9]"))),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("c"),
							symbol(
							new sdf.model.CharacterClassSymbol("[0-9]")))
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("StrChar")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"decimal\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							symbol(
							characterClass(
							characterClassComplement(
							new sdf.model.CharacterClassSymbol("[\\0-\\31\\n\\t\\\"\\\\]"))))
						]
						as Symbol[],
						new sdf.model.SortSymbol("StrChar"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"normal\"")))
							]
							as ATerm[])
						]
						as ATerm[]),
						productionWithAttributes(
						[
							new sdf.model.CharacterClassSymbol("[\\\"]"),
							symbol(
							repetitionSymbolAtLeastZero(
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("chars"),
							symbol(
							new sdf.model.SortSymbol("StrChar"))))),
							symbol(
							characterClass(
							new sdf.model.CharacterClassSymbol("[\\\"]")))
						]
						as Symbol[],
						new sdf.model.SortSymbol("StrCon"),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"default\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])


	moduleWithoutParameters(
			new sdf.model.ModuleId("basic/IdentifierCon"),
			[
				exportOrHiddenSection(
				exports(
				[
					grammarElement(
					sortsDeclaration(
					[
						new sdf.model.SortSymbol("IdCon")
					]
					as SortSymbol[])),
					grammarElement(
					syntax(
					lexicalSyntax(
					[
						productionWithAttributes(
						[
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("head"),
							symbol(
							characterClass(
							new sdf.model.CharacterClassSymbol("[A-Za-z]")))),
							labeledSymbol(
							new sdf.SdfDSL.SymbolLabel("tail"),
							symbol(
							repetitionSymbolAtLeastZero(
							symbol(
							characterClass(
							new sdf.model.CharacterClassSymbol("[A-Za-z\\-0-9]"))))))
						]
						as Symbol[],
						symbol(
						new sdf.model.SortSymbol("IdCon")),
						[
							atermFunctionApplication(
							aterm(
							new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("cons")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("\"default\"")))
							]
							as ATerm[])
						]
						as ATerm[])
					]
					as Production[])))
				]
				as GrammarElement[]))
			]
			as ExportOrHiddenSection[])

	//	lexical restrictions
	//
	//	IdCon -/- [A-Za-z\-0-9]

	sdfInstance = getSdfInstance()


	String mainModule = "languages/aterm/syntax/ATerms"

	// print generated grammar
	printGeneratedGrammarHTML(mainModule, "debug/ATermLang.html")

	// test with an example aterm
	result = parseString(mainModule,
			"hello(\"world\",42,[one,two(three),four(fix,six,seven(\"eight\\n:)\",9))])")

	//	println result.parseTree
	println result.consTree

	MultiTreeViewer tv = new MultiTreeViewer();
	MultiTreeViewer.initializeGui();
	tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
	tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
	tv.show(true)



}

println "hello world"
println sdfInstance

