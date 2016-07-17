package org.eclipse.vorto.server.devtool.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.server.devtool.models.Project;
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
	public ArrayList<String> getProjectResources(String sessionId, String projectName) {
		ArrayList<String> resourceList = new ArrayList<>();
		Project project = openProject(sessionId, projectName);
		ResourceSet resourceSet = project.getResourceSet();
		for(Resource resource : resourceSet.getResources()){
			System.out.println(resource.getURI().toString());
			resourceList.add(resource.getURI().toString());
		}
		return resourceList;
	}
	
	@Override
	public String getProjectResourceContents(String sessionId, String projectName, String resourceId) {
		Project project = openProject(sessionId, projectName);
		ResourceSet resourceSet = project.getResourceSet();
		URI uri = URI.createURI(resourceId);
		Resource resource = resourceSet.getResource(uri, true);
		if(resource == null){
			throw new RuntimeException("No resource with resourceId : " + resourceId);
		}
		try{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			resource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		}catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public ArrayList<String> getProjects(String sessionId) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null || projectList.isEmpty()){
			projectList = new ArrayList<>();
		}
		ArrayList<String> projectNameList = new ArrayList<>();
		for(Project project : projectList){
			projectNameList.add(project.getProjectName());
		}
		return projectNameList;
	}	
}
