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
package org.eclipse.vorto.remoterepository.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.RAMDirectory;
import org.eclipse.vorto.remoterepository.internal.lucene.LuceneModelQuery;
import org.junit.Before;
import org.junit.Test;

public class LuceneModelQueryTest extends ModelQueryTestBase {
	
	LuceneModelQuery query = null;
	
	@Before
	public void setUp() throws IOException {
		RAMDirectory indexDirectory = new RAMDirectory();
		indexSampleDocuments(indexDirectory);
		
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(indexDirectory));
		query = new LuceneModelQuery(indexSearcher);
	}
	
	/*
	 * To test that a query with no filter returns all element
	 */
	@Test
	public void testAll() {
		assertTrue(query.list().size() ==  8);
	}
	
	/*
	 * To test that a using a previous query will not return any 
	 * unexpected results
	 */
	@Test
	public void testNewFilterDoesNotChangeOldFilters() {
		assertTrue(query.list().size() ==  8);
		assertTrue(query.name("Freezer").list().size() ==  1);
		assertTrue(query.list().size() ==  8);
	}
	
	@Test
	public void testSearchByNamespaceLike() {
		assertEquals("Search Results count::" , 3, (query.namespaceLike("com.bosch").list().size()));
	}
	
	@Test
	public void testSearchByNameEquals() {
		assertEquals("Search Results count::" , 1, (query.name("Freezer").list().size()));
	}
	
	@Test
	public void testSearchByNameLike() {
		assertEquals("Search Results count::" , 4, (query.nameLike("er").list().size()));
	}
	
	@Test
	public void testSearchByNameLikeAndNamespaceEquals() {
		assertEquals("Search Results count::" , 2, (query.nameLike("er").namespace("com.bosch.fridge").list().size()));
	}
	
	@Test
	public void testSearchByNameAndNamespaceEquals() {
		assertEquals("Search Results count::" , 1, (query.name("Robot").namespace("com.robot.lg").list().size()));
	}
	
	@Test
	public void testSearchByNameLikeAndNamespaceLike() {
		assertEquals("Search Results count::" , 1, (query.nameLike("bot").namespaceLike("robot.lg").list().size()));
	}
	
	@Test
	public void testNot() {
		assertEquals("Search Results count::" , 6, query.not(query.name("Robot")).list().size());
	}
}
