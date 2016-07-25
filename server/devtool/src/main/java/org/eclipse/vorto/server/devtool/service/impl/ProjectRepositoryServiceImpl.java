package org.eclipse.vorto.server.devtool.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
import org.springframework.stereotype.Service;

@Service
public class ProjectRepositoryServiceImpl implements IProjectRepositoryService{

	private HashMap<String, ArrayList<Project>> projectRepositoryHashMap = new HashMap<>();
	
	@Override
	public Project createProject(String sessionId, String projectName, ResourceSet resourceSet) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null || projectList.isEmpty()){
			projectList = new ArrayList<>();
		}
		for(Project project : projectList){
			if(project.getProjectName().equals(projectName)){
				throw new RuntimeException("The project " + projectName + " already exists. Please choose another name");
			}
		}
		Project project = new Project(projectName);
		project.setResourceSet(resourceSet);
		project.setReferencedResourceSet(new HashSet<String>());
		project.setResourceList(new ArrayList<>());
		projectList.add(project);
		projectRepositoryHashMap.put(sessionId, projectList);
		return project;
	}

	@Override
	public Project openProject(String sessionId, String projectName) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null || projectList.isEmpty()){
			throw new RuntimeException("The project with name : " + projectName + " does not exist");
		}
		for(Project project : projectList){
			if(project.getProjectName().equals(projectName)){
				return project;
			}
		}
		throw new RuntimeException("The project with name : " + projectName + " does not exist");
	}

	@Override
	public ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) {
		Project project = openProject(sessionId, projectName);
		return project.getResourceList();
	}
	
	@Override
	public ArrayList<Project> getProjects(String sessionId) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null || projectList.isEmpty()){
			projectList = new ArrayList<>();
		}
		ArrayList<Project> projectNameList = new ArrayList<>();
		for(Project iterProject : projectList){
			Project project = new  Project(iterProject.getProjectName());
			projectNameList.add(project);
		}
		return projectNameList;
	}

	@Override
	public void createResource(String sessionId, String projectName, String resourceName, String resourceId) {
		Project project = openProject(sessionId, projectName);
		ArrayList<ProjectResource> resourceList = project.getResourceList();
		URI uri = URI.createURI(resourceId);
		ResourceSet resourceSet = project.getResourceSet();
		Resource resource = resourceSet.getResource(uri, true);
		if(resource == null){
			throw new NullPointerException("The editor for resource id : " + resourceId + " does not exist");
		}
		ProjectResource projectResource = new ProjectResource();
		projectResource.setResourceId(resourceId);
		projectResource.setName(resourceName);
		resourceList.add(projectResource);
	}

	@Override
	public void deleteResource(String sessionId, String projectName, String resourceId) {
		Project project = openProject(sessionId, projectName);		
		URI uri = URI.createURI(resourceId);
		ResourceSet resourceSet = project.getResourceSet();
		Resource resource = resourceSet.getResource(uri, true);
		resourceSet.getResources().remove(resource);
		ProjectResource projectResource = new ProjectResource();
		projectResource.setResourceId(resourceId);
		project.getResourceList().remove(projectResource);
	}	
}