package sdf.model;

/**
 * Character class intersection operator.
 * Accepts all characters that are in both the first (left) and second (right) class.
 * 
 * <p>SDF Syntax:
 * <pre>
 * <i>CharacterClass</i> /\ <i>CharacterClass</i>
 * </pre>
 * 
 * <p>Example:
 * <pre>
 * [0-7] /\ [4-9]
 * </pre>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.characters">SDF Documentation</a>
 *
 */
public class CharacterClassIntersection extends CharacterClass {

	CharacterClass left;
	CharacterClass right;

	public CharacterClassIntersection(CharacterClass left,
			CharacterClass right) {
		super();
		this.left = left;
		this.right = right;
	}
	
	public CharacterClassIntersection(CharacterClass left, CharacterClass right, String label) {
		this(left, right);
		this.setLabel(label);
	}

	public CharacterClass getLeft() {
		return left;
	}

	public void setLeft(CharacterClass left) {
		this.left = left;
	}

	public CharacterClass getRight() {
		return right;
	}

	public void setRight(CharacterClass right) {
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

	@Override
	public String getRegexpPattern() {
		return left.getRegexpPattern() + "&&[" + right.getRegexpPattern() + "]";
	}
	
	
}
