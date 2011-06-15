package sdf.model;

/**
 * Character class intersection operator.
 * Accepts all characters that are in both the first (left) and second (right) class.
 * 
 * SDF syntax:
 * <code>
 * &lt;CharacterClass&gt; /\ &lt;CharacterClass&gt;
 * </code>
 * 
 * @author Pablo Hoch
 *
 */
public class CharacterClassIntersection extends Symbol {

	CharacterClassSymbol left;
	CharacterClassSymbol right;

	public CharacterClassIntersection(CharacterClassSymbol left,
			CharacterClassSymbol right) {
		super();
		this.left = left;
		this.right = right;
	}

	public CharacterClassSymbol getLeft() {
		return left;
	}

	public void setLeft(CharacterClassSymbol left) {
		this.left = left;
	}

	public CharacterClassSymbol getRight() {
		return right;
	}

	public void setRight(CharacterClassSymbol right) {
		this.right = right;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitCharacterClassIntersection(this, o);
	}

	@Override
	public String toString() {
		return left + " /\\ " + right;
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
		CharacterClassIntersection other = (CharacterClassIntersection) obj;
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
