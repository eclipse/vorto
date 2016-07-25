package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;

public interface IProjectRepositoryService {
	
	Project createProject(String sessionId, String projectName, ResourceSet resourceSet);
	
	Project openProject(String sessionId, String projectName);
	
	ArrayList<Project> getProjects(String sessionId);	
	
	ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName);
	
	void createResource(String sessionId, String projectName, String resourceName, String resourceId);
	
	void deleteResource(String sessionId, String projectName, String resourceId);
}
