/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.repository.core;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;

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
	List<ModelInfo> search(String queryExpression);
	
	/**
	 * Gets a model resource for the given model id
	 * @param modelId
	 * @return
	 */
	ModelInfo getById(ModelId modelId);
	
	/**
	 * Returns the actual model content for the given model id
	 * @param modelId
	 * @param content type
	 * @throws ModelNotFoundException
	 * @return
	 */
	IModelContent getModelContent(ModelId modelId, ContentType contentType);
	
	/**
	 * Uploads model content and validates it. If the model is valid, the returned upload handle is used
	 * to checkin the model into the repository via {@link IModelRepository#checkin(String)}}
	 * 
	 * @param content to validate
	 * @param fileName
	 * @param callerId
	 * @return result about information of the uploaded content and the upload handle. 
	 */
	UploadModelResult upload(byte[] content, String fileName, IUserContext userContext);
	
	/**
	 * @pre {@link UploadModelResult#isValid() == true}}
	 * 
	 * @post model was stored in persistence layer. Notifications were sent out to watchers. 
	 * 
	 * Checks in a new model into the repository
	 * @param uploadHandle
	 * @param callerId
	 * @return model that was been checked in
	 */
	ModelInfo checkin(String handleId, IUserContext userContext);
		
	/**
	 * Adds a model image for the given model id
	 * @param modelId
	 * @param image
	 */
	void addModelImage(ModelId modelId, byte[] image);
	
	/**
	 * Removes a model image for the given model id
	 * @param modelId
	 */
	void removeModelImage(ModelId modelId);
	
	/**
	 * 
	 * @param modelId
	 * @return
	 */
	byte[] getModelImage(ModelId modelId);
	
	/**
	 * Gets the mapping model for the given modelId and the given target platform
	 * @param modelId
	 * @param targetPlatform
	 * @return
	 */
	List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform);
	
	/**
	 * Removes the model for the given ModelID
	 * @param modelId
	 */
    void removeModel(ModelId modelId);
    
    /**
     * Updates the model meta information 
     * @param model
     * @return
     */
	ModelInfo updateMeta(ModelInfo model);

		
	public enum ContentType {
		XMI,DSL
	}

}
