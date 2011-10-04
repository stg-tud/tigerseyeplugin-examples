package sdf.test.dsl

import groovy.lang.Closure;
import sdf.*
import sdf.model.*

Closure c = {
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
	
};

SdfDSL sdfdsl = new SdfDSL();
//	sdfdsl.eval(null, c);
c.setDelegate(sdfdsl);
c.setResolveStrategy(Closure.DELEGATE_FIRST);
c.call();

sdfdsl.printGeneratedGrammar("LeesPlank")
sdfdsl.parseString("LeesPlank", "AaPnootmies")
