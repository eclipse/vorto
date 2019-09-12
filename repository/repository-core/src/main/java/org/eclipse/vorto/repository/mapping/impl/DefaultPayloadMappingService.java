package org.eclipse.vorto.repository.mapping.impl;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.ModelConversionUtils;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.mapping.engine.MappingEngine;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.mapping.engine.model.spec.MappingSpecification;
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
import org.eclipse.vorto.repository.conversion.ModelIdToModelContentConverter;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IModelRepositoryFactory;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.core.ModelNotFoundException;
import org.eclipse.vorto.repository.core.impl.utils.DependencyManager;
import org.eclipse.vorto.repository.mapping.IPayloadMappingService;
import org.eclipse.vorto.repository.utils.ModelUtils;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.eclipse.vorto.utilities.reader.ModelWorkspaceReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class DefaultPayloadMappingService implements IPayloadMappingService {

  private static final String EMPTY_STRING = "";

  private static final String STEREOTYPE_CONDITION = "condition";

  private static final String STEREOTYPE_FUNCTIONS = "functions";

  private static final String STEREOTYPE_SOURCE = "source";
  
  @Autowired
  private IModelRepositoryFactory modelRepositoryFactory;
  
  @Autowired
  private IWorkflowService workflowService;
  
  private static Logger logger = Logger.getLogger(DefaultPayloadMappingService.class);
  
  public DefaultPayloadMappingService() {
  }
  
  public DefaultPayloadMappingService(IModelRepositoryFactory repositoryFactory, IWorkflowService workflowService) {
    this.modelRepositoryFactory = repositoryFactory;
    this.workflowService = workflowService;
  }
  
  @Override
  public IMappingSpecification getOrCreateSpecification(ModelId modelId) {
    ModelContent modelContent = getModelContent(modelId,createTargetPlatformKey(modelId));
    
    Infomodel infomodel = (Infomodel) modelContent.getModels().get(modelContent.getRoot());
        
    MappingSpecification specification = new MappingSpecification();
    specification.setInfoModel(infomodel);
    addReferencesRecursive(infomodel,infomodel.getTargetPlatformKey()); 
    return specification;
  }
  
  /**
   * 
   * @param modelId
   * @return
   */
  private String createTargetPlatformKey(ModelId modelId) {
    return modelId.getPrettyFormat().replace(".", "_").replace(":", "_");
  }
  
  private ModelContent getModelContent(ModelId modelId, String targetPlatformKey) {
    ModelIdToModelContentConverter converter = new ModelIdToModelContentConverter(this.modelRepositoryFactory);  
    return converter.convert(modelId, Optional.of(targetPlatformKey));
  }
  
  /**
   * Adds reference types of the given properties to the mapping Specification (needed for lookup)
   * @param model to traverse properties
   */
  private void addReferencesRecursive(IModel model, String targetPlatformKey) {
    
    if (model instanceof Infomodel) {
      Infomodel infomodel = (Infomodel)model;
      for (ModelProperty property : infomodel.getFunctionblocks()) {
        ModelId referenceModelId = (ModelId)property.getType();
        ModelId mappingId = property.getMappingReference();
        IModel referenceModel = null;
        if (mappingId != null) {
          referenceModel = getModelContentByModelAndMappingId(referenceModelId.getPrettyFormat(),mappingId.getPrettyFormat());
        } else {
          ModelContent modelContent = getModelContent(referenceModelId,targetPlatformKey);
          referenceModel = modelContent.getModels().get(modelContent.getRoot());
        }
        property.setType((FunctionblockModel)referenceModel);
        addReferencesRecursive(referenceModel,targetPlatformKey);
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
            ModelContent modelContent = getModelContent(referenceModelId,targetPlatformKey);
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }
          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel)referenceModel);
            addReferencesRecursive(referenceModel,targetPlatformKey);
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
            ModelContent modelContent = getModelContent(referenceModelId,targetPlatformKey);
            referenceModel = modelContent.getModels().get(modelContent.getRoot());
          }
          
          if (referenceModel instanceof EntityModel) {
            property.setType((EntityModel)referenceModel);
            addReferencesRecursive(referenceModel,targetPlatformKey);
          } else {
            property.setType((EnumModel)referenceModel);
          }
        }
      }
    }  
  }
  
  private IModel getModelContentByModelAndMappingId(final String _modelId,
      final @PathVariable String mappingId) {
    
    final ModelId modelId = ModelId.fromPrettyFormat(_modelId);
    final ModelId mappingModelId = ModelId.fromPrettyFormat(mappingId);
    IModelRepository repository = this.modelRepositoryFactory.getRepositoryByModel(modelId);

    ModelInfo vortoModelInfo = repository.getById(modelId);
    ModelInfo mappingModelInfo = this.modelRepositoryFactory.getRepositoryByModel(mappingModelId).getById(mappingModelId);

    if (vortoModelInfo == null) {
      throw new ModelNotFoundException("Could not find vorto model with ID: " + modelId);
    } else if (mappingModelInfo == null) {
      throw new ModelNotFoundException("Could not find mapping with ID: " + mappingId);

    }

    IModelWorkspace mappingWorkspace = getWorkspaceForModel(mappingModelInfo.getId());

    Optional<Model> model = mappingWorkspace.get().stream().filter(_model -> ModelUtils.fromEMFModelId(ModelIdFactory.newInstance(_model)).equals(vortoModelInfo.getId())).findFirst();
    if (model.isPresent()) {
      
      final Model flattenedModel = ModelConversionUtils.convertToFlatHierarchy(model.get());
      return ModelDtoFactory.createResource(flattenedModel, Optional.of((MappingModel) mappingWorkspace.get()
          .stream().filter(_model -> _model instanceof MappingModel && mappingMatchesModelId((MappingModel)_model, vortoModelInfo)).findFirst().get()));
    } else {
      return null;
    }
  }
  
  private IModelWorkspace getWorkspaceForModel(final ModelId modelId) {
    List<ModelInfo> allModels = getModelWithAllDependencies(modelId);
    DependencyManager dm = new DependencyManager(new HashSet<>(allModels));
    allModels = dm.getSorted();

    ModelWorkspaceReader workspaceReader = IModelWorkspace.newReader();
    for (ModelInfo model : allModels) {
      FileContent modelContent = this.modelRepositoryFactory.getRepositoryByModel(model.getId())
          .getFileContent(model.getId(), Optional.of(model.getFileName())).get();
      workspaceReader.addFile(new ByteArrayInputStream(modelContent.getContent()), model.getType());
    }

    return workspaceReader.read();
  }


  private List<ModelInfo> getModelWithAllDependencies(ModelId modelId) {
    List<ModelInfo> modelInfos = new ArrayList<>();

    ModelInfo modelResource = this.modelRepositoryFactory.getRepositoryByModel(modelId).getById(modelId);
    modelInfos.add(modelResource);

    for (ModelId reference : modelResource.getReferences()) {
      modelInfos.addAll(getModelWithAllDependencies(reference));
    }

    return modelInfos;
  }

  private boolean mappingMatchesModelId(MappingModel mappingModel, ModelInfo modelToMatchAgainst) {  
    return mappingModel.getReferences().stream().filter(reference -> ModelId.fromReference(reference.getImportedNamespace(), reference.getVersion()).equals(modelToMatchAgainst.getId())).count() > 0;    
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
  
  private Map<String, String> unescapeExpression(Map<String, String> attributes) {
    Map<String, String> unescapedAttributes = new HashMap<String, String>(attributes.size());
    for (String key : attributes.keySet()) {
      String expression = attributes.get(key);
      unescapedAttributes.put(key, StringEscapeUtils.unescapeJava(expression));
    }
    return unescapedAttributes;
  }

  @Override
  public void saveSpecification(IMappingSpecification specification, IUserContext user) {
    MappingSpecificationSerializer.create(specification, specification.getInfoModel().getTargetPlatformKey()).iterator()
    .forEachRemaining(serializer -> {
      final ModelId createdModelId = serializer.getModelId();
      
      String serializedMapping = serializer.serialize();
      logger.trace("Saving mapping: "+serializedMapping);
      this.modelRepositoryFactory.getRepositoryByModel(createdModelId).save(createdModelId, serializedMapping.getBytes(),
          createdModelId.getName() + ".mapping", user);
      
      try {
        workflowService.start(createdModelId, user);
      } catch (WorkflowException e) {
        throw new RuntimeException("Could not start workflow for mapping model",e);
      }
      
    }); 
  }

  @Override
  public InfomodelValue runTest(IMappingSpecification specification, Object testData) {
    MappingEngine engine = MappingEngine.create(specification);
    return engine.mapSource(testData);
  }

  @Override
  public Optional<ModelInfo> resolveMappingIdForModelId(ModelId modelId) {
     return this.modelRepositoryFactory.getRepositoryByModel(modelId)
      .getMappingModelsForTargetPlatform(modelId, createTargetPlatformKey(modelId),Optional.of(modelId.getVersion())).stream()
      .filter(modelInfo -> modelInfo.getType().equals(org.eclipse.vorto.model.ModelType.Mapping) && modelInfo.getReferences().contains(modelId)).findAny();
  }

  @Override
  public boolean exists(ModelId modelId) {
    return !this.modelRepositoryFactory.getRepositoryByModel(modelId).getMappingModelsForTargetPlatform(modelId, createTargetPlatformKey(modelId),Optional.of(modelId.getVersion())).isEmpty();
  }

}
