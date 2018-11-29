/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of the Eclipse Public
 * License v1.0 and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html The Eclipse
 * Distribution License is available at http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors: Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.repository.importer;

import java.util.List;
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
   * @param callerId
   * @return result about information of the uploaded content and the upload handle.
   */
  UploadModelResult upload(FileUpload fileUpload, IUserContext userContext);

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
