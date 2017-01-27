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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService;
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.exception.ProjectResourceAlreadyExistsException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IEditorProjectService;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.inject.Injector;

@Service
public class XtextEditorProjectService implements IEditorProjectService{
	
	@Autowired
	private Injector injector;
	
	@Autowired
	private IProjectRepositoryService projectRepository;
	
	
	@Override
	public void checkProjectExists(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		if (projectRepository.createQuery().name(projectName).type(ResourceType.ProjectResource).singleResult() != null) {
			throw new ProjectAlreadyExistsException();
		}
	}	
	
	@Override
	public void checkResourceExists(String sessionId, String projectName, ProjectResource projectResource)
			throws ProjectAlreadyExistsException, ProjectResourceAlreadyExistsException, ProjectNotFoundException {
		List<org.eclipse.vorto.devtool.projectrepository.model.Resource> projectResources = this.projectRepository.createQuery()
				.pathLike(projectName).type(ResourceType.FileResource).list(); // only interested in file resources of the project
		
		for (org.eclipse.vorto.devtool.projectrepository.model.Resource resource : projectResources) {
			if (resource.getName().equals(projectResource.getName())) {
				throw new ProjectResourceAlreadyExistsException();
			}
		}
	}
	
	@Override
	public Project createProject(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector.getInstance(IWebResourceSetProvider.class);		
		Project project = new Project(projectName);
		project.setResourceSet(webEditorResourceSetProvider.getNewResourceSet());
		project.setReferencedResourceSet(new HashSet<String>());
		project.setResourceList(new ArrayList<>());
		//FIXME
//		if(!projectRespositoryDAO.projectExists(projectName, sessionId)){
//			projectRespositoryDAO.createProject(project, sessionId);
//			return project;
//		}else{
//			throw new ProjectAlreadyExistsException();
//		}
		return null;
	}

	@Override
	public Project openProject(String sessionId, String projectName) throws ProjectNotFoundException {
		//FIXME
//		Project project = projectRespositoryDAO.openProject(projectName, sessionId);
//		if(project == null){
//			throw new ProjectNotFoundException();			
//		}
//		return project;
		return null;
	}

	@Override
	public ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName);
		return project.getResourceList();
	}
	
	@Override
	public List<Project> getProjects(String sessionId) {
		//FIXME
//		ArrayList<Project> projectList = projectRespositoryDAO.getProjects(sessionId);
//		ArrayList<Project> projectNameList = new ArrayList<>();
//		for(Project iterProject : projectList){
//			Project project = new  Project(iterProject.getProjectName());
//			projectNameList.add(project);
//		}
//		return projectNameList;
		return Collections.emptyList();
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