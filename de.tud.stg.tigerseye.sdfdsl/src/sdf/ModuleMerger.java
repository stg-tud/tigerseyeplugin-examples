package sdf;

import java.util.ArrayList;
import java.util.HashMap;

import aterm.ATerm;

import sdf.model.*;

/**
 * Pre-processes an SDF definition by resolving imports and alias definitions.
 * The returned module does no longer depend on other modules and does not include
 * either import or alias statements.
 * 
 * <p>The elements of the input modules are not reused in the generated modules,
 * instead copies of the elements are created where needed, so the input modules
 * are not modified.
 * 
 * @author Pablo Hoch
 * @see SdfDSL
 *
 */
public class ModuleMerger implements Visitor {
	
	private static final boolean DEBUG = false;

	private SdfDSL dsl;
	
	// state information
	private Module newMod;
	private HashMap<Symbol,Symbol> replacements;
	private HashMap<Production,Production> lexProductions, cfProductions;
	private boolean inHiddens;
	private boolean inCFSyntax;
	
	public ModuleMerger(SdfDSL dsl) {
		this.dsl = dsl;
	}
	
	/**
	 * Process the given module by resolving import and alias statements.
	 * No parameters or replacements are specified for the given module.
	 * 
	 * @param mod	Module to process
	 * @return		a new Module
	 */
	public Module processModule(Module mod) {
		return processModule(mod, null, null);
	}
	
	/**
	 * Process the given module by resolving import and alias statements.
	 * The given actual parameters are used to replace the formal parameters declared
	 * by the given module. The renamings are used to replace symbols in the given module.
	 * 
	 * <p>This method is used when the module is imported with the given parameters or renamings.
	 * 
	 * @param mod			Module to process
	 * @param parameters	actual values for the parameters of the module
	 * @param renamings		a list of symbol replacements to be performed in the module
	 * @return				a new Module
	 */
	public Module processModule(Module mod, ArrayList<Symbol> parameters, HashMap<Symbol,Symbol> renamings) {
		
		if (DEBUG) System.out.println("*** ModuleMerger.processModule(" + mod.getName() + ")");
		
		// reset production maps
		this.lexProductions = new HashMap<Production, Production>();
		this.cfProductions = new HashMap<Production, Production>();
		
		// set up replacement table
		this.replacements = new HashMap<Symbol, Symbol>();
		if (renamings != null) {
			this.replacements.putAll(renamings);
		}
		ArrayList<Symbol> formalParameters = mod.getParameters();
		if (parameters != null) {
			if (parameters.size() != formalParameters.size()) {
				// TODO: error
				System.out.println("=== INVALID NUMBER OF PARAMETERS GIVEN FOR MODULE '" + mod.getName() + "' ===");
			}
			for (int i = 0; i < formalParameters.size(); i++) {
				this.replacements.put(formalParameters.get(i), parameters.get(i));
			}
		} else if (formalParameters.size() > 0) {
			// TODO: error
			System.out.println("=== NO PARAMETERS GIVEN FOR MODULE '" + mod.getName() + "' ===");
		}
		
		this.inHiddens = false;
		
		Module processedModule = (Module)mod.visit(this, null);
//		dsl.setProcessedModule(mod.getName(), processedModule);
		return processedModule;
	}
	
	/**
	 * Gets the symbol by which the given symbol should be replaced inside this module.
	 * If no replacement is registered for the given symbol, null is returned.
	 * 
	 * @param original		Symbol to check
	 * @return				if the Symbol should be replaced, a replacement Symbol, otherwise null
	 */
	private Symbol getReplacementSymbol(Symbol original) {
		Symbol replacement = replacements.get(original);
		if (DEBUG && replacement != null) {
			System.out.println("*** Replacing " + original + " => " + replacement + " (in " + newMod.getName() + ")");
		}
		return replacement;
	}
	
