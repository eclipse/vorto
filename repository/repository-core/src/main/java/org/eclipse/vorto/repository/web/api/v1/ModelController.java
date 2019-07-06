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
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.conversion.ModelIdToModelContentConverter;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @Autowired
  private ITenantService tenantService;

  private static Logger logger = Logger.getLogger(ModelController.class);

  @ApiOperation(value = "Returns a full model by its model ID")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of model info"),
      @ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model not found"),
      @ApiResponse(code = 403, message = "Not Authorized to view model")})
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/{modelId:.+}", method = RequestMethod.GET)
  public ModelInfo getModelInfo(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "modelId must not be null");

    ModelId modelID = ModelId.fromPrettyFormat(modelId);
    
    logger.info("getModelInfo: [" + modelID.getPrettyFormat() + "]");

    ModelInfo resource = getRepo(modelID).getById(modelID);

    if (resource == null) {
      throw new ModelNotFoundException("Model does not exist", null);
    }
    return ModelDtoFactory.createDto(resource);
  }

  @ApiOperation(value = "Returns the complete model content")
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Successful retrieval of model content"),
          @ApiResponse(code = 400, message = "Wrong input"),
          @ApiResponse(code = 404, message = "Model not found")})
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/{modelId:.+}/content", method = RequestMethod.GET)
  public ModelContent getModelContent(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    if (!getRepo(modelID).exists(modelID)) {
      throw new ModelNotFoundException("Model does not exist", null);
    }
    
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.modelRepositoryFactory);
    
    return converter.convert(modelID, Optional.empty());
  }

  @ApiOperation(
      value = "Returns the complete model content including target platform specific attributes")
  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Successful retrieval of model content"),
          @ApiResponse(code = 400, message = "Wrong input"),
          @ApiResponse(code = 404, message = "Model not found")})
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/{modelId:.+}/content/{targetplatformKey}", method = RequestMethod.GET)
  public ModelContent getModelContentForTargetPlatform(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "The key of the targetplatform, e.g. lwm2m",
          required = true) final @PathVariable String targetplatformKey) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.modelRepositoryFactory);
    
    return converter.convert(modelID, Optional.of(targetplatformKey));
  }

  @ApiOperation(value = "Downloads the model file")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful download of model file"),
      @ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model not found")})
  @PreAuthorize("hasRole('ROLE_USER')")
  @RequestMapping(value = "/{modelId:.+}/file", method = RequestMethod.GET)
  public void downloadModelById(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Set true if dependencies shall be included",
          required = false) final @RequestParam(value = "includeDependencies",
              required = false) boolean includeDependencies,
      final HttpServletResponse response) {

    Objects.requireNonNull(modelId, "modelId must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    final String tenantId = getTenant(modelID).orElseThrow(
        () -> new ModelNotFoundException("The tenant for '" + modelID.getPrettyFormat() + "' could not be found."));

    logger.info("Download of Model file : [" + modelID.toString() + "]");

    if (includeDependencies) {
      byte[] zipContent = createZipWithAllDependencies(tenantId, modelID);
      response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getNamespace() + "_"
          + modelID.getName() + "_" + modelID.getVersion() + ".zip");
      response.setContentType(APPLICATION_OCTET_STREAM);
      try {
        IOUtils.copy(new ByteArrayInputStream(zipContent), response.getOutputStream());
        response.flushBuffer();
      } catch (IOException e) {
        throw new GenericApplicationException("Error copying file.", e);
      }
    } else {
      createSingleModelContent(tenantId, modelID, response);
    }
  }

  private byte[] createZipWithAllDependencies(String tenantId, ModelId modelId) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    try {
      addModelToZip(tenantId, zos, modelId);

      zos.close();
      baos.close();

      return baos.toByteArray();

    } catch (Exception ex) {
      throw new GenericApplicationException("Error while generating zip file.", ex);
    }
  }

  private IModelRepository getRepo(ModelId modelId) {
    return modelRepositoryFactory.getRepositoryByModel(modelId);
  }

  private Optional<String> getTenant(ModelId modelId) {
    return tenantService.getTenantFromNamespace(modelId.getNamespace())
        .map(tenant -> tenant.getTenantId());
  }
}
