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
package org.eclipse.vorto.core.api.repository;

import java.util.Collection;

import org.eclipse.vorto.core.model.ModelType;

/**
 * This class is used for querying the contents of the repository.
 * 
 * Note: This API is chainable.
 */
public interface IModelQuery {

	/**
	 * Returns a query object that filters based on the given modelType
	 * @param modelType
	 * @return
	 */
	IModelQuery modelType(ModelType modelType);

	/**
	 * Returns a query object that filters based on the given name
	 * @param name
	 * @return
	 */
	IModelQuery name(String name);

	/**
	 * Returns a query object that filters based on the given name
	 * @param name
	 * @return
	 */
	IModelQuery nameLike(String name);

	/**
	 * Returns a query object that filters based on the given namespace
	 * @param namespace
	 * @return
	 */
	IModelQuery namespace(String namespace);

	/**
	 * Returns a query object that filters based on the given namespace
	 * @param namespace
	 * @return
	 */
	IModelQuery namespaceLike(String namespace);

	/**
	 * Returns a query object that filters based on the given version
	 * @param version
	 * @return
	 */
	IModelQuery version(String version);

	/**
	 * Returns a query object that filters based on the given version
	 * @param version
	 * @return
	 */
	IModelQuery versionLike(String version);
	
	/**
	 * Returns a query object that's a conjunction of the given queries
	 * @param version
	 * @return
	 */
	IModelQuery and(IModelQuery ... queries);
	
	/**
	 * Returns a query object that's a disjunction of the given queries
	 * @param version
	 * @return
	 */
	IModelQuery or(IModelQuery ... queries);
	
	/**
	 * Returns a query object that's a negation of the given query
	 * @param version
	 * @return
	 */
	IModelQuery not(IModelQuery query);

	/**
	 * Returns the resources that satisfies the query
	 * @return
	 */
	Collection<ModelResource> list();
}
