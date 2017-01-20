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

import java.util.ArrayList;

import org.eclipse.vorto.server.devtool.models.Project;

public interface IProjectRespositoryDAO {
	
	void createProject(Project project, String sessionId);
	
	boolean projectExists(String projectName, String sessionId);
	
	Project openProject(String projectName, String sessionId);
	
	ArrayList<Project> getProjects(String sessionId);

}
