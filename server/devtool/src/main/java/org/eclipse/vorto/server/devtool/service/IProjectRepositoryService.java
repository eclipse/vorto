/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;

import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.exception.ProjectResourceAlreadyExistsException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;

public interface IProjectRepositoryService {
	
	void checkProjectExists(String sessionId, String projectName) throws ProjectAlreadyExistsException;
	
	void checkResourceExists(String sessionId, String projectName, ProjectResource projectResource) throws ProjectAlreadyExistsException, ProjectResourceAlreadyExistsException, ProjectNotFoundException;
	
	Project createProject(String sessionId, String projectName) throws ProjectAlreadyExistsException;
		
	Project openProject(String sessionId, String projectName) throws ProjectNotFoundException;
	
	ArrayList<Project> getProjects(String sessionId);	
	
	ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) throws ProjectNotFoundException;
	
	void createResource(String sessionId, String projectName, ProjectResource projectResource) throws ProjectNotFoundException, ProjectResourceAlreadyExistsException;
	
	void deleteResource(String sessionId, String projectName, ProjectResource projectResource) throws ProjectNotFoundException;
}
