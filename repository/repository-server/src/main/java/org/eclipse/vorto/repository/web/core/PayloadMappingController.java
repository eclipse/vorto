package org.eclipse.vorto.repository.web.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelContent;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.repository.api.content.Stereotype;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.IUserContext;
import org.eclipse.vorto.repository.core.ModelAlreadyExistsException;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.api.v1.ModelController;
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
import org.eclipse.vorto.service.mapping.spec.MappingSpecification;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
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

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/rest/mappings")
public class PayloadMappingController extends AbstractRepositoryController {
	
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
			ModelContent modelContent = this.modelController.getModelContent(modelId);
			MappingSpecification spec = new MappingSpecification();
			spec.setInfoModel((Infomodel)modelContent.getModels().get(modelContent.getRoot()));
			for (ModelProperty property : spec.getInfoModel().getFunctionblocks()) {
				spec.getProperties().put(property.getName(), (FunctionblockModel)modelContent.getModels().get(property.getType()));
			}
			this.saveMappingSpecification(spec,modelId,targetPlatform);
			return spec;
		}
	}

	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}", method = RequestMethod.GET)
	public IMappingSpecification getMappingSpecification(@PathVariable final String modelId,@PathVariable String targetPlatform) throws Exception {
		ModelContent infoModelContent = modelController.getModelContentForTargetPlatform(modelId, targetPlatform);
		Infomodel infomodel = (Infomodel)infoModelContent.getModels().get(infoModelContent.getRoot());		
		
		MappingSpecification specification = new MappingSpecification();
		specification.setInfoModel(infomodel);
		for (ModelProperty fbProperty : infomodel.getFunctionblocks()) {
			ModelId fbModelId = (ModelId) fbProperty.getType();
			
			ModelId mappingId = fbProperty.getMappingReference();
			
			FunctionblockModel fbm = null;
			if (mappingId != null) {
				fbm = getModelContentByModelAndMappingId(fbModelId.getPrettyFormat(), mappingId.getPrettyFormat());
			} else {
				ModelContent fbmContent = modelController.getModelContentForTargetPlatform(fbModelId.getPrettyFormat(), targetPlatform);
				fbm = (FunctionblockModel)fbmContent.getModels().get(fbmContent.getRoot());
			}			
			
			specification.getProperties().put(fbProperty.getName(), initEmptyProperties(fbm));
		}
		return specification;
	}
	
	public FunctionblockModel getModelContentByModelAndMappingId( final @PathVariable String modelId, final @PathVariable String mappingId) {

		ModelInfo vortoModelInfo = this.repository.getById(ModelId.fromPrettyFormat(modelId));
		ModelInfo mappingModelInfo = this.repository.getById(ModelId.fromPrettyFormat(mappingId));

		if (vortoModelInfo == null) {
			throw new ModelNotFoundException("Could not find vorto model with ID: " + modelId);
		} else if (mappingModelInfo == null) {
			throw new ModelNotFoundException("Could not find mapping with ID: " + mappingId);

		}

		byte[] mappingContentZip = createZipWithAllDependencies(mappingModelInfo.getId());
		IModelWorkspace mappingWorkspace = IModelWorkspace.newReader()
				.addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();

		org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel fbm = (org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel)mappingWorkspace.get().stream().filter(model -> model instanceof org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel).findFirst().get();
		
		return ModelDtoFactory.createResource(fbm,Optional.of((MappingModel)mappingWorkspace.get().stream().filter(model -> model instanceof MappingModel).findFirst().get()));
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
			throw new RuntimeException(ex);
		}
	}
	
	private FunctionblockModel initEmptyProperties(FunctionblockModel fbm) {
	
		// adding empty stereotype for all status properties, if necessary
		fbm.getStatusProperties().stream().filter(p -> !p.getStereotype("source").isPresent())
				.forEach(p -> p.addStereotype(Stereotype.createWithXpath("")));
		
		// adding empty stereotype for all configuration properties, if necessary
		fbm.getConfigurationProperties().stream().filter(p -> !p.getStereotype("source").isPresent())
			.forEach(p -> p.addStereotype(Stereotype.createWithXpath("")));

		unescapeMappingAttributesForStereotype(fbm, "functions");
		unescapeMappingAttributesForStereotype(fbm,"source");
		
		return fbm;
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
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Successful download of mapping specification"), @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@RequestMapping(value = "/{modelId:.+}/{targetPlatform:.+}/file", method = RequestMethod.GET)
	public void downloadModelById(@PathVariable final String modelId,@PathVariable String targetPlatform,
			final HttpServletResponse response) {

		Objects.requireNonNull(modelId, "modelId must not be null");

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);

		response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + modelID.getNamespace() + "_"
				+ modelID.getName() + "_" + modelID.getVersion() + "-mappingspec.json");
		response.setContentType(APPLICATION_OCTET_STREAM);
		try {	
			MappingSpecification spec = (MappingSpecification)this.getMappingSpecification(modelId, targetPlatform);
			ObjectMapper mapper = new ObjectMapper();	
			IOUtils.copy(new ByteArrayInputStream(mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(spec)), response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}
	
	@ResponseStatus(value=HttpStatus.CONFLICT, reason = "Mapping Specification already exists.")  // 409
    @ExceptionHandler(ModelAlreadyExistsException.class)
    public void ModelExists(final ModelAlreadyExistsException ex){
		// do logging
    }
}
