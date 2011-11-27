package sdf.test.dsl

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

sdf(name:'ConsTest1'){

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
			Expr "+" Term		-> Expr		{cons("Plus")}
			Expr "-" Term		-> Expr		{cons("Minus")}
			Term				-> Expr

			Term "*" Factor		-> Term		{cons("Mult")}
			Term "/" Factor		-> Term		{cons("Div")}
			Factor				-> Term
	
			Number				-> Factor	{cons("Int")}
			"(" Expr ")"		-> Factor
	
	hiddens
		context-free start-symbols Expr

		
			
	module SimpleLang
		imports ArithExpr2
	exports
		sorts Program Stmt
	
		context-free syntax
		"print" Expr		-> Stmt			{cons("Print")}
		"exit"				-> Stmt			{cons("Exit")}
							-> Stmt
	
		{Stmt ";"}*			-> Program		{cons("Program")}
	
	hiddens
		context-free start-symbols Program
		
		
	// <- comment is required here, otherwise result is also parsed as a sort symbol :(
	result = parse "SimpleLang" "print 4 + 8 * (15 / (16+23)) - 42; print 12345+2; exit"
	
//	println result.parseTree
	println result.consTree
	
	MultiTreeViewer tv = new MultiTreeViewer();
	MultiTreeViewer.initializeGui();
	tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
	tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
	tv.show(true)
	
	
}
