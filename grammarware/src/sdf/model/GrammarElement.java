package sdf.model;

/**
 * A grammar element is the entity that can be defined in an export section or hidden
 * section of a module. It is a catch-all notion that covers more than pure
 * grammar productions and includes:
 * 
 * <ul>
 * <li>{@link Imports Imports}: include one module in another one.</li>
 * <li>{@link Aliases Aliases}: abbreviations for complex symbols.</li>
 * <li>{@link Sorts Sorts}: the non-terminals of the grammar.</li>
 * <li>{@link StartSymbols Start-symbols}: the start symbols of the grammar.</li>
 * <li>{@link LexicalSyntax Lexical syntax}: the lexical productions of the grammar.</li>
 * <li>{@link ContextFreeSyntax Context-free syntax}: the context-free productions of the grammar.</li>
 * <li>Priorities: the disambiguation rules.</li>
 * <li>Variables: definitions of variables.</li>
 * <li>Restrictions</li>
 * </ul>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.grammars">SDF Documentation</a>
 * 
 */
public abstract class GrammarElement extends SdfElement {

}
