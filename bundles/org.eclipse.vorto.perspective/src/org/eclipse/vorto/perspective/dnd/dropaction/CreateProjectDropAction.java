package org.eclipse.vorto.perspective.dnd.dropaction;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.vorto.codegen.api.context.IModelProjectContext;
import org.eclipse.vorto.codegen.api.tasks.Generated;
import org.eclipse.vorto.codegen.api.tasks.ICodeGeneratorTask;
import org.eclipse.vorto.codegen.api.tasks.IOutputter;
import org.eclipse.vorto.codegen.ui.progresstask.IProgressTask;
import org.eclipse.vorto.codegen.ui.progresstask.ProgressTaskExecutionService;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.core.model.nature.FbDatatypeProjectNature;
import org.eclipse.vorto.core.model.nature.InformationModelProjectNature;
import org.eclipse.vorto.core.model.nature.IoTProjectNature;
import org.eclipse.vorto.core.service.IModelProjectService;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.IDropAction;
import org.eclipse.vorto.wizard.ProjectCreationTask;

import com.google.common.base.Function;
import com.google.common.base.Functions;

public class CreateProjectDropAction implements IDropAction {

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();
	private Function<ModelType, String> modelTypeToSuffix = Functions.forMap(modelTypeToSuffix());
	private Function<ModelType, String[]> modelTypeToNature = Functions.forMap(modelTypeToNature());

	@Override
	public boolean performDrop(IModelProject receivingProject, Object droppedObject) {
		if (droppedObject == null || !ModelResource.class.isInstance(droppedObject)) {
			throw new IllegalArgumentException("Dropped object should not be null and should be a ModelResource");
		}

		ModelResource model = (ModelResource) droppedObject;

		IModelProjectContext context = getModelProjectContext(model);

		ProgressTaskExecutionService progressTaskExecutionService = ProgressTaskExecutionService
				.getProgressTaskExecutionService();

		progressTaskExecutionService.syncRun(getProjectCreationTask(context, model));

		return false;
	}

	private IProgressTask getProjectCreationTask(final IModelProjectContext context, final ModelResource model) {
		return new ProjectCreationTask(context) {
			@Override
			public IModelProject getIotproject(IProject project) {
				return ModelProjectServiceFactory.getDefault().getProjectFromEclipseProject(project);
			}

			@Override
			protected ICodeGeneratorTask<IModelProjectContext> getCodeGeneratorTask() {
				return new SharedModelCodeGenerationTask(model);
			}

			@Override
			protected String[] getProjectNature() {
				return modelTypeToNature.apply(model.getId().getModelType());
			}
		};
	}

	private class SharedModelCodeGenerationTask implements ICodeGeneratorTask<IModelProjectContext> {

		private ModelResource modelResource;

		public SharedModelCodeGenerationTask(ModelResource modelResource) {
			this.modelResource = modelResource;
		}

		public void generate(IModelProjectContext ctx, IOutputter outputter) {
			generateModel(outputter, IModelProjectService.MODELS_DIR, modelResource);
		}

		private void generateModel(IOutputter outputter, String modelDir, ModelResource model) {
			ModelId id = model.getId();
			String content = new String(modelRepo.downloadContent(id), StandardCharsets.UTF_8);
			Generated generated = new Generated(id.getName() + modelTypeToSuffix.apply(id.getModelType()), modelDir, content);
			outputter.output(generated);
			
			for(ModelId referenceId : model.getReferences()) {
				ModelResource referenceModel = modelRepo.getModel(referenceId);
				generateModel(outputter, IModelProjectService.SHARED_MODELS_DIR, referenceModel);
			}
		}
	}
	
	private Map<ModelType, String> modelTypeToSuffix() {
		Map<ModelType, String> modelTypeSuffixMap = new HashMap<ModelType, String>();
		modelTypeSuffixMap.put(ModelType.InformationModel, IModelProjectService.INFOMODEL_EXT);
		modelTypeSuffixMap.put(ModelType.Functionblock, IModelProjectService.FBMODEL_EXT);
		modelTypeSuffixMap.put(ModelType.Datatype, IModelProjectService.TYPE_EXT);
		return modelTypeSuffixMap;
	}
	
	private Map<ModelType, String[]> modelTypeToNature() {
		Map<ModelType, String[]> modelTypeNatureMap = new HashMap<ModelType, String[]>();
		modelTypeNatureMap.put(ModelType.InformationModel, new String[] { InformationModelProjectNature.NATURE_ID });
		modelTypeNatureMap.put(ModelType.Functionblock, new String[] { IoTProjectNature.NATURE_ID });
		modelTypeNatureMap.put(ModelType.Datatype, new String[] { FbDatatypeProjectNature.NATURE_ID });
		return modelTypeNatureMap;
	}

	private IModelProjectContext getModelProjectContext(final ModelResource model) {
		return new IModelProjectContext() {
			@Override
			public String getProjectName() {
				return model.getId().getName();
			}

			@Override
			public String getWorkspaceLocation() {
				return ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
			}

			@Override
			public String getModelName() {
				return model.getId().getName();
			}

			@Override
			public String getModelVersion() {
				return model.getId().getVersion();
			}

			@Override
			public String getModelDescription() {
				return model.getDescription();
			}
		};
	}

}
