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
package org.eclipse.vorto.core.service;

import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.ModelId;
import org.eclipse.vorto.core.model.ModelType;

/**
 * Allows the retrieval of {@link IModelProject}s as well as saving them in case
 * models have been modified.
 * 
 */
public interface IModelProjectService {

	/**
	 * Gets a model project by its project name
	 * 
	 * @param modelProjectName
	 * @return
	 */
	IModelProject getProjectByName(String modelProjectName);

	/**
	 * Gets a model project by its full qualified model Identifier
	 * 
	 * @param modelId
	 * @return
	 */
	IModelProject getProjectByModelId(ModelId modelId);

	/**
	 * Gets a model project for a specified Eclipse {@link IProject}
	 * 
	 * @param eclipseProject
	 * @return
	 */
	IModelProject getProjectFromEclipseProject(IProject eclipseProject);

	/**
	 * Gets a model project from a user selection
	 * 
	 * @return
	 */
	IModelProject getProjectFromSelection();

	/**
	 * Gets all model projects which are open in the workspace.
	 * 
	 * @return
	 */
	Set<IModelProject> getProjectsInWorkspace();

	/**
	 * Gets all model projects which are open in the workspace for a give
	 * modeltype
	 * 
	 * @param modelType
	 * @return
	 */
	Set<IModelProject> getProjectsInWorkspace(ModelType modelType);

	/**
	 * Saves a model project, if it was changed, e.g. references were added.
	 * 
	 * @param modelProject
	 */
	void save(IModelProject modelProject);
}
