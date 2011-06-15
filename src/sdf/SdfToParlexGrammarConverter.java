package sdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import sdf.model.AlternativeSymbol;
import sdf.model.CharacterClassComplement;
import sdf.model.CharacterClassDifference;
import sdf.model.CharacterClassIntersection;
import sdf.model.CharacterClassSymbol;
import sdf.model.CharacterClassUnion;
import sdf.model.ContextFreeStartSymbols;
import sdf.model.ContextFreeSyntax;
import sdf.model.Definition;
import sdf.model.ExportOrHiddenSection;
import sdf.model.Exports;
import sdf.model.GrammarElement;
import sdf.model.Hiddens;
import sdf.model.Import;
import sdf.model.Imports;
import sdf.model.LexicalStartSymbols;
import sdf.model.LexicalSyntax;
import sdf.model.ListSymbol;
import sdf.model.LiteralSymbol;
import sdf.model.Module;
import sdf.model.OptionalSymbol;
import sdf.model.Production;
import sdf.model.RepetitionSymbol;
import sdf.model.SequenceSymbol;
import sdf.model.SortSymbol;
import sdf.model.Sorts;
import sdf.model.Symbol;
import sdf.model.Visitor;
import de.tud.stg.parlex.core.*;
import de.tud.stg.parlex.core.groupcategories.StringCategory;

public class SdfToParlexGrammarConverter implements Visitor {
	
	private static final boolean DEBUG = false;
	
	// namespaces
	private static final String NS_CF = "CF";
	private static final String NS_LEX = "LEX";
	
	private SdfDSL sdfDSL;
	private Grammar grammar;
	
	// state information
	private Module currentModule;
	private boolean inCFSyntax;
	private boolean inLHS;
	private int startRuleCount;
	private Rule startRule;
	private Category optLayoutCat;
	
	
	public SdfToParlexGrammarConverter(SdfDSL sdfDSL) {
		this.sdfDSL = sdfDSL;
		this.grammar = new Grammar();
	}
	
	public Grammar getGrammar(Module topLevelModule) {
		
		if (DEBUG) System.out.println("*** SDF -> Parlex Grammar ***");
		
		this.startRuleCount = 0;
		this.startRule = null;
		
		// Layout symbol
		this.optLayoutCat = createNonTerminal("LAYOUT?");
		addRule(NS_CF, optLayoutCat);
		
		topLevelModule.visit(this, null);
		
		// <START> is the start symbol, but Grammar requires a start *rule*, not a symbol
		// so if there are multiple <START> -> ... rules, we add a single <START-SYMBOL> -> <START> rule as the start rule
		if (startRuleCount > 1) {
			Category topLevelStartCat = createNonTerminal("<START-SYMBOL>");
			Category startCat = createNonTerminal("<START>");
			startRule = addRule(null, topLevelStartCat, startCat);
		} else if (startRuleCount == 0) {
			// NO start rule
			if (DEBUG) System.out.println("** NO start symbols specified!");
		}
		grammar.setStartRule(startRule);
		
		return grammar;
	}
	
	
	//// VISITOR METHODS ////

	@Override
	public Object visitDefinition(Definition def, Object o) {
		// TODO brauchen wir eigtl gar nicht...
		return null;
	}

	@Override
	public Object visitModule(Module mod, Object o) {
		if (DEBUG) System.out.println("** Module: " + mod.getName());
		this.currentModule = mod;
		this.inCFSyntax = false;
		
		for (Imports imp : mod.getImportSections()) {
			imp.visit(this, null);
		}
		for (ExportOrHiddenSection section : mod.getExportOrHiddenSections()) {
			section.visit(this, null);
		}
		
		if (DEBUG) System.out.println();
		return null;
	}

