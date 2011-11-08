package sdf;

import java.util.ArrayList;
import java.util.List;

import sdf.ruleannotations.CustomATermAnnotation;

import aterm.*;
import aterm.pure.SingletonFactory;
import de.tud.stg.parlex.ast.IAbstractNode;
import de.tud.stg.parlex.ast.Terminal;
import de.tud.stg.parlex.core.ICategory;
import de.tud.stg.parlex.core.IRuleAnnotation;
import de.tud.stg.parlex.core.Rule;

/**
 * Builds an ATerm tree out of a parse tree.
 * It uses cons annotations from the productions to create the tree.
 * Nodes without cons annotations are not included in the tree, if possible.
 * 
 * <b>This class is currently in development and unfinished!</b>
 * 
 * @author Pablo Hoch
 *
 */
public class ATermConstructor {
	
	IAbstractNode parseTree;
	ATermFactory factory;
	ATerm consPattern;
	ATerm annNamespace, annLex, annCF;

	public ATermConstructor(IAbstractNode parseTree) {
		this.parseTree = parseTree;
		this.factory = SingletonFactory.getInstance();
		
		this.consPattern = factory.parse("cons(<str>)");
		this.annNamespace = factory.parse("namespace");
		this.annLex = factory.parse("lex");
		this.annCF = factory.parse("cf");
	}
	
	public ATerm constructTree() {
		return constructTree(parseTree);
	}
	
	private ATerm constructTree(IAbstractNode node) {
		Rule rule = node.getItem().getRule();
		List<IAbstractNode> children = node.getChildren();
		boolean terminal = node instanceof Terminal;
		String consName = getConstructorForRule(rule);
		
		/*
		 * **UNFINISHED**
		 * 
		 * TODO:
		 * - for CF rules: remove all terminals
		 * - for LEX rules: combine all terminals
		 * need to remember where the terminals came from (namespace)
		 * 
		 * also:
		 * - remove layout nodes
		 * - flatten lists (only same rules?)
		 * - add annotations (rules)
		 */
		
		// remove layout
		if (rule.getLhs().getName().equals("<LAYOUT?-CF>")) {
			return null;
		}
		
		if (terminal) {
			
			// terminals are only added to the ast for lexical rules
			if (isLexRule(rule)) {
				String matched = ((Terminal)node).getTerm();
				ATerm terminalTerm = factory.makeAppl(factory.makeAFun(matched, 0, true));
				terminalTerm = addNamespaceAnnotation(terminalTerm, rule);
				
				if (consName != null && rule.getRhs().size() == 1) {
					// terminal has cons attribute
					AFun fun = factory.makeAFun(consName, 1, false);
					return factory.makeAppl(fun, terminalTerm);
				} else {
					return terminalTerm;
				}
			} else {
				// TODO: check condition
				if (consName != null && rule.getRhs().size() <= 1) {
					AFun fun = factory.makeAFun(consName, 0, false);
					return factory.makeAppl(fun);
				} else {
					return null;
				}
			}
			
		} else { // non-terminal
			
			// visit children
			ArrayList<ATerm> childTerms = new ArrayList<ATerm>();
			if (children != null) {
				
				// process children
				for (IAbstractNode child : children) {
					ATerm childTerm = constructTree(child);
					if (childTerm != null) {
						childTerms.add(childTerm);
					}
				}
				
				if (isLexRule(rule)) {
					
					// LEX rule
					
					// append children if possible
					
					StringBuilder sb = new StringBuilder();
					for (ATerm childTerm : childTerms) {
						if (childTerm instanceof ATermAppl) {
							ATermAppl appl = (ATermAppl)childTerm;
							if (appl.isQuoted() && appl.getAnnotation(annNamespace) == annLex) {
								sb.append(appl.getName());
							} else {
								// non-terminal, in this case the children are returned as is
								return createNode(consName, childTerms);
							}
						}
					}
					AFun fun = factory.makeAFun(sb.toString(), 0, true);
					ATermAppl appl = factory.makeAppl(fun);
					return appl.setAnnotation(annNamespace, annLex);
	
					
				} else {
					
					// CF rule
					return createNode(consName, childTerms);
					
				}
			}
			
			return createNode(consName, childTerms);
			
		}
				
	}
	
	private ATerm createNode(String consName, List<ATerm> childTerms) {
		if (consName != null) {
			AFun fun = factory.makeAFun(consName, childTerms.size(), false);
			return factory.makeAppl(fun, childTerms.toArray(new ATerm[childTerms.size()]));
		} else {
			if (childTerms.isEmpty()) {
				return null;
			} else if (childTerms.size() == 1) {
				return childTerms.get(0);
			} else {
				ATermList list = factory.makeList();
				for (int i = childTerms.size() - 1; i >= 0; i--) {
					list = factory.makeList(childTerms.get(i), list);
				}
				return list;
			}
		}
	}
	
	private String getConstructorForRule(Rule rule) {
		List<IRuleAnnotation> annotations = rule.getAnnotations();
		for (IRuleAnnotation ann : annotations) {
			if (ann instanceof CustomATermAnnotation) {
				CustomATermAnnotation atermAnn = (CustomATermAnnotation)ann;
				ATerm term = atermAnn.getAterm();
				List<Object> captures = term.match(consPattern);
				if (captures != null && captures.size() == 1) {
					String consName = (String)captures.get(0);
					return consName;
				}
			}
		}
		
		return null;
	}
	
	private ATerm addNamespaceAnnotation(ATerm term, Rule rule) {
		if (isLexRule(rule)) {
			return term.setAnnotation(annNamespace, annLex);
		} else if (isCFRule(rule)) {
			return term.setAnnotation(annNamespace, annCF);
		} else {
			return term;
		}
	}
	
	private boolean isLexRule(Rule rule) {
		ICategory<String> lhs = rule.getLhs();
		return lhs.getName().endsWith("-LEX>");
	}
	
	private boolean isCFRule(Rule rule) {
		ICategory<String> lhs = rule.getLhs();
		return lhs.getName().endsWith("-CF>");
	}
}
