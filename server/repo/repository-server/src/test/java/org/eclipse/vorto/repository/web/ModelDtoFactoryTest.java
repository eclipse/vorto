package org.eclipse.vorto.repository.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.EntityModel;
import org.junit.Test;

public class ModelDtoFactoryTest extends ModelTestBase {

	@Test
	public void testLoadingOfEntity() {
		try {
			Model color = getModel("Color");

			assertEquals(true, color instanceof Entity);
			assertEquals(true, ((Entity) color).getProperties().size() == 1);
			assertEquals(((Entity) color).getProperties().get(0).getName(), "color");
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testConversionOfEntity() {
		try {
			Model color = getModel("Color");
			EntityModel model = ModelDtoFactory.createResource((Entity) color);
			assertEquals(true, model != null);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testLoadingOfStandaloneFb() {
		try {
			Model switcher = getModel("Switcher");
			assertEquals(true, switcher instanceof FunctionblockModel);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testConversionOfStandaloneFb() {
		try {
			Model switcher = getModel("Switcher");
			assertEquals(true, switcher instanceof FunctionblockModel);
			assertEquals(true, ModelDtoFactory.createResource((FunctionblockModel) switcher) != null);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testLoadingOfFbWithOperation() {
		try {
			Model colorLight = getModel("ColorLight");
			assertEquals(true, colorLight instanceof FunctionblockModel);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testConversionOfFbWithOperation() {
		try {
			Model colorLight = getModel("ColorLight");
			assertEquals(true, colorLight instanceof FunctionblockModel);
			assertEquals(true, ModelDtoFactory.createResource((FunctionblockModel) colorLight) != null);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}

	@Test
	public void testConversionOfFbWithOperationReturnType() {
		try {
			Model colorLight = getModel("ColorLight2");

			assertEquals(true, colorLight instanceof FunctionblockModel);

			assertEquals(true, ModelDtoFactory.createResource((FunctionblockModel) colorLight) != null);

			org.eclipse.vorto.repository.api.content.FunctionblockModel fbm = ModelDtoFactory
					.createResource((FunctionblockModel) colorLight);

			assertEquals(fbm.getOperations().size(), 1);

			assertEquals(fbm.getOperations().get(0).getParams().size(), 1);

			assertEquals(fbm.getOperations().get(0).getParams().get(0).getName(), "color");

			assertEquals(fbm.getOperations().get(0).getParams().get(0).getType() instanceof ModelId, true);

			assertEquals(fbm.getOperations().get(0).getResult().getType() instanceof ModelId, true);
		} catch (IOException e) {
			fail("Getting color has exception");
		}
	}
}
