/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.remoterepository.builder;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.eclipse.vorto.remoterepository.builder.mockmodelquery.PredicateModelQuery;
import org.eclipse.vorto.remoterepository.dao.IModelDAO;
import org.eclipse.vorto.remoterepository.internal.builder.DefaultModelQueryBuilder;
import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.ModelQueryTestBase;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

/**
 * 
 * @deprecated only for testing purpose
 */
public class ModelQueryBuilderTest extends ModelQueryTestBase {

	IModelQueryBuilder builder;
	IModelQuery query;
	IModelDAO modelDao = getMockDao();

	public ModelQueryBuilderTest() {
		query = new PredicateModelQuery(modelDao, ModelType.FUNCTIONBLOCK);
		builder = new DefaultModelQueryBuilder();
	}

	public void testEmpty() {
		IModelQuery a = builder.buildFromExpression(query, "");
		assertEquals(9, a.list().size());

	}

	public void testName() {
		IModelQuery a = builder.buildFromExpression(query, "name(\"light\")");
		assertEquals(3, a.list().size());
	}

	public void testNameLike() {
		IModelQuery a = builder.buildFromExpression(query, "nameLike(\"lig\")");
		assertEquals(3, a.list().size());
		IModelQuery b = builder.buildFromExpression(query, "nameLike(\"sp\")");
		assertEquals(2, b.list().size());
	}

	public void testNamespace() {
		IModelQuery a = builder.buildFromExpression(query,
				"namespace(\"com.erle\")");
		assertEquals(4, a.list().size());
	}

	public void testNamespaceLike() {
		IModelQuery a = builder.buildFromExpression(query,
				"namespaceLike(\"erle\")");
		assertEquals(5, a.list().size());
	}

	public void testAnd() {
		IModelQuery a = builder.buildFromExpression(query,
				"and(namespace(\"com.erle\"),name(\"light\"))");
		assertEquals(3, a.list().size());
		IModelQuery b = builder.buildFromExpression(query,
				"and(namespace(\"org.erle\"),name(\"light\"))");
		assertEquals(0, b.list().size());
		IModelQuery c = builder.buildFromExpression(query,
				"and(namespace(\"org.erle\"),name(\"rf\"))");
		assertEquals(1, c.list().size());
	}

	public void testOr() {
		IModelQuery a = builder.buildFromExpression(query,
				"or(namespace(\"com.erle\"),namespace(\"org.erle\"))");
		assertEquals(5, a.list().size());
	}

	public void testNot() {
		IModelQuery a = builder.buildFromExpression(query,
				"not(namespace(\"com.erle\"))");
		assertEquals(5, a.list().size());
	}

	public void testCombinationAndOr() {
		IModelQuery a = builder.buildFromExpression(query,
				"and(namespaceLike(\"erle\"),or(name(\"spi\"),name(\"rf\")))");
		assertEquals(2, a.list().size());
		IModelQuery b = builder
				.buildFromExpression(
						query,
						"or(namespaceLike(\"org.erle\"),namespaceLike(\"com.mantos\"), and(name(\"light\"),namespace(\"com.erle\")))");
		assertEquals(6, b.list().size());
	}

	public void testCombinationAndOrNot() {
		IModelQuery a = builder.buildFromExpression(query,
				"and(namespace(\"com.erle\"),not(name(\"light\")))");
		assertEquals(1, a.list().size());
		IModelQuery b = builder.buildFromExpression(query,
				"or(namespace(\"com.erle\"),not(name(\"i2c\")))");
		assertEquals(8, b.list().size());
		IModelQuery c = builder
				.buildFromExpression(
						query,
						"or(namespace(\"com.mantos\"),not(and(name(\"light\"), namespace(\"com.erle\"))))");
		assertEquals(6, c.list().size());
	}

	private IModelDAO getMockDao() {
		return new IModelDAO() {
			public ModelContent getModelById(ModelId id) {
				return null;
			}

			public Collection<ModelContent> getAllModels(ModelType modelType) {
				return null;
			}

			@Override
			public ModelView saveModel(ModelContent modelContent) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean exists(ModelId id) {
				// TODO Auto-generated method stub
				return false;
			}

		};
	}
}
