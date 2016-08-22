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
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.repository.service.IModelRepository;
import org.eclipse.vorto.repository.service.IModelRepository.ContentType;
import org.eclipse.vorto.repository.service.ModelNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="/find", description="Find useful vorto models")
@RestController
@RequestMapping(value = "/rest/model")
public class ModelRepositoryController extends RepositoryController {

	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String XMI = ".xmi";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";

	@Autowired
	private IModelRepository modelRepository;

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Find a model by a free-text search expression")
	@RequestMapping(value = "/query={expression:.*}", method = RequestMethod.GET)
	public List<ModelResource> searchByExpression(@ApiParam(value = "a free-text search expression", required = true) @PathVariable String expression) {
		List<ModelResource> modelResources = modelRepository.search(expression);
		logger.info("searchByExpression: [" + expression + "] Rows returned: " + modelResources.size());
		return modelResources;
	}

	@ApiOperation(value = "Returns a model by its full qualified model ID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public ModelResource getModelResource(	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
											@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
											@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);
		logger.info("getModelResource: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");
		ModelResource resource =  modelRepository.getById(modelId);
		if (resource == null) {
			throw new ModelNotFoundException("Model does not exist", null);
		}
		return resource;
	}
	
	@ApiOperation(value = "Returns the image of a vorto model")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/image/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void getModelImage(	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
								@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
								@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version, 
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
	
	@ApiOperation(value = "Adds an image for a vorto model",hidden=true)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	public void uploadModelImage(	@ApiParam(value = "The image to upload", required = true)	@RequestParam("file") MultipartFile file,
									@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @RequestParam String namespace,
									@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @RequestParam String name,
									@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @RequestParam String version) {
		logger.info("uploadImage: [" + file.getOriginalFilename() + "]");
		try {
			modelRepository.addModelImage(new ModelId(name, namespace, version), file.getBytes());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@ApiOperation(value = "Downloads the model content in a specific output format")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/file/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void downloadModelById(	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
									@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
									@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version, 
									@ApiParam(value = "Choose output file format, e.g. DSL", required = true) final @RequestParam(value="output",required=false) ContentType outputType, 
									@ApiParam(value = "Set true if dependencies shall be included", required = false) final @RequestParam(value="includeDependencies",required=false) boolean includeDependencies, 
									final HttpServletResponse response) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);

		logger.info("downloadModelById: [" + modelId.toString() + "] - Fullpath: [" + modelId.getFullPath() + "]");

		final ContentType contentType = outputType;

		if (includeDependencies) {
			createZipWithAllDependencies(modelId, contentType, response);
		} else {
			createSingleModelContent(modelId,contentType, response);
		}
		
		
	}
	
	private void createZipWithAllDependencies(ModelId modelId, ContentType contentType, HttpServletResponse response) {
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
	
	private void createSingleModelContent(ModelId modelId, ContentType contentType, HttpServletResponse response) {
		byte[] modelContent = modelRepository.getModelContent(modelId, contentType).getContent();
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
	
	private void addModelToZip(ZipOutputStream zipOutputStream, ModelId modelId, ContentType contentType) throws Exception {
		byte[] modelContent = modelRepository.getModelContent(modelId,contentType).getContent();
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

	@ApiOperation(value = "Getting all mapping resources")
	@RequestMapping(value = "/mapping/zip/{namespace}/{name}/{version:.+}/{targetPlatform}", method = RequestMethod.GET)
	public void getMappingResources( 	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace,
										@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name, 
										@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version,
										@ApiParam(value = "The name of target platform, e.g. lwm2m", required = true) final @PathVariable String targetPlatform, final HttpServletResponse response) {
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
