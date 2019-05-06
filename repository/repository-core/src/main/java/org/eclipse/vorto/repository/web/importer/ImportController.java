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
package org.eclipse.vorto.repository.web.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.IModelImportService;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.eclipse.vorto.repository.importer.UploadModelResult;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.eclipse.vorto.repository.web.importer.dto.ImporterInfo;
import org.eclipse.vorto.repository.web.importer.dto.UploadModelResponse;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController
@RequestMapping(value = "/rest/importers")
public class ImportController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private final String UPLOAD_VALID = "%s is valid and ready for import.";
  private final String UPLOAD_FAIL = "%s has errors. Cannot import.";
  private final String UPLOAD_WARNING = "Warning! You are about to overwrite an existing model!";

  @Autowired
  private IModelImportService importerService;

  @Value("${repo.config.maxModelSize}")
  private long maxModelSize;

  @Autowired
  private IWorkflowService workflowService;

  @RequestMapping(method = RequestMethod.POST)
  @PreAuthorize("hasRole('MODEL_CREATOR')")
  public ResponseEntity<UploadModelResponse> uploadModel(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The vorto model file to upload",
          required = true) @RequestParam("file") MultipartFile file,
      @RequestParam("key") String key) {
    if (file.getSize() > maxModelSize) {
      throw new UploadTooLargeException("model", maxModelSize);
    }

    LOGGER.info("uploadModel: [" + file.getOriginalFilename() + "]");
    try {
      IModelImporter importer = importerService.getImporterByKey(key).get();
      UploadModelResult result = importer.upload(
          FileUpload.create(file.getOriginalFilename(), file.getBytes()), getUserContext(tenantId));

      if (!result.isValid()) {
        return validResponse(new UploadModelResponse(
            String.format(UPLOAD_FAIL, file.getOriginalFilename()), result));
      } else {
        if (result.hasWarnings()) {
          return validResponse(new UploadModelResponse(
              String.format(UPLOAD_WARNING, file.getOriginalFilename()), result));
        } else {
          return validResponse(new UploadModelResponse(
              String.format(UPLOAD_VALID, file.getOriginalFilename()), result));
        }
      }

    } catch (IOException e) {
      LOGGER.error("Error upload model." + e.getStackTrace());
      UploadModelResponse errorResponse =
          new UploadModelResponse("Error during upload. Try again. " + e.getMessage(),
              new UploadModelResult(null, Collections.emptyList()));
      return new ResponseEntity<UploadModelResponse>(errorResponse,
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private UserContext getUserContext(String tenantId) {
    return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName(),
        tenantId);
  }

  @RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
  @PreAuthorize("hasRole('MODEL_CREATOR')")
  public ResponseEntity<List<ModelInfo>> doImport(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The file name of uploaded model",
          required = true) final @PathVariable String handleId,
      @RequestParam("key") String key) {
    LOGGER.info("Importing Model with handleID " + handleId);
    try {

      IModelImporter importer = importerService.getImporterByKey(key).get();

      IUserContext user = getUserContext(tenantId);
      List<ModelInfo> importedModels = importer.doImport(handleId, user);
      for (ModelInfo modelInfo : importedModels) {
        workflowService.start(modelInfo.getId(), user);
      }

      return new ResponseEntity<List<ModelInfo>>(importedModels, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Error Importing model. " + handleId, e);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @ApiOperation(value = "Returns a list of supported importers")
  @PreAuthorize("hasRole('MODEL_CREATOR')")
  @RequestMapping(method = RequestMethod.GET, produces = "application/json")
  public List<ImporterInfo> getImporters() {
    List<ImporterInfo> importers = new ArrayList<ImporterInfo>();
    this.importerService.getImporters().stream().forEach(importer -> {
      importers.add(new ImporterInfo(importer.getKey(), importer.getSupportedFileExtensions(),
          importer.getShortDescription()));
    });

    return importers;
  }

  private ResponseEntity<UploadModelResponse> validResponse(
      UploadModelResponse successModelResponse) {
    return new ResponseEntity<UploadModelResponse>(successModelResponse, HttpStatus.OK);
  }
}
