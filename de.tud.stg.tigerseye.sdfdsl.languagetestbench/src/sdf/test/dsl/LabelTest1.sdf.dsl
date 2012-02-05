package sdf.test.dsl

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

sdf(name:'LabelTest1'){

	module LabelTest1
	exports
		lexical syntax
			[a-zA-Z_] [a-zA-Z0-9_]*		-> Id
			"-"? [0-9]+					-> Int
			[ \t\n]						-> LAYOUT
			
		context-free syntax
			obj:Id "." method:Id "(" args:{Param ","}* ")"	-> Call		{cons("Call")}
			Id							-> Param		{cons("Id")}
			Int							-> Param		{cons("Int")}
			{Call ";"}+					-> Program		{cons("Program")}
			
		context-free start-symbols Program

	//
	printGeneratedGrammarHTML "LabelTest1" "debug/LabelTest1.html"
	input = "foo.bar(1, 23, test); a.b(); c.d(e, f); g.h(i)"
	
	println "Parsing: " + input
	result = parse "LabelTest1" input
	
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
