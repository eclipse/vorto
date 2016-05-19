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
package org.eclipse.vorto.core.internal.service;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.internal.model.ModelProjectFactory;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.IModelElementResolver;
import org.eclipse.vorto.core.service.IModelProjectService;

public class DefaultModelProjectService implements IModelProjectService {

	@Override
	public IModelElementResolver getWorkspaceProjectResolver() {
		return new IModelElementResolver() {
			public IModelElement resolve(ModelId modelId) {
				return getProjectByModelId(modelId);
			}
		};
	}
	
	@Override
	public IModelProject getProjectByName(String modelProjectName) {
		return ModelProjectFactory.getInstance().getProjectByName(
				modelProjectName);
	}

	@Override
	public IModelProject getProjectFromEclipseProject(IProject eclipseProject) {
		return ModelProjectFactory.getInstance().getProject(eclipseProject);
	}

	@Override
	public void save(IModelProject modelProject) {
		modelProject.save();
		modelProject.refresh(new NullProgressMonitor());
	}

	@Override
	public Set<IModelProject> getProjectsInWorkspace() {
		Set<IModelProject> modelElements = new TreeSet<IModelProject>();

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();

		for (IProject iProject : projects) {
			try {
				if (iProject.isOpen()) {
					modelElements.add(ModelProjectFactory.getInstance()
							.getProject(iProject));
				}
			} catch (Exception e) {
				continue;
			}
		}
		return modelElements;
	}

	@Override
	public Set<IModelProject> getProjectsInWorkspace(final ModelType modelType) {
		Set<IModelProject> allModelProjectsInWorkspace = getProjectsInWorkspace();
		CollectionUtils.filter(allModelProjectsInWorkspace, new Predicate() {

			@Override
			public boolean evaluate(Object o) {
				IModelProject modelProject = (IModelProject) o;
				return modelProject.getId().getModelType() == modelType;
			}
		});

		return allModelProjectsInWorkspace;
	}

	@Override
	public Set<IModelProject> getModelsInProject(ModelType modelType, IProject project) {
		
		Set<IModelProject> modelElements = new TreeSet<IModelProject>();
		modelElements.add(ModelProjectFactory.getInstance().getProject(project));
		return modelElements;
	}

	@Override
	public IModelProject getProjectFromSelection() {
		return ModelProjectFactory.getInstance().getProjectFromSelection();
	}

	@Override
	public IModelProject getProjectByModelId(ModelId modelId) {
		for (IModelProject modelProject : getProjectsInWorkspace()) {
			if (modelProject.getId().equals(modelId)) {
				return modelProject;
			}
		}
		return null;
	}

}