	private void importModule(Import imp) {
		if (DEBUG) System.out.println("*** Importing module: " + imp + " into " + newMod.getName());
		
		// process module to import recursively
		Module moduleToImport = dsl.getModule(imp.getModuleName());
		if (moduleToImport == null) {
			// TODO: Error!
			System.out.println("=== MODULE '" + imp.getModuleName() + "' NOT FOUND! ===");
		}
		ModuleMerger subMerge = new ModuleMerger(dsl);
		Module importedModule = subMerge.processModule(moduleToImport, imp.getParameters(), imp.getRenamings());

		if (inHiddens) {
			// if the import statement is in an hiddens section, copy the exports sections of the processed
			// module into this module, but turn them into hiddens sections
			for (ExportOrHiddenSection sect : importedModule.getExportOrHiddenSections()) {
				if (sect instanceof Exports) {
					Hiddens hiddensSection = new Hiddens(sect.getGrammarElements());
					newMod.getExportOrHiddenSections().add(hiddensSection);
				}
			}
		} else {
			// if the import statement is in an exports section (or module level),
			// copy the exports sections of the processed module into this module
			for (ExportOrHiddenSection sect : importedModule.getExportOrHiddenSections()) {
				if (sect instanceof Exports) {
					newMod.getExportOrHiddenSections().add(sect);
				}
			}
		}
		
		if (DEBUG) System.out.println("*** Module imported: " + imp.getModuleName() + " into " + newMod.getName());
	}
	
	
	
	
	//// VISITOR METHODS ////

	@Override
	public Object visitDefinition(Definition def, Object o) {
		// can't occur, modules are visited directly
		return null;
	}

	@Override
	public Object visitModule(Module mod, Object o) {
		this.newMod = new Module(mod.getName());
		
		newMod.setParameters(new ArrayList<Symbol>(mod.getParameters()));
		// imports at module level are treated like imports in an exports section
		this.inHiddens = false;
		this.inCFSyntax = false;
		for (Imports impSect : mod.getImportSections()) {
			impSect.visit(this, null);
		}
		for (ExportOrHiddenSection sect : mod.getExportOrHiddenSections()) {
			ExportOrHiddenSection newSect = (ExportOrHiddenSection)sect.visit(this, null);
			newMod.getExportOrHiddenSections().add(newSect);
		}
		
		return newMod;
	}

	@Override
	public Object visitExports(Exports exp, Object o) {
		ArrayList<GrammarElement> newElements = new ArrayList<GrammarElement>();
		
		this.inHiddens = false;
		
		for (GrammarElement ge : exp.getGrammarElements()) {
			GrammarElement newGe = (GrammarElement)ge.visit(this, null);
			if (newGe != null) { // imports are removed
				newElements.add(newGe);
			}
		}
		
		return new Exports(newElements);
	}

	@Override
	public Object visitHiddens(Hiddens hid, Object o) {
		ArrayList<GrammarElement> newElements = new ArrayList<GrammarElement>();
		
		this.inHiddens = true;
		
		for (GrammarElement ge : hid.getGrammarElements()) {
			GrammarElement newGe = (GrammarElement)ge.visit(this, null);
			if (newGe != null) { // imports are removed
				newElements.add(newGe);
			}
		}
		
		return new Hiddens(newElements);
	}

	@Override
	public Object visitImports(Imports imp, Object o) {
		for (Import i : imp.getImportList()) {
			i.visit(this, null);
		}
		return null; // imports are removed
	}

	@Override
	public Object visitSorts(Sorts sor, Object o) {
		ArrayList<SortSymbol> newSymbols = new ArrayList<SortSymbol>(sor.getSymbols().size());
		
		// don't replace symbols here (because they could be replaced by non-sort symbols, which would be
		// invalid in a sorts declaration)
		for (SortSymbol s : sor.getSymbols()) {
			newSymbols.add(new SortSymbol(s.getName(), s.getLabel()));
		}
		
		return new Sorts(newSymbols);
	}

	@Override
	public Object visitContextFreeSyntax(ContextFreeSyntax syn, Object o) {
		ArrayList<Production> newProductions = new ArrayList<Production>(syn.getProductions().size());

		this.inCFSyntax = true;
		
		for (Production p : syn.getProductions()) {
			newProductions.add((Production)p.visit(this, null));
		}
	
		return new ContextFreeSyntax(newProductions);
	}

