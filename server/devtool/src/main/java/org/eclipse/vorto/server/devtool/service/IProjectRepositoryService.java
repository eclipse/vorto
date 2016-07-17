package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.server.devtool.models.Project;

public interface IProjectRepositoryService {
	
	Project createProject(String sessionId, String projectName, ResourceSet resourceSet);
	
	Project openProject(String sessionId, String projectName);
	
	ArrayList<String> getProjects(String sessionId);	
	
	ArrayList<String> getProjectResources(String sessionId, String projectName);
	
	String getProjectResourceContents(String sessionId, String projectName, String resourceId);
}
