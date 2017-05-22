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

import java.util.List;

import org.eclipse.vorto.devtool.projectrepository.model.ProjectResource;
import org.eclipse.vorto.devtool.projectrepository.model.Resource;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.models.ModelResource;

public interface IProjectService {
			
	ProjectResource createProject(String projectName, String author);
			
	List<Resource> getProjects(String author);	
	
	List<Resource> getProjectResources(String projectName);
	
	Resource createProjectResource(String projectName, ModelResource modelResource);

	Resource getReferencedResource(ModelInfo modelInfo);
	
	Resource checkResourceExists(Resource resource);
	
	void deleteResource(String projectName, String resourceId);
}
