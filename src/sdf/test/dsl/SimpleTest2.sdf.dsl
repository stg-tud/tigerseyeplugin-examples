package sdf.test.dsl

import sdf.model.*

// TODO: grammarware muss manuell zum buildpath hinzugefuegt werden?
// TODO: ebenso parlex zum ausfuehren

sdf(name:'SimpleTest2'){

	module LeesPlank
	
	exports
		context-free start-symbols LeesPlank
		sorts Aap Noot Mies LeesPlank
		lexical syntax
			'aap'			-> Aap
			"noot"			-> Noot
			"mies"			-> Mies
			Aap Noot Mies	-> LeesPlank
	
	printGeneratedGrammar "LeesPlank"
	parse "LeesPlank" "AaPnootmies"
}
