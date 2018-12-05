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
package org.eclipse.vorto.repository.web.api.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.core.api.model.mapping.EntitySource;
import org.eclipse.vorto.core.api.model.mapping.EnumSource;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSource;
import org.eclipse.vorto.core.api.model.mapping.InfomodelSource;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.Source;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.repository.web.core.ModelRepositoryController;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
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
@Api(value = "/models")
@RestController("modelRepositoryController")
@RequestMapping(value = "/api/v1/models")
public class ModelController extends AbstractRepositoryController {

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Returns a model by its full qualified model ID")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successful retrieval of model info"), @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found"),
			@ApiResponse(code = 403, message = "Not Authorized to view model") })
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{modelId:.+}", method = RequestMethod.GET)
	public ModelInfo getModelInfo(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0", required = true) final @PathVariable String modelId) {
		Objects.requireNonNull(modelId, "modelId must not be null");

		logger.info("getModelInfo: [" + modelId + "]");
		ModelInfo resource = modelRepository.getById(ModelId.fromPrettyFormat(modelId));

		if (resource == null) {
			throw new ModelNotFoundException("Model does not exist", null);
		}
		return ModelDtoFactory.createDto(resource,
				UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	@ApiOperation(value = "Returns the complete model content")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of model content"), @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{modelId:.+}/content", method = RequestMethod.GET)
	public ModelContent getModelContent(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0", required = true) final @PathVariable String modelId) {

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		if (this.modelRepository.getById(modelID) == null) {
			throw new ModelNotFoundException("Model does not exist", null);
		}

		IModelWorkspace workspace = getWorkspaceForModel(modelID);

		ModelContent result = new ModelContent();
		result.setRoot(modelID);

		workspace.get().stream().forEach(model -> {
			result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()), ModelDtoFactory.createResource(model,Optional.empty()));
		});

		return result;
	}

	@ApiOperation(value = "Returns the complete model content including target platform specific attributes")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of model content"), @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{modelId:.+}/content/{targetplatformKey}", method = RequestMethod.GET)
	public ModelContent getModelContentForTargetPlatform(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0", required = true) final @PathVariable String modelId,
			@ApiParam(value = "The key of the targetplatform, e.g. lwm2m", required = true) final @PathVariable String targetplatformKey) {

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		List<ModelInfo> mappingResource = modelRepository
				.getMappingModelsForTargetPlatform(modelID, targetplatformKey);
		if (!mappingResource.isEmpty()) {

			IModelWorkspace workspace = getWorkspaceForModel(mappingResource.get(0).getId());
			
			ModelContent result = new ModelContent();
			result.setRoot(modelID);

			workspace.get().stream().forEach(model -> {
				if (!(model instanceof MappingModel)) {
					Optional<Model> mappingModel =  workspace.get().stream().filter(p -> p instanceof MappingModel).filter(p -> isMappingForModel((MappingModel)p,model))
							.findFirst();
					if (mappingModel.isPresent()) {
						result.getModels().put(new ModelId(model.getName(), model.getNamespace(), model.getVersion()), ModelDtoFactory.createResource(model,Optional.of((MappingModel)mappingModel.get())));
					}
				}
			});

			return result;

		} else {
			throw new ModelNotFoundException("Content for provided target platform key does not exist", null);
		}
	}

	private boolean isMappingForModel(MappingModel p, Model model) {
		if (p.getRules().isEmpty() || p.getRules().get(0).getSources().isEmpty()) {
			return false;
		}
		Source mappingSource = p.getRules().get(0).getSources().get(0);
		if (mappingSource instanceof InfomodelSource) {
			return EcoreUtil.equals(((InfomodelSource)mappingSource).getModel(),model);
		} else if (mappingSource instanceof FunctionBlockSource) {
			return EcoreUtil.equals(((FunctionBlockSource)mappingSource).getModel(),model);
		} else if (mappingSource instanceof EntitySource) {
			return EcoreUtil.equals(((EntitySource)mappingSource).getModel(),model);
		} else if (mappingSource instanceof EnumSource) {
			return EcoreUtil.equals(((EnumSource)mappingSource).getModel(),model);
		} else {
			return false;
		}
	}

	@ApiOperation(value = "Downloads the model file")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successful download of model file"), @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{modelId:.+}/file", method = RequestMethod.GET)
	public void downloadModelById(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0", required = true) final @PathVariable String modelId,
			@ApiParam(value = "Set true if dependencies shall be included", required = false) final @RequestParam(value = "includeDependencies", required = false) boolean includeDependencies,
			final HttpServletResponse response) {

		Objects.requireNonNull(modelId, "modelId must not be null");

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);

		logger.info("Download of Model file : [" + modelID.toString() + "]");

		if (includeDependencies) {
			byte[] zipContent = createZipWithAllDependencies(modelID);
			response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getNamespace() + "_"
					+ modelID.getName() + "_" + modelID.getVersion() + ".zip");
			response.setContentType(APPLICATION_OCTET_STREAM);
			try {
				IOUtils.copy(new ByteArrayInputStream(zipContent), response.getOutputStream());
				response.flushBuffer();
			} catch (IOException e) {
				throw new RuntimeException("Error copying file.", e);
			}
		} else {
			createSingleModelContent(modelID, response);
		}
	}
	
	private byte[] createZipWithAllDependencies(ModelId modelId) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);

		try {
			addModelToZip(zos, modelId);

			zos.close();
			baos.close();

			return baos.toByteArray();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
}