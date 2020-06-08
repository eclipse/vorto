/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
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

import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.conversion.ModelIdToModelContentConverter;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipOutputStream;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController("modelRepositoryController")
@RequestMapping(value = "/api/v1/models")
public class ModelController extends AbstractRepositoryController {

  private static final Logger LOGGER = Logger.getLogger(ModelController.class);

  @PreAuthorize("isAuthenticated() or hasRole('ROLE_USER')")
  @GetMapping("/{modelId:.+}")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public ModelInfo getModelInfo(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "modelId must not be null");

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    LOGGER.info(String.format("Generated model info: [%s]", modelID.getPrettyFormat()));

    ModelInfo resource = getModelRepository(modelID).getByIdWithPlatformMappings(modelID);

    if (resource == null) {
      LOGGER.warn(String.format("Could not find model with ID [%s] in repository", modelID));
      throw new ModelNotFoundException("Model does not exist", null);
    }
    return ModelDtoFactory.createDto(resource);
  }

  @PreAuthorize("isAuthenticated() or hasRole('ROLE_USER')")
  @GetMapping("/{modelId:.+}/content")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public ModelContent getModelContent(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.modelRepositoryFactory);
    
    return converter.convert(modelID, Optional.empty());
  }

  @PreAuthorize("isAuthenticated() or hasRole('ROLE_USER')")
  @GetMapping("/{modelId:.+}/content/{targetplatformKey}")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public ModelContent getModelContentForTargetPlatform(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "The key of the targetplatform, e.g. lwm2m",
          required = true) final @PathVariable String targetplatformKey) {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.modelRepositoryFactory);

    return converter.convert(modelID, Optional.of(targetplatformKey));
  }

  @PreAuthorize("isAuthenticated() or hasRole('ROLE_USER')")
  @GetMapping("/{modelId:.+}/file")
  @CrossOrigin(origins = "https://www.eclipse.org")
  public void downloadModelById(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany:Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Set true if dependencies shall be included",
          required = false) final @RequestParam(value = "includeDependencies",
              required = false) boolean includeDependencies,
      final HttpServletResponse response) {

    Objects.requireNonNull(modelId, "modelId must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    LOGGER.info("Download of Model file : [" + modelID.toString() + "]");

    if (includeDependencies) {
      byte[] zipContent = createZipWithAllDependencies(modelID);
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
      throw new GenericApplicationException("Error while generating zip file.", ex);
    }
  }
}
