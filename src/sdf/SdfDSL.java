package sdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.tud.stg.parlex.core.Grammar;

import sdf.model.*;

public class SdfDSL {

	/**
	 * All unmodified modules as they appear in the input specification
	 */
	private HashMap<String, Module> modules = new HashMap<String, Module>();
	
//	/**
//	 * Versions of the input modules, where imports and renamings/parameter mappings have been performed
//	 */
//	private HashMap<String, Module> processedModules = new HashMap<String, Module>();
	
	public HashMap<String, Module> getModules() {
		return modules;
	}
	
	public Module getModule(String moduleName) {
		return modules.get(moduleName);
	}
	
//	public Module getProcessedModule(String moduleName) {
//		return processedModules.get(moduleName);
//	}
//	
//	protected void setProcessedModule(String moduleName, Module mod) {
//		processedModules.put(moduleName, mod);
//	}
	
	public Grammar getGrammar(String topLevelModuleName) {
		return getGrammar(topLevelModuleName, true);
	}
	
	public Grammar getGrammar(String topLevelModuleName, boolean cleanGrammar) {
		// find top level module
		Module topLevelModule = modules.get(topLevelModuleName);
		
		// merge imports in top level module (and imported modules)
		ModuleMerger merger = new ModuleMerger(this);
		Module mainModule = merger.processModule(topLevelModule);
		
		// convert sdf model -> parlex grammar
		SdfToParlexGrammarConverter converter = new SdfToParlexGrammarConverter(this);
		Grammar g = converter.getGrammar(mainModule);

		// remove unused rules if requested
		if (cleanGrammar) {
			return GrammarCleaner.clean(g);
		} else {
			return g;
		}
	}
	
	
	//// TOP LEVEL ELEMENTS ////
	
	
	// TODO: Definition




	// module p0 p1 p2
	public Module moduleWithoutParameters(String name, Imports[] imports, ExportOrHiddenSection[] exportOrHiddenSections) {
		Module mod = new Module(name);
		
		mod.setImportSections(new ArrayList<Imports>(Arrays.asList(imports)));
		mod.setExportOrHiddenSections(new ArrayList<ExportOrHiddenSection>(Arrays.asList(exportOrHiddenSections)));
		
		modules.put(name, mod);
		
		return mod;
	}
	
	// module p0[p1] p2 p3
	public Module moduleWithParameters(String name, Symbol[] params, Imports[] imports, ExportOrHiddenSection[] exportOrHiddenSections) {
		Module mod = moduleWithoutParameters(name, imports, exportOrHiddenSections);
		
		mod.setParameters(new ArrayList<Symbol>(Arrays.asList(params)));
		
		return mod;
	}
	
	
	
	
	//// SYMBOLS ////
	
	
	
	
	// "p0"
	public LiteralSymbol caseSensitiveliteralSymbol(String text) {
		return new LiteralSymbol(text, true);
	}
	
	// 'p0'
	public LiteralSymbol caseInsensitiveLiteralSymbol(String text) {
		return new LiteralSymbol(text, false);
	}
	
	// p0
	// Sort names always start with a capital letter and may be followed by letters and/or digits. Hyphens (-) may be embedded in a sort name. 
	public SortSymbol sortSymbol(String name) {
		return new SortSymbol(name);
	}
	
	// [p0]
	public CharacterClassSymbol characterClassSymbol(String pattern) {
		return new CharacterClassSymbol(pattern);
	}
	
	// ~p0
	public CharacterClassComplement characterClassComplement(CharacterClassSymbol sym) {
		return new CharacterClassComplement(sym);
	}
	
	// p0/p1
	public CharacterClassDifference characterClassDifference(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassDifference(left, right);
	}
	
	// p0/\p1
	public CharacterClassIntersection characterClassIntersection(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassIntersection(left, right);
	}
	
	// p0\/p1
	public CharacterClassUnion characterClassUnion(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassUnion(left, right);
	}
	
	// p0?
	public OptionalSymbol optionalSymbol(Symbol symbol) {
		return new OptionalSymbol(symbol);
	}
	
	// p0*
	public RepetitionSymbol repetitionSymbolAtLeastZero(Symbol symbol) {
		return new RepetitionSymbol(symbol, false);
	}
	
	// p0+
	public RepetitionSymbol repetitionSymbolAtLeastOnce(Symbol symbol) {
		return new RepetitionSymbol(symbol, true);
	}
	
	// (p0)
	public SequenceSymbol sequenceSymbol(Symbol[] symbols) {
		return new SequenceSymbol(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// {p0 p1}*
	public ListSymbol listSymbolAtLeastZero(Symbol element, Symbol seperator) {
		return new ListSymbol(element, seperator, false);
	}
	
	// {p0 p1}+
	public ListSymbol listSymbolAtLeastOnce(Symbol element, Symbol seperator) {
		return new ListSymbol(element, seperator, true);
	}
	
	
	
	
	//// MODULE LEVEL /////
	
	
	
	
	public Exports exports(GrammarElement[] grammarElements) {
		return new Exports(new ArrayList<GrammarElement>(Arrays.asList(grammarElements)));
	}
	
	public Hiddens hiddens(GrammarElement[] grammarElements) {
		return new Hiddens(new ArrayList<GrammarElement>(Arrays.asList(grammarElements)));
	}
	
	
	
	//// GRAMMAR ELEMENTS ////
	
	
	
	
	// imports p0
	public Imports imports(Import[] importList) {
		return new Imports(new ArrayList<Import>(Arrays.asList(importList)));
	}
	
	// p0
	public Import importModuleWithoutParameters(String moduleName) {
		return new Import(moduleName);
	}
	
	// p0[p1]
	public Import importModuleWithParameters(String moduleName, Symbol[] params) {
		return new Import(moduleName, new ArrayList<Symbol>(Arrays.asList(params)));
	}
	
	// TODO: imports mit renamings, imports mit params UND renamings
	
	// sorts p0
	public Sorts sorts(SortSymbol[] sortSymbols) {
		return new Sorts(new ArrayList<SortSymbol>(Arrays.asList(sortSymbols)));
	}
	
	// lexical syntax p0
	public LexicalSyntax lexicalSyntax(Production[] productions) {
		return new LexicalSyntax(new ArrayList<Production>(Arrays.asList(productions)));
	}
	
	// context-free syntax p0
	public ContextFreeSyntax contextFreeSyntax(Production[] productions) {
		return new ContextFreeSyntax(new ArrayList<Production>(Arrays.asList(productions)));
	}
	
	// lexical start-symbols p0
	public LexicalStartSymbols lexicalStartSymbols(Symbol[] symbols) {
		return new LexicalStartSymbols(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// context-free start-symbols p0
	public ContextFreeStartSymbols contextFreeStartSymbols(Symbol[] symbols) {
		return new ContextFreeStartSymbols(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// p0 -> p1
	// TODO: attributes
	public Production production(Symbol[] lhs, Symbol rhs) {
		return new Production(new ArrayList<Symbol>(Arrays.asList(lhs)), rhs);
	}
}
