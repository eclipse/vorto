/*******************************************************************************
 * Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.repository.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.repository.model.ModelId;
import org.eclipse.vorto.repository.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="Model Controller", description="REST API to manage Models")
@RestController
@RequestMapping(value = "/rest/model")
public class ModelRepositoryController {

	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String XMI = ".xmi";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";

	@Autowired
	private IModelRepository modelRepository;

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Search in the model repository for the expression")
	@RequestMapping(value = "/query={expression:.*}", method = RequestMethod.GET)
	public List<ModelResource> searchByExpression(@ApiParam(value = "Search expression", required = true) @PathVariable String expression) {
		List<ModelResource> modelResources = modelRepository.search(expression);
		logger.info("searchByExpression: [" + expression + "] Rows returned: " + modelResources.size());
		return modelResources;
	}

	@ApiOperation(value = "Returns the Model for a specific Model ID")
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public ModelResource getModelResource(	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace, 
											@ApiParam(value = "name", required = true) final @PathVariable String name,
											@ApiParam(value = "version", required = true) final @PathVariable String version) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);
		logger.info("getModelResource: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");
		return modelRepository.getById(modelId);
	}
	
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public void deleteModelResource(	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace, 
											@ApiParam(value = "name", required = true) final @PathVariable String name,
											@ApiParam(value = "version", required = true) final @PathVariable String version) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");
		final ModelId modelId = new ModelId(name, namespace, version);
		logger.info("getModelResource: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");
		modelRepository.removeModel(modelId);
	}
	
	@ApiOperation(value = "Returns the image of a Model")
	@RequestMapping(value = "/image/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void getModelImage(	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace, 
								@ApiParam(value = "Namespace", required = true) final @PathVariable String name,
								@ApiParam(value = "Namespace", required = true) final @PathVariable String version, 
								@ApiParam(value = "Response", required = true) final HttpServletResponse response) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");
		
		final ModelId modelId = new ModelId(name, namespace, version);
		byte[] modelImage = modelRepository.getModelImage(modelId);
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelId.getName()+".png");
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {
			IOUtils.copy(new ByteArrayInputStream(modelImage), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}
	
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public void uploadModelImage(	@ApiParam(value = "Image", required = true)	@RequestParam("file") MultipartFile file,
									@ApiParam(value = "Namespace", required = true) final @RequestParam String namespace,
									@ApiParam(value = "name", required = true) final @RequestParam String name,
									@ApiParam(value = "version", required = true) final @RequestParam String version) {
		logger.info("uploadImage: [" + file.getOriginalFilename() + "]");
		try {
			modelRepository.addModelImage(new ModelId(name, namespace, version), file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@ApiOperation(value = "Download a Model specified by Model ID")
	@RequestMapping(value = "/file/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void downloadModelById(	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace, 
									@ApiParam(value = "Name", required = true) final @PathVariable String name,
									@ApiParam(value = "Version", required = true) final @PathVariable String version, 
									@ApiParam(value = "Output Type", required = true) final @RequestParam(value="output",required=false) String outputType,
									@ApiParam(value = "Response", required = true) final HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);

		logger.info("downloadModelById: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");

		final ContentType contentType = getContentType(outputType);
		byte[] modelContent = modelRepository.getModelContent(modelId, contentType);
		if (modelContent != null && modelContent.length > 0) {
			final ModelResource modelResource = modelRepository.getById(modelId);
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + getFileName(modelResource, contentType));
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

	@ApiOperation(value = "Download a specified Model by Model ID, including all References")
	@RequestMapping(value = "/zip/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void downloadModelByIdWithReferences( 	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace, 
													@ApiParam(value = "Name", required = true) final @PathVariable String name,
													@ApiParam(value = "Version", required = true) final @PathVariable String version, 
													@ApiParam(value = "Output Type", required = true) final @RequestParam(value="output",required=false) String outputType,
													@ApiParam(value = "Response", required = true) final HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);

		logger.info("downloadModelById: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");

		final ContentType contentType = getContentType(outputType);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		
		try {
			addModelToZip(zos, modelId, contentType);

			zos.close();
			baos.close();
			
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelId.getNamespace() + "_"
					+ modelId.getName() + "_" + modelId.getVersion() + ".zip");
			response.setContentType(APPLICATION_OCTET_STREAM);
			try {
				IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
				response.flushBuffer();
			} catch (IOException e) {
				throw new RuntimeException("Error copying file.", e);
			}
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void addModelToZip(ZipOutputStream zipOutputStream, ModelId modelId, ContentType contentType) throws Exception {
		byte[] modelContent = modelRepository.getModelContent(modelId,contentType);
		ModelResource modelResource = modelRepository.getById(modelId);
		
		try {
			ZipEntry zipEntry = new ZipEntry(getFileName(modelResource, contentType));
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.write(modelContent);
			zipOutputStream.closeEntry();
		} catch(Exception ex) {
			// entry possible exists already, so skipping TODO: ugly hack!!
		}
		
		for (ModelId reference : modelResource.getReferences()) {
			addModelToZip(zipOutputStream,reference,contentType);
		}
	}

	private String getFileName(ModelResource modelResource, ContentType contentType) {
		if (contentType == ContentType.XMI) {
			return modelResource.getId().getName() + XMI;
		} else {
			return modelResource.getId().getName() + modelResource.getModelType().getExtension();
		}
	}

	private ContentType getContentType(String output) {
		if (output == null || output.isEmpty()) {
			return ContentType.DSL;
		} else {
			return ContentType.valueOf(output.toUpperCase());
		}
	}

	@ApiOperation(value = "Getting all mapped Resources")
	@RequestMapping(value = "/mapping/zip/{namespace}/{name}/{version:.+}/{targetPlatform}", method = RequestMethod.GET)
	public void getMappingResources( 	@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace,
										@ApiParam(value = "Name", required = true) final @PathVariable String name, 
										@ApiParam(value = "Version", required = true) final @PathVariable String version,
										@ApiParam(value = "Target Platform", required = true) final @PathVariable String targetPlatform,
										@ApiParam(value = "Response", required = true) final HttpServletResponse response) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		
		final ModelId modelId = new ModelId(name, namespace, version);
		List<ModelResource> mappingResources = modelRepository.getMappingModelsForTargetPlatform(modelId, targetPlatform);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		final ContentType contentType = ContentType.DSL;
		
		try {
		for (ModelResource mappingResource : mappingResources) {
			addModelToZip(zos, mappingResource.getId(), contentType);
		}

		zos.close();
		baos.close();
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
		
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelId.getNamespace() + "_"
				+ modelId.getName() + "_" + modelId.getVersion() + ".zip");
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {
			IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

}
