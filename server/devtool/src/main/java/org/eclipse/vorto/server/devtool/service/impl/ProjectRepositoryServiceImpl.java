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
package org.eclipse.vorto.server.devtool.service.impl;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.exception.ProjectResourceAlreadyExistsException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
import org.eclipse.vorto.server.devtool.service.IProjectRespositoryDAO;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.inject.Injector;

@Component
public class ProjectRepositoryServiceImpl implements IProjectRepositoryService{
	
	@Autowired
	Injector injector;
	
	@Autowired
	IProjectRespositoryDAO projectRespositoryDAO;
	
	@Override
	public void checkProjectExists(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		if(projectRespositoryDAO.projectExists(projectName, sessionId)){
			throw new ProjectAlreadyExistsException();
		}
	}	
	
	@Override
	public void checkResourceExists(String sessionId, String projectName, ProjectResource projectResource)
			throws ProjectAlreadyExistsException, ProjectResourceAlreadyExistsException, ProjectNotFoundException {
		Project project = openProject(sessionId, projectName);
		if(project.getResourceList().contains(projectResource)){
			throw new ProjectResourceAlreadyExistsException();
		}
	}
	
	@Override
	public Project createProject(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector.getInstance(IWebResourceSetProvider.class);		
		Project project = new Project(projectName);
		project.setResourceSet(webEditorResourceSetProvider.getNewResourceSet());
		project.setReferencedResourceSet(new HashSet<String>());
		project.setResourceList(new ArrayList<>());
		if(!projectRespositoryDAO.projectExists(projectName, sessionId)){
			projectRespositoryDAO.createProject(project, sessionId);
			return project;
		}else{
			throw new ProjectAlreadyExistsException();
		}
	}

	@Override
	public Project openProject(String sessionId, String projectName) throws ProjectNotFoundException {
		Project project = projectRespositoryDAO.openProject(projectName, sessionId);
		if(project == null){
			throw new ProjectNotFoundException();			
		}
		return project;
	}

	@Override
	public ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName);
		return project.getResourceList();
	}
	
	@Override
	public ArrayList<Project> getProjects(String sessionId) {
		ArrayList<Project> projectList = projectRespositoryDAO.getProjects(sessionId);
		ArrayList<Project> projectNameList = new ArrayList<>();
		for(Project iterProject : projectList){
			Project project = new  Project(iterProject.getProjectName());
			projectNameList.add(project);
		}
		return projectNameList;
	}

	@Override
	public void createResource(String sessionId, String projectName, ProjectResource projectResource) throws ProjectNotFoundException, ProjectResourceAlreadyExistsException {
		Project project = openProject(sessionId, projectName);
		ArrayList<ProjectResource> resourceList = project.getResourceList();
		if(resourceList.contains(projectResource)){
			throw new ProjectResourceAlreadyExistsException();
		}
		resourceList.add(projectResource);
	}

	@Override
	public void deleteResource(String sessionId, String projectName, ProjectResource projectResource) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName) ;		
		ProjectResource pResource = getProjectResource(projectResource, project.getResourceList());
		if(pResource != null){
			String resourceId = pResource.getResourceId();
			URI uri = URI.createURI(resourceId);
			ResourceSet resourceSet = project.getResourceSet();
			Resource resource = resourceSet.getResource(uri, true);
			resourceSet.getResources().remove(resource);
			projectResource.setResourceId(resourceId);
			project.getResourceList().remove(projectResource);
		}
	}
	
	private ProjectResource getProjectResource(ProjectResource projectResource, ArrayList<ProjectResource> resourceList){
		for(ProjectResource pResource : resourceList){
			if(pResource.equals(projectResource)){
				return pResource;
			}
		}
		return null;
	}

}