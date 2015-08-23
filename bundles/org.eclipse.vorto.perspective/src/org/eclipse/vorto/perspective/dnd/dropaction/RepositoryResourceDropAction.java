/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
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
import org.eclipse.vorto.codegen.ui.display.MessageDisplayFactory;
import org.eclipse.vorto.core.api.repository.IModelRepository;
import org.eclipse.vorto.core.api.repository.ModelRepositoryFactory;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.core.service.IModelProjectService;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.IDropAction;

/**
 * A drop action for dropping a Model Resource from Repository view to an
 * IModelProject
 *
 */
public class RepositoryResourceDropAction implements IDropAction {

	private static final String INFOMODEL_EXT = ".infomodel";

	private static final String FBMODEL_EXT = ".fbmodel";

	private static final String TYPE_EXT = ".type";

	private static final String SHARED_MODELS_DIR = "src/shared_models/";

	private static final String SHARED_MODEL_IS_PROJ_ERROR = "Cannot copy shared model %s to %s, a local project already exist for shared model.";

	private static final String SAVING = "Saving model to %s";

	private IModelRepository modelRepo = ModelRepositoryFactory.getModelRepository();

	private IModelProjectService projectService = ModelProjectServiceFactory.getDefault();

	private Map<ModelType, String> modelFileExtensionMap = initializeExtensionMap();

	@Override
	public boolean performDrop(IModelProject receivingProject, Object droppedObject) {
		Objects.requireNonNull(receivingProject, "receivingProject shouldn't be null.");
		Objects.requireNonNull(droppedObject, "droppedObject shouldn't be null.");

		ModelResource modelResource = (ModelResource) droppedObject;

		ModelResource droppedObjectModel = downloadAndSaveModel(receivingProject.getProject(), modelResource.getId());

		if (droppedObjectModel != null) {
			receivingProject.addReference(droppedObjectModel.getId());
			ModelProjectServiceFactory.getDefault().save(receivingProject);
		}

		return true;
	}

	// Download and save model from repository to local project.
	// It also recursively do the same for the model references.
	private ModelResource downloadAndSaveModel(IProject project, ModelId modelId) {
		// Check if we have a local project with that same modelId
		if (projectService.getProjectByModelId(modelId) == null) {
			MessageDisplayFactory.getMessageDisplay().display("Downloading " + modelId.toString());

			ModelResource model = modelRepo.getModel(modelId);
			if (model != null) {
				byte[] modelContent = modelRepo.downloadContent(model.getId());
				// Download references also
				for (ModelId reference : model.getReferences()) {
					downloadAndSaveModel(project, reference);
				}

				saveToProject(project, modelContent, getFileName(model, modelId.getModelType()));
			} else {
				MessageDisplayFactory.getMessageDisplay().displayError(
						"Model " + modelId.toString() + " not found in repository.");
			}

			return model;
		} else {
			MessageDisplayFactory.getMessageDisplay().displayError(
					String.format(SHARED_MODEL_IS_PROJ_ERROR, modelId.toString(), project.getName()));
			return null;
		}
	}

	private void saveToProject(IProject project, byte[] modelContent, String fileName) {
		assert (project != null);
		assert (modelContent != null);
		assert (fileName != null);
		try {
			IFolder folder = project.getFolder(SHARED_MODELS_DIR);
			if (!folder.exists()) {
				folder.create(IResource.NONE, true, null);
			}

			IFile file = folder.getFile(fileName);
			if (file.exists()) {
				file.delete(true, new NullProgressMonitor());
			}

			MessageDisplayFactory.getMessageDisplay().display(String.format(SAVING, file.getFullPath().toString()));
			file.create(new ByteArrayInputStream(modelContent), true, new NullProgressMonitor());
		} catch (CoreException e) {
			MessageDisplayFactory.getMessageDisplay().displayError(e);
		}
	}

	private String getFileName(ModelResource model, ModelType modelType) {
		return model.getId().getName() + modelFileExtensionMap.get(modelType);
	}

	private Map<ModelType, String> initializeExtensionMap() {
		Map<ModelType, String> extensionMap = new HashMap<ModelType, String>();
		extensionMap.put(ModelType.Datatype, TYPE_EXT);
		extensionMap.put(ModelType.Functionblock, FBMODEL_EXT);
		extensionMap.put(ModelType.InformationModel, INFOMODEL_EXT);
		return extensionMap;
	}
}
