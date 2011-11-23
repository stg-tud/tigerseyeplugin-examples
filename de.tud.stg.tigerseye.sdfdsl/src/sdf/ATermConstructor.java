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
 * It uses cons attributes from the productions to create the tree.
 * Nodes without cons attributes are not included in the tree, if possible.
 * 
 * <p>A cons attribute looks like the following: <code>cons("<i>NodeName</i>")</code>
 * 
 * <p>The following rules are used to construct the AST:
 * <ul>
 * <li>Layout nodes are removed</li>
 * <li>Terminals in context-free productions are removed</li>
 * <li>Terminals in lexical productions are concatenated if possible (only if they don't have cons attributes)</li>
 * <li>For productions with a cons attribute, function applications are created</li>
 * <li>For productions without a cons attribute, the following rules apply:
 * <ul>
 * <li>If the node does not have any children, it is removed from the AST</li>
 * <li>If the node has exactly one child, the intermediate node is removed</li>
 * <li>If the node has more than one child, a list of the children is added to the AST</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author Pablo Hoch
 *
 */
public class ATermConstructor {
	
	/*
	 * TODO:
	 * - add sdf productions as annotations
	 * - add symbol labels as annotations
	 */
	
	IAbstractNode parseTree;
	ATermFactory factory;
	ATerm consPattern;
	ATerm annNamespace, annLex, annCF, annLHS, annRHS;

	public ATermConstructor(IAbstractNode parseTree) {
		this.parseTree = parseTree;
		this.factory = SingletonFactory.getInstance();
		
		this.consPattern = factory.parse("cons(<str>)");
		this.annNamespace = factory.parse("namespace");
		this.annLex = factory.parse("lex");
		this.annCF = factory.parse("cf");
		this.annLHS = factory.parse("LHS");
		this.annRHS = factory.parse("RHS");
	}
	
	public ATerm constructTree() {
		return constructTree(parseTree);
	}
	
	private ATerm constructTree(IAbstractNode node) {
		Rule rule = node.getItem().getRule();
		List<IAbstractNode> children = node.getChildren();
		boolean terminal = node instanceof Terminal;
		String consName = getConstructorForRule(rule);
		
		// remove layout
		if (rule.getLhs().getName().equals("<LAYOUT?-CF>")) {
			return null;
		}
		
		if (terminal) {
			
			// terminals are only added to the AST for lexical rules
			if (isLexRule(rule)) {
				String matched = ((Terminal)node).getTerm();
				ATerm terminalTerm = factory.makeAppl(factory.makeAFun(matched, 0, true));
				terminalTerm = addNamespaceAnnotation(terminalTerm, rule);
				
				if (consName != null && rule.getRhs().size() == 1) {
					// terminal has cons attribute
					AFun fun = factory.makeAFun(consName, 1, false);
					ATermAppl appl = factory.makeAppl(fun, terminalTerm);
					return addNamespaceAnnotation(appl, rule);
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
								return createNode(consName, childTerms, rule);
							}
						} else {
							// if there are other children (e.g. lists), create a normal node
							return createNode(consName, childTerms, rule);
						}
					}
					AFun fun = factory.makeAFun(sb.toString(), 0, true);
					ATermAppl appl = factory.makeAppl(fun);
					return appl.setAnnotation(annNamespace, annLex);
	
					
				} else {
					
					// CF rule
					return createNode(consName, childTerms, rule);
					
				}
			}
			
			return createNode(consName, childTerms, rule);
			
		}
				
	}
	
	private ATerm createNode(String consName, List<ATerm> childTerms, Rule rule) {
		if (consName != null) {
			// rule has cons attribute -> create appl node
			AFun fun = factory.makeAFun(consName, childTerms.size(), false);
			ATermAppl appl = factory.makeAppl(fun, childTerms.toArray(new ATerm[childTerms.size()]));
			return addRuleAnnotation(appl, rule);
		} else {
			// rule has NO cons attribute
			if (childTerms.isEmpty()) {
				// no children? -> remove node
				return null;
			} else if (childTerms.size() == 1) {
				// exactly 1 child -> remove intermediate node
				return childTerms.get(0);
			} else {
				// more than 1 child -> create unnamed list
				return flattenList(childTerms, getLhsAnnotation(rule), getRhsAnnotation(rule));
			}
		}
	}

	private ATermList buildList(List<ATerm> terms) {
		ATermList list = factory.makeList();
		for (int i = terms.size() - 1; i >= 0; i--) {
			list = factory.makeList(terms.get(i), list);
		}
		return list;
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
	
	private ATerm addRuleAnnotation(ATerm term, Rule rule) {
		return addRuleAnnotation(term, getLhsAnnotation(rule), getRhsAnnotation(rule));
	}
	
	private ATerm addRuleAnnotation(ATerm term, ATerm lhsAnnotation, ATerm rhsAnnotation) {
		
		term = term.setAnnotation(annLHS, lhsAnnotation);
		term = term.setAnnotation(annRHS, rhsAnnotation);
		
		return term;
	}

	private ATermAppl getRhsAnnotation(Rule rule) {
		return factory.makeAppl(factory.makeAFun(rule.getRhs().toString(), 0, true));
	}

	private ATermAppl getLhsAnnotation(Rule rule) {
		return factory.makeAppl(factory.makeAFun(rule.getLhs().toString(), 0, true));
	}
	
	private ATermList flattenList(List<ATerm> terms, ATerm lhsAnnotation, ATerm rhsAnnotation) {
		ArrayList<ATerm> newTerms = new ArrayList<ATerm>(terms.size() * 2);
		
		for (ATerm elm : terms) {
			if (elm instanceof ATermList) {
				ATermList innerList = (ATermList)elm;
				if (lhsAnnotation.equals(elm.getAnnotation(annLHS)) &&
						rhsAnnotation.equals(elm.getAnnotation(annRHS))) {
					// same rule -> flatten list
					for (int j = 0; j < innerList.getLength(); j++) {
						newTerms.add(innerList.elementAt(j));
					}
					continue;
				}
			}
			newTerms.add(elm);
		}

		
		ATermList newList = buildList(newTerms);
		newList = (ATermList)addRuleAnnotation(newList, lhsAnnotation, rhsAnnotation);

		return newList;
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
