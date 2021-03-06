package sdf.test;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import sdf.ATermTypeHandlers;
import sdf.SdfDSL;
import sdf.model.CaseInsensitiveLiteralSymbol;

import de.tud.stg.tigerseye.eclipse.core.codegeneration.typeHandling.TypeHandler;

/**
 * Test cases for the SdfDSL type handlers that are used by Tigerseye
 * 
 * @author Pablo Hoch
 *
 */
public class TypeHandlerTest {

	@Test
	public void testModuleIdType() {
		Pattern pattern = getPattern(new SdfDSL.ModuleIdType());
		
		assertMatch(pattern, "foo");
		assertMatch(pattern, "Foo");
		assertMatch(pattern, "foo42");
		assertMatch(pattern, "foo/bar");
		assertMatch(pattern, "lang/sdf/basic");
		assertMatch(pattern, "foo_1");
	}
	
	@Test
	public void testCharacterClassSymbolType() {
		Pattern pattern = getPattern(new SdfDSL.CharacterClassSymbolType());
		
		assertMatch(pattern, "[a]");
		assertMatch(pattern, "[a-z]");
		assertMatch(pattern, "[0-9]");
		assertMatch(pattern, "[0-9a-z]");
		assertMatch(pattern, "[-0-9a-z_]");
		assertMatch(pattern, "[\\n]");
//		assertMatch(pattern, "[\\]]"); // not actually required by sdf documentation
	}
	
	@Test
	public void testCaseInsensitiveLiteralSymbolType() {
		Pattern pattern = getPattern(new SdfDSL.CaseInsensitiveLiteralSymbolType());
		
		assertMatch(pattern, "''");
		assertMatch(pattern, "'a'");
		assertMatch(pattern, "'test'");
		assertMatch(pattern, "'hello world'");
		assertMatch(pattern, "'this is a \\'test\\''");
		assertMatch(pattern, "'this is a \\'test\\' too'");
		assertMatch(pattern, "'c:\\\\test.txt'");
		
		assertNoMatch(pattern, "'unclosed string\\'");
		assertNoMatch(pattern, "'unescaped 'quote in string'");
	}
	
	/**
	 * tests that case-insensitive literals result in the correct model
	 */
	@Test
	public void testCaseInsensitiveLiteralConversion() {
		assertEquals("", new CaseInsensitiveLiteralSymbol("''").getText());
		assertEquals("foo", new CaseInsensitiveLiteralSymbol("'foo'").getText());
		assertEquals("this is a 'test'", new CaseInsensitiveLiteralSymbol("'this is a \\'test\\''").getText());
		assertEquals("this is a 'test' too", new CaseInsensitiveLiteralSymbol("'this is a \\'test\\' too'").getText());
		assertEquals("c:\\test.txt", new CaseInsensitiveLiteralSymbol("'c:\\\\test.txt'").getText());
	}
	
	@Test
	public void testATermIntConstantType() {
		Pattern pattern = getPattern(new ATermTypeHandlers.IntConstantTypeHandler());
		
		assertMatch(pattern, "0");
		assertMatch(pattern, "123");
		assertMatch(pattern, "+42");
		assertMatch(pattern, "-42");
	}
	
	@Test
	public void testATermRealConstantType() {
		Pattern pattern = getPattern(new ATermTypeHandlers.RealConstantTypeHandler());
		
		assertMatch(pattern, "3.14");
		assertMatch(pattern, "+3.14");
		assertMatch(pattern, "-3.14");
		assertMatch(pattern, "1.23e5");
		assertMatch(pattern, "1.23e-5");
	}
	
	@Test
	public void testATermFunctionNameType() {
		Pattern pattern = getPattern(new ATermTypeHandlers.FunctionNameTypeHandler());
		
		assertMatch(pattern, "foo");
		assertMatch(pattern, "fooBar42");
		assertMatch(pattern, "\"hello world\"");
		assertMatch(pattern, "\"c:\\\\test.txt\"");
		assertMatch(pattern, "\"simple \\\"test\\\" string\"");
		assertMatch(pattern, "\"\"");
	}
	
	@Test
	public void testATermFunctionNameConversion() {
		assertEquals(
				"foo",
				new ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant(
						"foo").getATerm().getName());
		assertEquals(
				"foo",
				new ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant(
						"\"foo\"").getATerm().getName());
		assertEquals(
				"c:\\test.txt",
				new ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant(
						"\"c:\\\\test.txt\"").getATerm().getName());
		assertEquals(
				"simple \"test\" string",
				new ATermTypeHandlers.FunctionNameTypeHandler.FunctionNameConstant(
						"\"simple \\\"test\\\" string\"").getATerm().getName());
	}
	
	
	// helper methods

	private Pattern getPattern(TypeHandler typeHandler) {
		// for this test, the whole string must match
		String regex = "\\A" + typeHandler.getRegularExpression() + "\\z";
		return Pattern.compile(regex);
	}
	
	private void assertMatch(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (!matcher.matches()) {
			fail("pattern <" + pattern + "> does not match <" + input + ">");
		}
	}
	
	private void assertNoMatch(Pattern pattern, String input) {
		Matcher matcher = pattern.matcher(input);
		if (matcher.matches()) {
			fail("pattern <" + pattern + "> matches <" + input + "> (" + matcher.group() + ")");
		}
	}
	
}
