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
import java.util.HashMap;

import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.service.IProjectRespositoryDAO;
import org.springframework.stereotype.Service;

@Service
@Deprecated
public class MapProjectRespositoryDAOImpl implements IProjectRespositoryDAO {

	private HashMap<String, ArrayList<Project>> projectRepositoryHashMap = new HashMap<>();
	
	@Override
	public void createProject(Project project, String sessionId) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null){
			projectList = new ArrayList<>();
		}
		projectRepositoryHashMap.put(sessionId, projectList);
		if(!projectExists(project.getProjectName(), sessionId)){
			projectList.add(project);
			projectRepositoryHashMap.put(sessionId, projectList);
		}
	}

	@Override
	public boolean projectExists(String projectName, String sessionId) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null){
			projectList = new ArrayList<>();
		}
		for(Project project : projectList){
			if(project.getProjectName().equals(projectName)){
				return true;
			}
		}
		return false;
	}

	@Override
	public Project openProject(String projectName, String sessionId) {
		if(projectExists(projectName, sessionId)){
			ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
			for(Project project : projectList){
				if(project.getProjectName().equals(projectName)){
					return project;
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<Project> getProjects(String sessionId) {
		ArrayList<Project> projectList = projectRepositoryHashMap.get(sessionId);
		if(projectList == null){
			projectList = new ArrayList<>();
		}
		return projectList;
	}

}
