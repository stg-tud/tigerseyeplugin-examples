package sdf;

import groovy.lang.Closure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.tud.stg.parlex.core.Grammar;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;

import de.tud.stg.popart.builder.core.annotations.DSL;
import de.tud.stg.popart.builder.core.annotations.DSLMethod;
import de.tud.stg.popart.eclipse.core.debug.annotations.PopartType;
import de.tud.stg.popart.eclipse.core.debug.model.keywords.PopartOperationKeyword;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.typeHandling.TypeHandler;

import sdf.model.*;

/**
 * 
 * 
 * @author Pablo Hoch
 *
 */
@DSL(	whitespaceEscape = " ",
		typeRules = {
				SdfDSL.SortSymbolType.class,
				SdfDSL.ModuleIdType.class,
				SdfDSL.CharacterClassSymbolType.class,
		})
public class SdfDSL implements de.tud.stg.popart.dslsupport.DSL {

	/**
	 * All unmodified modules as they appear in the input specification
	 */
	private HashMap<String, Module> modules = new HashMap<String, Module>();
	
	public HashMap<String, Module> getModules() {
		return modules;
	}
	
	public Module getModule(String moduleName) {
		return modules.get(moduleName);
	}
	
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
	
	public Object eval(HashMap map, Closure cl) {
		cl.setDelegate(this);
		cl.setResolveStrategy(Closure.DELEGATE_FIRST);
		return cl.call();
	}
	
	
	//// TOP LEVEL ELEMENTS ////
	
	
	// TODO: Definition



	// TODO: apparently arrays cannot be empty? e.g. a module needs at least one import etc :(

	// module p0 p1 p2
	@DSLMethod(prettyName = "module p0 p1 p2", topLevel = true)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Module moduleWithoutParameters(
			ModuleId name,
			@DSL(arrayDelimiter = " ") Imports[] imports,
			@DSL(arrayDelimiter = " ") ExportOrHiddenSection[] exportOrHiddenSections) {
		Module mod = new Module(name);

		mod.setImportSections(new ArrayList<Imports>(Arrays.asList(imports)));
		mod.setExportOrHiddenSections(new ArrayList<ExportOrHiddenSection>(Arrays.asList(exportOrHiddenSections)));

		modules.put(name.toString(), mod);

		return mod;
	}
	
	// module p0[p1] p2 p3
	@DSLMethod(prettyName = "module p0 [ p1 ] p2 p3")
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Module moduleWithParameters(
			ModuleId name,
			@DSL(arrayDelimiter = ",") Symbol[] params,
			@DSL(arrayDelimiter = " ") Imports[] imports,
			@DSL(arrayDelimiter = " ") ExportOrHiddenSection[] exportOrHiddenSections) {
		Module mod = moduleWithoutParameters(name, imports, exportOrHiddenSections);

		mod.setParameters(new ArrayList<Symbol>(Arrays.asList(params)));

		return mod;
	}
	
	
	// for testing purposes
	
	@DSLMethod(prettyName = "parse  p0  p1")
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public boolean parseString(String topLevelModule, String input) {
		Grammar grammar = getGrammar(topLevelModule);
		
		System.out.println("== SDF: Testing module " + topLevelModule + " with input: \"" + input + "\" ==");
		EarleyParser parser = new EarleyParser(grammar);
		Chart chart = (Chart) parser.parse(input);
		chart.rparse((de.tud.stg.parlex.core.Rule)grammar.getStartRule());
		System.out.println(chart.toString());
		boolean valid = chart.isValidParse();
		
		if (valid) {
			System.out.println("String recognized!");
			System.out.println("AST:");
			System.out.println(chart.getAST().toString());
			System.out.println();
		} else {
			System.out.println("String not recognized");
		}
		
		return valid;
	}
	
	
	
	
	//// SYMBOLS ////
	
	
	
	
	// "p0"
	@DSLMethod(prettyName = "p0", topLevel = false) // TODO ?
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public LiteralSymbol caseSensitiveLiteralSymbol(String text) {
		return new LiteralSymbol(text, true);
	}
	
	// 'p0'
	@DSLMethod(prettyName = "p0", topLevel = false) // TODO ?
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public LiteralSymbol caseInsensitiveLiteralSymbol(@DSL(stringQuotation = "('.*?')")String text) {
		return new LiteralSymbol(text, false);
	}
	
	// p0
	//@DSLMethod(prettyName = "p0", topLevel = false)
	public SortSymbol sortSymbol(String name) {
		return new SortSymbol(name);
	}
	
//	@DSLMethod(prettyName = "p0", topLevel = false)
//	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
//	public SortSymbol sortSymbol(SortSymbol s) { return s; }
	
