package sdf;

import java.util.ArrayList;
import java.util.List;

import sdf.model.Production;
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
 * <p>The following ATerm annotations are added:
 * <ul>
 * <li>{@code production} (String): a normalized string representation of the SDF production that created the node</li>
 * <li>{@code productionIndex} (Int): an index into the production list, can be used to retrieve the SDF production object</li>
 * </ul>
 * 
 * @author Pablo Hoch
 *
 */
public class ATermConstructor {
	
	IAbstractNode parseTree;
	ATermFactory factory;
	ATerm consPattern;
	ATerm annNamespace, annLex, annCF, annLHS, annRHS, annLabel, annProduction, annProductionIndex;
	GeneratedGrammar grammar;
	ProductionIndex productionIndex;

	public ATermConstructor(GeneratedGrammar grammar, IAbstractNode parseTree) {
		this.grammar = grammar;
		this.parseTree = parseTree;
		this.factory = SingletonFactory.getInstance();
		this.productionIndex = new ProductionIndex();
		
		this.consPattern = factory.parse("cons(<str>)");
		this.annNamespace = factory.parse("namespace");
		this.annLex = factory.parse("lex");
		this.annCF = factory.parse("cf");
		this.annLHS = factory.parse("LHS");
		this.annRHS = factory.parse("RHS");
		this.annLabel = factory.parse("label");
		this.annProduction = factory.parse("production");
		this.annProductionIndex = factory.parse("productionIndex");
	}
	
	public ATerm constructTree() {
		return constructTree(parseTree);
	}
	
	public ProductionIndex getProductionIndex() {
		return productionIndex;
	}

	@Deprecated
	public List<Production> getProductionList() {
		return productionIndex.getList();
	}
	
