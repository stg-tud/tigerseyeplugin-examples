package sdf.test.treeviewer;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jjtraveler.VisitFailure;

import org.apache.bsf.util.IOUtils;

import aterm.ATerm;

import sdf.SdfDSL;
import treeviewer.tree.aterms.ATermTreeBuilder;
import treeviewer.tree.aterms.ATermTreeViewer;
import treeviewer.tree.parlex.ParlexTreeViewer;
import treeviewer.tree.parlex.ParseTreeBuilder;
import treeviewer.ui.MultiTreeViewer;
import de.tud.stg.parlex.ast.IAbstractNode;
import de.tud.stg.parlex.core.IGrammar;
import de.tud.stg.parlex.core.Rule;
import de.tud.stg.parlex.lexer.ILexer;
import de.tud.stg.parlex.lexer.KeywordSensitiveLexer;
import de.tud.stg.parlex.lexer.KeywordSeperator;
import de.tud.stg.parlex.parser.earley.Chart;
import de.tud.stg.parlex.parser.earley.EarleyParser;
import de.tud.stg.tigerseye.dslsupport.DSL;
import de.tud.stg.tigerseye.eclipse.core.builder.transformers.Context;
import de.tud.stg.tigerseye.eclipse.core.builder.transformers.ast.KeywordChainingTransformation;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.GrammarBuilder;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.UnicodeLookupTable;
import de.tud.stg.tigerseye.eclipse.core.codegeneration.aterm.ATermBuilder;
import de.tud.stg.tigerseye.test.PrettyGroovyCodePrinterFactory;
import de.tud.stg.tigerseye.test.TestDSLTransformation;
import de.tud.stg.tigerseye.test.TransformationUtils;

public class ConsTest2TreeViewer {

	private static final UnicodeLookupTable ult = TransformationUtils.getDefaultLookupTable();
	
	public static void main(String[] args) {

		GrammarBuilder gb = new GrammarBuilder(ult);

		List<Class<? extends DSL>> classList = new ArrayList<Class<? extends DSL>>();
		classList.add(SdfDSL.class);
		IGrammar<String> grammar = gb.buildGrammar(classList);
		
//		System.out.println(grammar.toString());
//		System.out.println();

		try {
			InputStream inputStream = new FileInputStream(
					"src/sdf/test/resources/ConsTest2.input");
			InputStreamReader reader = new InputStreamReader(inputStream);
			String testInput = IOUtils.getStringFromReader(reader);
			inputStream.close();

			ILexer lexer = new KeywordSensitiveLexer(new KeywordSeperator());
			EarleyParser earleyParser = new EarleyParser(lexer, grammar);
			earleyParser.detectUsedOracles(); ///////
			Chart chart = (Chart)earleyParser.parse(testInput.trim());
			chart.rparse((Rule)grammar.getStartRule());
			
			IAbstractNode ast = chart.getAST();
			
			MultiTreeViewer tv = new MultiTreeViewer();
			MultiTreeViewer.initializeGui();
			
			tv.addTree("Parse Tree", new ParseTreeBuilder(ast));
			
			ATermBuilder aTermBuilder = new ATermBuilder(ast);
			ATerm originalTerm = aTermBuilder.getATerm();

			tv.addTree("ATerms (original)", new ATermTreeBuilder(originalTerm));
			
			try {
				Context context = new Context("dummy");
				context.setDSLMethodDescriptions(gb.getMethodOptions());
				ATerm transformedTerm = new KeywordChainingTransformation().transform(context, originalTerm);
//				if (classList.size() > 1) {
//					transformedTerm = new InvokationDispatcherTransformation().transform(gb.getMethodOptions(), transformedTerm);
//				}
				
				tv.addTree("ATerms (transformed)", new ATermTreeBuilder(transformedTerm));
			} catch (Exception e) {
				System.err.println("Transformation failed:");
				e.printStackTrace();
			}
			
			tv.show(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String transform(String input, Class<? extends DSL>... classes)
			throws VisitFailure {
		TestDSLTransformation trans = new TestDSLTransformation(ult,
				new PrettyGroovyCodePrinterFactory());
		List<Class<? extends DSL>> classList = Arrays.asList(classes);

		return trans.performTransformation(input, classList);
	}
	
}
