package org.eclipse.vorto.perspective.dnd.dropaction;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelFactory;
import org.eclipse.vorto.core.api.model.model.ModelReference;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.IDropAction;
import org.eclipse.vorto.perspective.function.ModelToDslFunction;

import com.google.common.base.Function;

public class RepositoryResourceDropAction implements IDropAction {

	private IModelRepository modelRepo = ModelRepositoryFactory
			.getModelRepository();

	private Function<Model, String> modelToDsl = new ModelToDslFunction();

	private Map<ModelType, String> modelFileExtensionMap = initializeExtensionMap();

	@Override
	public boolean performDrop(IModelProject receivingProject,
			Object droppedObject) {
		Objects.requireNonNull(receivingProject,
				"receivingProject shouldn't be null.");
		Objects.requireNonNull(droppedObject,
				"droppedObject shouldn't be null.");

		ModelResource modelResource = (ModelResource) droppedObject;

		// Download and save model from repository to local project
		Model droppedObjectModel = saveModelResourceToProject(receivingProject,
				modelResource.getId());

		// Download references also
		for (ModelReference reference : droppedObjectModel.getReferences()) {
			saveModelResourceToProject(receivingProject,
					toModelId(reference, modelResource.getId().getModelType()));
		}

		// Add the dropped model to the receiving project's model
		addReferenceToModel(receivingProject.getModel(), droppedObjectModel);

		ModelProjectServiceFactory.getDefault().save(receivingProject);

		return true;
	}

	private ModelId toModelId(ModelReference reference, ModelType parentType) {
		String importedNamespace = reference.getImportedNamespace();
		int lastIndex = importedNamespace.lastIndexOf('.');
		if (lastIndex > 0) {
			String namespace = importedNamespace.substring(0, lastIndex);
			String name = importedNamespace.substring(lastIndex + 1,
					importedNamespace.length());
			ModelType modelType = ModelType.DATATYPE;
			if (parentType == ModelType.INFORMATIONMODEL) {
				modelType = ModelType.FUNCTIONBLOCK;
			}
			return new ModelId(modelType, name, namespace,
					reference.getVersion());
		}

		throw new RuntimeException("Malformed imported namespace = "
				+ importedNamespace);
	}

	private Model saveModelResourceToProject(IModelProject project,
			ModelId modelId) {
		MessageDisplayFactory.getMessageDisplay().display("Downloading " + modelId.toString());
		Model model = modelRepo.getModel(modelId);

		saveToProject(project.getProject(), modelToDsl.apply(model),
				getFileName(model, modelId.getModelType()));

		return model;
	}

	private void saveToProject(IProject project, String fileAsString,
			String fileName) {
		assert (project != null);
		assert (fileAsString != null);
		assert (fileName != null);
		try {
			IFolder folder = project.getFolder("src/shared_models/");
			if (!folder.exists()) {
				folder.create(IResource.NONE, true, null);
			}

			IFile file = folder.getFile(fileName);
			if (file.exists()) {
				file.delete(true, new NullProgressMonitor());
			}

			file.create(new ByteArrayInputStream(fileAsString.getBytes()),
					true, new NullProgressMonitor());
		} catch (CoreException e) {
			// TODO : make some noise
			e.printStackTrace();
		}
	}

	private void addReferenceToModel(Model targetModel, Model modelToBeAdded) {
		ModelReference referenceToAdd = toModelReference(modelToBeAdded);
		for (ModelReference modelReference : targetModel.getReferences()) {
			if (EcoreUtil.equals(modelReference, referenceToAdd)) {
				return; // model reference already exists
			}
		}
		targetModel.getReferences().add(referenceToAdd);
		targetModel.eResource().getContents().add(modelToBeAdded);
	}

	private ModelReference toModelReference(Model model) {
		ModelReference reference = ModelFactory.eINSTANCE
				.createModelReference();
		reference.setVersion(model.getVersion());
		reference.setImportedNamespace(model.getNamespace() + "."
				+ model.getName());

		return reference;
	}

	private String getFileName(Model model, ModelType modelType) {
		return model.getName() + modelFileExtensionMap.get(modelType);
	}

	private Map<ModelType, String> initializeExtensionMap() {
		Map<ModelType, String> extensionMap = new HashMap<ModelType, String>();
		extensionMap.put(ModelType.DATATYPE, ".type");
		extensionMap.put(ModelType.FUNCTIONBLOCK, ".fbmodel");
		extensionMap.put(ModelType.INFORMATIONMODEL, ".infomodel");
		return extensionMap;
	}
}
