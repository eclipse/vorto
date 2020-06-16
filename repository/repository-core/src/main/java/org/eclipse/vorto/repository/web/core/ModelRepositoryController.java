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
package org.eclipse.vorto.repository.web.core;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.DefaultUserAccountService;
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
import org.eclipse.vorto.repository.core.PolicyEntry.Permission;
import org.eclipse.vorto.repository.core.PolicyEntry.PrincipalType;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.parser.ModelParserFactory;
import org.eclipse.vorto.repository.core.impl.utils.ModelValidationHelper;
import org.eclipse.vorto.repository.core.impl.validation.AttachmentValidator;
import org.eclipse.vorto.repository.core.impl.validation.ValidationException;
import org.eclipse.vorto.repository.domain.Namespace;
import org.eclipse.vorto.repository.domain.Tenant;
import org.eclipse.vorto.repository.domain.User;
import org.eclipse.vorto.repository.importer.ValidationReport;
import org.eclipse.vorto.repository.model.IBulkOperationsService;
import org.eclipse.vorto.repository.model.ModelNamespaceNotOfficialException;
import org.eclipse.vorto.repository.model.ModelNotReleasedException;
import org.eclipse.vorto.repository.services.NamespaceService;
import org.eclipse.vorto.repository.services.UserRepositoryRoleService;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.ControllerUtils;
import org.eclipse.vorto.repository.web.GenericApplicationException;
import org.eclipse.vorto.repository.web.Status;
import org.eclipse.vorto.repository.web.api.v1.dto.AttachResult;
import org.eclipse.vorto.repository.web.core.dto.ModelContent;
import org.eclipse.vorto.repository.web.core.exceptions.NotAuthorizedException;
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

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController("internal.modelRepositoryController2")
@RequestMapping(value = "/rest/models")
public class ModelRepositoryController extends AbstractRepositoryController {

  @Autowired
  private ITenantService tenantService;

  @Autowired
  private DefaultUserAccountService accountService;

  @Autowired
  private ModelParserFactory modelParserFactory;

  @Autowired
  private IWorkflowService workflowService;

  @Autowired
  private IBulkOperationsService modelService;

  @Autowired
  private ModelValidationHelper modelValidationHelper;

  @Autowired
  private AttachmentValidator attachmentValidator;

  @Autowired
  private NamespaceService namespaceService;

  @Autowired
  private UserRepositoryRoleService userRepositoryRoleService;

  private static final Logger LOGGER = Logger.getLogger(ModelRepositoryController.class);

