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

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.eclipse.vorto.remoterepository.internal.builder.DefaultModelQueryBuilder;
import org.eclipse.vorto.remoterepository.internal.lucene.LuceneModelQuery;
import org.eclipse.vorto.remoterepository.service.ModelQueryTestBase;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;
import org.junit.Before;
import org.junit.Test;

public class LuceneModelQueryBuilderTest extends ModelQueryTestBase {

	LuceneModelQuery query = null;
	IModelQueryBuilder builder;
	
	@Before
	public void setUp() throws IOException {
		RAMDirectory indexDirectory = new RAMDirectory();
		indexSampleModelViews(indexDirectory);
		
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		query = new LuceneModelQuery(indexSearcher);
		
		builder = new DefaultModelQueryBuilder();
	}

	@Test
	public void testEmpty() {
		IModelQuery a = builder.buildFromExpression(query, "");
		assertEquals(9, a.list().size());
		
	}
	
	@Test
	public void testName() {
		IModelQuery a = builder.buildFromExpression(query, "name(\"light\")");
		assertEquals(3, a.list().size());
	}
	
	@Test
	public void testNameLike() {
		IModelQuery a = builder.buildFromExpression(query, "nameLike(\"lig\")");
		assertEquals(3, a.list().size());
		IModelQuery b = builder.buildFromExpression(query, "nameLike(\"sp\")");
		assertEquals(2, b.list().size());
	}
	
	@Test
	public void testNamespace() {		
		IModelQuery a = builder.buildFromExpression(query, "namespace(\"com.erle\")");
		assertEquals(4, a.list().size());
	}
	
	@Test
	public void testNamespaceLike() {		
		IModelQuery a = builder.buildFromExpression(query, "namespaceLike(\"erle\")");
		assertEquals(5, a.list().size());
	}
	
	@Test
	public void testAnd() {
		IModelQuery a = builder.buildFromExpression(query, "and(namespace(\"com.erle\"),name(\"light\"))");
		assertEquals(3, a.list().size());
		IModelQuery b = builder.buildFromExpression(query, "and(namespace(\"org.erle\"),name(\"light\"))");
		assertEquals(0, b.list().size());
		IModelQuery c = builder.buildFromExpression(query, "and(namespace(\"org.erle\"),name(\"rf\"))");
		assertEquals(1, c.list().size());
	}
	
	@Test
	public void testOr() {
		IModelQuery a = builder.buildFromExpression(query, "or(namespace(\"com.erle\"),namespace(\"org.erle\"))");
		assertEquals(5, a.list().size());
	}
	
	@Test
	public void testNot() {
		IModelQuery a = builder.buildFromExpression(query, "not(namespace(\"com.erle\"))");
		assertEquals(5, a.list().size());
	}
	
	@Test
	public void testCombinationAndOr() {
		IModelQuery a = builder.buildFromExpression(query, "and(namespaceLike(\"erle\"),or(name(\"spi\"),name(\"rf\")))");
		assertEquals(2, a.list().size());
		IModelQuery b = builder.buildFromExpression(query, "or(namespaceLike(\"org.erle\"),namespaceLike(\"com.mantos\"), and(name(\"light\"),namespace(\"com.erle\")))");
		assertEquals(6, b.list().size());
	}
	
	@Test
	public void testCombinationAndOrNot() {
		IModelQuery a = builder.buildFromExpression(query, "and(namespace(\"com.erle\"),not(name(\"light\")))");
		assertEquals(1, a.list().size());
		IModelQuery b = builder.buildFromExpression(query, "or(namespace(\"com.erle\"),not(name(\"i2c\")))");
		assertEquals(8, b.list().size());
		IModelQuery c = builder.buildFromExpression(query, "or(namespace(\"com.mantos\"),not(and(name(\"light\"), namespace(\"com.erle\"))))");
		assertEquals(6, c.list().size());
	}
}
