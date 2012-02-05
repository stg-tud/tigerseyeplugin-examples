package sdf.test.dsl

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

// This example grammar is taken from:
// http://hydra.nixos.org/build/1451061/download/1/manual/chunk-chapter/tutorial-parsing.html#id1302289

sdf(name:'ConsTest2'){

	module Expression
	imports
	  Lexical Operators
	
	exports
	  context-free start-symbols Exp
	  context-free syntax
	    Id          -> Exp {cons("Var")}
	    IntConst    -> Exp {cons("Int")}
	    "(" Exp ")" -> Exp {bracket, skip}

		
	module Lexical
	exports
	  sorts Id IntConst
	
	  lexical syntax
		[\ \t\n]  -> LAYOUT
		[a-zA-Z]+ -> Id
		[0-9]+    -> IntConst
			
			
	module Operators
	exports
	  sorts Exp
	  context-free syntax
	    Exp "*" Exp -> Exp {left, cons("Times")}
	    Exp "/" Exp -> Exp {left, cons("Div")}
	    Exp "%" Exp -> Exp {left, cons("Mod")}
	  
	    Exp "+" Exp -> Exp {left, cons("Plus")}
	    Exp "-" Exp -> Exp {left, cons("Minus")}
	
	  context-free priorities
	    {
	      Exp "*" Exp -> Exp
	      Exp "/" Exp -> Exp
	      Exp "%" Exp -> Exp
	    } 
	  > {
	      Exp "+" Exp -> Exp
	      Exp "-" Exp -> Exp
	    }

		
	// <- comment is required here to mark the end of the sdf part
	result = parse "Expression" "(bar + n) * 1234"

	// Expected AST (from Stratego documentation linked above):
	// Times(Plus(Var("a"),Var("n")),Int("1"))	
	println result.consTree
	
	MultiTreeViewer tv = new MultiTreeViewer();
	MultiTreeViewer.initializeGui();
	tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
	tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
	tv.show(true)
	
	
}
