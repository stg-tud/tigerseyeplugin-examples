package sdf.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple character class literal.
 * 
 * A character class contains a list of zero or more characters (which stand for
 * themselves) or character ranges such as, for instance, {@code [0-9]} as an
 * abbreviation for the characters 0, 1, ..., 9. In a character range of the
 * form c1-c2 one of the following restrictions should apply:
 * 
 * <ul>
 * <li>c1 and c2 are both lower-case letters and c2 follows c1 in the alphabet, or</li>
 * 
 * <li>c1 and c2 are both upper-case letters and c2 follows c1 in the alphabet, or</li>
 * 
 * <li>c1 and c2 are both digits and the numeric value of c2 is greater than that of
 * c1, or</li>
 * 
 * <li>c1 and c2 are both escaped non-printable characters and the character code of
 * c2 is greater than that of c1</li>
 * </ul>
 * 
 * Escape Conventions Characters with a special meaning in SDF may cause
 * problems when they are needed as ordinary characters in the lexical syntax.
 * The backslash character (\) is used as escape character for the quoting of
 * special characters. You should use \c whenever you need special character c
 * as ordinary character in a definition. All individual characters in character
 * classes, except digits and letters, are always escaped with a backslash.
 * 
 * You may use the following abbreviations in literals and in character classes:
 * 
 * <ul>
 * <li>\n: newline character</li>
 * 
 * <li>\r: carriage return</li>
 * 
 * <li>\t: horizontal tabulation</li>
 * 
 * <li>\x: a non-printable character with the decimal code x.</li>
 * </ul>
 * 
 * @author Pablo Hoch
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/syntax/sdf/sdf.html#section.characters">SDF Documentation</a>
 * 
 */
public class CharacterClassSymbol extends CharacterClass {

	String pattern;

	public CharacterClassSymbol(String pattern) {
		super();
		if (pattern.startsWith("[") && pattern.endsWith("]")) {
			this.pattern = pattern.substring(1, pattern.length() - 1);
		} else {
			this.pattern = pattern;
		}
	}
	
	public CharacterClassSymbol(String pattern, String label) {
		this(pattern);
		this.setLabel(label);
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Object visit(Visitor visitor, Object o) {
		return visitor.visitCharacterClassSymbol(this, o);
	}

	@Override
	public String toString() {
		return "[" + pattern + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
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
		CharacterClassSymbol other = (CharacterClassSymbol) obj;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		return true;
	}
	
	static final Pattern decimalEscapePattern = Pattern.compile("\\\\([0-9]{1,3})");

	@Override
	public String getRegexpPattern() {
		// SDF uses a different syntax for escaping non-printable characters.
		// SDF syntax: \decimal, e.g. \0, \31, \255
		// Java regex syntax: \xHH (hexadecimal), e.g. \x00, \x1f, \xff
		// These escapes are converted in the following loop
		
		Matcher m = decimalEscapePattern.matcher(pattern);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String decimal = m.group(1);
			String hex = Integer.toString(Integer.parseInt(decimal), 16);
			if (hex.length() == 1) hex = "0" + hex;
			String newEscape = "\\x" + hex;
			m.appendReplacement(sb, Matcher.quoteReplacement(newEscape));
		}
		m.appendTail(sb);
		
		return sb.toString();
	}

	
}
