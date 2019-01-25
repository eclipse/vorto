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
package org.eclipse.vorto.repository.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.generation.GenerationException;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public abstract class AbstractRepositoryController extends ResponseEntityExceptionHandler {

  @Autowired
  protected IModelRepository modelRepository;

  protected static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  protected static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  protected static final String CONTENT_DISPOSITION = "content-disposition";

  @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Model not found.") // 404
  @ExceptionHandler(ModelNotFoundException.class)
  public void notFound(final ModelNotFoundException ex) {
    // do logging
  }

  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Model already exists.") // 409
  @ExceptionHandler(ModelAlreadyExistsException.class)
  public void modelExists(final ModelAlreadyExistsException ex) {
    // do logging
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST) // 405
  @ExceptionHandler(IllegalArgumentException.class)
  public Object wrongInput(final IllegalArgumentException ex) {
	  Map<String, Object> error = new HashMap<String, Object>();
	  error.put("message", ex.getMessage());
	  return new ResponseEntity<Object>(error, HttpStatus.BAD_REQUEST);
  }

  @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Not authorized to view the model") // 403
  @ExceptionHandler(NotAuthorizedException.class)
  public void unAuthorized(final NotAuthorizedException ex) {
    // do logging
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error during generation.")
  @ExceptionHandler(GenerationException.class)
  public void generatorProblem(final GenerationException ex) {
    // do logging
  }
  
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Object> cannotLoadSpecification(final ValidationException ex) {
    Map<String, Object> validationError = new HashMap<String, Object>();
    validationError.put("message", ex.getMessage());
    validationError.put("modelId", ex.getModelResource().getId().getPrettyFormat());
    return new ResponseEntity<Object>(validationError, HttpStatus.BAD_REQUEST);
  }

  protected void createSingleModelContent(ModelId modelId, HttpServletResponse response) {
    Optional<FileContent> fileContent = modelRepository.getFileContent(modelId, Optional.empty());

    final byte[] modelContent = fileContent.get().getContent();
    if (modelContent != null && modelContent.length > 0) {
      response.setHeader(CONTENT_DISPOSITION,
          ATTACHMENT_FILENAME + fileContent.get().getFileName());
      response.setContentType(APPLICATION_OCTET_STREAM);
      try {
        IOUtils.copy(new ByteArrayInputStream(modelContent), response.getOutputStream());
        response.flushBuffer();
      } catch (IOException e) {
        throw new RuntimeException("Error copying file.", e);
      }
    } else {
      throw new RuntimeException("File not found.");
    }
  }

  protected void addModelToZip(ZipOutputStream zipOutputStream, ModelId modelId) throws Exception {
    try {
      FileContent modelFile = modelRepository.getFileContent(modelId, Optional.empty()).get();
      ModelInfo modelResource = modelRepository.getById(modelId);

      try {
        ZipEntry zipEntry = new ZipEntry(modelResource.getId().getPrettyFormat()+modelResource.getType().getExtension());
        zipOutputStream.putNextEntry(zipEntry);
        zipOutputStream.write(modelFile.getContent());
        zipOutputStream.closeEntry();
      } catch (Exception ex) {
        // entry possible exists already, so skipping TODO: ugly hack!!
      }

      for (ModelId reference : modelResource.getReferences()) {
        addModelToZip(zipOutputStream, reference);
      }
    } catch(NotAuthorizedException notAuthorized) {
        return;
    }
    
  }

  protected void sendAsZipFile(final HttpServletResponse response, final String fileName,
      List<ModelInfo> modelInfos) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    try {
      for (ModelInfo modelInfo : modelInfos) {
        addModelToZip(zos, modelInfo.getId());
      }

      zos.close();
      baos.close();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  protected IModelWorkspace getWorkspaceForModel(final ModelId modelId) {
    List<ModelInfo> allModels = getModelWithAllDependencies(modelId);
    DependencyManager dm = new DependencyManager(new HashSet<>(allModels));
    allModels = dm.getSorted();

    ModelWorkspaceReader workspaceReader = IModelWorkspace.newReader();
    for (ModelInfo model : allModels) {
      FileContent modelContent = this.modelRepository
          .getFileContent(model.getId(), Optional.of(model.getFileName())).get();
      workspaceReader.addFile(new ByteArrayInputStream(modelContent.getContent()), model.getType());
    }

    return workspaceReader.read();
  }


  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    List<ModelInfo> modelInfos = new ArrayList<>();

    ModelInfo modelResource = modelRepository.getById(modelId);
    modelInfos.add(modelResource);

    for (ModelId reference : modelResource.getReferences()) {
      modelInfos.addAll(getModelWithAllDependencies(reference));
    }

    return modelInfos;
  }

}
