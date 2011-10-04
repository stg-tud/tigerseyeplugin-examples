package sdf.test.dsl

import sdf.model.*;
import aterm.*;

/*
 * This file is currently not transformed correctly.
 * 
 * Problem:
 * the "add" inside the annotation cons("add")
 * gets transformed into
 * new FunctionNameConstant(""add"")
 * 
 * additionally, CustomCategory does not allow parsing of
 * numbers, so numbers in aterms don't work either :(
 *
 */

sdf(name:'SimpleArithExprWithAttributes'){

	module SimpleArithExprWithAttributes
	exports
		context-free start-symbols Expr
		sorts Expr Number
	 
		lexical syntax
			[0-9]+				-> Number
			[ ]+				-> LAYOUT
	 
		context-free syntax
			Expr "+" Expr		-> Expr {left}
			Expr "+" Expr		-> Expr {cons("add")}
			Expr "-" Expr		-> Expr {left}
			Number				-> Expr {custom("test",123)}
	 
	 
	
	printGeneratedGrammar "SimpleArithExprWithAttributes"
	printGeneratedGrammarHTML "SimpleArithExprWithAttributes" "debug/SimpleArithExprWithAttributes.html"
	parse "SimpleArithExprWithAttributes" "2+3+4"
	
}
