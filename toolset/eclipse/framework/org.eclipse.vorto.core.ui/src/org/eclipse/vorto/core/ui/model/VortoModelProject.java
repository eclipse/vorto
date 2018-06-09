/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.core.ui.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.ui.model.nature.VortoProjectNature;
import org.eclipse.vorto.core.ui.parser.IModelParser;

public class VortoModelProject implements IModelProject {

	protected IProject project;

	private static final String FOLDER_DATATYPES = "datatypes";
	private static final String FOLDER_FUNCTIONBLOCKS = "functionblocks";
	private static final String FOLDER_INFOMODELS = "infomodels";
	private static final String FOLDER_MAPPINGS = "mappings";

	private IModelParser modelParser;

	public VortoModelProject(IProject project, IModelParser modelParser) {
		this.project = project;
		this.modelParser = modelParser;
	}

	public static boolean isVortoModelProject(IProject project) {
		try {
			return project.getNature(VortoProjectNature.VORTO_NATURE) != null;
		} catch (CoreException e) {
			return false;
		}
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public void refresh(IProgressMonitor monitor) {
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException e) {
			// ignore exception
		}
	}

	@Override
	public List<IModelElement> getModelElements() {
		return Collections.emptyList();
	}

	@Override
	public List<IModelElement> getModelElementsByType(ModelType modelType) {
		List<IModelElement> result = new ArrayList<>();
		IResource[] members;
		try {
			members = getFolderByType(modelType).members();
			for (IResource member : members) {
				result.add(createModelElement(modelType, (IFile) member));
			}
		} catch (CoreException e) {
			throw new ModelNotFoundException("Invalid model file found", e, project);
		}
		return result;
	}

	private IModelElement createModelElement(ModelType modelType, IFile member) {
		if (modelType == ModelType.Datatype) {
			return new DatatypeModelElement(this, member, modelParser);
		} else if (modelType == ModelType.Functionblock) {
			return new FunctionblockModelElement(this, member, modelParser);
		} else if (modelType == ModelType.InformationModel) {
			return new InformationModelElement(this, member, modelParser);
		} else {
			return new MappingModelElement(this, member, modelParser);
		}
	}

	private IFolder getFolderByType(ModelType modelType) {
		if (modelType == ModelType.Datatype) {
			return this.project.getFolder(FOLDER_DATATYPES);
		} else if (modelType == ModelType.Functionblock) {
			return this.project.getFolder(FOLDER_FUNCTIONBLOCKS);
		} else if (modelType == ModelType.InformationModel) {
			return this.project.getFolder(FOLDER_INFOMODELS);
		} else if (modelType == ModelType.Mapping) {
			return this.project.getFolder(FOLDER_MAPPINGS);
		} else {
			throw new UnsupportedOperationException(modelType + " cannot be resolved to a folder in the project");
		}
	}

	@Override
	public IModelElement getModelElementById(ModelId modelId) {
		for (IModelElement modelElement : getModelElementsByType(modelId.getModelType())) {
			if (modelElement.getId().equals(modelId)) {
				return modelElement;
			}
		}
		return null;
	}

	@Override
	public List<MappingModel> getMapping(String targetPlatform) {
		List<MappingModel> mappingModels = new ArrayList<MappingModel>();
		for (IModelElement modelElement : getModelElementsByType(ModelType.Mapping)) {
			if (((MappingModel) modelElement.getModel()).getTargetPlatform().equalsIgnoreCase(targetPlatform)) {
				mappingModels.add((MappingModel) modelElement.getModel());
			}
		}
		return mappingModels;

	}

	@Override
	public IModelElement addModelElement(ModelId modelId, InputStream inputStream) {

		try {
			final ModelType modelType = modelId.getModelType();
			IFolder folder = getFolderByType(modelId.getModelType());

			IFile file = folder.getFile(modelId.getFileName());

			if (file.exists()) {
				file.delete(true, new NullProgressMonitor());
			}

			file.create(inputStream, true, new NullProgressMonitor());

			if (modelType == ModelType.InformationModel) {
				return new InformationModelElement(this, file, modelParser);
			} else if (modelType == ModelType.Functionblock) {
				return new FunctionblockModelElement(this, file, modelParser);
			} else if (modelType == ModelType.Datatype) {
				return new DatatypeModelElement(this, file, modelParser);
			} else if (modelType == ModelType.Mapping) {
				return new MappingModelElement(this, file, modelParser);
			} else {
				throw new UnsupportedOperationException("Modeltype is not supported");
			}

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

	}

	@Override
	public boolean exists(ModelId modelId) {
		return getModelElementById(modelId) != null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VortoModelProject other = (VortoModelProject) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}
}
