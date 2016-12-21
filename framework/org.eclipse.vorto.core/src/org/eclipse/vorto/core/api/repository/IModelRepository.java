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
package org.eclipse.vorto.core.api.repository;

import java.util.List;

import org.eclipse.vorto.core.api.model.model.ModelId;

/**
 * The Repository object that is used for querying, uploading and downloading resources.
 *
 */
public interface IModelRepository {
	
	/**
	 * Searches model resources with the given full text search expression
	 * 
	 * @return
	 */
	List<ModelResource> search(String expression);
	
	/**
	 * Gets the model resource and its references for the given model id. 
	 * It does not download the actual content. Please use {@link IModelRepository#downloadContent(ModelId)}
	 * 
	 * @param resource
	 * @return
	 */
	ModelResource getModel(ModelId modelId);
	
	/**
	 * Downloads the actual content for the given model id
	 * @param modelId
	 * @return model content in DSL representation 
	 */
	byte[] downloadContent(ModelId modelId);
	
	/**
	 * Uploads the file to the repository. It needs to be committed first though for the upload to be complete.
	 * 
	 * @param name
	 * @param model
	 * @return the handle of the uploaded model
	 * @throws CheckInModelException
	 */
	UploadResult upload(String name, byte[] model);
	
	/**
	 * Commits an uploaded file finalizing the upload.
	 * 
	 * @param String the handle of the uploaded model
	 */
	void commit(String handleId);
	
	/**
	 * list all generator resources
	 * 
	 * @return
	 */
	List<GeneratorResource> listGenerators();
	
	/**
	 * Generate code for model with specified code generator
	 * 
	 * @param String the model name
	 * @param String the code generator name
	 * @return generated model file
	 */
	Attachment generateCode(ModelId model, String serviceKey);
	
}