	@Override
	public Object visitLexicalSyntax(LexicalSyntax syn, Object o) {
		ArrayList<Production> newProductions = new ArrayList<Production>(syn.getProductions().size());

		this.inCFSyntax = false;
		
		for (Production p : syn.getProductions()) {
			newProductions.add((Production)p.visit(this, null));
		}
	
		return new LexicalSyntax(newProductions);
	}

	@Override
	public Object visitLexicalStartSymbols(LexicalStartSymbols sta, Object o) {
		ArrayList<Symbol> newSymbols = new ArrayList<Symbol>();
		
		this.inCFSyntax = false;

		for (Symbol s : sta.getSymbols()) {
			newSymbols.add((Symbol)s.visit(this, null));
		}
		
		return new LexicalStartSymbols(newSymbols);
	}

	@Override
	public Object visitContextFreeStartSymbols(ContextFreeStartSymbols sta,
			Object o) {
		ArrayList<Symbol> newSymbols = new ArrayList<Symbol>();
		
		this.inCFSyntax = true;

		for (Symbol s : sta.getSymbols()) {
			newSymbols.add((Symbol)s.visit(this, null));
		}
		
		return new ContextFreeStartSymbols(newSymbols);
	}

	@Override
	public Object visitProduction(Production pro, Object o) {
		ArrayList<Symbol> newLhs = new ArrayList<Symbol>();
		
		for (Symbol s : pro.getLhs()) {
			newLhs.add((Symbol)s.visit(this, null));
		}
		
		Symbol newRhs = (Symbol) pro.getRhs().visit(this, null);
		
		Production newPro;
		if (pro.getAttributes() == null) {
			newPro = new Production(newLhs, newRhs);
		} else {
			ArrayList<ATerm> newAttributes = new ArrayList<ATerm>(pro.getAttributes());
			newPro = new Production(newLhs, newRhs, newAttributes);
		}
		
		// check if an equal production already exists.
		// a production is equal if the lhs + rhs are equal. attributes are not considered.
		HashMap<Production, Production> map = inCFSyntax ? cfProductions : lexProductions;
		Production existingPro = map.get(newPro);
		
		if (existingPro != null) {
			// an equal production already exists. in this case, we need to merge the attributes.
			if (newPro.hasAttributes()) {
				// new production has attributes that need to be merged into the
				// existing production.
				existingPro.addAttributes(newPro.getAttributes());
				newPro = existingPro;
			} else {
				// new production doesn't have any attributes.
				// in this case, we can just reuse the old one.
				newPro = existingPro;
			}
			// TODO: as an optimization, it would be even better to remove this production
			// when it already exists. however, this could cause problems if the first occurence
			// is in a hiddens section, and a later occurence is in an exports section.
			// the current implementation retains all productions. this is valid since parlex
			// uses a rule set and therefore removes duplicate rules.
		} else {
			map.put(newPro, newPro);
		}
		
		return newPro;
	}

	@Override
	public Object visitImport(Import imp, Object o) {
		importModule(imp);
		return null; // imports are removed
	}

	@Override
	public Object visitCharacterClassSymbol(CharacterClassSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new CharacterClassSymbol(sym.getPattern(), sym.getLabel());
	}

	@Override
	public Object visitCharacterClassComplement(CharacterClassComplement sym,
			Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		Symbol inner = (Symbol)sym.getSymbol().visit(this, null);
		if (inner instanceof CharacterClass) {
			return new CharacterClassComplement((CharacterClass)inner, sym.getLabel());
		} else {
			// TODO: inner character class was replaced by something which is not a character class
			// this results in an error.
			return null;
		}
	}

	@Override
	public Object visitCharacterClassDifference(CharacterClassDifference sym,
			Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		Symbol left = (Symbol)sym.getLeft().visit(this, null);
		Symbol right = (Symbol)sym.getRight().visit(this, null);
		if (left instanceof CharacterClass && right instanceof CharacterClass) {
			return new CharacterClassDifference((CharacterClass)left, (CharacterClass)right, sym.getLabel());
		} else {
			// TODO: inner character class was replaced by something which is not a character class
			// this results in an error.
			return null;
		}
	}

