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
package org.eclipse.vorto.repository.web.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.domain.Role;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.core.dto.ModelContent;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController("internal.modelRepositoryController2")
@RequestMapping(value = "/rest/models")
public class ModelRepositoryController2 extends AbstractRepositoryController {

  @Autowired
  private ITenantService tenantService;

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private ModelRepositoryController modelRepositoryController;

  private static Logger logger = Logger.getLogger(ModelRepositoryController2.class);

  @ApiOperation(value = "Returns the image of a vorto model")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model not found")})
  @GetMapping(value = "/{modelId:.+}/images")
  public void getModelImage(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Response", required = true) final HttpServletResponse response) {

    modelRepositoryController.getModelImage(getTenant(modelId), modelId, response);
  }

  @PostMapping(value = "/{modelId:.+}/images")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<Boolean> uploadModelImage(
      @ApiParam(value = "The image to upload",
          required = true) @RequestParam("file") MultipartFile file,
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId) {

    logger.info("uploadImage: [" + file.getOriginalFilename() + ", " + file.getSize() + "]");

    return modelRepositoryController.uploadModelImage(getTenant(modelId), file, modelId);
  }

  // ToDo add Getter method
  @ApiOperation(value = "Saves a model to the repository.")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  @PutMapping(value = "/{modelId:.+}", produces = "application/json")
  public ResponseEntity<ValidationReport> saveModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @RequestBody ModelContent content) {

    return modelRepositoryController.saveModel(getTenant(modelId), modelId, content);
  }

  @ApiOperation(value = "Creates a model in the repository with the given model ID and model type.")
  @PostMapping(value = "/{modelId:.+}/{modelType}", produces = "application/json")
  public ResponseEntity<ModelInfo> createModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelType", required = true) @PathVariable ModelType modelType,
      @RequestBody(required = false) List<ModelProperty> properties) throws WorkflowException {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    final String tenantId = getTenant(modelID).orElseThrow(
        () -> new ModelNotFoundException("The tenant for '" + modelId + "' could not be found."));

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!accountService.hasRole(tenantId, authentication,
        Role.rolePrefix + Role.MODEL_CREATOR.name())) {
      throw new NotAuthorizedException(modelID);
    }

    return modelRepositoryController.createModel(tenantId, modelId, modelType, properties);
  }

  @ApiOperation(value = "Creates a new version for the given model in the specified version")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or (hasRole('ROLE_MODEL_CREATOR') and "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).READ))")
  @PostMapping(value = "/{modelId:.+}/versions/{modelVersion:.+}", produces = "application/json")
  public ResponseEntity<ModelInfo> createVersionOfModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelVersion", required = true) @PathVariable String modelVersion)
      throws WorkflowException, IOException {

    return modelRepositoryController.createVersionOfModel(getTenant(modelId), modelId, modelVersion);
  }

  @DeleteMapping(value = "/{modelId:.+}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  public ResponseEntity<Boolean> deleteModelResource(final @PathVariable String modelId) {

    return modelRepositoryController.deleteModelResource(getTenant(modelId), modelId);
  }


  // ##################### Downloads ################################
  @GetMapping(value = {"/mine/download"})
  public void getUserModels(Principal principal, final HttpServletResponse response) {
    List<ModelId> userModels = Lists.newArrayList();

    User user = accountService.getUser(principal.getName());
    Set<Tenant> userTenants = user.getTenants();

    for (Tenant tenant : userTenants) {
      IModelRepository modelRepo = getModelRepository(tenant.getTenantId());
      List<ModelInfo> modelInfos = modelRepo.search("author:" + user.getUsername());
      List<ModelId> modelIds =
          modelInfos.stream().map(modelInfo -> modelInfo.getId()).collect(Collectors.toList());
      userModels.addAll(modelIds);
    }

    logger.info("Exporting information models for " + user.getUsername() + " results: "
        + userModels.size());

    sendAsZipFile(response, user.getUsername() + "-models.zip",
        getModelsAndDependencies(userModels));
  }

  private Map<ModelInfo, FileContent> getModelsAndDependencies(Collection<ModelId> modelIds) {
    Map<ModelInfo, FileContent> modelsMap = new HashMap<>();

    if (modelIds != null && !modelIds.isEmpty()) {
      for (ModelId modelId : modelIds) {
        Optional<Tenant> tenant = tenantService.getTenantFromNamespace(modelId.getNamespace());
        if (tenant.isPresent()) {
          IModelRepository modelRepo = getModelRepository(tenant.get().getTenantId());
          ModelInfo modelInfo = modelRepo.getById(modelId);
          Optional<FileContent> modelContent = modelRepo.getFileContent(modelId, Optional.empty());
          if (modelContent.isPresent()) {
            modelsMap.put(modelInfo, modelContent.get());
            modelsMap.putAll(getModelsAndDependencies(modelInfo.getReferences()));
          }
        }
      }
    }

    return modelsMap;
  }

  private void sendAsZipFile(final HttpServletResponse response, final String fileName,
      Map<ModelInfo, FileContent> models) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ZipOutputStream zos = new ZipOutputStream(baos);

    try {
      for (Map.Entry<ModelInfo, FileContent> model : models.entrySet()) {
        ModelInfo modelResource = model.getKey();
        ZipEntry zipEntry = new ZipEntry(
            modelResource.getId().getPrettyFormat() + modelResource.getType().getExtension());
        zos.putNextEntry(zipEntry);
        zos.write(model.getValue().getContent());
        zos.closeEntry();
      }

      zos.close();
      baos.close();
    } catch (Exception ex) {
      throw new GenericApplicationException("error in creating zip file.", ex);
    }

    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      IOUtils.copy(new ByteArrayInputStream(baos.toByteArray()), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new GenericApplicationException("Error copying file.", e);
    }
  }

  @ApiOperation(value = "Getting all mapping resources for the given model")
  @PreAuthorize("hasRole('ROLE_USER') or hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId), 'model:get')")
  @GetMapping(value = "/{modelId:.+}/download/mappings/{targetPlatform}")
  public ResponseEntity<Boolean> downloadMappingsForPlatform(
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "The name of target platform, e.g. lwm2m",
          required = true) final @PathVariable String targetPlatform,
      final HttpServletResponse response) {

    Objects.requireNonNull(modelId, "model ID must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    final String tenantId = getTenant(modelID).orElseThrow(
        () -> new ModelNotFoundException("The tenant for '" + modelId + "' could not be found."));
    
    try {
      
      IModelRepository modelRepository = getModelRepository(tenantId);

      if (modelRepository.getById(modelID) == null) {
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
      }

      List<ModelInfo> mappingResources = modelRepository.getMappingModelsForTargetPlatform(modelID,
          ControllerUtils.sanitize(targetPlatform));

      List<ModelId> mappingModelIds =
          mappingResources.stream().map(ModelInfo::getId).collect(Collectors.toList());

      final String fileName =
          modelID.getNamespace() + "_" + modelID.getName() + "_" + modelID.getVersion() + ".zip";

      sendAsZipFile(response, fileName, getModelsAndDependencies(mappingModelIds));
      return new ResponseEntity<>(true, HttpStatus.OK);

    } catch (FatalModelRepositoryException ex) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping(value = "/{modelId:.+}/diagnostics")
  public ResponseEntity<Collection<Diagnostic>> runDiagnostics(final @PathVariable String modelId) {

    return modelRepositoryController.runDiagnostics(getTenant(modelId), modelId);
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @GetMapping(value = "/{modelId:.+}/policies")
  public ResponseEntity<Collection<PolicyEntry>> getPolicies(final @PathVariable String modelId) {

    return modelRepositoryController.getPolicies(getTenant(modelId), modelId);
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping(value = "/{modelId:.+}/policy")
  public ResponseEntity<PolicyEntry> getUserPolicy(final @PathVariable String modelId) {

    return modelRepositoryController.getUserPolicy(getTenant(modelId), modelId);
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @PutMapping(value = "/{modelId:.+}/policies")
  public void addOrUpdatePolicyEntry(final @PathVariable String modelId,
      final @RequestBody PolicyEntry entry) {

    modelRepositoryController.addOrUpdatePolicyEntry(getTenant(modelId), modelId, entry);
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @DeleteMapping(value = "/{modelId:.+}/policies/{principalId:.+}/{principalType:.+}")
  public void removePolicyEntry(final @PathVariable String modelId,
      final @PathVariable String principalId, final @PathVariable String principalType) {
    
    modelRepositoryController.removePolicyEntry(getTenant(modelId), modelId, principalId, principalType);
  }
  
  private String getTenant(String modelId) {
    return getTenant(ModelId.fromPrettyFormat(modelId)).orElseThrow(
        () -> new ModelNotFoundException("The tenant for '" + modelId + "' could not be found."));
  }

  private Optional<String> getTenant(ModelId modelId) {
    return tenantService.getTenantFromNamespace(modelId.getNamespace())
        .map(tenant -> tenant.getTenantId());
  }
}