	// [p0]
	// TODO: CharacterClassSymbolType - ???
//	@DSLMethod(prettyName = "[p0]", topLevel = false)
//	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClassSymbol characterClassSymbol(String pattern) {
		return new CharacterClassSymbol(pattern);
	}
	
	// ~p0
	@DSLMethod(prettyName = "~  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClassComplement characterClassComplement(CharacterClassSymbol sym) {
		return new CharacterClassComplement(sym);
	}
	
	// p0/p1
	@DSLMethod(prettyName = "p0  /  p1", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClassDifference characterClassDifference(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassDifference(left, right);
	}
	
	// p0/\p1
	@DSLMethod(prettyName = "p0  /\\  p1", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClassIntersection characterClassIntersection(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassIntersection(left, right);
	}
	
	// p0\/p1
	@DSLMethod(prettyName = "p0  \\/  p1", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClassUnion characterClassUnion(CharacterClassSymbol left, CharacterClassSymbol right) {
		return new CharacterClassUnion(left, right);
	}
	
	// p0?
	@DSLMethod(prettyName = "p0  ?", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public OptionalSymbol optionalSymbol(Symbol symbol) {
		return new OptionalSymbol(symbol);
	}
	
	// p0*
	@DSLMethod(prettyName = "p0  *", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public RepetitionSymbol repetitionSymbolAtLeastZero(Symbol symbol) {
		return new RepetitionSymbol(symbol, false);
	}
	
	// p0+
	@DSLMethod(prettyName = "p0  +", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public RepetitionSymbol repetitionSymbolAtLeastOnce(Symbol symbol) {
		return new RepetitionSymbol(symbol, true);
	}
	
	// (p0)
	@DSLMethod(prettyName = "(  p0  )", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public SequenceSymbol sequenceSymbol(Symbol[] symbols) {
		return new SequenceSymbol(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// {p0 p1}*
	@DSLMethod(prettyName = "{  p0  p1  }  *", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ListSymbol listSymbolAtLeastZero(Symbol element, Symbol seperator) {
		return new ListSymbol(element, seperator, false);
	}
	
	// {p0 p1}+
	@DSLMethod(prettyName = "{  p0  p1  }  +", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ListSymbol listSymbolAtLeastOnce(Symbol element, Symbol seperator) {
		return new ListSymbol(element, seperator, true);
	}
	
	// p0 | p1
	@DSLMethod(prettyName = "p0  |  p1", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public AlternativeSymbol alternativeSymbol(Symbol left, Symbol right) {
		return new AlternativeSymbol(left, right);
	}
	
	// Methods to convert symbol subclasses to symbol
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(SortSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(LiteralSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(AlternativeSymbol s) { return s; }	
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(ListSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(OptionalSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(RepetitionSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(SequenceSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Symbol symbol(CharacterClass s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClass characterClass(CharacterClassSymbol s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClass characterClass(CharacterClassComplement s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClass characterClass(CharacterClassDifference s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClass characterClass(CharacterClassIntersection s) { return s; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public CharacterClass characterClass(CharacterClassUnion s) { return s; }
	
	
	//// MODULE LEVEL /////
	
	
	
	@DSLMethod(prettyName = "exports  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Exports exports(@DSL(arrayDelimiter = " ")GrammarElement[] grammarElements) {
		return new Exports(new ArrayList<GrammarElement>(Arrays.asList(grammarElements)));
	}
	
	@DSLMethod(prettyName = "hiddens  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Hiddens hiddens(@DSL(arrayDelimiter = " ")GrammarElement[] grammarElements) {
		return new Hiddens(new ArrayList<GrammarElement>(Arrays.asList(grammarElements)));
	}
	
	// Methods to convert Exports/Hiddens to ExportOrHiddenSection
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ExportOrHiddenSection exportOrHiddenSection(Exports e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ExportOrHiddenSection exportOrHiddenSection(Hiddens e) { return e; }
	
	
	
	//// GRAMMAR ELEMENTS ////
	
	
	
	
	// imports p0
	@DSLMethod(prettyName = "imports  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Imports importsStatement(@DSL(arrayDelimiter = " ")Import[] importList) {
		return new Imports(new ArrayList<Import>(Arrays.asList(importList)));
	}
	
	// p0
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Import importModuleWithoutParameters(ModuleId moduleName) {
		return new Import(moduleName.toString());
	}
	
	// p0[p1]
	@DSLMethod(prettyName = "p0  [  p1  ]", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Import importModuleWithParameters(ModuleId moduleName, @DSL(arrayDelimiter = ",")Symbol[] params) {
		return new Import(moduleName.toString(), new ArrayList<Symbol>(Arrays.asList(params)));
	}
	
	// TODO: imports mit renamings, imports mit params UND renamings
	// syntax für imports ist wie folgt:
	// foo/bar/Test									% simpler import
	// foo/bar/Test[A, B, C]						% import mit parametern
	// foo/bar/Test[A => B, C => D]					% import mit renamings
	// foo/bar/Test[A, B][C => D]					% import mit beidem
	
	// sorts p0
	@DSLMethod(prettyName = "sorts  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Sorts sortsDeclaration(@DSL(arrayDelimiter = " ")SortSymbol[] sortSymbols) {
		return new Sorts(new ArrayList<SortSymbol>(Arrays.asList(sortSymbols)));
	}
	
	// lexical syntax p0
	@DSLMethod(prettyName = "lexical  syntax  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public LexicalSyntax lexicalSyntax(@DSL(arrayDelimiter = " ")Production[] productions) {
		return new LexicalSyntax(new ArrayList<Production>(Arrays.asList(productions)));
	}
	
	// context-free syntax p0
	@DSLMethod(prettyName = "context-free  syntax  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ContextFreeSyntax contextFreeSyntax(@DSL(arrayDelimiter = " ")Production[] productions) {
		return new ContextFreeSyntax(new ArrayList<Production>(Arrays.asList(productions)));
	}
	
	// lexical start-symbols p0
	@DSLMethod(prettyName = "lexical  start-symbols  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public LexicalStartSymbols lexicalStartSymbols(@DSL(arrayDelimiter = " ")Symbol[] symbols) {
		return new LexicalStartSymbols(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// context-free start-symbols p0
	@DSLMethod(prettyName = "context-free  start-symbols  p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public ContextFreeStartSymbols contextFreeStartSymbols(@DSL(arrayDelimiter = " ")Symbol[] symbols) {
		return new ContextFreeStartSymbols(new ArrayList<Symbol>(Arrays.asList(symbols)));
	}
	
	// p0 -> p1
	// TODO: attributes
	@DSLMethod(prettyName = "p0  ->  p1", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Production production(@DSL(arrayDelimiter = " ")Symbol[] lhs, Symbol rhs) {
		return new Production(new ArrayList<Symbol>(Arrays.asList(lhs)), rhs);
	}
	
	
	// Methods to convert grammar elements to GrammarElement
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public GrammarElement grammarElement(Imports e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public GrammarElement grammarElement(Sorts e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public GrammarElement grammarElement(StartSymbols e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public GrammarElement grammarElement(Syntax e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public StartSymbols startSymbols(ContextFreeStartSymbols e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public StartSymbols startSymbols(LexicalStartSymbols e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Syntax syntax(ContextFreeSyntax e) { return e; }
	
	@DSLMethod(prettyName = "p0", topLevel = false)
	@PopartType(clazz = PopartOperationKeyword.class, breakpointPossible = 1)
	public Syntax syntax(LexicalSyntax e) { return e; }
	
	
	
	
	//// TYPE HANDLERS ////
	
	/**
	 * A sort corresponds to a non-terminal, e.g., Bool. Sort names always start with a capital letter and may be followed by
	 * letters and/or digits. Hyphens (-) may be embedded in a sort name. 
	 * 
	 * Parameterized sort names (TODO): <Sort>[[<Symbol1>, <Symbol2>, ... ]]
	 */
	public static class SortSymbolType extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return SortSymbol.class;
		}

		@Override
		public String getRegularExpression() {
			return "([A-Z][-A-Za-z0-9]*)";
		}
		
	}
	
	/**
	 * 
	 * letters:[A-Za-z0-9\_\-]+ -> ModuleWord {cons("word")}
	 * 
	 * ModuleWord -> ModuleId {cons("leaf")}
	 * sep:"/" basename:ModuleId -> ModuleId {cons("root")}
	 * dirname:ModuleWord sep:"/" basename:ModuleId -> ModuleId {cons("path")}
	 * 
	 * @author Pablo Hoch
	 * 
	 */
	public static class ModuleIdType extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return ModuleId.class;
		}

		@Override
		public String getRegularExpression() {
			return "(/?([-_A-Za-z0-9]+)(/[-_A-Za-z0-9]+)*)";
		}
		
	}
	
	public static class CharacterClassSymbolType extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return CharacterClassSymbol.class;
		}

		@Override
		public String getRegularExpression() {
			// TODO: escapes (\] etc) inside the class
			// TODO: [] should not be part of the match
			return "\\[([^\\]]+)\\]";
//			return "([^\\]]+)";
		}
		
	}
}
