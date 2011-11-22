package sdf.test.dsl.complex

import sdf.model.*
import aterm.*

import treeviewer.ui.*
import treeviewer.tree.parlex.*
import treeviewer.tree.aterms.*

/*
 * This example is taken from the SDF Library
 * Changes made:
 * - Lexical/context-free restrictions removed (not supported by SDF DSL)
 * - a:[0-9]b:[0-9]c:[0-9] changed to a:[0-9] b:[0-9] c:[0-9] (space between symbols required)
 */

public class ATermLang {

	private SdfDSL sdfDslInstance;
	
	public ATermLang() {
		
		sdf(name:'ATerm'){
			
				module languages/aterm/syntax/ATerms
				
				imports languages/aterm/syntax/IntCon
						languages/aterm/syntax/RealCon
					basic/StrCon
					basic/IdentifierCon
				
				exports
				  sorts AFun ATerm Annotation
				
				  context-free syntax
					StrCon 	-> AFun {cons("quoted")}
					IdCon 	-> AFun {cons("unquoted")}
				
				  context-free syntax
					IntCon 								-> ATerm {cons("int")}
					RealCon								-> ATerm {cons("real")}
					fun:AFun 							-> ATerm {cons("fun")}
					fun:AFun "(" args:{ATerm ","}+ ")" 	-> ATerm {cons("appl")}
					"<" type:ATerm ">" 					-> ATerm {cons("placeholder")}
					"[" elems:{ATerm ","}* "]" 			-> ATerm {cons("list")}
					trm:ATerm Annotation 				-> ATerm {cons("annotated")}
				
				  context-free syntax
					"{" annos:{ ATerm ","}+ "}" 		-> Annotation {cons("default")}
				
				hiddens
				  context-free start-symbols
					ATerm
					
					
				
				module languages/aterm/syntax/IntCon
				
				imports basic/Whitespace
					basic/NatCon
				
				hiddens
				  context-free start-symbols
					IntCon
				
				exports
				  sorts IntCon
				  context-free syntax
					NatCon         -> IntCon {cons("natural")}
					pos:"+" NatCon -> IntCon {cons("positive")}
					neg:"-" NatCon -> IntCon {cons("negative")}
					
				
				module languages/aterm/syntax/RealCon
				
				imports languages/aterm/syntax/IntCon
				
				hiddens
				  context-free start-symbols
					RealCon
				
				exports
				  sorts OptExp RealCon
				
				  context-free syntax
					"e" IntCon -> OptExp {cons("present")}
							   -> OptExp {cons("absent")}
				
					base:IntCon "." decimal:NatCon exp:OptExp  -> RealCon {cons("real-con")}
				
				/* basic modules */
					
				module basic/Whitespace
				
				exports
				  lexical syntax
					[\ \t\n\r]	-> LAYOUT {cons("whitespace")}
				
			//	  context-free restrictions
			//		LAYOUT? -/- [\ \t\n\r]
				
						
						
				module basic/NatCon
			
				exports
				
				sorts NatCon
				
				lexical syntax
				  
				 [0-9]+ -> NatCon {cons("digits")}
				
			//	lexical restrictions
			//
			//	 NatCon -/- [0-9]
			
						
				module basic/StrCon
			
				exports
				
				sorts StrCon StrChar
				
				lexical syntax
					"\\n"                      -> StrChar {cons("newline")}
					"\\t"                      -> StrChar {cons("tab")}
					"\\\""                     -> StrChar {cons("quote")}
					"\\\\"                     -> StrChar {cons("backslash")}
					"\\" a:[0-9] b:[0-9] c:[0-9] -> StrChar {cons("decimal")}
					~[\0-\31\n\t\"\\]          -> StrChar {cons("normal")}
				
					[\"] chars:StrChar* [\"]   -> StrCon  {cons("default")}
				
			
				module basic/IdentifierCon
			
				exports
				
				sorts IdCon
				
				lexical syntax
				
				head:[A-Za-z] tail:[A-Za-z\-0-9]* -> IdCon {cons("default")}
				
			//	lexical restrictions
			//
			//	IdCon -/- [A-Za-z\-0-9]
			
				this.sdfDslInstance = sdfInstance
						
			}
			
		
		
		
	}
	
	public SdfDSL getInstance() {
		return this.sdfDslInstance;
	}
		
}

