package sdf.test.dsl.complex
import de.tud.stg.tigerseye.dslsupport.DSLInvoker;
import sdf.SdfDSL;

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

try {

	//DSLInvoker.eval([
	//SdfDSL.class]){
	new SdfDSL().sdf() {

		moduleWithoutParameters(
				new sdf.model.ModuleId("languages/bnf/syntax/BNF"),
				[
					importsStatement(
					[
						importModuleWithoutParameters(
						new sdf.model.ModuleId("basic/Whitespace"))
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
							new sdf.model.SortSymbol("Rules")
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
							new sdf.model.SortSymbol("Rule"),
							new sdf.model.SortSymbol("Rules"),
							new sdf.model.SortSymbol("NonTerminal"),
							new sdf.model.SortSymbol("Terminal"),
							new sdf.model.SortSymbol("Element"),
							new sdf.model.SortSymbol("Elements")
						]
						as SortSymbol[])),
						syntax(
						syntax(
						lexicalSyntax(
						[
							production(
							[
								caseSensitiveLiteralSymbol(
								"<"),
								symbol(
								repetitionSymbolAtLeastOnce(
								symbol(
								characterClass(
								characterClassComplement(
								new sdf.model.CharacterClassSymbol("[\\<\\>]")))))),
								symbol(
								caseSensitiveLiteralSymbol(
								">"))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("NonTerminal"))),
							production(
							[
								symbol(
								characterClass(
								characterClassComplement(
								new sdf.model.CharacterClassSymbol("[\\<\\ \\t\\|\\[\\]\\{\\}]")))),
								symbol(
								repetitionSymbolAtLeastZero(
								characterClass(
								characterClassComplement(
								new sdf.model.CharacterClassSymbol("[\\ \\t\\n]")))))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Terminal")),
							productionWithAttributes(
							[
								symbol(
								caseSensitiveLiteralSymbol(
								"..."))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Terminal"),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("reject")))
							]as ATerm[]),
							productionWithAttributes(
							[
								symbol(
								caseSensitiveLiteralSymbol(
								"::="))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Terminal")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("reject")))
							]
							as ATerm[])
						]
						as Production[]))),
						syntax(
						syntax(
						contextFreeSyntax(
						[
							production(
							[
								symbol(
								new sdf.model.SortSymbol("NonTerminal")),
								caseSensitiveLiteralSymbol(
								"::="),
								symbol(
								new sdf.model.SortSymbol("Elements"))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Rule")),
							production(
							[
								symbol(
								listSymbolAtLeastOnce(
								new sdf.model.SortSymbol("Element"),
								symbol(
								caseSensitiveLiteralSymbol(
								"|"))))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Elements"))),
							production(
							[
								symbol(
								repetitionSymbolAtLeastZero(
								new sdf.model.SortSymbol("Rule")))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Rules"))),
							production(
							[
								new sdf.model.SortSymbol("NonTerminal")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								symbol(
								new sdf.model.SortSymbol("Terminal"))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Element")),
							production(
							[
								caseSensitiveLiteralSymbol(
								"["),
								symbol(
								listSymbolAtLeastOnce(
								new sdf.model.SortSymbol("Element"),
								symbol(
								caseSensitiveLiteralSymbol(
								"|")))),
								caseSensitiveLiteralSymbol(
								"]")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								symbol(
								caseSensitiveLiteralSymbol(
								"{")),
								symbol(
								listSymbolAtLeastOnce(
								symbol(
								new sdf.model.SortSymbol("Element")),
								symbol(
								caseSensitiveLiteralSymbol(
								"|")))),
								symbol(
								caseSensitiveLiteralSymbol(
								"}"))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								new sdf.model.SortSymbol("Element"),
								symbol(
								caseSensitiveLiteralSymbol(
								"..."))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Element")),
							productionWithAttributes(
							[
								symbol(
								new sdf.model.SortSymbol("Element")),
								symbol(
								new sdf.model.SortSymbol("Element"))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Element"),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("left")))
							]
							as ATerm[]),
							production(
							[
								caseSensitiveLiteralSymbol(
								"|")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								"[")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								"]")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								"{")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								"}")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								"<")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								caseSensitiveLiteralSymbol(
								">")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element"))),
							production(
							[
								symbol(
								caseSensitiveLiteralSymbol(
								"..."))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Element")),
							production(
							[
								symbol(
								caseSensitiveLiteralSymbol(
								"::="))
							]
							as Symbol[],
							new sdf.model.SortSymbol("Element")),
							productionWithAttributes(
							[
								new sdf.model.SortSymbol("Element"),
								new sdf.model.SortSymbol("Element")
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("Element")),
							[
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("left")))
							]
							as ATerm[])
						]
						as Production[]))),
						grammarElement(
						contextFreePriorities(
						[
							priority(
							[
								priorityGroup(
								production(
								[
									new sdf.model.SortSymbol("Element"),
									symbol(
									caseSensitiveLiteralSymbol(
									"..."))
								]
								as Symbol[],
								new sdf.model.SortSymbol("Element"))),
								priorityGroup(
								productionWithAttributes(
								[
									symbol(
									new sdf.model.SortSymbol("Element")),
									symbol(
									new sdf.model.SortSymbol("Element"))
								]
								as Symbol[],
								symbol(
								new sdf.model.SortSymbol("Element")),
								[
									atermFunctionApplication(
									aterm(
									new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("left")))
								]
								as ATerm[]))
							]
							as PriorityGroup[]),
							priority(
							[
								priorityGroupWithAssociativityAnnotation(
								atermFunctionApplication(
								aterm(
								new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("non-assoc"))),
								[
									production(
									[
										symbol(
										caseSensitiveLiteralSymbol(
										"|"))
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element"))),
									production(
									[
										caseSensitiveLiteralSymbol(
										"[")
									]
									as Symbol[],
									new sdf.model.SortSymbol("Element")),
									production(
									[
										symbol(
										caseSensitiveLiteralSymbol(
										"]"))
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element"))),
									production(
									[
										caseSensitiveLiteralSymbol(
										"{")
									]
									as Symbol[],
									new sdf.model.SortSymbol("Element")),
									production(
									[
										symbol(
										caseSensitiveLiteralSymbol(
										"}"))
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element"))),
									production(
									[
										caseSensitiveLiteralSymbol(
										"<")
									]
									as Symbol[],
									new sdf.model.SortSymbol("Element")),
									production(
									[
										symbol(
										caseSensitiveLiteralSymbol(
										">"))
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element"))),
									production(
									[
										symbol(
										caseSensitiveLiteralSymbol(
										"..."))
									]
									as Symbol[],
									new sdf.model.SortSymbol("Element")),
									production(
									[
										caseSensitiveLiteralSymbol(
										"::=")
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element"))),
									productionWithAttributes(
									[
										symbol(
										new sdf.model.SortSymbol("Element")),
										symbol(
										new sdf.model.SortSymbol("Element"))
									]
									as Symbol[],
									symbol(
									new sdf.model.SortSymbol("Element")),
									[
										atermFunctionApplication(
										aterm(
										new sdf.ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant("left")))
									]
									as ATerm[])
								]
								as Production[])
							]
							as PriorityGroup[])
						]
						as Priority[]))
					]
					as GrammarElement[]))
				]
				as ExportOrHiddenSection[])


		moduleWithoutParameters(
				new sdf.model.ModuleId("basic/Whitespace"),
				[
					exportOrHiddenSection(
					exports(
					[
						grammarElement(
						syntax(
						lexicalSyntax(
						[
							productionWithAttributes(
							[
								characterClass(
								new sdf.model.CharacterClassSymbol("[\\ \\t\\n\\r]"))
							]
							as Symbol[],
							symbol(
							new sdf.model.SortSymbol("LAYOUT")),
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


		//
		printGeneratedGrammarHTML(
				"languages/bnf/syntax/BNF",
				"debug/BNFLang.html")
		input = """<start> ::= <expr>
<expr> ::= <num> | <expr> + <expr> | <expr> - <expr> | <expr> * <expr> | <expr> / <expr>
<num> ::= <digit> {<digit>}
<digit> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
"""

		println "Parsing: " + input
		result = parseString(
				"languages/bnf/syntax/BNF",
				input)

		if (result.valid) {
			println result.consTree

			MultiTreeViewer tv = new MultiTreeViewer();
			MultiTreeViewer.initializeGui();
			tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
			tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
			tv.show(true)
		} else {
			println "invalid parse"
		}


	}

} catch (MissingPropertyException ex) {
	println ex.getMessage()
}