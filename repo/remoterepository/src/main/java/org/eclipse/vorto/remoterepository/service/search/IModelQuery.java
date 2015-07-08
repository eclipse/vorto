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
package org.eclipse.vorto.remoterepository.service.search;

import java.util.Collection;

import org.eclipse.vorto.remoterepository.model.ModelType;
import org.eclipse.vorto.remoterepository.model.ModelView;

/**
 * To be used for build model query, and retrieve list of ModelView based on query parameter specrifed
 */
public interface IModelQuery {
	
	/**
	 * Add query parameter with modelType equels to given modelType
	 * @param modelType
	 * @return
	 */
	IModelQuery modelType(ModelType modelType);
	
	/**
	 * Add query parameter with name equals to given modelName
	 * @param modelName
	 * @return Updated Model Query
	 */
	IModelQuery name(String modelName);

	/**
	 * Add query parameter with name that contains given modelName	 
	 * @param modelName
	 * @return
	 */
	IModelQuery nameLike(String modelName);

	/**
	 * Add query parameter with namespace that equals given value	 
	 * @param namespace
	 * @return
	 */
	IModelQuery namespace(String namespace);

	/**
	 * Add query parameter with namespace that contains given value	 
	 * @param namespace
	 * @return
	 */
	IModelQuery namespaceLike(String namespace);
	
	/**
	 * Add query parameter with version that equals given value	 
	 * @param version
	 * @return
	 */
	IModelQuery version(String version);
	
	/**
	 * Add query parameter with version that contains given value	 
	 * @param version
	 * @return
	 */
	IModelQuery versionLike(String version);

	/**
	 * A conjunction of {@code queries }
	 * @param queries
	 * @return
	 */
	IModelQuery and(IModelQuery ... queries);
	
	/**
	 * A disjunction of {@code queries}
	 * @param queries
	 * @return
	 */
	IModelQuery or(IModelQuery ... queries);
	
	/**
	 * a negation of {@code query}
	 * @param query
	 * @return
	 */
	IModelQuery not(IModelQuery query);
	
	/**
	 * @return List of ModelView that matches given query parameters
	 */
	Collection<ModelView> list();
}
