package de.tud.stg.tigerseye.examples.mapdsl;

import org.junit.Ignore;
import org.junit.Test;

import utilities.DSLTransformationTestBase;
import utilities.TodoTest;

public class MapDSLTest extends DSLTransformationTestBase {

	@Test
	public void testAMapDSLTransformationShorter() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLShorter",
				MapDSL.class);
	}

//	@Ignore("fails because priorization of Number before String was removed")
	@TodoTest
	@Test
	public void testAMapDSLTransformation() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSL", MapDSL.class);
	}

//	@Ignore("fails because priorization of Number before String was removed")
	@TodoTest
	@Test
	public void testmultipleMapstatementstransformations() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLlonger",
				MapDSL.class);
	}

	@Ignore("fails because the semantic information is missing for variables; needs types inference to solve this problem.")
	@Test
	public void testMapDSLWithVariablesAsKeys() throws Exception {
		/*
		 * Fails because variables instead of the concrete type are passed for
		 * values
		 */
		assertTransformedDSLEqualsExpected(
				"MapDSLWithVariablesAsKeys", MapDSL.class);
	}

	@Ignore("Test needs different transforamtion procedure")
	@Test
	public void testMapDSLFileFormatTransformation() throws Exception {
		assertTransformedDSLEqualsExpected("MapDSLinDSLFileFormat",
				MapDSL.class);
	}

}
