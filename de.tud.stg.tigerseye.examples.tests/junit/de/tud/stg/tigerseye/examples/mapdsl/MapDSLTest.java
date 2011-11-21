package de.tud.stg.tigerseye.examples.mapdsl;

import org.junit.Rule;
import org.junit.Test;

import utilities.DSLTransformationTestBase;
import utilities.SystemPropertyRule;
import utilities.TodoTest;

public class MapDSLTest extends DSLTransformationTestBase {

	@Rule
	public SystemPropertyRule spr = new SystemPropertyRule();

	// once grammar and transformation are correct this should work
	@TodoTest
	@Test
	public void testAMapDSLTransformationShorter() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLShorter", MapDSL.class);
	}

	// once grammar and transformation are correct this should work
	@TodoTest
	@Test
	public void testAMapDSLTransformation() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSL", MapDSL.class);
	}

	// once grammar and transformation are correct this should work
	@TodoTest
	@Test
	public void testmultipleMapstatementstransformations() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLlonger", MapDSL.class);
	}

	@TodoTest
	@Test
	public void testMapDSLWithVariablesAsKeys() throws Exception {
		/*
		 * Fails because variables instead of the concrete type are passed for
		 * values
		 */
		assertTransformedDSLEqualsExpected("MapDSLWithVariablesAsKeys", MapDSL.class);
	}

	// once grammar and transformation are correct this should work
	@Test(expected = org.junit.ComparisonFailure.class)
	public void testMapDSLFileFormatTransformation() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLinDSLFileFormat", MapDSL.class);
	}

}
