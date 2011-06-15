package sdf.model;

public interface Visitor {
	
	// Top level
	public Object visitDefinition(Definition def, Object o);
	public Object visitModule(Module mod, Object o);
	
	// Sections
	public Object visitExports(Exports exp, Object o);
	public Object visitHiddens(Hiddens hid, Object o);
	
	// Grammar
	public Object visitImports(Imports imp, Object o);
	public Object visitSorts(Sorts sor, Object o);
	public Object visitContextFreeSyntax(ContextFreeSyntax syn, Object o);
	public Object visitLexicalSyntax(LexicalSyntax syn, Object o);
	public Object visitLexicalStartSymbols(LexicalStartSymbols sta, Object o);
	public Object visitContextFreeStartSymbols(ContextFreeStartSymbols sta, Object o);
	
	// Grammar subelements
	public Object visitProduction(Production pro, Object o);
	public Object visitImport(Import imp, Object o);
	
	// Symbols
	public Object visitCharacterClassSymbol(CharacterClassSymbol sym, Object o);
	public Object visitCharacterClassComplement(CharacterClassComplement sym, Object o);
	public Object visitCharacterClassDifference(CharacterClassDifference sym, Object o);
	public Object visitCharacterClassIntersection(CharacterClassIntersection sym, Object o);
	public Object visitCharacterClassUnion(CharacterClassUnion sym, Object o);
	public Object visitLiteralSymbol(LiteralSymbol sym, Object o);
	public Object visitOptionalSymbol(OptionalSymbol sym, Object o);
	public Object visitRepetitionSymbol(RepetitionSymbol sym, Object o);
	public Object visitSortSymbol(SortSymbol sym, Object o);
	public Object visitSequenceSymbol(SequenceSymbol sym, Object o);
	public Object visitListSymbol(ListSymbol sym, Object o);
	public Object visitAlternativeSymbol(AlternativeSymbol sym, Object o);
}
