package sdf.test.dsl.complex

import sdf.SdfDSL
import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

ATermLang lang = new ATermLang();
SdfDSL s = lang.getInstance();

String mainModule = "languages/aterm/syntax/ATerms"

// print generated grammar
s.printGeneratedGrammarHTML(mainModule, "debug/ATermLang.html")

// test with an example aterm
result = s.parseString(mainModule,
		"hello(\"world\",42,[one,two(three),four(fix,six,seven(\"eight\\n:)\",9))])")

//	println result.parseTree
println result.consTree

MultiTreeViewer tv = new MultiTreeViewer();
MultiTreeViewer.initializeGui();
tv.addTree("Parse Tree", new ParseTreeBuilder(result.parseTree));
tv.addTree("Cons Tree", new ATermTreeBuilder(result.consTree));
tv.show(true)
