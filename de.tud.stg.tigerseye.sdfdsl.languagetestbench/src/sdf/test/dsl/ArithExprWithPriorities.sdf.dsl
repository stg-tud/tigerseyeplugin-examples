package sdf.test.dsl

import sdf.model.*;
import aterm.*;

sdf(name:'ArithExprWithPriorities'){

	module ArithExprWithPriorities
	exports
		context-free start-symbols Expr
		sorts Expr Number
	
		lexical syntax
			[0-9]+				-> Number
			[ ]+				-> LAYOUT
	
		context-free syntax
			Expr "+" Expr		-> Expr {left}
			Expr "-" Expr		-> Expr {left}
			Expr "*" Expr		-> Expr {left}
			Expr "/" Expr		-> Expr {left}
			Number				-> Expr
			"(" Expr ")"		-> Expr
		
		context-free priorities
			{
			  Expr "*" Expr		-> Expr {left}
			  Expr "/" Expr		-> Expr {left}
			} > {
			  Expr "+" Expr		-> Expr {left}
			  Expr "-" Expr		-> Expr {left}
			}
	
	printGeneratedGrammar "ArithExprWithPriorities"
	printGeneratedGrammarHTML "ArithExprWithPriorities" "debug/ArithExprWithPriorities.html"
	parse "ArithExprWithPriorities" "4 + 8 * (15 / (16+23)) - 42"
	
}
