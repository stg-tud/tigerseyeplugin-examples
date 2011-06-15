package sdf.model;

/**
 * Alternative operator
 * 
 * SDF syntax:
 * <code>
 * &lt;Symbol&gt; | &lt;Symbol&gt;
 * </code>
 * 
 * @author Pablo Hoch
 *
 */
public class AlternativeSymbol extends Symbol {

	Symbol left;
	Symbol right;

	public AlternativeSymbol(Symbol left, Symbol right) {
		super();
		this.left = left;
		this.right = right;
	}

	public Symbol getLeft() {
		return left;
	}

	public void setLeft(Symbol left) {
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
		return visitor.visitAlternativeSymbol(this, o);
	}

	@Override
	public String toString() {
		return "(" + left + " | " + right + ")";
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
		AlternativeSymbol other = (AlternativeSymbol) obj;
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
