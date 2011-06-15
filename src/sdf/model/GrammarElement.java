package sdf.model;

/**
 * A grammar is the entity that can be defined in an export section or hidden
 * section of a module. It is a catch-all notion that covers more than pure
 * grammar productions and includes
 * 
 * Imports: include one module in another one.
 * 
 * Aliases: abbreviations for complex symbols.
 * 
 * Sorts: the non-terminals of the grammar.
 * 
 * Start-symbols: the start symbols of the grammar.
 * 
 * Lexical syntax: the lexical productions of the grammar.
 * 
 * Context-free syntax: the context-free productions of the grammar.
 * 
 * Priorities: the disambiguation rules.
 * 
 * Variables: definitions of variables.
 * 
 * Restrictions
 * 
 * @author Pablo Hoch
 * 
 */
public abstract class GrammarElement extends SdfElement {

}
