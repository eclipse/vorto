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
import java.util.Optional;
import java.util.Set;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;

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
	 * @throws ModelNotFoundException
	 * @return
	 */
	ModelFileContent getModelContent(ModelId modelId);
	
	/**
	 * Saves the model to the repo. If it does not exist, the model is created.
	 * 
	 * @param modelId the id of the model
	 * @param content the content
	 * @param fileName the filename of the model
	 * @param user user who has modified the model
	 * @return model info containing model meta data of the saved model
	 */
	ModelInfo save(ModelId modelId, byte[] content, String fileName, IUserContext user);
		
	/**
	 * Removes a model image for the given model id
	 * @param modelId
	 */
	void removeModelImage(ModelId modelId);
	
	/**
	 * Gets the model image for the given model id
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
	
	/**
     * Updates the state of the model
     * 
     * @param modelId the model Id
     * @param state the state of the model
     * @return
     */
    ModelId updateState(ModelId modelId, String state);

    /**
     * adds the given file content to the model
     * @param id
     * @param fileContent
     */
	void addFileContent(ModelId id, FileContent fileContent);
	
	/**
	 * Gets all available file names for the given model ID.
	 * To load its content, please use IModelRepository#getFileContent(modelId, fileName)
	 * @param id
	 * @return
	 */
	Set<String> getFileNames(ModelId id);

	/**
	 * gets file content for the given model id and file name
	 * @param modelId
	 * @param fileName
	 * @return
	 */
	Optional<FileContent> getFileContent(ModelId modelId, String fileName);
	
	/**
	 * Attaches the given file to the model
	 * 
	 * @param modelid The modelId where to attach the file
	 * @param fileName the filename
	 * @param content the content of the file
	 * @param userContext the user context
	 * @return
	 */
	boolean attachFile(ModelId modelid, FileContent fileContent, IUserContext userContext);
	
	/**
	 * Gets the list of attachments for the model
	 * 
	 * @param modelId
	 * @return
	 */
	List<String> getAttachmentFilenames(ModelId modelId);
	
	/**
	 * Gets the content of the attachment
	 * 
	 * @param modelid The model id where the file was attached
	 * @param fileName the filename of the attachment
	 * @return
	 */
	Optional<FileContent> getAttachmentContent(ModelId modelid, String fileName);
}