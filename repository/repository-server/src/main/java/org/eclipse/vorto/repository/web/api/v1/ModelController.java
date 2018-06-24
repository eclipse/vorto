package org.eclipse.vorto.repository.web.api.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.exception.ModelNotFoundException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.AbstractRepositoryController;
import org.eclipse.vorto.repository.web.core.ModelDtoFactory;
import org.eclipse.vorto.repository.web.core.ModelRepositoryController;
import org.eclipse.vorto.utilities.reader.IModelWorkspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value = "/models", description = "Access information models")
@RestController("modelRepositoryController")
@RequestMapping(value = "/api/v1/models")
public class ModelController extends AbstractRepositoryController {

	@Autowired
	private IModelRepository modelRepository;
	
	private static Logger logger = Logger.getLogger(ModelRepositoryController.class);

	@ApiOperation(value = "Returns a model by its full qualified model ID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found"),
			@ApiResponse(code = 403, message = "Not Authorized to view model") })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:get')")
	@RequestMapping(value = "/{modelId:.+}", method = RequestMethod.GET)
	public ModelInfo getModelInfo(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId) {
		Objects.requireNonNull(modelId, "modelId must not be null");

		logger.info("getModelInfo: [" + modelId + "]");
		ModelInfo resource = modelRepository.getById(ModelId.fromPrettyFormat(modelId));
		
		if (resource == null) {
			throw new ModelNotFoundException("Model does not exist", null);
		}
		return ModelDtoFactory.createDto(resource,
				UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName()));
	}

	@ApiOperation(value = "Returns the model content")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:get')")
	@RequestMapping(value = "/{modelId:.+}/content", method = RequestMethod.GET)
	public AbstractModel getModelContent(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId) {

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		byte[] modelContent = createZipWithAllDependencies(modelID);

		IModelWorkspace workspace = IModelWorkspace.newReader()
				.addZip(new ZipInputStream(new ByteArrayInputStream(modelContent))).read();
		return ModelDtoFactory.createResource(
				workspace.get().stream().filter(p -> p.getName().equals(modelID.getName())).findFirst().get(), Optional.empty());
	}
	
	@ApiOperation(value = "Returns the model content including target platform specific attributes")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:get')")
	@RequestMapping(value = "/{modelId:.+}/content/{targetplatformKey}", method = RequestMethod.GET)
	public AbstractModel getModelContentForTargetPlatform(
			@ApiParam(value = "The modelId of vorto model, e.g. com.mycompany.Car:1.0.0", required = true) final @PathVariable String modelId,
			@ApiParam(value = "The key of the targetplatform, e.g. lwm2m", required = true) final @PathVariable String targetplatformKey) {

		final ModelId modelID = ModelId.fromPrettyFormat(modelId);
		List<ModelInfo> mappingResource = modelRepository
				.getMappingModelsForTargetPlatform(modelID, targetplatformKey);
		if (!mappingResource.isEmpty()) {
			byte[] mappingContentZip = createZipWithAllDependencies(mappingResource.get(0).getId());
			IModelWorkspace workspace = IModelWorkspace.newReader()
					.addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();

			MappingModel mappingModel = (MappingModel) workspace.get().stream().filter(p -> p instanceof MappingModel)
					.findFirst().get();

			byte[] modelContent = createZipWithAllDependencies(modelID);

			workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent)))
					.read();

			return ModelDtoFactory.createResource(
					workspace.get().stream().filter(p -> p.getName().equals(modelID.getName())).findFirst().get(),
					Optional.of(mappingModel));
		} else {
			return getModelContent(modelId);
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
			throw new RuntimeException(ex);
		}
	}


	private void addModelToZip(ZipOutputStream zipOutputStream, ModelId modelId) throws Exception {
		final ModelInfo modelResource = modelRepository.getById(modelId);
		final FileContent modelFile = modelRepository.getFileContent(modelId,modelResource.getFileName()).get();
		try {
			ZipEntry zipEntry = new ZipEntry(modelFile.getFileName());
			zipOutputStream.putNextEntry(zipEntry);
			zipOutputStream.write(modelFile.getContent());
			zipOutputStream.closeEntry();
		} catch (Exception ex) {
			// entry possible exists already, so skipping TODO: ugly hack!!
		}

		for (ModelId reference : modelResource.getReferences()) {
			addModelToZip(zipOutputStream, reference);
		}
	}
	
	@ApiOperation(value = "Returns the model content including target platform specific attributes for the given model- and mapping modelID")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Wrong input"),
			@ApiResponse(code = 404, message = "Model not found") })
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(modelId),'model:get')")
	@RequestMapping(value = "/{modelId:.+}/content/mappings/{mappingId:.+}", method = RequestMethod.GET)
	public AbstractModel getModelContentByModelAndMappingId(
			@ApiParam(value = "The model ID (prettyFormat)", required = true) final @PathVariable String modelId,
			@ApiParam(value = "The mapping Model ID (prettyFormat)", required = true) final @PathVariable String mappingId) {

		ModelInfo vortoModelInfo = modelRepository.getById(ModelId.fromPrettyFormat(modelId));
		ModelInfo mappingModelInfo = modelRepository.getById(ModelId.fromPrettyFormat(mappingId));

		if (vortoModelInfo == null) {
			throw new ModelNotFoundException("Could not find vorto model with ID: " + modelId);
		} else if (mappingModelInfo == null) {
			throw new ModelNotFoundException("Could not find mapping with ID: " + mappingId);

		}

		byte[] mappingContentZip = createZipWithAllDependencies(mappingModelInfo.getId());
		IModelWorkspace workspace = IModelWorkspace.newReader()
				.addZip(new ZipInputStream(new ByteArrayInputStream(mappingContentZip))).read();
		MappingModel mappingModel = (MappingModel) workspace.get().stream().filter(p -> p instanceof MappingModel)
				.findFirst().get();

		byte[] modelContent = createZipWithAllDependencies(vortoModelInfo.getId());
		workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(modelContent)))
				.read();

		return ModelDtoFactory.createResource(workspace.get().stream()
				.filter(p -> p.getName().equals(vortoModelInfo.getId().getName())).findFirst().get(),
				Optional.of(mappingModel));

	}
}
