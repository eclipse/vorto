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
import java.util.Set;
import org.eclipse.vorto.repository.core.ModelInfo;

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
   * @param fileUpload
   * @param context
   * @return result about information of the uploaded content and the upload handle.
   */
  UploadModelResult upload(FileUpload fileUpload, Context context);


  /**
   * Performs the actual conversion for the uploaded file
   * 
   * @pre {@link UploadModelResult#isValid() == true}}
   * 
   * @post model was stored in persistence layer.
   * 
   * @param uploadHandleId
   * @param context
   * @return
   * @throws ModelImporterException
   */
  List<ModelInfo> doImport(String uploadHandleId, Context context) throws ModelImporterException;
}
