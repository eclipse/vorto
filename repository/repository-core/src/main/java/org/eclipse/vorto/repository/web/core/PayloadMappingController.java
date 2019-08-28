/**
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional information regarding copyright
 * ownership.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.web.core;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.mapping.engine.MappingEngine;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification;
import org.eclipse.vorto.mapping.engine.serializer.IMappingSerializer;
import org.eclipse.vorto.mapping.engine.serializer.MappingSpecificationSerializer;
import org.eclipse.vorto.model.EntityModel;
import org.eclipse.vorto.model.EnumModel;
import org.eclipse.vorto.model.FunctionblockModel;
import org.eclipse.vorto.model.IModel;
import org.eclipse.vorto.model.Infomodel;
import org.eclipse.vorto.model.ModelContent;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.model.ModelProperty;
import org.eclipse.vorto.model.Stereotype;
import org.eclipse.vorto.model.runtime.InfomodelValue;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.tenant.ITenantService;
import org.eclipse.vorto.repository.utils.ModelUtils;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingRequest;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingResponse;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/rest/mappings")
public class PayloadMappingController extends AbstractRepositoryController {

  private static final String EMPTY_STRING = "";

  private static final String STEREOTYPE_CONDITION = "condition";

  private static final String STEREOTYPE_FUNCTIONS = "functions";

  private static final String STEREOTYPE_SOURCE = "source";

  private Function<String, IUserContext> userContextFn = (tenantId) -> UserContext
      .user(SecurityContextHolder.getContext().getAuthentication(), tenantId);

  @Autowired
  private ModelController modelController;

  @Autowired
  private IWorkflowService workflowService;

  @Autowired
  private ITenantService tenantService;

  private static Logger logger = Logger.getLogger(PayloadMappingController.class);

  private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
  private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  private static final String CONTENT_DISPOSITION = "content-disposition";

  @GetMapping(value = "/{modelId:.+}/{targetPlatform:.+}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public IMappingSpecification getMappingSpecification(@PathVariable final String modelId,
      @PathVariable String targetPlatform) throws Exception {
    ModelContent infoModelContent =
        modelController.getModelContentForTargetPlatform(modelId, targetPlatform);

    Infomodel infomodel = (Infomodel) infoModelContent.getModels().get(infoModelContent.getRoot());

    if (infomodel == null) {
      throw new ModelNotFoundException("Cannot find mapping specification for model "+modelId);
    }

    MappingSpecification specification = new MappingSpecification();
    specification.setInfoModel(infomodel);
    addReferencesRecursive(infomodel);  
    
    return specification;
  }

  /**
   * Adds reference types of the given properties to the mapping Specification (needed for lookup)
   * @param model to traverse properties
   */
  private void addReferencesRecursive(IModel model) {
    
    if (model instanceof Infomodel) {
      Infomodel infomodel = (Infomodel)model;
      for (ModelProperty property : infomodel.getFunctionblocks()) {
        ModelId referenceModelId = (ModelId)property.getType();
        ModelId mappingId = property.getMappingReference();
        IModel referenceModel = null;
        if (mappingId != null) {
          referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),mappingId.getPrettyFormat());
        } else {
          ModelContent modelContent = modelController.getModelContent(referenceModelId.getPrettyFormat());
          referenceModel = modelContent.getModels().get(modelContent.getRoot());
        }
        property.setType((FunctionblockModel)referenceModel);
        addReferencesRecursive(referenceModel);
      }
    } else if (model instanceof EntityModel) {
      EntityModel entityModel = (EntityModel)model;
      for (ModelProperty property : entityModel.getProperties()) {
        initStereotypeIfMissing(property);
        if (property.getType() instanceof ModelId) {
          ModelId referenceModelId = (ModelId)property.getType();
          ModelId mappingId = property.getMappingReference();
          IModel referenceModel = null;
          if (mappingId != null) {
            referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),mappingId.getPrettyFormat());
          } else {
            ModelContent modelContent = modelController.getModelContent(referenceModelId.getPrettyFormat());
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }
          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel)referenceModel);
            addReferencesRecursive(referenceModel);
          } else {
            property.setType((EnumModel)referenceModel);
          }     
        }      
      }
    }  else if (model instanceof FunctionblockModel) {
      FunctionblockModel fbModel = (FunctionblockModel)model;
      for (ModelProperty property : fbModel.getProperties()) {
        initStereotypeIfMissing(property);
        if (property.getType() instanceof ModelId) {
          ModelId referenceModelId = (ModelId)property.getType();
          ModelId mappingId = property.getMappingReference();
          IModel referenceModel = null;
          if (mappingId != null) {
            referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),mappingId.getPrettyFormat());
          } else {
            ModelContent modelContent = modelController.getModelContent(referenceModelId.getPrettyFormat());
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }
          
          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel)referenceModel);
            addReferencesRecursive(referenceModel);
          } else {
            property.setType((EnumModel)referenceModel);
          }
        }
      }
    }  
  }

  private void initStereotypeIfMissing(ModelProperty property) {
    if (!property.getStereotype(STEREOTYPE_SOURCE).isPresent()) {
      property.addStereotype(Stereotype.createWithXpath(EMPTY_STRING));
      unescapeMappingAttributesForStereotype(property, STEREOTYPE_FUNCTIONS);
      unescapeMappingAttributesForStereotype(property, STEREOTYPE_CONDITION);
      unescapeMappingAttributesForStereotype(property, STEREOTYPE_SOURCE);
    }
  }
  
  private void unescapeMappingAttributesForStereotype(ModelProperty property, String stereotype) {
    if (property.getStereotype(stereotype).isPresent()) {
      property.getStereotype(stereotype).get().setAttributes(unescapeExpression(property.getStereotype(stereotype).get().getAttributes()));
    }
  }

  @PostMapping(value = "/{modelId:.+}/{targetPlatform:.+}")
  @PreAuthorize("hasRole('ROLE_MODEL_CREATOR')")
  public Map<String, Object> createMappingSpecification(@PathVariable final String modelId,
      @PathVariable String targetPlatform) throws Exception {
    logger.info("Creating Mapping Specification for " + modelId + " using key " + targetPlatform);

    if (!getModelRepository(ModelId.fromPrettyFormat(modelId))
        .getMappingModelsForTargetPlatform(ModelId.fromPrettyFormat(modelId), targetPlatform,
            Optional.of(ModelId.fromPrettyFormat(modelId).getVersion()))
        .isEmpty()) {
      throw new ModelAlreadyExistsException();
    } else {
      ModelContent modelContent = this.modelController.getModelContent(modelId);
      
      Infomodel infomodel = (Infomodel) modelContent.getModels().get(modelContent.getRoot());
      
      MappingSpecification specification = new MappingSpecification();
      specification.setInfoModel(infomodel);
      addReferencesRecursive(infomodel); 
      
      final ModelId createdId = this.saveMappingSpecification(specification, modelId, targetPlatform);
      Map<String, Object> response = new HashMap<String, Object>();
      response.put("mappingId", createdId.getPrettyFormat());
      response.put("spec", specification);
      return response;
    }
  }

  @PutMapping(value = "/test")
  @PreAuthorize("hasRole('ROLE_USER')")
  public TestMappingResponse testMapping(@RequestBody TestMappingRequest testRequest)
      throws Exception {
    MappingEngine engine = MappingEngine.create(testRequest.getSpecification());

    InfomodelValue mappedOutput =
        engine.mapSource(new ObjectMapper().readValue(testRequest.getSourceJson(), Object.class));

    TestMappingResponse response = new TestMappingResponse();
    response.setMappedOutput(new ObjectMapper().writeValueAsString(mappedOutput.serialize()));
    response.setReport(mappedOutput.validate());
    return response;
  }

  @PutMapping(value = "/{modelId:.+}/{targetPlatform:.+}")
  @PreAuthorize("hasRole('ROLE_MODEL_CREATOR')")
  public ModelId saveMappingSpecification(@RequestBody MappingSpecification mappingSpecification,
      @PathVariable String modelId, @PathVariable String targetPlatform) {
    logger.info("Saving mapping specification " + modelId + " with key " + targetPlatform);

    IUserContext userContext = userContextFn.apply(getTenant(modelId));

    final List<ModelId> publishedModelIds = new ArrayList<>();

      MappingSpecificationSerializer.create(mappingSpecification, targetPlatform).iterator()
        .forEachRemaining(m -> publishedModelIds.add(serializeAndSave(m, userContext)));

    return publishedModelIds.get(publishedModelIds.size() - 1);
  }

  @ApiResponses(
      value = {@ApiResponse(code = 200, message = "Successful download of mapping specification"),
          @ApiResponse(code = 400, message = "Wrong input"),
          @ApiResponse(code = 404, message = "Model not found")})
  @PreAuthorize("hasRole('ROLE_USER')")
  @GetMapping(value = "/{modelId:.+}/{targetPlatform:.+}/file")
  public void downloadModelById(@PathVariable final String modelId,
      @PathVariable String targetPlatform, final HttpServletResponse response) {
    Objects.requireNonNull(modelId, "modelId must not be null");

    final ModelId modelID = ModelId.fromPrettyFormat(modelId);

    response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getNamespace() + "_"
        + modelID.getName() + "_" + modelID.getVersion() + "-mappingspec.json");
    response.setContentType(APPLICATION_OCTET_STREAM);
    try {
      MappingSpecification spec =
          (MappingSpecification) this.getMappingSpecification(modelId, targetPlatform);
      ObjectMapper mapper = new ObjectMapper();
      IOUtils.copy(
          new ByteArrayInputStream(mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(spec)),
          response.getOutputStream());
      response.flushBuffer();
    } catch (Exception e) {
      throw new RuntimeException("Error copying file.", e);
    }
  }

  @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Mapping Specification already exists.")
  // 409
  @ExceptionHandler(ModelAlreadyExistsException.class)
  public void modelExists(final ModelAlreadyExistsException ex) {
    // do logging
  }

  public IModel getModelContentByModelAndMappingId(final String _modelId,
      final @PathVariable String mappingId) {
    
    final ModelId modelId = ModelId.fromPrettyFormat(_modelId);
    final ModelId mappingModelId = ModelId.fromPrettyFormat(mappingId);
    IModelRepository repository = getModelRepository(modelId);

    ModelInfo vortoModelInfo = repository.getById(modelId);
    ModelInfo mappingModelInfo = getModelRepository(mappingModelId).getById(mappingModelId);

    if (vortoModelInfo == null) {
      throw new ModelNotFoundException("Could not find vorto model with ID: " + modelId);
    } else if (mappingModelInfo == null) {
      throw new ModelNotFoundException("Could not find mapping with ID: " + mappingId);

    }

    IModelWorkspace mappingWorkspace = getWorkspaceForModel(mappingModelInfo.getId());

    Optional<Model> model = mappingWorkspace.get().stream().filter(_model -> ModelUtils.fromEMFModelId(ModelIdFactory.newInstance(_model)).equals(vortoModelInfo.getId())).findFirst();
    if (model.isPresent()) {
      return ModelDtoFactory.createResource(model.get(), Optional.of((MappingModel) mappingWorkspace.get()
          .stream().filter(_model -> _model instanceof MappingModel && mappingMatchesModelId((MappingModel)_model, vortoModelInfo)).findFirst().get()));
    } else {
      return null;
    }
  }

  private boolean mappingMatchesModelId(MappingModel mappingModel, ModelInfo modelToMatchAgainst) {  
    return mappingModel.getReferences().stream().filter(reference -> ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion()).equals(modelToMatchAgainst.getId())).count() > 0;    
  }

  private Map<String, String> unescapeExpression(Map<String, String> attributes) {
    Map<String, String> unescapedAttributes = new HashMap<String, String>(attributes.size());
    for (String key : attributes.keySet()) {
      String expression = attributes.get(key);
      unescapedAttributes.put(key, StringEscapeUtils.unescapeJava(expression));
    }
    return unescapedAttributes;
  }

  public ModelId serializeAndSave(IMappingSerializer m, IUserContext user) {
    final ModelId createdModelId = m.getModelId();
    getModelRepository(createdModelId).save(createdModelId, m.serialize().getBytes(),
        createdModelId.getName() + ".mapping", user);
    try {
      workflowService.start(createdModelId, user);
    } catch (WorkflowException e) {
      //
    }
    return createdModelId;
  }


  public void setUserContextFn(Function<String, IUserContext> userContextFn) {
    this.userContextFn = userContextFn;
  }


  public void setModelController(ModelController modelController) {
    this.modelController = modelController;
  }

  public void setWorkflowService(IWorkflowService workflowService) {
    this.workflowService = workflowService;
  }

  public void setTenantService(ITenantService tenantService) {
    this.tenantService = tenantService;
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
