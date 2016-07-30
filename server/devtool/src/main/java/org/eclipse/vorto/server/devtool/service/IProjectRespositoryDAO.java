package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;

import org.eclipse.vorto.server.devtool.models.Project;

public interface IProjectRespositoryDAO {
	
	void createProject(Project project, String sessionId);
	
	boolean projectExists(String projectName, String sessionId);
	
	Project openProject(String projectName, String sessionId);
	
	ArrayList<Project> getProjects(String sessionId);

}
