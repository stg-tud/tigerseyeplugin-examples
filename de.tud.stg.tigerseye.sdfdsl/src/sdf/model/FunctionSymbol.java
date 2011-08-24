package sdf.model;

import java.util.ArrayList;

/**
 * The function operator {@code (...=>...)} allows the definition of function types.
 * Left of {@code =>} zero or more symbols may occur, right of {@code =>} exactly one symbol may
 * occur. For example, {@code (Bool Int) => Int} represents a function with two argument
 * (of types Bool and Int, respectively) and a result type Int. The function
 * symbol may be used to mimick a higher order type system. The function symbol
 * also introduces some arbitrary syntax (the {@code ( )} brackets).
 * 
 * <p>SDF generates the following syntax for {@code (A B => C)}:
 * 
 * <pre>{@code (A B => C) "(" A B ")" -> C}</pre>
 * 
 * <p>Read this as "something of type {@code (A B => C)} may be applied to A and B to become a C". Note
 * that this is the only symbol that is not defined by generating productions
 * with the defined symbol on the right-hand side. The user must still define
 * the syntax for {@code (A B => C)} manually like:
 * 
 * <pre>{@code "myfunction" -> (A B => C)}</pre>
 * 
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.functionsymbols">SDF Documentation</a>
 * 
 */
public class FunctionSymbol extends Symbol {
	
	ArrayList<Symbol> left;
	Symbol right;
	
	public FunctionSymbol(ArrayList<Symbol> left, Symbol right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public FunctionSymbol(ArrayList<Symbol> left, Symbol right, String label) {
		this(left, right);
		this.setLabel(label);
	}
	
	public ArrayList<Symbol> getLeft() {
		return left;
	}

	public void setLeft(ArrayList<Symbol> left) {
		this.left = left;
	}

	public Symbol getRight() {
		return right;
	}

	public void setRight(Symbol right) {
		this.right = right;
	}
	
	@Override
	public Object visit(Visitor visitor, Object o) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		
		boolean first = true;
		for (Symbol sym : left) {
			if (!first) sb.append(" ");
			
			sb.append(sym.toString());
			
			first = false;
		}
		
		sb.append(" => ");
		sb.append(right.toString());
		sb.append(")");
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionSymbol other = (FunctionSymbol) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}



}