	protected ATerm constructTree(IAbstractNode node) {
		Rule rule = node.getItem().getRule();
		List<IAbstractNode> children = node.getChildren();
		boolean terminal = node instanceof Terminal;
		String consName = getConstructorForRule(rule);
		
		// remove layout
		if (rule.getLhs().getName().equals("<LAYOUT?-CF>")) {
			return null;
		}
		
		// get mapping to sdf rule
		// note that this is null for all automatically generated rules
		ProductionMapping prodMapping = grammar.getProductionMapping(rule);
		
		if (terminal) {
			
			ATerm result;
			
			// terminals are only added to the AST for lexical rules
			if (isLexRule(rule)) {
				String matched = ((Terminal)node).getTerm();
				ATerm terminalTerm = makeString(matched);
				terminalTerm = addNamespaceAnnotation(terminalTerm, rule);
				
				if (consName != null && rule.getRhs().size() == 1) {
					// terminal has cons attribute
					AFun fun = factory.makeAFun(consName, 1, false);
					ATermAppl appl = factory.makeAppl(fun, terminalTerm);
					result = addNamespaceAnnotation(appl, rule);
				} else {
					result = terminalTerm;
				}
			} else {
				// TODO: check condition
				if (consName != null && rule.getRhs().size() <= 1) {
					AFun fun = factory.makeAFun(consName, 0, false);
					result = factory.makeAppl(fun);
				} else {
					result = null;
				}
			}
			
			return addProductionAnnotation(result, prodMapping);
			
		} else { // non-terminal
			
			// visit children
			ArrayList<ATerm> childTerms = new ArrayList<ATerm>();
			if (children != null) {
				
				// process children
				for (int childIndex = 0; childIndex < children.size(); childIndex++) {
					IAbstractNode child = children.get(childIndex);
					String label = null;
					// get symbol label (if specified)
					if (prodMapping != null) {
						label = prodMapping.getLabelForCategoryAtPosition(childIndex);
					}
					ATerm childTerm = constructTree(child);
					if (childTerm != null) {
						// add label annotation
						if (label != null) {
							childTerm = childTerm.setAnnotation(annLabel, makeString(label));
						}
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
					ATerm appl = factory.makeAppl(fun);
					appl = appl.setAnnotation(annNamespace, annLex);
					return addProductionAnnotation(appl, prodMapping);
					
				} else {
					
					// CF rule
					return createNode(consName, childTerms, rule);
					
				}
			}
			
			return createNode(consName, childTerms, rule);
			
		}
				
	}
	
	protected ATerm createNode(String consName, List<ATerm> childTerms, Rule rule) {
		if (consName != null) {
			// rule has cons attribute -> create appl node
			return createApplNode(consName, childTerms, rule);
		} else {
			// rule has NO cons attribute
			return skipNodeIfPossible(childTerms, rule);
		}
	}

	protected ATerm createApplNode(String consName, List<ATerm> childTerms, Rule rule) {
		AFun fun = factory.makeAFun(consName, childTerms.size(), false);
		ATermAppl appl = factory.makeAppl(fun, childTerms.toArray(new ATerm[childTerms.size()]));
		ATerm result = addRuleAnnotation(appl, rule);
		ProductionMapping prodMapping = grammar.getProductionMapping(rule);
		return addProductionAnnotation(result, prodMapping);
	}
	
	protected ATerm skipNodeIfPossible(List<ATerm> childTerms, Rule rule) {
		if (childTerms.isEmpty()) {
			// no children? -> remove node
			return null;
		} else if (childTerms.size() == 1) {
			// exactly 1 child -> remove intermediate node
			return childTerms.get(0);
		} else {
			// more than 1 child -> create unnamed list
			ATermList list = flattenList(childTerms, getLhsAnnotation(rule), getRhsAnnotation(rule));
			ProductionMapping prodMapping = grammar.getProductionMapping(rule);
			return addProductionAnnotation(list, prodMapping);
		}
	}

	protected ATermList buildList(List<ATerm> terms) {
		ATermList list = factory.makeList();
		for (int i = terms.size() - 1; i >= 0; i--) {
			list = factory.makeList(terms.get(i), list);
		}
		return list;
	}
	
	protected String getConstructorForRule(Rule rule) {
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
	
	protected ATerm addNamespaceAnnotation(ATerm term, Rule rule) {
		if (isLexRule(rule)) {
			return term.setAnnotation(annNamespace, annLex);
		} else if (isCFRule(rule)) {
			return term.setAnnotation(annNamespace, annCF);
		} else {
			return term;
		}
	}
	
	protected ATerm addProductionAnnotation(ATerm term, ProductionMapping mapping) {
		if (mapping == null || term == null)
			return term;
		
		Production production = mapping.getProduction();
		String productionString = production.toString();
		term = term.setAnnotation(annProduction, makeString(productionString));
		term = term.setAnnotation(annProductionIndex, factory.makeInt(productionIndex.getIndex(production)));
		return term;
	}

	protected ATermAppl makeString(String productionString) {
		return factory.makeAppl(factory.makeAFun(productionString, 0, true));
	}
	
	protected ATerm addRuleAnnotation(ATerm term, Rule rule) {
		return addRuleAnnotation(term, getLhsAnnotation(rule), getRhsAnnotation(rule));
	}
	
	protected ATerm addRuleAnnotation(ATerm term, ATerm lhsAnnotation, ATerm rhsAnnotation) {
		
		term = term.setAnnotation(annLHS, lhsAnnotation);
		term = term.setAnnotation(annRHS, rhsAnnotation);
		
		return term;
	}

	protected ATermAppl getRhsAnnotation(Rule rule) {
		return makeString(rule.getRhs().toString());
	}

	protected ATermAppl getLhsAnnotation(Rule rule) {
		return makeString(rule.getLhs().toString());
	}
	
	protected ATermList flattenList(List<ATerm> terms, ATerm lhsAnnotation, ATerm rhsAnnotation) {
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
	
	protected boolean isLexRule(Rule rule) {
		ICategory<String> lhs = rule.getLhs();
		return lhs.getName().endsWith("-LEX>");
	}
	
	protected boolean isCFRule(Rule rule) {
		ICategory<String> lhs = rule.getLhs();
		return lhs.getName().endsWith("-CF>");
	}
}
