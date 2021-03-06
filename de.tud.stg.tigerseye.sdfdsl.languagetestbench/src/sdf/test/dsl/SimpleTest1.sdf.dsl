package sdf.test.dsl

import sdf.model.*

/*
 *  module LeesPlank

 exports
 context-free start-symbols LeesPlank
 sorts Aap Noot Mies LeesPlank
 lexical syntax
 'aap'         -> Aap
 "noot"        -> Noot
 "mies"        -> Mies
 Aap Noot Mies -> LeesPlank
 
///
 */

sdf(name:'SimpleTest1'){

	moduleWithoutParameters(new ModuleId("LeesPlank"), [] as Imports[], [
		
		exports([
			contextFreeStartSymbols(sortSymbol("LeesPlank")),
			sortsDeclaration(sortSymbol("Aap"), sortSymbol("Noot"), sortSymbol("Mies"), sortSymbol("LeesPlank")),
			lexicalSyntax([
				production([caseInsensitiveLiteralSymbol("aap")] as Symbol[], sortSymbol("Aap")),
				production([caseSensitiveLiteralSymbol("noot")] as Symbol[], sortSymbol("Noot")),
				production([caseSensitiveLiteralSymbol("mies")] as Symbol[], sortSymbol("Mies")),
				production([sortSymbol("Aap"), sortSymbol("Noot"), sortSymbol("Mies")] as Symbol[], sortSymbol("LeesPlank")),
			] as Production[])
		] as GrammarElement[])
		
	] as ExportOrHiddenSection[])
	
	printGeneratedGrammar("LeesPlank")
	parseString("LeesPlank", "AaPnootmies")
	
}
