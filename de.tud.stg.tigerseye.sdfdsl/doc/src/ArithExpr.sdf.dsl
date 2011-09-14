import sdf.model.*

sdf(name:'ArithExpr'){

	module ArithExpr
	exports
		context-free start-symbols Expr
		sorts Expr Number Term Factor
		
		lexical syntax
			[0-9]+					-> Number
			[ ]+						-> LAYOUT
		
		context-free syntax
			Expr "+" Term		-> Expr
			Expr "-" Term		-> Expr
			Term						-> Expr
			
			Term "*" Factor	-> Term
			Term "/" Factor	-> Term
			Factor					-> Term
			
			Number					-> Factor
			"(" Expr ")"		-> Factor
	
	
	printGeneratedGrammarHTML "ArithExpr" "debug/ArithExpr.html"
	parse "ArithExpr" "4 + 8 * (15 / (16+23)) - 42"
	
}
