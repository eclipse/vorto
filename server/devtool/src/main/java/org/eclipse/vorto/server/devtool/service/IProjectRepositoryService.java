package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;

import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;

public interface IProjectRepositoryService {
	
	void checkProjectExists(String sessionId, String projectName) throws ProjectAlreadyExistsException;
	
	Project createProject(String sessionId, String projectName) throws ProjectAlreadyExistsException;
		
	Project openProject(String sessionId, String projectName) throws ProjectNotFoundException;
	
	ArrayList<Project> getProjects(String sessionId);	
	
	ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) throws ProjectNotFoundException;
	
	void createResource(String sessionId, String projectName, ProjectResource projectResource) throws ProjectNotFoundException;
	
	void deleteResource(String sessionId, String projectName, String resourceId) throws ProjectNotFoundException;
}
