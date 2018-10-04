package org.eclipse.vorto.repository.web.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
import org.eclipse.vorto.repository.web.core.dto.mapping.MappingSpecification;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingRequest;
import org.eclipse.vorto.repository.web.core.dto.mapping.TestMappingResponse;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.eclipse.vorto.repository.workflow.WorkflowException;
import org.eclipse.vorto.service.mapping.DataInputFactory;
import org.eclipse.vorto.service.mapping.IDataMapper;
import org.eclipse.vorto.service.mapping.MappingContext;
import org.eclipse.vorto.service.mapping.normalized.InfomodelData;
import org.eclipse.vorto.service.mapping.serializer.IMappingSerializer;
import org.eclipse.vorto.service.mapping.serializer.MappingSpecificationSerializer;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(value = "/rest/mappings")
public class PayloadMappingController {
	
	@Autowired
	private ModelController modelController;
	
	@Autowired
	private IModelRepository repository;
	
	@Autowired
	private IWorkflowService workflowService;
	
	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}", method = RequestMethod.POST)
	public IMappingSpecification createMappingSpecification(@PathVariable final String modelId, @PathVariable String targetPlatform) throws Exception {
		if (!this.repository.getMappingModelsForTargetPlatform(ModelId.fromPrettyFormat(modelId), targetPlatform).isEmpty()){
			throw new ModelAlreadyExistsException();
		} else {
			MappingSpecification mappingSpec = (MappingSpecification)this.getMappingSpecification(modelId, targetPlatform);
			this.saveMappingSpecification(mappingSpec,modelId,targetPlatform);
			return mappingSpec;
		}
	}

	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}", method = RequestMethod.GET)
	public IMappingSpecification getMappingSpecification(@PathVariable final String modelId,@PathVariable String targetPlatform) throws Exception {
		Infomodel infoModel = (Infomodel)modelController.getModelContentForTargetPlatform(modelId, targetPlatform);
		Map<String, FunctionblockModel> properties = new HashMap<String, FunctionblockModel>();
		for (ModelProperty prop : infoModel.getFunctionblocks()) {
			FunctionblockModel fbm = null;
			if (prop.getMappingReference() != null) {
				fbm = (FunctionblockModel)modelController.getModelContentByModelAndMappingId(((ModelId) prop.getType()).getPrettyFormat(), prop.getMappingReference().getPrettyFormat());
			} else {
				fbm = (FunctionblockModel)modelController.getModelContentForTargetPlatform(((ModelId) prop.getType()).getPrettyFormat(), targetPlatform);
			}
			// adding empty stereotype for all status properties, if necessary
			fbm.getStatusProperties().stream().filter(p -> !p.getStereotype("source").isPresent())
					.forEach(p -> p.addStereotype(Stereotype.createWithXpath("")));
			
			// adding empty stereotype for all configuration properties, if necessary
			fbm.getConfigurationProperties().stream().filter(p -> !p.getStereotype("source").isPresent())
				.forEach(p -> p.addStereotype(Stereotype.createWithXpath("")));

			unescapeMappingAttributesForStereotype(fbm, "functions");
			unescapeMappingAttributesForStereotype(fbm,"source");

			properties.put(prop.getName(), fbm);
		}
		return new MappingSpecification(infoModel, properties);
	}
	
	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}/info", method = RequestMethod.GET)
	public List<ModelInfo> getMappingModels(@PathVariable final String modelId,@PathVariable String targetPlatform){
		return this.repository.getMappingModelsForTargetPlatform(ModelId.fromPrettyFormat(modelId), targetPlatform);
	}
	

	private void unescapeMappingAttributesForStereotype(FunctionblockModel fbm, String stereotype) {
		fbm.getStatusProperties().stream().filter(p -> p.getStereotype(stereotype).isPresent())
		.forEach(p -> p.getStereotype(stereotype).get()
				.setAttributes(unescapeExpression(p.getStereotype(stereotype).get().getAttributes())));		
	}

	private Map<String, String> unescapeExpression(Map<String, String> attributes) {
		Map<String, String> unescapedAttributes = new HashMap<String, String>(attributes.size());
		for (String key : attributes.keySet()) {
			String expression = attributes.get(key);
			unescapedAttributes.put(key, StringEscapeUtils.unescapeJava(expression));
		}
		return unescapedAttributes;
	}

	@RequestMapping(value = "/test", method = RequestMethod.PUT)
	public TestMappingResponse testMapping(@RequestBody TestMappingRequest testRequest) throws Exception {
		IDataMapper mapper = IDataMapper.newBuilder().withSpecification(testRequest.getSpecification())
				.build();
		InfomodelData mappedOutput = mapper
				.map(DataInputFactory.getInstance().fromJson(testRequest.getSourceJson()), MappingContext.empty());

		TestMappingResponse response = new TestMappingResponse();
		response.setMappedOutput(new ObjectMapper().writeValueAsString(mappedOutput.getProperties()));
		return response;
	}

	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}", method = RequestMethod.PUT)
	public ModelId saveMappingSpecification(@RequestBody MappingSpecification mappingSpecification, @PathVariable String modelId, @PathVariable String targetPlatform) {
		IUserContext userContext = UserContext
				.user(SecurityContextHolder.getContext().getAuthentication().getName());
		
		final List<ModelId> publishedModelIds = new ArrayList<>();

		MappingSpecificationSerializer.create(mappingSpecification,targetPlatform).iterator()
				.forEachRemaining(m -> publishedModelIds.add(serializeAndSave(m, userContext)));

		return publishedModelIds.get(publishedModelIds.size() - 1);
	}

	public ModelId serializeAndSave(IMappingSerializer m, IUserContext user) {
		final ModelId createdModelId = m.getModelId();	
		repository.save(createdModelId, m.serialize().getBytes(), createdModelId.getName()+".mapping", user);
		try {
			workflowService.start(createdModelId);
		} catch (WorkflowException e) {
			// 
		}
		return createdModelId;
	}
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason = "Mapping Specification already exists.")  // 409
    @ExceptionHandler(ModelAlreadyExistsException.class)
    public void ModelExists(final ModelAlreadyExistsException ex){
		// do logging
    }
}
