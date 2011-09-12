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
	
	public boolean hasAttributes() {
		return (attributes != null) && !attributes.isEmpty();
	}
	
	public void addAttributes(ArrayList<ATerm> newAttributes) {
		if (hasAttributes()) {
			for (ATerm attr : newAttributes) {
				if (!this.attributes.contains(attr)) {
					this.attributes.add(attr);
				}
			}
		} else {
			setAttributes(new ArrayList<ATerm>(newAttributes));
		}
	}
	
	public void addAttribute(ATerm newAttribute) {
		ArrayList<ATerm> list = new ArrayList<ATerm>(1);
		list.add(newAttribute);
		addAttributes(list);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lhs == null) ? 0 : lhs.hashCode());
		result = prime * result + ((rhs == null) ? 0 : rhs.hashCode());
		return result;
	}

	@Override
	// note that attributes are not included in the comparison (this is intended).
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Production other = (Production) obj;
		if (lhs == null) {
			if (other.lhs != null)
				return false;
		} else if (!lhs.equals(other.lhs))
			return false;
		if (rhs == null) {
			if (other.rhs != null)
				return false;
		} else if (!rhs.equals(other.rhs))
			return false;
		return true;
	}
	
	
}
