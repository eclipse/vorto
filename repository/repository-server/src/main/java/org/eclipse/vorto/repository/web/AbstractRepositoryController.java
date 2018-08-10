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
package org.eclipse.vorto.repository.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.GenerationException;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

public abstract class AbstractRepositoryController {
	
	@Autowired
	protected IModelRepository modelRepository;
	
	protected static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	protected static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	protected static final String CONTENT_DISPOSITION = "content-disposition";

	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Model not found.")  // 404
    @ExceptionHandler(ModelNotFoundException.class)
    public void NotFound(final ModelNotFoundException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason = "Model already exists.")  // 409
    @ExceptionHandler(ModelAlreadyExistsException.class)
    public void ModelExists(final ModelAlreadyExistsException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "Wrong Input")  // 405
    @ExceptionHandler(IllegalArgumentException.class)
    public void WrongInput(final IllegalArgumentException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.UNAUTHORIZED, reason = "Not authorized to view the model")  // 403
    @ExceptionHandler(NotAuthorizedException.class)
    public void unAuthorized(final NotAuthorizedException ex){
		// do logging
    }
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "Error during generation.")
    @ExceptionHandler(GenerationException.class)
    public void GeneratorProblem(final GenerationException ex){
		// do logging
    }
	
	protected void createSingleModelContent(ModelId modelId, HttpServletResponse response) {
		FileContent fileContent = modelRepository.getModelContent(modelId);
		
		final byte[] modelContent = fileContent.getContent();
		if (modelContent != null && modelContent.length > 0) {
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileContent.getFileName());
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
		ModelFileContent modelFile = modelRepository.getModelContent(modelId);
		ModelInfo modelResource = modelRepository.getById(modelId);

		try {
			ZipEntry zipEntry = new ZipEntry(modelFile.getFileName());
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.write(modelFile.getContent());
			zipOutputStream.closeEntry();
		} catch (Exception ex) {
			// entry possible exists already, so skipping TODO: ugly hack!!
		}

		for (ModelId reference : modelResource.getReferences()) {
			addModelToZip(zipOutputStream, reference);
		}
	}
	
	protected void sendAsZipFile(final HttpServletResponse response, final String fileName, List<ModelInfo> modelInfos) {
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

}
