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

import java.io.InputStream;

import org.eclipse.vorto.core.model.ModelId;

/**
 * The Repository object that is used for querying, uploading and downloading resources.
 *
 */
public interface IModelRepository {
	
	/**
	 * creates a new model query builder
	 * 
	 * @return
	 */
	IModelQuery newQuery();
	
	/**
	 * Gets the actual model for the given model resource
	 * 
	 * @param resource
	 * @return
	 */
	IModelContent getModelContentForResource(ModelId modelId);
	
	/**
	 * Saves a model to the repository
	 * 
	 * @param resource
	 * @return
	 */
	void checkIn(ModelId modelId, InputStream file) throws ModelAlreadyExistException;
}
