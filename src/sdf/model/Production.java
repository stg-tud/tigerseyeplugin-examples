package sdf.model;

import java.util.ArrayList;

/**
 * A production in an SDF grammar.
 * 
 * SDF syntax:
 * <code>
 * &lt;Symbol&gt;* -> &lt;Symbol&gt;
 * </code>
 * 
 * Note that, unlike in BNF, the symbol on the RHS is defined by the LHS.
 * 
 * @author Pablo Hoch
 *
 */
public class Production extends SdfElement {
	ArrayList<Symbol> lhs;
	Symbol rhs;

	// TODO: attributes
	public Production(ArrayList<Symbol> lhs, Symbol rhs) {
		super();
		this.lhs = lhs;
		this.rhs = rhs;
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
