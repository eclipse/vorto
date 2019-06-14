/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.importer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;

/**
 * Importer is in charge of converting model files to Vorto Information Models and store them in the
 * repository
 *
 */
public interface IModelImporter {

  /**
   * 
   * @return unique identifier of the importer, e.g. LwM2M
   */
  String getKey();

  /**
   * 
   * @return short description of what the importer does
   */
  String getShortDescription();

  /**
   * 
   * @return a list of file extensions that the importer handles
   */
  Set<String> getSupportedFileExtensions();

  /**
   * Uploads model content and validates it. If the model is valid, the returned upload handle is
   * used to import the model into the repository via
   * {@link IModelImporter#doImport(String,UserContext)}}
   * 
   * @param fileUpload
   * @param targetNamespace namespace that the uploaded model will be converted to. If not present, the original model namespace is taken
   * @param userContext
   * @return result about information of the uploaded content and the upload handle.
   */
  UploadModelResult upload(FileUpload fileUpload, Optional<String> targetNamespace, IUserContext userContext);

  /**
   * @pre {@link UploadModelResult#isValid() == true}}
   * 
   * @post model was stored in persistence layer.
   * 
   *       Checks in a new model into the repository
   * @param uploadHandle
   * @param user user context
   * @return model that was been checked in
   */
  List<ModelInfo> doImport(String uploadHandleId, IUserContext user) throws ModelImporterException;

}
