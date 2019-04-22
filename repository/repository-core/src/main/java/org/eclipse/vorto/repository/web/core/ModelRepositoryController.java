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
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.IUserAccountService;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelResource;
import org.eclipse.vorto.repository.core.PolicyEntry;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.dto.ModelContent;
import org.eclipse.vorto.repository.web.core.templates.InfomodelTemplate;
import org.eclipse.vorto.repository.web.core.templates.ModelTemplate;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

// TODO: Most of the methods here are doing something which are supposed to be relegated
// to a service. MUST REFACTOR!!!!!!!!!!!!!!
/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController("internal.modelRepositoryController")
@RequestMapping(value = "/rest/tenants/{tenantId}/models")
public class ModelRepositoryController extends AbstractRepositoryController {

  @Autowired
  private IUserAccountService accountService;

  @Autowired
  private IWorkflowService workflowService;

  @Autowired
  private ModelParserFactory modelParserFactory;

  private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

  @ApiOperation(value = "Returns the image of a vorto model")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model not found")})
  @GetMapping(value = "/{modelId:.+}/images")
  public void getModelImage(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Response", required = true) final HttpServletResponse response) {
    Objects.requireNonNull(modelId, "modelId must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    List<Attachment> imageAttachments =
        getModelRepository(tenantId).getAttachmentsByTag(modelID, Attachment.TAG_IMAGE);
    if (imageAttachments.isEmpty()) {
      response.setStatus(404);
      return;
    }
    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getName() + ".png");
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      FileContent imageContent =
          getModelRepository(tenantId, SecurityContextHolder.getContext().getAuthentication())
              .getAttachmentContent(modelID, imageAttachments.get(0).getFilename()).get();
      IOUtils.copy(new ByteArrayInputStream(imageContent.getContent()), response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  @PostMapping(value = "/{modelId:.+}/images")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') || "
      + "@modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<Boolean> uploadModelImage(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The image to upload",
          required = true) @RequestParam("file") MultipartFile file,
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId) {

    logger.info("uploadImage: [" + file.getOriginalFilename() + ", " + file.getSize() + "]");

    try {
      IUserContext user =
          UserContext.user(SecurityContextHolder.getContext().getAuthentication(), tenantId);

      getModelRepository(tenantId, SecurityContextHolder.getContext().getAuthentication())
          .attachFile(ModelId.fromPrettyFormat(modelId),
              new FileContent(file.getOriginalFilename(), file.getBytes()), user,
              Attachment.TAG_IMAGE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ResponseEntity<>(false, HttpStatus.CREATED);
  }

  // ToDo add Getter method
  @ApiOperation(value = "Saves a model to the repository.")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "@modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  @PutMapping(value = "/{modelId:.+}", produces = "application/json")
  public ResponseEntity<ValidationReport> saveModel(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @RequestBody ModelContent content) {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      IModelRepository modelRepository = getModelRepository(tenantId, authentication);

      ModelId modelID = ModelId.fromPrettyFormat(modelId);
      if (modelRepository.getById(modelID) == null) {
        return new ResponseEntity<>(ValidationReport.invalid(null, "Model was not found"),
            HttpStatus.NOT_FOUND);
      }

      IUserContext userContext = UserContext.user(authentication, tenantId);

      ModelResource modelInfo = (ModelResource) modelParserFactory
          .getParser("model" + ModelType.valueOf(content.getType()).getExtension())
          .parse(new ByteArrayInputStream(content.getContentDsl().getBytes()));

      if (!modelID.equals(modelInfo.getId())) {
        return new ResponseEntity<>(ValidationReport.invalid(modelInfo,
            "You may not change the model ID (name, namespace, version). For this please create a new model."),
            HttpStatus.BAD_REQUEST);
      }

      ModelValidationHelper validationHelper =
          new ModelValidationHelper(getModelRepositoryFactory(), this.accountService);
      ValidationReport validationReport = validationHelper.validate(modelInfo, userContext);
      if (validationReport.isValid()) {
        modelRepository.save(modelInfo.getId(), content.getContentDsl().getBytes(),
            modelInfo.getId().getName() + modelInfo.getType().getExtension(), userContext);
      }
      return new ResponseEntity<>(validationReport, HttpStatus.OK);
    } catch (ValidationException validationException) {
      logger.error(validationException);
      return new ResponseEntity<>(ValidationReport.invalid(null, validationException),
          HttpStatus.BAD_REQUEST);
    }

  }

  @ApiOperation(value = "Creates a model in the repository with the given model ID and model type.")
  @PreAuthorize("@userAccountService.hasRole(#tenantId, authentication, 'ROLE_MODEL_CREATOR')")
  @PostMapping(value = "/{modelId:.+}/{modelType}", produces = "application/json")
  public ResponseEntity<ModelInfo> createModel(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelType", required = true) @PathVariable ModelType modelType,
      @RequestBody(required = false) List<ModelProperty> properties) throws WorkflowException {
    // Todo Add Validation to this

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    if (getModelRepository(tenantId).exists(modelID)) {
      throw new ModelAlreadyExistsException();
    } else {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      IUserContext userContext = UserContext.user(authentication, tenantId);

      String modelTemplate = null;

      if (modelType == ModelType.InformationModel && properties != null) {
        modelTemplate = new InfomodelTemplate().createModelTemplate(modelID, properties);
      } else {
        modelTemplate = new ModelTemplate().createModelTemplate(modelID, modelType);
      }

      ModelInfo savedModel = getModelRepository(tenantId, authentication).save(modelID,
          modelTemplate.getBytes(), modelID.getName() + modelType.getExtension(), userContext);
      this.workflowService.start(modelID, userContext);
      return new ResponseEntity<>(savedModel, HttpStatus.CREATED);

    }
  }

  @ApiOperation(value = "Creates a new version for the given model in the specified version")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or (hasRole('ROLE_MODEL_CREATOR') and "
      + "@modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).READ))")
  @PostMapping(value = "/{modelId:.+}/versions/{modelVersion:.+}", produces = "application/json")
  public ResponseEntity<ModelInfo> createVersionOfModel(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelVersion", required = true) @PathVariable String modelVersion)
      throws WorkflowException, IOException {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    IUserContext userContext = UserContext.user(authentication, tenantId);

    ModelResource resource = getModelRepository(tenantId, authentication).createVersion(modelID,
        modelVersion, userContext);
    this.workflowService.start(resource.getId(), userContext);
    return new ResponseEntity<>(resource, HttpStatus.CREATED);

  }

  @DeleteMapping(value = "/{modelId:.+}")
  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or @modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  public ResponseEntity<Boolean> deleteModelResource(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "modelId must not be null");
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      getModelRepository(tenantId, authentication).removeModel(ModelId.fromPrettyFormat(modelId));
      return new ResponseEntity<>(false, HttpStatus.OK);
    } catch (FatalModelRepositoryException | NullPointerException nullPointerException) {
      logger.error(nullPointerException);
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }


  // ##################### Downloads ################################

  // TODO : this is probably wrong as this only downloads from one tenant

  @GetMapping(value = {"/mine/download"})
  public void getUserModels(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      Principal user, final HttpServletResponse response) {
    // TODO : Checking for hashedUsername is legacy and needs to be removed once full migration has
    // taken place
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    List<ModelInfo> userModels = getModelRepository(tenantId, authentication)
        .search("author:" + UserContext.user(user.getName(), tenantId).getHashedUsername());

    userModels.addAll(getModelRepository(tenantId, authentication)
        .search("author:" + UserContext.user(user.getName(), tenantId).getUsername()));
    // TODO: end

    logger.info(
        "Exporting information models for " + user.getName() + " results: " + userModels.size());

    sendAsZipFile(response, tenantId, user.getName() + "-models.zip", userModels);
  }

  @ApiOperation(value = "Getting all mapping resources for the given model")
  @PreAuthorize("hasRole('ROLE_USER') or hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId), 'model:get')")
  @GetMapping(value = "/{modelId:.+}/download/mappings/{targetPlatform}")
  public ResponseEntity<Boolean> downloadMappingsForPlatform(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "The name of target platform, e.g. lwm2m",
          required = true) final @PathVariable String targetPlatform,
      final HttpServletResponse response) {

    try {
      Objects.requireNonNull(modelId, "model ID must not be null");

      final ModelId modelID = ModelId.fromPrettyFormat(modelId);

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      IModelRepository modelRepository = getModelRepository(tenantId, authentication);

      if (modelRepository.getById(modelID) == null) {
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
      }

      List<ModelInfo> mappingResources =
          modelRepository.getMappingModelsForTargetPlatform(modelID, targetPlatform);

      final String fileName =
          modelID.getNamespace() + "_" + modelID.getName() + "_" + modelID.getVersion() + ".zip";

      sendAsZipFile(response, tenantId, fileName, mappingResources);
      return new ResponseEntity<>(true, HttpStatus.OK);

    } catch (FatalModelRepositoryException ex) {
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping(value = "/{modelId:.+}/diagnostics")
  public ResponseEntity<Collection<Diagnostic>> runDiagnostics(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "model ID must not be null");
    try {
      return new ResponseEntity<>(
          getDiagnosticService(tenantId).diagnoseModel(ModelId.fromPrettyFormat(modelId)),
          HttpStatus.OK);
    } catch (FatalModelRepositoryException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or "
      + "@modelRepositoryFactory.getPolicyManager(#tenantId, authentication).hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @GetMapping(value = "/{modelId:.+}/policies")
  public ResponseEntity<Collection<PolicyEntry>> getPolicies(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId) {

    Objects.requireNonNull(modelId, "model ID must not be null");
    try {
      return new ResponseEntity<>(
          getPolicyManager(tenantId).getPolicyEntries(ModelId.fromPrettyFormat(modelId)),
          HttpStatus.OK);
    } catch (FatalModelRepositoryException ex) {
      ex.printStackTrace();
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping(value = "/{modelId:.+}/policy")
  public ResponseEntity<PolicyEntry> getUserPolicy(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId, Principal user) {
    Objects.requireNonNull(modelId, "model ID must not be null");
    try {

      return new ResponseEntity<>(
          getPolicyManager(tenantId).getPolicyEntries(ModelId.fromPrettyFormat(modelId)).stream()
              .filter(p -> p.getPrincipalType() == PrincipalType.User
                  && p.getPrincipalId().equals(user.getName()))
              .findFirst().get(),
          HttpStatus.OK);
    } catch (NoSuchElementException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or @modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @PutMapping(value = "/{modelId:.+}/policies")
  public void addOrUpdatePolicyEntry(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId, final @RequestBody PolicyEntry entry) {
    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(entry, "entry must not be null");

    if (attemptChangePolicyOfCurrentUser(entry)) {
      throw new IllegalArgumentException("Cannot change policy of current user");
    } else if (!this.accountService.exists(entry.getPrincipalId())) {
      throw new IllegalArgumentException("User is not a registered Vorto user");
    }

    getPolicyManager(tenantId).addPolicyEntry(ModelId.fromPrettyFormat(modelId), entry);
  }

  private boolean attemptChangePolicyOfCurrentUser(PolicyEntry entry) {
    return SecurityContextHolder.getContext().getAuthentication().getName()
        .equals(entry.getPrincipalId());
  }

  @PreAuthorize("hasRole('ROLE_SYS_ADMIN') or @modelRepositoryFactory.getPolicyManager(#tenantId, authentication)"
      + ".hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @DeleteMapping(value = "/{modelId:.+}/policies/{principalId:.+}/{principalType:.+}")
  public void removePolicyEntry(
      @ApiParam(value = "The id of the tenant",
          required = true) final @PathVariable String tenantId,
      final @PathVariable String modelId, final @PathVariable String principalId,
      final @PathVariable String principalType) {
    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(principalId, "principalID must not be null");
    final PolicyEntry entry =
        PolicyEntry.of(principalId, PrincipalType.valueOf(principalType), null);

    if (attemptChangePolicyOfCurrentUser(entry)) {
      throw new IllegalArgumentException("Cannot change policy of current user");
    }
    getPolicyManager(tenantId).removePolicyEntry(ModelId.fromPrettyFormat(modelId), entry);
  }

  private IModelPolicyManager getPolicyManager(final String tenantId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    IModelPolicyManager policyManager =
        getModelRepositoryFactory().getPolicyManager(tenantId, authentication);
    return policyManager;
  }

  private IDiagnostics getDiagnosticService(final String tenantId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    return getModelRepositoryFactory().getDiagnosticsService(tenantId, authentication);
  }
}
