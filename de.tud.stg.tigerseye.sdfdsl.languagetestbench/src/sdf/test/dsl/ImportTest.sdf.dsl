package sdf.test.dsl

import sdf.model.*


sdf(name:'ImportTest'){

	module test/Numbers
	exports
		sorts Integer
		lexical syntax
			[0-9]+	-> Integer
	hiddens
		lexical start-symbols Integer
		
		
	
	module test/Whitespace
	exports
		lexical syntax
			[ ]+	-> LAYOUT
	
	
	
	module ArithExpr2
	imports
		test/Numbers[ Integer => Number ]
		test/Whitespace
	exports
		sorts Expr Term Factor
	
		context-free syntax
			Expr "+" Term		-> Expr
			Expr "-" Term		-> Expr
			Term				-> Expr

			Term "*" Factor		-> Term
			Term "/" Factor		-> Term
			Factor				-> Term
	
			Number				-> Factor
			"(" Expr ")"		-> Factor
	
	hiddens
		context-free start-symbols Expr

		
			
	module SimpleLang
		imports ArithExpr2
	exports
		sorts Program Stmt
	
		context-free syntax
		"print" Expr		-> Stmt
		"exit"				-> Stmt
							-> Stmt
	
		{Stmt ";"}*			-> Program
	
	hiddens
		context-free start-symbols Program
		
		
		
	printGeneratedGrammarHTML "SimpleLang" "debug/ImportTest"
	parse "SimpleLang" "print 4 + 8 * (15 / (16+23)) - 42; print 1+2; exit"
	
	
	
}
