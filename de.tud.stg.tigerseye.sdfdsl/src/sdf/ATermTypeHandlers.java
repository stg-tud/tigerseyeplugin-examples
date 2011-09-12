package sdf;

import de.tud.stg.tigerseye.eclipse.core.codegeneration.typeHandling.TypeHandler;
import aterm.*;
import aterm.pure.*;

/**
 * ATermTypeHandlers contains Tigerseye type handlers in order to parse some basic elements
 * of ATerms. Compound ATerms (i.e. function applications and lists) are parsed using
 * DSL methods in SdfDSL.
 * 
 * @author Pablo Hoch
 *
 * @see SdfDSL
 * @see <a href="http://homepages.cwi.nl/~daybuild/daily-books/technology/aterm-guide/aterm-guide.html">ATerm documentation</a>
 *
 */
public class ATermTypeHandlers {

	public static class IntConstantTypeHandler extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return IntConstant.class;
		}

		@Override
		public String getRegularExpression() {
			return "[-+]?[0-9]+";
		}
		
		public static class IntConstant {
			int value;
			
			public IntConstant(String text) {
				this.value = Integer.parseInt(text);
			}
			
			public ATermInt getATerm() {
				return SingletonFactory.getInstance().makeInt(value);
			}
		}
		
	}
	
	public static class RealConstantTypeHandler extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return RealConstant.class;
		}

		@Override
		public String getRegularExpression() {
			// this is derived from the sdf/aterm grammar, where only the e-part is optional
			return "[-+]?[0-9]+\\.[0-9]+(e[-+]?[0-9]+)?";
		}
		
		public static class RealConstant {
			double value;
			
			public RealConstant(String text) {
				this.value = Double.parseDouble(text);
			}
			
			public ATermReal getATerm() {
				return SingletonFactory.getInstance().makeReal(value);
			}
		}
		
	}
	
	public static class FunctionNameTypeHandler extends TypeHandler {

		@Override
		public Class<?> getMainType() {
			return FunctionNameConstant.class;
		}

		@Override
		public String getRegularExpression() {
			// can be either unquoted (identifier) or quoted (string)
			return "([A-Za-z][-A-Za-z0-9]*|\"([^\"\\\\]|\\\\.)*\")";
		}
		
		public static class FunctionNameConstant {
			String name;
			boolean isQuoted;
			
			public FunctionNameConstant(String text) {
				if (text.startsWith("\"")) {
					// quoted string
					name = getStringContents(text);
					isQuoted = true;
				} else {
					// unquoted identifier
					name = text;
					isQuoted = false;
				}
			}
			
			private static String getStringContents(String input) {
				// strip quotation marks
				String result = input.substring(1, input.length() - 1);
				// replace escapes
				result = result.replace("\\\"", "\"");
				// need regexp here. if "\\n" is always replaced by "\n", it would
				// also be replaced inside "\\\\n" (which should become "\\n").
				result = result.replaceAll("(?<!\\\\)\\\\n", "\n");
				result = result.replaceAll("(?<!\\\\)\\\\t", "\t");
				result = result.replace("\\\\", "\\");
				return result;
			}
			
			public AFun getATerm() {
				// use arity = 0 here, because it is not yet known.
				// this is fixed later in the function application.
				return SingletonFactory.getInstance().makeAFun(name, 0, isQuoted);
			}
		}
		
	}
	
}
