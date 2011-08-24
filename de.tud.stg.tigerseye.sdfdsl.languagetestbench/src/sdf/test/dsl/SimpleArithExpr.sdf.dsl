package sdf.test.dsl

import sdf.model.*


sdf(name:'SimpleArithExpr'){

	module SimpleArithExpr
	exports
		context-free start-symbols Expr
		sorts Expr Number
		
		lexical syntax
			[0-9]+				-> Number
			[ ]+				-> LAYOUT
		
		context-free syntax
			Expr "+" Number		-> Expr
			Expr "-" Number		-> Expr
			Number				-> Expr
	
	printGeneratedGrammarHTML "SimpleArithExpr" "debug/SimpleArithExpr.html"
	parse "SimpleArithExpr" "1 + 2 - 3 + 4 - 5" 
}