  @ApiOperation(value = "Returns the image of a vorto model")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Wrong input"),
      @ApiResponse(code = 404, message = "Model not found")})
  @GetMapping("/{modelId:.+}/images")
  public void getModelImage(
      @ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "Response", required = true) final HttpServletResponse response) {
    Objects.requireNonNull(modelId, "modelId must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    IModelRepository modelRepo = getModelRepository(modelID);

    // first searches by "display image" tag
    List<Attachment> imageAttachments =
        modelRepo.getAttachmentsByTag(modelID, Attachment.TAG_DISPLAY_IMAGE);

    // if none present, searches just by "image" tag (for backwards compatibility)
    if (imageAttachments.isEmpty()) {
      imageAttachments =
          modelRepo.getAttachmentsByTag(modelID, Attachment.TAG_IMAGE);
    }

    // still nope
    if (imageAttachments.isEmpty()) {
      response.setStatus(404);
      return;
    }

    // fetches the first element: either it's the only one (if the display image tag is present)
    // or arbitrarily the first image found (for backwards compatibility)
    Optional<FileContent> imageContent =
        modelRepo.getAttachmentContent(modelID, imageAttachments.get(0).getFilename());

    if (!imageContent.isPresent()) {
      response.setStatus(404);
      return;
    }

    try {
      response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getName() + ".png");
      response.setContentType(APPLICATION_OCTET_STREAM);
      IOUtils.copy(new ByteArrayInputStream(imageContent.get().getContent()),
          response.getOutputStream());
      response.flushBuffer();
    } catch (IOException e) {
      throw new GenericApplicationException("Error copying file.", e);
    }
  }

  @PostMapping("/{modelId:.+}/images")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<AttachResult> uploadModelImage(
      @ApiParam(value = "The image to upload",
          required = true) @RequestParam("file") MultipartFile file,
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId) {

    LOGGER.info("uploadImage: [" + file.getOriginalFilename() + ", " + file.getSize() + "]");

    ModelId actualModelID = ModelId.fromPrettyFormat(modelId);

    if (!attachmentValidator.validateAttachmentSize(file.getSize())) {
      return new ResponseEntity<>(
          AttachResult.fail(
              actualModelID,
              file.getOriginalFilename(),
              String.format(
                  "The attachment is too large. Maximum size allowed is %dMB",
                  attachmentValidator.getMaxFileSizeSetting()
              )
          ),
          HttpStatus.PAYLOAD_TOO_LARGE
      );
    }

    try {
      IUserContext user = UserContext.user(SecurityContextHolder.getContext().getAuthentication(),
          getWorkspaceId(modelId));

      getModelRepository(actualModelID)
          .attachFile(actualModelID,
              new FileContent(
                  file.getOriginalFilename(), file.getBytes()
              ),
              user,
              Attachment.TAG_IMAGE,
              Attachment.TAG_DISPLAY_IMAGE
          );
    } catch (IOException e) {
      throw new GenericApplicationException("error in attaching file to model '" + modelId + "'",
          e);
    }
    return new ResponseEntity<>(AttachResult.success(actualModelID, file.getOriginalFilename()),
        HttpStatus.CREATED);
  }

  @ApiOperation("Saves a model to the repository.")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  @PutMapping(value = "/{modelId:.+}", produces = "application/json")
  public ResponseEntity<ValidationReport> saveModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @RequestBody ModelContent content) {

    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      IModelRepository modelRepository = getModelRepository(ModelId.fromPrettyFormat(modelId));
      ModelId modelID = ModelId.fromPrettyFormat(modelId);

      if (modelRepository.getById(modelID) == null) {
        return new ResponseEntity<>(ValidationReport.invalid(null, "Model was not found"),
            HttpStatus.NOT_FOUND);
      }

      IUserContext userContext = UserContext.user(authentication, getWorkspaceId(modelId));

      ModelResource modelInfo = (ModelResource) modelParserFactory
          .getParser("model" + ModelType.valueOf(content.getType()).getExtension())
          .parse(new ByteArrayInputStream(content.getContentDsl().getBytes()));

      if (!modelID.equals(modelInfo.getId())) {
        return new ResponseEntity<>(ValidationReport.invalid(modelInfo,
            "You may not change the model ID (name, namespace, version). For this please create a new model."),
            HttpStatus.BAD_REQUEST);
      }

      ValidationReport validationReport = modelValidationHelper
          .validateModelUpdate(modelInfo, userContext);
      if (validationReport.isValid()) {
        modelRepository.save(modelInfo.getId(), content.getContentDsl().getBytes(),
            modelInfo.getId().getName() + modelInfo.getType().getExtension(), userContext);
      }
      return new ResponseEntity<>(validationReport, HttpStatus.OK);
    } catch (ValidationException validationException) {
      LOGGER.warn(validationException);
      return new ResponseEntity<>(ValidationReport.invalid(null, validationException),
          HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/refactorings/{oldId:.+}/{newId:.+}", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#oldId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<ModelInfo> refactorModelId(@PathVariable String oldId,
      @PathVariable String newId) {
    final ModelId oldModelId = ModelId.fromPrettyFormat(oldId);
    final ModelId newModelId = ModelId.fromPrettyFormat(newId);

    IUserContext userContext = UserContext
        .user(SecurityContextHolder.getContext().getAuthentication(), getWorkspaceId(oldId));

    ModelInfo result = this.modelRepositoryFactory.getRepositoryByModel(oldModelId)
        .rename(oldModelId, newModelId, userContext);

    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping(value = "/refactorings/{modelId:.+}", produces = "application/json")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
  public ResponseEntity<Map<String, String>> newRefactoring(@PathVariable String modelId) {
    Tenant tenant = this.tenantService.getTenant(
        this.modelRepositoryFactory.getRepositoryByModel(ModelId.fromPrettyFormat(modelId))
            .getWorkspaceId()).get();
    Map<String, String> response = new HashMap<>();
    response.put("namespace", tenant.getDefaultNamespace());
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @ApiOperation(value = "Creates a model in the repository with the given model ID and model type.")
  @PostMapping(value = "/{modelId:.+}/{modelType}", produces = "application/json")
  public ResponseEntity<ModelInfo> createModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelType", required = true) @PathVariable ModelType modelType,
      @RequestBody(required = false) List<ModelProperty> properties) throws WorkflowException {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    IUserContext userContext = UserContext.user(authentication, getWorkspaceId(modelId));
    IModelRepository modelRepo = getModelRepository(modelID);

    if (modelRepo.exists(modelID)) {
      throw new ModelAlreadyExistsException();
    } else {
      String modelTemplate = null;
      if (modelType == ModelType.InformationModel && properties != null) {
        modelTemplate = new InfomodelTemplate().createModelTemplate(modelID, properties);
      } else {
        modelTemplate = new ModelTemplate().createModelTemplate(modelID, modelType);
      }

      ModelInfo savedModel = modelRepo.save(modelID, modelTemplate.getBytes(),
          modelID.getName() + modelType.getExtension(), userContext);
      this.workflowService.start(modelID, userContext);
      return new ResponseEntity<>(savedModel, HttpStatus.CREATED);
    }
  }

  @ApiOperation(value = "Creates a new version for the given model in the specified version")
  @PreAuthorize("hasAuthority('sysadmin') or (hasAuthority('model_creator') and "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).READ))")
  @PostMapping(value = "/{modelId:.+}/versions/{modelVersion:.+}", produces = "application/json")
  public ResponseEntity<ModelInfo> createVersionOfModel(
      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
      @ApiParam(value = "modelVersion", required = true) @PathVariable String modelVersion)
      throws WorkflowException {

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    try {
      IUserContext userContext = UserContext.user(authentication, getWorkspaceId(modelId));
      ModelResource resource = getModelRepository(modelID)
          .createVersion(modelID, modelVersion, userContext);
      this.workflowService.start(resource.getId(), userContext);
      return new ResponseEntity<>(resource, HttpStatus.CREATED);
    } catch (FatalModelRepositoryException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping(value = "/{modelId:.+}")
  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  public ResponseEntity<Boolean> deleteModelResource(final @PathVariable String modelId) {

    Objects.requireNonNull(modelId, "modelId must not be null");

    try {
      getModelRepository(ModelId.fromPrettyFormat(modelId))
          .removeModel(ModelId.fromPrettyFormat(modelId));
      return new ResponseEntity<>(false, HttpStatus.OK);
    } catch (FatalModelRepositoryException | NullPointerException e) {
      LOGGER.error(e);
      return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
    }
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

    LOGGER.info("Exporting information models for user - results: "
        + userModels.size());

    sendAsZipFile(response, user.getUsername() + "-models.zip",
        getModelsAndDependencies(userModels));
  }

  @ApiOperation(value = "Getting all mapping resources for the given model")
  @PreAuthorize("isAuthenticated() or hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId), 'model:get')")
  @GetMapping("/{modelId:.+}/download/mappings/{targetPlatform}")
  public ResponseEntity<Boolean> downloadMappingsForPlatform(
      @ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0",
          required = true) final @PathVariable String modelId,
      @ApiParam(value = "The name of target platform, e.g. lwm2m",
          required = true) final @PathVariable String targetPlatform,
      final HttpServletResponse response) {

    Objects.requireNonNull(modelId, "model ID must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    try {
      IModelRepository modelRepository = getModelRepository(modelID);
      if (modelRepository.getById(modelID) == null) {
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
      }

      List<ModelInfo> mappingResources = modelRepository.getMappingModelsForTargetPlatform(modelID,
          ControllerUtils.sanitize(targetPlatform), Optional.empty());

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

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{modelId:.+}/diagnostics")
  public ResponseEntity<Collection<Diagnostic>> runDiagnostics(final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "model ID must not be null");

    try {
      return new ResponseEntity<>(
          getDiagnosticService(getWorkspaceId(modelId))
              .diagnoseModel(ModelId.fromPrettyFormat(modelId)),
          HttpStatus.OK);
    } catch (FatalModelRepositoryException ex) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @PreAuthorize("isAuthenticated() or hasAuthority('model_viewer')")
  @GetMapping("/{modelId:.+}/policies")
  public ResponseEntity<Collection<PolicyEntry>> getPolicies(final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "model ID must not be null");
    try {
      ModelId modelID = ModelId.fromPrettyFormat(modelId);
      String workspaceId = getWorkspaceId(modelId);
      Authentication user = SecurityContextHolder.getContext().getAuthentication();
      return new ResponseEntity<>(
          getPolicyManager(workspaceId).getPolicyEntries(modelID)
              .stream()
              .filter(userHasPolicyEntry(user, workspaceId))
              .collect(Collectors.toList()),
          HttpStatus.OK);
    } catch (FatalModelRepositoryException ex) {
      LOGGER.error(ex);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (NotAuthorizedException ex) {
      LOGGER.warn(ex);
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PreAuthorize("isAuthenticated() or hasAuthority('model_viewer')")
  @GetMapping("/{modelId:.+}/policy")
  public ResponseEntity<PolicyEntry> getUserPolicy(final @PathVariable String modelId) {
    Objects.requireNonNull(modelId, "model ID must not be null");

    Authentication user = SecurityContextHolder.getContext().getAuthentication();
    ModelId modelID = ModelId.fromPrettyFormat(modelId);
    String tenantId = getWorkspaceId(modelId);

    try {
      List<PolicyEntry> policyEntries =
          getPolicyManager(tenantId).getPolicyEntries(modelID).stream()
              .filter(userHasPolicyEntry(user, tenantId)).collect(Collectors.toList());

      return getBestPolicyEntryForUser(policyEntries)
          .map(p -> new ResponseEntity<>(p, HttpStatus.OK))
          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    } catch (NotAuthorizedException ex) {
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
  }

  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @PutMapping(value = "/{modelId:.+}/policies")
  public void addOrUpdatePolicyEntry(final @PathVariable String modelId,
      final @RequestBody PolicyEntry entry) {

    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(entry, "entry must not be null");

    if (attemptChangePolicyOfCurrentUser(entry)) {
      throw new IllegalArgumentException("Cannot change policy of current user");
    } else if (!entry.getPrincipalId().equals(IModelPolicyManager.ANONYMOUS_ACCESS_POLICY)
        && !this.accountService.exists(entry.getPrincipalId())) {
      throw new IllegalArgumentException("User is not a registered Vorto user");
    }

    getPolicyManager(getWorkspaceId(modelId))
        .addPolicyEntry(ModelId.fromPrettyFormat(modelId), entry);
  }

  @PreAuthorize("hasAuthority('sysadmin') or "
      + "hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),"
      + "T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
  @DeleteMapping(value = "/{modelId:.+}/policies/{principalId:.+}/{principalType:.+}")
  public void removePolicyEntry(final @PathVariable String modelId,
      final @PathVariable String principalId, final @PathVariable String principalType) {

    Objects.requireNonNull(modelId, "modelID must not be null");
    Objects.requireNonNull(principalId, "principalID must not be null");
    final PolicyEntry entry =
        PolicyEntry.of(principalId, PrincipalType.valueOf(principalType), null);

    if (attemptChangePolicyOfCurrentUser(entry)) {
      throw new IllegalArgumentException("Cannot change policy of current user");
    }
    getPolicyManager(getWorkspaceId(modelId)).removePolicyEntry(ModelId.fromPrettyFormat(modelId),
        entry);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping(value = "/{modelId:.+}/makePublic", produces = "application/json")
  public ResponseEntity<Status> makeModelPublic(final @PathVariable String modelId) {

    IUserContext user =
        UserContext.user(SecurityContextHolder.getContext().getAuthentication(),
            getWorkspaceId(ControllerUtils.sanitize(modelId)));

    if (!userRepositoryRoleService.isSysadmin(user.getUsername()) &&
        !accountService
            .hasRole(user.getWorkspaceId(), user.getAuthentication(), "model_publisher")) {
      return new ResponseEntity<>(
          Status.fail("Only users with Publisher roles are allowed to make models public"),
          HttpStatus.UNAUTHORIZED);
    }

    ModelId modelID = ModelId.fromPrettyFormat(modelId);

    if (modelID.getNamespace().startsWith(Namespace.PRIVATE_NAMESPACE_PREFIX)) {
      return new ResponseEntity<>(
          Status.fail("Only models with official namespace can be made public"),
          HttpStatus.FORBIDDEN);
    }

    try {
      LOGGER.info("Making the model '" + ControllerUtils.sanitize(modelId) + "' public.");
      modelService.makeModelPublic(ModelId.fromPrettyFormat(ControllerUtils.sanitize(modelId)));
    } catch (ModelNotReleasedException | ModelNamespaceNotOfficialException e) {
      return new ResponseEntity<>(Status.fail(e.getMessage()), HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(Status.success(), HttpStatus.OK);
  }

  private String getWorkspaceId(String modelId) {
    String namespace = ModelId.fromPrettyFormat(modelId).getNamespace();
    return namespaceService.resolveWorkspaceIdForNamespace(namespace)
        .orElseThrow(() -> new FatalModelRepositoryException(
            "Namespace " + namespace + " could not be found.", null));
  }

  private IDiagnostics getDiagnosticService(final String tenantId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getModelRepositoryFactory().getDiagnosticsService(tenantId, authentication);
  }

  private IModelPolicyManager getPolicyManager(final String tenantId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return getModelRepositoryFactory().getPolicyManager(tenantId, authentication);
  }

  private Predicate<PolicyEntry> userHasPolicyEntry(Authentication user, String tenantId) {
    return p -> (p.getPrincipalType() == PrincipalType.User
        && p.getPrincipalId().equals(user.getName()))
        || (p.getPrincipalType() == PrincipalType.Role
        && accountService.hasRole(tenantId, user, p.getPrincipalId()));
  }

  private Optional<PolicyEntry> getBestPolicyEntryForUser(List<PolicyEntry> policyEntries) {
    Optional<PolicyEntry> full =
        policyEntries.stream().filter(p -> p.getPermission() == Permission.FULL_ACCESS).findFirst();
    if (full.isPresent()) {
      return full;
    }

    Optional<PolicyEntry> modify =
        policyEntries.stream().filter(p -> p.getPermission() == Permission.MODIFY).findFirst();
    if (modify.isPresent()) {
      return modify;
    }

    Optional<PolicyEntry> read =
        policyEntries.stream().filter(p -> p.getPermission() == Permission.READ).findFirst();
    if (read.isPresent()) {
      return read;
    }

    return Optional.empty();
  }

  private boolean attemptChangePolicyOfCurrentUser(PolicyEntry entry) {
    return SecurityContextHolder.getContext().getAuthentication().getName()
        .equals(entry.getPrincipalId());
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

  private IModelRepository getModelRepository(String tenantId) {
    return this.modelRepositoryFactory.getRepository(tenantId);
  }

  private Map<ModelInfo, FileContent> getModelsAndDependencies(Collection<ModelId> modelIds) {
    Map<ModelInfo, FileContent> modelsMap = new HashMap<>();
    if (modelIds != null && !modelIds.isEmpty()) {
      for (ModelId modelId : modelIds) {
        IModelRepository modelRepo = getModelRepository(modelId);
        ModelInfo modelInfo = modelRepo.getById(modelId);
        Optional<FileContent> modelContent = modelRepo.getFileContent(modelId, Optional.empty());
        if (modelContent.isPresent()) {
          modelsMap.put(modelInfo, modelContent.get());
          modelsMap.putAll(getModelsAndDependencies(modelInfo.getReferences()));
        }
      }
    }

    return modelsMap;
  }
}
