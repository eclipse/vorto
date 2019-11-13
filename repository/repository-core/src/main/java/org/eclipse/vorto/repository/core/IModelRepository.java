/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.core;

import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Alexander Edelmann
 *
 */
public interface IModelRepository {

  String VISIBILITY_PRIVATE = "private";
  String VISIBILITY_PUBLIC = "public";

  /**
   * Searches model resources for the given expression
   *
   * @param queryExpression
   * @return
   */
  List<ModelInfo> search(String queryExpression);

  /**
   * Gets a very detailed model for the given model id, which includes resolving target platform mappings
   * If only basic meta - data is required, consider using {@link IModelRepository#getBasicInfo(ModelId)}
   * @param modelId
   * @return
   * @throws NotAuthorizedException if current user is not allowed to access the given model
   */
  ModelInfo getById(ModelId modelId) throws NotAuthorizedException;

  /**
   * Gets the model ID of the latest (highest and released) version for the given model.
   * @param modelId - the ID of the model, version can be blank - it will be ignored
   * @return the model ID with the latest version number
   */
  ModelId getLatestModelVersionId(ModelId modelId);

  /**
   * Convenience method that loads an entire model and resolves all mappings that are used by this model
   * @param modelId
   * @return
   * @throws NotAuthorizedException
   */
  ModelInfo getByIdWithPlatformMappings(ModelId modelId) throws NotAuthorizedException;

  /**
   * Get all models in this repository which are referencing the given modelId
   *
   * @param modelId the modelId that is being referenced
   * @return list of models referencing modelId
   */
  List<ModelInfo> getModelsReferencing(ModelId modelId);

  /**
   * Returns the actual model content for the given model id
   *
   * @param modelId
   * @throws ModelNotFoundException
   * @throws NotAuthorizedException if current user is not allowed to access the given model
   * @return
   */
  ModelFileContent getModelContent(ModelId modelId, boolean validate) throws NotAuthorizedException;

  /**
   * Creates a new version of an existing model
   *
   * @param modelId
   * @param newVersion
   * @param user
   * @return the model and content of the newly created model in the repository
   * @throws ModelNotFoundException if the given modelId cannot be found
   * @throws ModelAlreadyExistsException if the given model with the given version already exists
   */
  ModelResource createVersion(ModelId modelId, String newVersion, IUserContext user);

  /**
   * Return the resource for the modelId
   *
   * @param modelId
   * @return resource
   */
  ModelResource getEMFResource(ModelId modelId);

  /**
   * Saves a model resource in the repository
   * @param resource
   * @param user
   * @return
   */
  ModelInfo save(ModelResource resource, IUserContext user);


  /**
   *
   * @param modelId
   * @param content
   * @param fileName
   * @param user
   * @param validate
   * @return
   */
  ModelInfo save(ModelId modelId, byte[] content, String fileName, IUserContext user,boolean validate);

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
   * Renames a given old modelId to the specified new modelId
   * return updated model info
   */
  ModelInfo rename(ModelId oldModelId, ModelId newModelId, IUserContext user);

  /**
   * Gets the mapping model for the given modelId and the given target platform
   *
   * @param modelId
   * @param targetPlatform
   * @param version of the mapping. If not specified, latest is taken
   * @return
   */
  List<ModelInfo> getMappingModelsForTargetPlatform(ModelId modelId, String targetPlatform,
      Optional<String> version) throws NotAuthorizedException;;

  /**
   * Removes the model for the given ModelID
   *
   * @param modelId
   */
  void removeModel(ModelId modelId) throws NotAuthorizedException;;

  /**
   * Updates the model meta information
   *
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
   * Updates the visibility of the model
   *
   * @param modelId the model Id
   * @param visibility the visibility of the model
   * @return
   */
  ModelId updateVisibility(ModelId modelId, String visibility);

  /**
   * adds the given file content to the model
   *
   * @param id
   * @param fileContent
   */
  void addFileContent(ModelId id, FileContent fileContent);

  /**
   * gets file content for the given model id and file name
   *
   * @param modelId
   * @param fileName
   * @return
   */
  Optional<FileContent> getFileContent(ModelId modelId, Optional<String> fileName)
      throws NotAuthorizedException;;

  /**
   * Attaches the given file to the model
   *
   * @param modelid The modelId where to attach the file
   * @param fileContent the filename
   * @param userContext the user context
   * @param tags attachment tags
   * @throws Attachment when the attachment could not be attached to the node, e.g. because it is
   *         not valid.
   */
  void attachFile(ModelId modelid, FileContent fileContent, IUserContext userContext, Tag... tags)
      throws AttachmentException;

  /**
   * Gets a list of attachments for the model (without its content)
   *
   * @param modelId
   * @return list of attachments of the given model
   */
  List<Attachment> getAttachments(ModelId modelId) throws NotAuthorizedException;

  /**
   * Gets a list of attachments having the given tag
   *
   * @param modelId
   * @param attachmentTag
   * @return
   */
  List<Attachment> getAttachmentsByTag(ModelId modelId, Tag attachmentTag)
      throws NotAuthorizedException;

  /**
   * Gets the content of the attachment
   *
   * @param modelid The model id where the file was attached
   * @param fileName the filename of the attachment
   * @return
   */
  Optional<FileContent> getAttachmentContent(ModelId modelid, String fileName)
      throws NotAuthorizedException;

  /**
   * Deletes the attachment
   *
   * @param modelid The model id where the file was attached
   * @param fileName the filename of the attachment
   * @return
   */
  boolean deleteAttachment(ModelId modelId, String fileName) throws NotAuthorizedException;

  /**
   * Checks if the given model ID exists in the repository
   *
   * @param modelId
   * @return
   */
  boolean exists(ModelId modelId);

  /**
   *
   * @return
   */
  String getTenantId();

}
