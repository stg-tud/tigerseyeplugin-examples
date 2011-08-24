package sdf.util;

import de.tud.stg.parlex.ast.*;

public class ASTPrettyPrinter {
	
	private StringBuilder sb;
	private String identStr;
	private final String newLine = System.getProperty("line.separator");

	public String prettyPrint(IAbstractNode ast, String identStr) {
		this.sb = new StringBuilder();
		this.identStr = identStr;
		
		visit(ast, 0);
		
		String s = sb.toString();
		this.sb = null;
		return s;
	}
	
	public String prettyPrint(IAbstractNode ast) {
		return prettyPrint(ast, "|  ");
	}
	
	private void visit(IAbstractNode node, int depth) {
		if (node instanceof Terminal) {
			Terminal t = (Terminal)node;
			appendLine("T:[" + t.getTerm() + " (" + t.getItem() + ")]", depth);
		} else if (node instanceof NonTerminal) {
			NonTerminal nt = (NonTerminal)node;
			if (nt.getChildren().isEmpty()) {
				appendLine("NT:[]", depth);
			} else {
				appendLine("NT:[", depth);
				for (IAbstractNode child : nt.getChildren()) {
					visit(child, depth + 1);
				}
				appendLine("]", depth);
			}
		}
	}
	
	private void appendLine(String line, int depth) {
		for (int i = 0; i < depth; i++) {
			sb.append(identStr);
		}
		sb.append(line);
		sb.append(newLine);
	}
}
