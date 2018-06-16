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
import java.security.Principal;
import java.util.Collection;
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
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.api.upload.ValidationReport;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepository.ContentType;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.dto.ModelContent;
import org.eclipse.vorto.repository.workflow.impl.SimpleWorkflowModel;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@Autowired
	private IUserRepository userRepository;

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Find a model by a free-text search expression")
	@RequestMapping(value = "/query={expression:.*}", method = RequestMethod.GET)
	public List<ModelInfo> searchByExpression(@ApiParam(value = "a free-text search expression", required = true) @PathVariable String expression) throws UnsupportedEncodingException {
		IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
		List<ModelInfo> modelResources = modelRepository.search(URLDecoder.decode(expression, "utf-8"));
		return modelResources.stream().filter(model -> isReleasedOrDeprecated(model) || isUser(SecurityContextHolder.getContext().getAuthentication()) || isAdmin(SecurityContextHolder.getContext().getAuthentication())).map(resource -> ModelDtoFactory.createDto(resource, userContext)).collect(Collectors.toList());
	}
	
	private boolean isAdmin(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
	private boolean isUser(Authentication authentication) {
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
	}

	private boolean isReleasedOrDeprecated(ModelInfo model) {
		return SimpleWorkflowModel.STATE_RELEASED.getName().equals(model.getState()) ||
			   SimpleWorkflowModel.STATE_DEPRECATED.getName().equals(model.getState());
	}

	@ApiOperation(value = "Returns a model by its full qualified model ID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found"), @ApiResponse(code = 403, message = "Not Authorized to view model")})
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:get')")
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
		return ModelDtoFactory.createDto(resource, UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName()));
	}
	
	@ApiOperation(value = "Returns the model content")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:get')")
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
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:get')")
	@RequestMapping(value = "/content/{namespace}/{name}/{version:.+}/mapping/{targetplatformKey}", method = RequestMethod.GET)
	public AbstractModel getModelContentForTargetPlatform(@ApiParam(value = "The namespace of vorto model, e.g. com.mycompany", required = true) final @PathVariable String namespace, 
			@ApiParam(value = "The name of vorto model, e.g. NewInfomodel", required = true) final @PathVariable String name,
			@ApiParam(value = "The version of vorto model, e.g. 1.0.0", required = true) final @PathVariable String version,
			@ApiParam(value = "The key of the targetplatform, e.g. lwm2m", required = true) final @PathVariable String targetplatformKey) {
		
		List<ModelInfo> mappingResource = modelRepository.getMappingModelsForTargetPlatform(new ModelId(name, namespace, version), targetplatformKey);
		if (!mappingResource.isEmpty()) {
			byte[] mappingContentZip = createZipWithAllDependencies(mappingResource.get(0).getId(), ContentType.DSL);
			IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();

			MappingModel mappingModel = (MappingModel)workspace.get().stream().filter(p -> p instanceof MappingModel).findFirst().get();
			
			byte[] modelContent = createZipWithAllDependencies(new ModelId(name, namespace, version), ContentType.DSL);
			
		    workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent))).read();

			return ModelDtoFactory.createResource(workspace.get().stream().filter(p -> p.getName().equals(name)).findFirst().get(),Optional.of(mappingModel));
		} else {
			return getModelContent(namespace,name,version);
		}
	}
	
	@ApiOperation(value = "Returns the model content including target platform specific attributes for the given model- and mapping modelID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId.fromPrettyFormat(modelId),'model:get')")
	@RequestMapping(value = "/content/{modelId:.+}/mapping/{mappingId:.+}", method = RequestMethod.GET)
	public AbstractModel getModelContentByModelAndMappingId(@ApiParam(value = "The model ID (prettyFormat)", required = true) final @PathVariable String modelId, 
			@ApiParam(value = "The mapping Model ID (prettyFormat)", required = true) final @PathVariable String mappingId) {
		
		ModelInfo vortoModelInfo = modelRepository.getById(ModelId.fromPrettyFormat(modelId));
		ModelInfo mappingModelInfo = modelRepository.getById(ModelId.fromPrettyFormat(mappingId));
		
		if (vortoModelInfo == null) {
			throw new ModelNotFoundException("Could not find vorto model with ID: " + modelId);
		} else if (mappingModelInfo == null)  {
			throw new ModelNotFoundException("Could not find mapping with ID: " + mappingId);

		}
		
		byte[] mappingContentZip = createZipWithAllDependencies(mappingModelInfo.getId(), ContentType.DSL);
		IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();
		MappingModel mappingModel = (MappingModel)workspace.get().stream().filter(p -> p instanceof MappingModel).findFirst().get();

		byte[] modelContent = createZipWithAllDependencies(vortoModelInfo.getId(), ContentType.DSL);
	    workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent))).read();

		return ModelDtoFactory.createResource(workspace.get().stream().filter(p -> p.getName().equals(vortoModelInfo.getId().getName())).findFirst().get(),Optional.of(mappingModel));

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
		
	@ApiOperation(value = "Downloads the model content in a specific output format")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"), @ApiResponse(code = 404, message = "Model not found")})
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:get')")
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
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(new org.eclipse.vorto.repository.api.ModelId(#name,#namespace,#version),'model:get')")
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
		
		final String fileName = modelId.getNamespace() + "_" + modelId.getName() + "_" + modelId.getVersion() + ".zip";
		
		sendAsZipFile(response, fileName, mappingResources);
	}

	@RequestMapping(value ={ "/mine" }, method = RequestMethod.GET)
	public void getUserModels(Principal user, final HttpServletResponse response) {
		List<ModelInfo> userModels = this.modelRepository.search("author:" + UserContext.user(user.getName()).getHashedUsername());
		
		logger.info("Exporting information models for " + user.getName() + " results: " + userModels.size());
		
		sendAsZipFile(response, user.getName() + "-models.zip", userModels);
	}
	
	private void sendAsZipFile(final HttpServletResponse response, final String fileName, List<ModelInfo> modelInfos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		final ContentType contentType = ContentType.DSL;
		
		try {
		for (ModelInfo modelInfo : modelInfos) {
			addModelToZip(zos, modelInfo.getId(), contentType);
		}

		zos.close();
		baos.close();
		} catch(Exception ex) {
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
	
    @ApiOperation(value = "Saves a model to the repository.")
    @RequestMapping(method = RequestMethod.PUT,
    				value = "/{namespace}/{name}/{version:.+}",
    				produces = "application/json")
    public ValidationReport saveModel(	@ApiParam(value = "namespace", required = true) @PathVariable String namespace,
									    @ApiParam(value = "name", required = true) @PathVariable String name,
									    @ApiParam(value = "version", required = true) @PathVariable String version, @RequestBody ModelContent content) {
    	try {
    		ModelId modelId = new ModelId(name,namespace,version);
    		
    		IUserContext userContext = UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
    		ModelInfo modelInfo = ModelParserFactory.getParser("model"+ModelType.valueOf(content.getType()).getExtension()).parse(new ByteArrayInputStream(content.getContentDsl().getBytes()));
    		
    		if (!modelId.equals(modelInfo.getId())) {
    			return ValidationReport.invalid(modelInfo, "You may not change the model ID (name, namespace, version). For this please create a new model.");
    		}
    		ModelValidationHelper validationHelper = new ModelValidationHelper(modelRepository, userRepository);
    		ValidationReport validationReport =  validationHelper.validate(modelInfo, userContext);
    		if (validationReport.isValid()) {
    			this.modelRepository.save(modelInfo.getId(), content.getContentDsl().getBytes(), modelInfo.getId().getName()+modelInfo.getType().getExtension(), userContext);
    		} 
    		return validationReport;
    	} catch(ValidationException validationException) {
    		return ValidationReport.invalid(null, validationException.getMessage());
    	}
    	
    }
}
