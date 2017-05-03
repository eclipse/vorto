package org.eclipse.vorto.repository.api;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ModelQueryBuilderTest {
	
	ModelQueryBuilder modelquerybuilder;

	@Before
	public void setUp() throws Exception {
		modelquerybuilder = new ModelQueryBuilder();
	}

	@Test
	public void testName() {
		
		modelquerybuilder = modelquerybuilder.name("NewInfoModel")
				.namespace("com.mycompany")
				.type(ModelType.InformationModel);
		String query = modelquerybuilder.build().getExpression();
		assertEquals("name:NewInfoModel namespace:com.mycompany InformationModel ", query);
		assertNotEquals(" namespace:com.mycompany InformationModel ", query);

	}

}
