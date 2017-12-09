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
package org.eclipse.vorto.repository.web.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepository.ContentType;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.eclipse.vorto.server.commons.reader.IModelWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Api(value="/find", description="Find information models")
@RestController
@RequestMapping(value = "/rest/model")
public class ModelRepositoryController extends AbstractRepositoryController {

	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String XMI = ".xmi";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";

	@Value("${server.config.maxModelImageSize}")
	private long maxModelImageSize;
	
	@Autowired
	private IModelRepository modelRepository;

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Find a model by a free-text search expression")
	@RequestMapping(value = "/query={expression:.*}", method = RequestMethod.GET)
	public List<ModelInfo> searchByExpression(@ApiParam(value = "a free-text search expression", required = true) @PathVariable String expression) throws UnsupportedEncodingException {
		List<ModelInfo> modelResources = modelRepository.search(URLDecoder.decode(expression, "utf-8"));
		logger.info("searchByExpression: [" + expression + "] Rows returned: " + modelResources.size());
		return modelResources.stream().map(resource -> ModelDtoFactory.createDto(resource)).collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Returns a model by its full qualified model ID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public ModelInfo getModelResource(	@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
											@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
											@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version) {
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		final ModelId modelId = new ModelId(name, namespace, version);
		logger.info("getModelResource: [" + modelId.toString() + "]");
		ModelInfo resource =  modelRepository.getById(modelId);
		if (resource == null) {
			throw new ModelNotFoundException("Model does not exist", null);
		}
		return ModelDtoFactory.createDto(resource);
	}
	
	@ApiOperation(value = "Returns the model content")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/content/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public AbstractModel getModelContent(@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
			@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
			@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version) {
		
		byte[] modelContent = createZipWithAllDependencies(new ModelId(name, namespace, version), ContentType.DSL);
		
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent))).read();
		return ModelDtoFactory.createResource(workspace.get().stream().filter(p -> p.getName().equals(name)).findFirst().get(),Optional.empty());
	}
	
	@ApiOperation(value = "Returns the model content including target platform specific attributes")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@RequestMapping(value = "/content/{namespace}/{name}/{version:.+}/{targetplatformKey}", method = RequestMethod.GET)
	public AbstractModel getModelContentForTargetPlatform(@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
			@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
			@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version,
			@ApiParam(value = "The key of the targetplatform, e.g. lwm2m", required = true) final @PathVariable String targetplatformKey) {
		
		List<ModelInfo> mappingResources = modelRepository.getMappingModelsForTargetPlatform(new ModelId(name, namespace, version), targetplatformKey);
		if (!mappingResources.isEmpty()) {
			byte[] mappingContentZip = createZipWithAllDependencies(mappingResources.get(0).getId(), ContentType.DSL);
			IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();

			MappingModel mappingModel = (MappingModel)workspace.get().stream().filter(p -> p instanceof MappingModel).findFirst().get();
			
			byte[] modelContent = createZipWithAllDependencies(new ModelId(name, namespace, version), ContentType.DSL);
			
		    workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent))).read();

			return ModelDtoFactory.createResource(workspace.get().stream().filter(p -> p.getName().equals(name)).findFirst().get(),Optional.of(mappingModel));
		} else {
			return getModelContent(namespace,name,version);
		}
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
	@PreAuthorize("isAuthenticated()")
	public void uploadModelImage(	@ApiParam(value = "The image to upload", required = true)	@RequestParam("file") MultipartFile file,
									@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @RequestParam String namespace,
									@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @RequestParam String name,
									@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @RequestParam String version) {
		if (file.getSize() > maxModelImageSize) {
			throw new UploadTooLargeException("model image", maxModelImageSize);
		}
		
		logger.info("uploadImage: [" + file.getOriginalFilename() + ", " + file.getSize() + "]");
		
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

		logger.info("downloadModelById: [" + modelId.toString() + "]");

		final ContentType contentType = outputType;

		if (includeDependencies) {
			byte[] zipContent = createZipWithAllDependencies(modelId, contentType);
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelId.getNamespace() + "_"
					+ modelId.getName() + "_" + modelId.getVersion() + ".zip");
			response.setContentType(APPLICATION_OCTET_STREAM);
			try {
				IOUtils.copy(new ByteArrayInputStream(zipContent), response.getOutputStream());
				response.flushBuffer();
			} catch (IOException e) {
				throw new RuntimeException("Error copying file.", e);
			}
		} else {
			createSingleModelContent(modelId,contentType, response);
		}
	}
			
	private byte[] createZipWithAllDependencies(ModelId modelId, ContentType contentType) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		
		try {
			addModelToZip(zos, modelId, contentType);

			zos.close();
			baos.close();
			
			return baos.toByteArray();
			
		} catch(Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void createSingleModelContent(ModelId modelId, ContentType contentType, HttpServletResponse response) {
		byte[] modelContent = modelRepository.getModelContent(modelId, contentType).getContent();
		if (modelContent != null && modelContent.length > 0) {
			final ModelInfo modelResource = modelRepository.getById(modelId);
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
		ModelInfo modelResource = modelRepository.getById(modelId);
		
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

	private String getFileName(ModelInfo modelResource, ContentType contentType) {
		if (contentType == ContentType.XMI) {
			return modelResource.getId().getName() + XMI;
		} else {
			return modelResource.getId().getName() + modelResource.getType().getExtension();
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
		List<ModelInfo> mappingResources = modelRepository.getMappingModelsForTargetPlatform(modelId, targetPlatform);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		final ContentType contentType = ContentType.DSL;
		
		try {
		for (ModelInfo mappingResource : mappingResources) {
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
