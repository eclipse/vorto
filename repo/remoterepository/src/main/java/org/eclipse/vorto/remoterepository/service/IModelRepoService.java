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

import org.eclipse.vorto.remoterepository.model.ModelContent;
import org.eclipse.vorto.remoterepository.model.ModelId;
import org.eclipse.vorto.remoterepository.model.ModelView;
import org.eclipse.vorto.remoterepository.service.search.IModelQuery;

/**
 * Repo class that provide query access to model, and also retrieves model content
 *
 */
public interface IModelRepoService {
	
	/**
	 * Creates a new {@link IModelQuery} that can be used to query our models
	 * 
	 * @return
	 */
	IModelQuery newQuery();
	
	/**
	 * Creates an {@link IModelQuery} based on expression.
	 * 
	 * TODO : Write expression syntax here 
	 * 
	 * @param expression
	 * @return
	 */
	IModelQuery newQueryFromExpression(String expression);
	
	/**
	 * Gets the {@link ModelContent} of a model based on {@code modelId}
	 * 
	 * @param modelId 
	 * @return
	 */
	ModelContent getModelContent(ModelId modelId);
	
	/**
	 * Saves a {@code modelContent} to our repository 
	 * 
	 * @param modelContent
	 * @return
	 */
	ModelView saveModel(ModelContent modelContent);	
}
