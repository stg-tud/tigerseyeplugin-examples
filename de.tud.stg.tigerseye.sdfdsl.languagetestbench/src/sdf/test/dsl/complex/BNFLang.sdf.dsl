package sdf.test.dsl.complex

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

sdf(name:'BNFLang'){

	module languages/bnf/syntax/BNF
	
	imports basic/Whitespace
	
	hiddens
	context-free start-symbols
	  Rules
	
	exports
	sorts Rule Rules NonTerminal Terminal Element Elements
	
	lexical syntax
	  "<" ~[\<\>]+ ">"               -> NonTerminal
	  ~[\<\ \t\|\[\]\{\}] ~[\ \t\n]* -> Terminal
	  "..."                          -> Terminal    {reject}
	  "::="                          -> Terminal    {reject}
		
	context-free syntax
	  NonTerminal "::=" Elements -> Rule
	  {Element "|"}+             -> Elements
	  Rule*                      -> Rules
	  NonTerminal                -> Element
	  Terminal                   -> Element
	  "[" {Element "|"}+ "]"     -> Element
	  "{" {Element "|"}+ "}"     -> Element
		  
      Element "..." -> Element
	  Element Element -> Element {left}
	  "|"             -> Element
	   "["             -> Element
	   "]"             -> Element
	   "{"             -> Element
	   "}"             -> Element
	   "<"             -> Element
	   ">"             -> Element
	   "..."           -> Element
	   "::="           -> Element
	   Element Element -> Element {left}
	
	context-free priorities
	  Element "..." -> Element >
	  Element Element -> Element {left},
	  { non-assoc: "|"             -> Element
				   "["             -> Element
				   "]"             -> Element
				   "{"             -> Element
				   "}"             -> Element
				   "<"             -> Element
				   ">"             -> Element
				   "..."           -> Element
				   "::="           -> Element
				   Element Element -> Element {left}  }
	
		  
	module basic/Whitespace

	exports
	  lexical syntax
	    [\ \t\n\r]	-> LAYOUT {cons("whitespace")}
	

	//
	printGeneratedGrammarHTML "languages/bnf/syntax/BNF" "debug/BNFLang.html"
	input = """<start> ::= <expr>
<expr> ::= <num> | <expr> + <expr> | <expr> - <expr> | <expr> * <expr> | <expr> / <expr>
<num> ::= <digit> {<digit>}
<digit> ::= 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9
"""
	
	println "Parsing: " + input
	result = parse "languages/bnf/syntax/BNF" input
	
	if (result.valid) {
		println result.consTree
		
		MultiTreeViewer tv = new MultiTreeViewer();
		MultiTreeViewer.initializeGui();
		tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
		tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
		tv.show(true)
	} else {
		println "invalid parse"
	}

	
}