	@Override
	public Object visitCharacterClassIntersection(
			CharacterClassIntersection sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		Symbol left = (Symbol)sym.getLeft().visit(this, null);
		Symbol right = (Symbol)sym.getRight().visit(this, null);
		if (left instanceof CharacterClass && right instanceof CharacterClass) {
			return new CharacterClassIntersection((CharacterClass)left, (CharacterClass)right, sym.getLabel());
		} else {
			// TODO: inner character class was replaced by something which is not a character class
			// this results in an error.
			return null;
		}
	}

	@Override
	public Object visitCharacterClassUnion(CharacterClassUnion sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		Symbol left = (Symbol)sym.getLeft().visit(this, null);
		Symbol right = (Symbol)sym.getRight().visit(this, null);
		if (left instanceof CharacterClass && right instanceof CharacterClass) {
			return new CharacterClassUnion((CharacterClass)left, (CharacterClass)right, sym.getLabel());
		} else {
			// TODO: inner character class was replaced by something which is not a character class
			// this results in an error.
			return null;
		}
	}

	@Override
	public Object visitLiteralSymbol(LiteralSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new LiteralSymbol(sym.getText(), sym.isCaseSensitive(), sym.getLabel());
	}

	@Override
	public Object visitOptionalSymbol(OptionalSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;

		return new OptionalSymbol((Symbol)sym.getSymbol().visit(this, null), sym.getLabel());
	}

	@Override
	public Object visitRepetitionSymbol(RepetitionSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new RepetitionSymbol((Symbol)sym.getSymbol().visit(this, null), sym.isAtLeastOnce(), sym.getLabel());
	}

	@Override
	public Object visitSortSymbol(SortSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new SortSymbol(sym.getName(), sym.getLabel());
	}

	@Override
	public Object visitSequenceSymbol(SequenceSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		ArrayList<Symbol> newSymbols = new ArrayList<Symbol>();
		for (Symbol s : sym.getSymbols()) {
			newSymbols.add((Symbol)s.visit(this, null));
		}
		
		return new SequenceSymbol(newSymbols, sym.getLabel());
	}

	@Override
	public Object visitListSymbol(ListSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new ListSymbol((Symbol)sym.getElement().visit(this, null), (Symbol)sym.getSeperator().visit(this, null), sym.isAtLeastOnce(), sym.getLabel());
	}

	@Override
	public Object visitAlternativeSymbol(AlternativeSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		return new AlternativeSymbol((Symbol)sym.getLeft().visit(this, null), (Symbol)sym.getRight().visit(this, null), sym.getLabel());
	}

	@Override
	public Object visitTupleSymbol(TupleSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		ArrayList<Symbol> newSymbols = new ArrayList<Symbol>();
		for (Symbol s : sym.getSymbols()) {
			newSymbols.add((Symbol)s.visit(this, null));
		}
		
		return new TupleSymbol(newSymbols, sym.getLabel());
	}

	@Override
	public Object visitFunctionSymbol(FunctionSymbol sym, Object o) {
		Symbol replacement = getReplacementSymbol(sym);
		if (replacement != null) return replacement;
		
		ArrayList<Symbol> newLeft = new ArrayList<Symbol>();
		for (Symbol s : sym.getLeft()) {
			newLeft.add((Symbol)s.visit(this, null));
		}
		Symbol newRight = (Symbol)sym.getRight().visit(this, null);
		
		return new FunctionSymbol(newLeft, newRight, sym.getLabel());
	}

	@Override
	public Object visitAliases(Aliases ali, Object o) {
		for (Alias a : ali.getAliasList()) {
			a.visit(this, null);
		}
		// aliases are removed
		return null;
	}

	@Override
	public Object visitAlias(Alias ali, Object o) {
		// TODO: not sure if only occurences of the symbol *after* the alias delcaration
		// need to be replaced (current implementation), or even occurences before the
		// alias declaration. can't find anything regarding that in the sdf documentation.
		this.replacements.put(ali.getAliasName(), ali.getOriginal());
		return null;
	}
}