	@Override
	public Object visitExports(Exports exp, Object o) {
		for (GrammarElement elm : exp.getGrammarElements()) {
			elm.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitHiddens(Hiddens hid, Object o) {
		for (GrammarElement elm : hid.getGrammarElements()) {
			elm.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitImports(Imports imp, Object o) {
		// this should not happen, because imports are merge before the grammar is converted
		// TODO: error
		System.out.println("=== Error: imports not merged in " + currentModule.getName() + "===");
		return null;
	}
	
	@Override
	public Object visitImport(Import imp, Object o) {
		// this should not happen, because imports are merge before the grammar is converted
		// TODO: error
		System.out.println("=== Error: imports not merged in " + currentModule.getName() + "===");
		return null;
	}

	@Override
	public Object visitSorts(Sorts sor, Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitContextFreeSyntax(ContextFreeSyntax syn, Object o) {
		if (DEBUG) System.out.println("** CF syntax");
		this.inCFSyntax = true;
		for (Production prod : syn.getProductions()) {
			prod.visit(this, null);
		}
		return null;
	}

	@Override
	public Object visitLexicalSyntax(LexicalSyntax syn, Object o) {
		if (DEBUG) System.out.println("** LEX syntax");
		this.inCFSyntax = false;
		for (Production prod : syn.getProductions()) {
			prod.visit(this, null);
		}
		return null;
	}
	
	@Override
	public Object visitLexicalStartSymbols(LexicalStartSymbols sta, Object o) {
		this.inCFSyntax = false;
		Category startCat = createNonTerminal("<START>");
		for (Symbol sym : sta.getSymbols()) {
			ICategory<String> cat = wrapCategory((Category)sym.visit(this, null), NS_LEX);
			startRule = addRule(null, startCat, cat);
			startRuleCount++;
		}
		return null;
	}

	@Override
	public Object visitContextFreeStartSymbols(ContextFreeStartSymbols sta, Object o) {
		this.inCFSyntax = true;
		Category startCat = createNonTerminal("<START>");
		ICategory<String> cfLayout = wrapCategory(optLayoutCat, NS_CF);
		for (Symbol sym : sta.getSymbols()) {
			ICategory<String> cat = wrapCategory((Category)sym.visit(this, null), NS_CF);
			startRule = addRule(null, startCat, cfLayout, cat, cfLayout);
			startRuleCount++;
		}
		return null;
	}

	@Override
	public Object visitProduction(Production pro, Object o) {
		List<ICategory<String>> lhsCategories = new ArrayList<ICategory<String>>();
		Category rhsCategory;
		
		if (DEBUG) System.out.println("Production: " + pro);
		
		// LHS
		this.inLHS = true;
		for (Symbol sym : pro.getLhs()) {
			Category cat = (Category)sym.visit(this, null);
			lhsCategories.add(cat);
		}
		
		// RHS
		this.inLHS = false;
		rhsCategory = (Category)pro.getRhs().visit(this, null);
		
		// Add rule
		addRule(getCurrentNamespace(), rhsCategory, lhsCategories);
		
		// Add <rhs-LEX> -> <rhs-CF> rule for LEX rules
		if (!inCFSyntax) {
			
			addRule(null, wrapCategory(rhsCategory, NS_CF), wrapCategory(rhsCategory, NS_LEX));
			
			// Add <LAYOUT-CF>  ->  <LAYOUT?-CF>  rule
			if (rhsCategory.getName().equals("LAYOUT")) {
				addRule(NS_CF, optLayoutCat, rhsCategory);
			}
			
//			// TODO: cleaner solution =)
//			String lexName = rhsCategory.getName();
//			assert lexName.startsWith("<") && lexName.endsWith("-LEX>");
//			String baseName = lexName.substring(1, lexName.length() - 5);
//			Category cfCat = createNonTerminal("<" + baseName + "-CF>", false);
//			
//			addRule(cfCat, rhsCategory);
//			
//			// TODO: cleaner solution
//			if (baseName.equals("LAYOUT")) {
//				addRule(optLayoutCat, cfCat);
//			}
		}
		return null;
	}

	// TODO: http://download.oracle.com/javase/tutorial/essential/regex/char_classes.html
	// http://download.oracle.com/javase/6/docs/api/java/util/regex/Pattern.html
	// java character classes können union, intersection, subtraction!
	
	@Override
	public Object visitCharacterClassSymbol(CharacterClassSymbol sym, Object o) {
		// TODO ist das richtig? -> .matches -> double/boolean geht nicht?
		// TODO escapes
		Category cat = new StringCategory("[" + sym.getPattern() + "]");
		return cat;
	}
	
	@Override
	public Object visitCharacterClassComplement(CharacterClassComplement sym, Object o) {
		// TODO verschachtelte character classes
		Category cat = new StringCategory("[^" + sym.getSymbol().getPattern() + "]");
		return cat;
	}

	@Override
	public Object visitCharacterClassDifference(CharacterClassDifference sym, Object o) {
		// TODO Auto-generated method stub
		System.out.println("+++ character class difference not yet supported +++");
		return null;
	}

	@Override
	public Object visitCharacterClassIntersection(CharacterClassIntersection sym, Object o) {
		// TODO Auto-generated method stub
		System.out.println("+++ character class intersection not yet supported +++");
		return null;
	}

	@Override
	public Object visitCharacterClassUnion(CharacterClassUnion sym, Object o) {
		// TODO verschachtelte character classes
		Category cat = new StringCategory("([" + sym.getLeft().getPattern() + "]|[" + sym.getRight().getPattern() + "])");
		return cat;
	}

	@Override
	public Object visitLiteralSymbol(LiteralSymbol sym, Object o) {
		Category cat = createTerminalString(sym.getText());
		// TODO: case insensitive symbols
		return cat;
	}

	@Override
	public Object visitOptionalSymbol(OptionalSymbol sym, Object o) {
		Symbol innerSymbol = sym.getSymbol();
		Category innerCat = (Category)innerSymbol.visit(this, null);
		
		// Create category <sym>?
		String name = innerCat.getName() + "?";
		Category optCat = createNonTerminal(name);
		
		// Add rules:
		// 			-> <sym>?
		addRule(getCurrentNamespace(), optCat);
		// <sym>	-> <sym>?
		addRule(getCurrentNamespace(), optCat, innerCat);
		
		return createNonTerminal(name);
	}

	@Override
	public Object visitRepetitionSymbol(RepetitionSymbol sym, Object o) {
		Symbol innerSymbol = sym.getSymbol();
		Category innerCat = (Category)innerSymbol.visit(this, null);
		
		// <sym>+ (also needed for <sym>*)
		Category symPlus = createNonTerminal(innerCat.getName() + "+");
		// Add rules:
		// <sym>		-> <sym>+
		addRule(getCurrentNamespace(), symPlus, innerCat);
		// <sym>+ <sym>	-> <sym>+			TODO: spec uses <sym>+ <sym>+ -> <sym>+ {left)
		addRule(getCurrentNamespace(), symPlus, symPlus, innerCat);
		
		if (sym.isAtLeastOnce()) {
			return createNonTerminal(innerCat.getName() + "+");
		} else {
			// Create category <sym>*
			Category symStar = createNonTerminal(innerCat.getName() + "*");
			// Add rules:
			//			-> <sym>*
			addRule(getCurrentNamespace(), symStar);
			// <sym>+	-> <sym>*
			addRule(getCurrentNamespace(), symStar, symPlus);
			
			return createNonTerminal(innerCat.getName() + "*");
		}
	}

	@Override
	public Object visitSortSymbol(SortSymbol sym, Object o) {
		Category cat = createNonTerminal(sym.getName());
		return cat;
	}
	
	@Override
	public Object visitSequenceSymbol(SequenceSymbol sym, Object o) {
		Category cat = createNonTerminal(sym.toString());
		
		// Add rule with everything inside the sequence symbol -> cat
		ArrayList<ICategory<String>> inside = new ArrayList<ICategory<String>>();
		for (Symbol s : sym.getSymbols()) {
			inside.add((Category)s.visit(this, this));
		}
		addRule(getCurrentNamespace(), cat, inside);
		
		return createNonTerminal(sym.toString());
	}
	
	@Override
	public Object visitListSymbol(ListSymbol sym, Object o) {
		/*
		                             -> {Bool ","}*
		 {Bool ","}+                 -> {Bool ","}+
		 Bool                        -> {Bool ","}+
		 {Bool ","}+ "," {Bool ","}+ -> {Bool ","}+ {left}
		 */
		
		String innerName = sym.toString();
		innerName = innerName.substring(0, innerName.length()-1); // remove * or + at the end
		
		Category elementCat = (Category)sym.getElement().visit(this, null);
		Category seperatorCat = (Category)sym.getSeperator().visit(this, null);
		
		// <list>+ (also needed for <list>*)
		Category symPlus = createNonTerminal(innerName + "+");
		// Add rules:
		// <elm>					-> <list>+
		addRule(getCurrentNamespace(), symPlus, elementCat);
		// <elm> <sep> <list>+		-> <list>+			TODO: spec uses <list>+ <sep> <list>+ -> <list>+ {left}
		addRule(getCurrentNamespace(), symPlus, elementCat, seperatorCat, symPlus);
		
		if (sym.isAtLeastOnce()) {
			return createNonTerminal(innerName + "+");
		} else {
			// Create category <list>*
			Category symStar = createNonTerminal(innerName + "*");
			// Add rules:
			//			-> <list>*
			addRule(getCurrentNamespace(), symStar);
			// <list>+	-> <list>*
			addRule(getCurrentNamespace(), symStar, symPlus);
			
			return createNonTerminal(innerName + "*");
		}
	}
	
	public Object visitAlternativeSymbol(AlternativeSymbol sym, Object o) {
		Category cat = createNonTerminal(sym.toString());
		
		Category catLeft = (Category)sym.getLeft().visit(this, null);
		Category catRight = (Category)sym.getRight().visit(this, null);
		
		addRule(getCurrentNamespace(), cat, catLeft);
		addRule(getCurrentNamespace(), cat, catRight);
		
		return createNonTerminal(sym.toString());
	}

	
	
	//// HELPER METHODS ////
	
	private Category createNonTerminal(String name) {
		Category cat = new Category(name, false);
//		grammar.addCategory(cat); // (set)
		return cat;
	}
	
	private Category createTerminalString(String text) {
		Category cat = new Category(text, true);
//		grammar.addCategory(cat);
		return cat;
	}
	
	/**
	 * Note: LHS/RHS are swapped in SDF and Parlex!
	 * @param namespace		Namespace for the rule, applied to both RHS and LHS. Can be null when the categories already contain namespace information or namespaces are not needed.
	 * @param rhsCategory	The category on the RHS of the rule (the category that is defined by the LHS)
	 * @param lhsCategories	The categories on the LHS of the rule (can be empty)
	 */
	private Rule addRule(String namespace, ICategory<String> rhsCategory, List<ICategory<String>> lhsCategories) {
		Rule rule;
		// if in namespace, wrap non terminal categories with <name-namespace>
		if (namespace != null) {
			// if in NS_CF, add LAYOUT? between categories on the LHS
			if (namespace.equals(NS_CF)) {
				ArrayList<ICategory<String>> cfCats = new ArrayList<ICategory<String>>();
				
				for (Iterator<ICategory<String>> it = lhsCategories.iterator(); it.hasNext(); ) {
					ICategory<String> cat = it.next();
					cfCats.add(cat);
					if (it.hasNext()) {
						cfCats.add(optLayoutCat);
					}
				}
				
				lhsCategories = cfCats;
			}
			// wrap non terminals
			ArrayList<ICategory<String>> lhsMapped = new ArrayList<ICategory<String>>(lhsCategories.size());
			for (ICategory<String> cat : lhsCategories) {
				ICategory<String> mappedCat = wrapCategory(cat, namespace);
				lhsMapped.add(mappedCat);
				grammar.addCategory(mappedCat);
			}
			ICategory<String> rhsMapped = wrapCategory(rhsCategory, namespace);
			grammar.addCategory(rhsMapped);
			rule = new Rule(rhsMapped, lhsMapped);
		} else {
			grammar.addCategory(rhsCategory);
			for (ICategory<String> cat : lhsCategories) {
				grammar.addCategory(cat);
			}
			rule = new Rule(rhsCategory, lhsCategories);
		}
		grammar.addRule(rule);
		return rule;
	}
	
	private Rule addRule(String namespace, ICategory<String> rhsCategory, ICategory<String>... lhsCategories) {
		ArrayList<ICategory<String>> lhs = new ArrayList<ICategory<String>>(Arrays.asList(lhsCategories));
		return addRule(namespace, rhsCategory, lhs);
	}

	/**
	 * Adds namespace information to the category name (for non-terminal categories), returning a new category.
	 * Terminal categories are not modified and returned unchanged.
	 * @param cat
	 * @param namespace
	 * @return
	 */
	private ICategory<String> wrapCategory(ICategory<String> cat, String namespace) {
		if (cat.isTerminal()) {
			return cat;
		} else {
			return createNonTerminal("<" + cat.getName() + "-" + namespace + ">");
		}
	}

	private String getCurrentNamespace() {
		return inCFSyntax ? NS_CF : NS_LEX;
	}

}
