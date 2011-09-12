package sdf.model;

import java.util.ArrayList;

import aterm.ATerm;

/**
 * A production in an SDF grammar.
 * A production is read as the definition of a symbol. The symbol on the right-hand side is defined by the left-hand side of the production.
 * Note that this definition is different from the one used in BNF.
 * A production can be annotated with attributes, which are represented as ATerms.
 * 
 * <p>SDF Syntax:
 * <pre>
 * <i>Symbol*</i> -> <i>Symbol</i>
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.productions">SDF Documentation</a>
 * @see Symbol
 * @see aterm.ATerm
 * @see <a href="http://www.meta-environment.org/Meta-Environment/ATerms">ATerms documentation</a>
 *
 */
public class Production extends SdfElement {
	ArrayList<Symbol> lhs;
	Symbol rhs;
	ArrayList<ATerm> attributes;

	public Production(ArrayList<Symbol> lhs, Symbol rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.attributes = null;
	}

	public Production(ArrayList<Symbol> lhs, Symbol rhs,
			ArrayList<ATerm> attributes) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
		this.attributes = attributes;
	}

	public ArrayList<Symbol> getLhs() {
		return lhs;
	}

	public void setLhs(ArrayList<Symbol> lhs) {
		this.lhs = lhs;
	}

	public Symbol getRhs() {
		return rhs;
	}

	public void setRhs(Symbol rhs) {
		this.rhs = rhs;
	}
	
	public ArrayList<ATerm> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<ATerm> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitProduction(this, o);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Symbol sym : lhs) {
			sb.append(sym.toString());
			sb.append(" ");
		}
		sb.append("-> ");
		sb.append(rhs.toString());
		
		return sb.toString();
	}
}
