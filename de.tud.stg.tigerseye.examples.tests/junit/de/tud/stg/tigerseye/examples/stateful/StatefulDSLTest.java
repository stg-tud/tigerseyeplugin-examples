package de.tud.stg.tigerseye.examples.stateful;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.ComparisonFailure;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import utilities.TodoTest;

import de.tud.stg.tigerseye.examples.statefuldsl.StatefulDSL;
import de.tud.stg.tigerseye.test.TransformationUtils;

public class StatefulDSLTest {

	@Before
	public void beforeEachTest() throws Exception {

	}

	private void assertExpected(String input, String expectedTransformation) throws Exception {
		TransformationUtils.assertExpectedForInputTransformation(input, expectedTransformation, StatefulDSL.class);
	}

	@Test
	public void shouldTransformSetStatement() throws Exception {
		String input = "set \"OS\" = \"unix\"\nset \"isValid\" = true;";
		String expected = "set__p0__equals__p1(\n\"OS\",\n\"unix\")\n" + "set__p0__equals__p1(\n" + "\"isValid\",\n"
				+ "true);";
		assertExpected(input, expected);
	}

	@Test
	public void shouldTransformSingleSetStatement() throws Exception {
		assertExpected("set \"OS\" = \"unix\";", "set__p0__equals__p1(\"OS\",\"unix\");");
	}

	@Test
	public void shouldTransformSingleGetStatement() throws Exception {
		assertExpected("get \"isValid\";", "get__p0(\"isValid\");");
	}

	private String getResource(String name) throws IOException {
		InputStream stream = StatefulDSLTest.class.getResourceAsStream("resources/" + name);
		return IOUtils.toString(stream);
	}

	/*
	 * Has Problems with Strings if Objects are valid values
	 */
	@TodoTest
	@Test//(expected = ComparisonFailure.class)
	public void shouldTransformLongerExample() throws Exception {
		// needed to add semicolon s.t. last statement is also transformed
		// correctly
		assertExpected(getResource("statefullongerexample.input"), getResource("statefullongerexample.expected"));
	}

}
