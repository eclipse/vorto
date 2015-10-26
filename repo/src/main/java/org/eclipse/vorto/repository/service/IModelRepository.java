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
package org.eclipse.vorto.repository.service;

import java.util.List;

import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.model.UploadModelResult;

/**
 * 
 * @author Alexander Edelmann
 *
 */
public interface IModelRepository {

	/**
	 * Searches model resources for the given expression
	 * @param queryExpression
	 * @return
	 */
	List<ModelResource> search(String queryExpression);
	
	/**
	 * Gets a model resource for the given model id
	 * @param modelId
	 * @return
	 */
	ModelResource getById(ModelId modelId);
	
	/**
	 * 
	 * @param modelId
	 */
	void removeModel(ModelId modelId);
	
	/**
	 * Returns the actual model content for the given model id
	 * @param modelId
	 * @param content type
	 * @throws ModelNotFoundException
	 * @return
	 */
	byte[] getModelContent(ModelId modelId, ContentType contentType);
	
	/**
	 * Uploads model content and validates it. If the model is valid, the returned upload handle is used
	 * to checkin the model into the repository via {@link IModelRepository#checkin(String)}}
	 * 
	 * @param content to validate
	 * @param fileName
	 * @return result about information of the uploaded content and the upload handle. 
	 */
	UploadModelResult upload(byte[] content, String fileName);
	
	/**
	 * @pre {@link UploadModelResult#isValid() == true}}
	 * 
	 * Checks in a new model into the repository
	 * @param uploadHandle
	 */
	void checkin(String handleId);
	
	public enum ContentType {
		XMI,DSL
	}
}
