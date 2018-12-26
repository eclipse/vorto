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
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.ModelType;
import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.core.Attachment;
import org.eclipse.vorto.repository.core.Diagnostic;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IDiagnostics;
import org.eclipse.vorto.repository.core.IModelPolicyManager;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@RestController("internal.modelRepositoryController")
@RequestMapping(value = "/rest/{tenant}/models")
public class ModelRepositoryController extends AbstractRepositoryController  {
	
	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IWorkflowService workflowService;
	
	@Autowired
	private ModelParserFactory modelParserFactory;
	
	@Autowired
	private IDiagnostics diagnosticsService;
	
	@Autowired
	private IModelPolicyManager policyManager;

	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Returns the image of a vorto model")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@RequestMapping(value = "/{modelId:.+}/images", method = RequestMethod.GET)
	public void getModelImage(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId,
			@ApiParam(value = "Response", required = true) final HttpServletResponse response) {
		Objects.requireNonNull(modelId, "modelId must not be null");

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		List<Attachment> imageAttachments = modelRepository.getAttachmentsByTag(modelID, Attachment.TAG_IMAGE);
		if (imageAttachments.isEmpty()) {
			response.setStatus(404);
			return;
		}
		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getName() + ".png");
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {
			FileContent imageContent = modelRepository.getAttachmentContent(modelID, imageAttachments.get(0).getFilename()).get();
			IOUtils.copy(new ByteArrayInputStream(imageContent.getContent()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}
	
	@RequestMapping(value = "/{modelId:.+}/images", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_ADMIN') || @modelRepository.hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
	public void uploadModelImage(	@ApiParam(value = "The image to upload", required = true)	@RequestParam("file") MultipartFile file,
									@ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId) {
		
		logger.info("uploadImage: [" + file.getOriginalFilename() + ", " + file.getSize() + "]");
		
		try {
			IUserContext user = UserContext
					.user(SecurityContextHolder.getContext().getAuthentication().getName());
			
			modelRepository.attachFile(ModelId.fromPrettyFormat(modelId),new FileContent(file.getOriginalFilename(),file.getBytes()),user,Attachment.TAG_IMAGE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
		
	@ApiOperation(value = "Saves a model to the repository.")
	@PreAuthorize("@modelRepository.hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).MODIFY)")
	@RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}", produces = "application/json")
	public ValidationReport saveModel(@ApiParam(value = "modelId", required = true) @PathVariable String modelId,
			@RequestBody ModelContent content) {
		try {
			ModelId modelID = ModelId.fromPrettyFormat(modelId);

			IUserContext userContext = UserContext
					.user(SecurityContextHolder.getContext().getAuthentication().getName());
			ModelResource modelInfo = (ModelResource) modelParserFactory
					.getParser("model" + ModelType.valueOf(content.getType()).getExtension())
					.parse(new ByteArrayInputStream(content.getContentDsl().getBytes()));

			if (!modelID.equals(modelInfo.getId())) {
				return ValidationReport.invalid(modelInfo,
						"You may not change the model ID (name, namespace, version). For this please create a new model.");
			}
			ModelValidationHelper validationHelper = new ModelValidationHelper(modelRepository, policyManager, userRepository);
			ValidationReport validationReport = validationHelper.validate(modelInfo, userContext);
			if (validationReport.isValid()) {
				this.modelRepository.save(modelInfo.getId(), content.getContentDsl().getBytes(),
						modelInfo.getId().getName() + modelInfo.getType().getExtension(), userContext);
			}
			return validationReport;
		} catch (ValidationException validationException) {
			return ValidationReport.invalid(null, validationException);
		}

	}

	 @ApiOperation(value = "Creates a model in the repository with the given model ID and model type.")
	  @PreAuthorize("hasRole('ROLE_MODEL_CREATOR')")
	  @RequestMapping(method = RequestMethod.POST, value = "/{modelId:.+}/{modelType}",
	      produces = "application/json")
	  public ResponseEntity<ModelInfo> createModel(
	      @ApiParam(value = "modelId", required = true) @PathVariable String modelId,
	      @ApiParam(value = "modelType", required = true) @PathVariable ModelType modelType,
	      @RequestBody(required=false) List<ModelProperty> properties) throws WorkflowException {

	    final ModelId modelID = ModelId.fromPrettyFormat(modelId);
	    if (this.modelRepository.exists(modelID)) {
	      throw new ModelAlreadyExistsException();
	    } else {
	      IUserContext userContext =
	          UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());

	      String modelTemplate = null;

	      if (modelType == ModelType.InformationModel && properties != null) {
	        modelTemplate = new InfomodelTemplate().createModelTemplate(modelID, properties);
	      } else {
	        modelTemplate = new ModelTemplate().createModelTemplate(modelID, modelType);
	      }

	      ModelInfo savedModel = this.modelRepository.save(modelID, modelTemplate.getBytes(),
	          modelID.getName() + modelType.getExtension(), userContext);
	      this.workflowService.start(modelID,userContext);
	      return new ResponseEntity<>(savedModel, HttpStatus.CREATED);

	    }
	  }
	
	@ApiOperation(value = "Creates a new version for the given model in the specified version")
	@PreAuthorize("hasRole('ROLE_MODEL_CREATOR')")
	@RequestMapping(method = RequestMethod.POST, value = "/{modelId:.+}/versions/{modelVersion:.+}", produces = "application/json")
	public ResponseEntity<ModelInfo> createVersionOfModel(@ApiParam(value = "modelId", required = true) @PathVariable String modelId,
			@ApiParam(value = "modelVersion", required = true) @PathVariable String modelVersion)
			throws WorkflowException, IOException {

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		IUserContext userContext = UserContext
				.user(SecurityContextHolder.getContext().getAuthentication().getName());
		
		ModelResource resource = this.modelRepository.createVersion(modelID, modelVersion, userContext);
		this.workflowService.start(resource.getId(),userContext);
		return new ResponseEntity<>(resource,HttpStatus.CREATED);

	}
	
	@RequestMapping(value = "/{modelId:.+}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ROLE_ADMIN') or @modelRepository.hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
	public void deleteModelResource(final @PathVariable String modelId) {
		Objects.requireNonNull(modelId, "modelId must not be null");
		this.modelRepository.removeModel(ModelId.fromPrettyFormat(modelId));
	}
	
	
	// ##################### Downloads ################################

	@RequestMapping(value = { "/mine/download" }, method = RequestMethod.GET)
	public void getUserModels(Principal user, final HttpServletResponse response) {
		//TODO : Checking for hashedUsername is legacy and needs to be removed once full migration has taken place
		List<ModelInfo> userModels = this.modelRepository
				.search("author:" + UserContext.user(user.getName()).getHashedUsername());
		
		userModels.addAll(this.modelRepository.search("author:" + UserContext.user(user.getName()).getUsername()));
		// TODO: end

		logger.info("Exporting information models for " + user.getName() + " results: " + userModels.size());

		sendAsZipFile(response, user.getName() + "-models.zip", userModels);
	}
	
	@ApiOperation(value = "Getting all mapping resources for the given model")
	@PreAuthorize("hasRole('ROLE_USER') or hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),'model:get')")
	@RequestMapping(value = "/{modelId:.+}/download/mappings/{targetPlatform}", method = RequestMethod.GET)
	public void downloadMappingsForPlatform(
			@ApiParam(value = "The model ID of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId,
			@ApiParam(value = "The name of target platform, e.g. lwm2m", required = true) final @PathVariable String targetPlatform,
			final HttpServletResponse response) {
		Objects.requireNonNull(modelId, "model ID must not be null");

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);

		List<ModelInfo> mappingResources = modelRepository.getMappingModelsForTargetPlatform(modelID, targetPlatform);

		final String fileName = modelID.getNamespace() + "_" + modelID.getName() + "_" + modelID.getVersion() + ".zip";

		sendAsZipFile(response, fileName, mappingResources);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/{modelId:.+}/diagnostics", method = RequestMethod.GET)
	public Collection<Diagnostic> runDiagnostics(final @PathVariable String modelId) {
		Objects.requireNonNull(modelId, "model ID must not be null");
		return diagnosticsService.diagnoseModel(ModelId.fromPrettyFormat(modelId));
	}
	
	@PreAuthorize("@modelRepository.hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
	@RequestMapping(value = "/{modelId:.+}/policies", method = RequestMethod.GET)
	public Collection<PolicyEntry> getPolicies(final @PathVariable String modelId) {
		Objects.requireNonNull(modelId, "model ID must not be null");
		return policyManager.getPolicyEntries(ModelId.fromPrettyFormat(modelId));
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{modelId:.+}/policy", method = RequestMethod.GET)
    public PolicyEntry getUserPolicy(final @PathVariable String modelId, Principal user) {
        Objects.requireNonNull(modelId, "model ID must not be null");
        return policyManager.getPolicyEntries(ModelId.fromPrettyFormat(modelId)).stream().filter(p -> p.getPrincipalType() == PrincipalType.User && p.getPrincipalId().equals(user.getName())).findFirst().get();
    }
	
	@PreAuthorize("@modelRepository.hasPermission(T(org.eclipse.vorto.model.ModelId).fromPrettyFormat(#modelId),T(org.eclipse.vorto.repository.core.PolicyEntry.Permission).FULL_ACCESS)")
	@RequestMapping(value = "/{modelId:.+}/policies", method = RequestMethod.PUT)
	public void addOrUpdatePolicyEntry(final @PathVariable String modelId, final @RequestBody PolicyEntry entry) {
		Objects.requireNonNull(modelId, "modelID must not be null");
		Objects.requireNonNull(entry, "entry must not be null");
		
		if (attemptChangePolicyOfCurrentUser(entry)) {
		  throw new IllegalArgumentException("Cannot change policy of current user");
		}
		
		policyManager.addPolicyEntry(ModelId.fromPrettyFormat(modelId),entry);
	}
	
	private boolean attemptChangePolicyOfCurrentUser(PolicyEntry entry) {
      return SecurityContextHolder.getContext().getAuthentication().getName().equals(entry.getPrincipalId());
  }

  @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "/{modelId:.+}/policies/{principalId:.+}/{principalType:.+}", method = RequestMethod.DELETE)
    public void removePolicyEntry(final @PathVariable String modelId, final @PathVariable String principalId,final @PathVariable String principalType) {
        Objects.requireNonNull(modelId, "modelID must not be null");
        Objects.requireNonNull(principalId, "principalID must not be null");
        final PolicyEntry entry = PolicyEntry.of(principalId, PrincipalType.valueOf(principalType), null);
        
        if (attemptChangePolicyOfCurrentUser(entry)) {
          throw new IllegalArgumentException("Cannot change policy of current user");
        }
        policyManager.removePolicyEntry(ModelId.fromPrettyFormat(modelId),entry);
    }
}
