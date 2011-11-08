package sdf;

import aterm.ATerm;
import de.tud.stg.parlex.ast.IAbstractNode;
import de.tud.stg.parlex.parser.earley.Chart;

/**
 * Represents the result of a parse using an SDF grammar.
 * If the input was valid, this class provides:
 * <ul>
 * <li>The parse tree generated by the earley parser</li>
 * <li>The AST built using the cons annotations in the SDF grammar</li>
 * </ul>
 * 
 * @author Pablo Hoch
 *
 */
public class ParseResult {

	private boolean valid;
	private IAbstractNode parseTree;
	private ATerm consTree;
	
	public ParseResult(Chart chart) {
		this.valid = chart.isValidParse();
		if (valid) {
			this.parseTree = chart.getAST();
		}
	}
	
	private void constructTree() {
		ATermConstructor atermConstructor = new ATermConstructor(parseTree);
		this.consTree = atermConstructor.constructTree();
	}

	public boolean isValid() {
		return valid;
	}

	public IAbstractNode getParseTree() {
		return parseTree;
	}

	public ATerm getConsTree() {
		if (valid && consTree == null) {
			// build tree
			constructTree();
		}
		return consTree;
	}
	
}