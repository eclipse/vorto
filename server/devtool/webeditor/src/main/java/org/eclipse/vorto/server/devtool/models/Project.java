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
package org.eclipse.vorto.server.devtool.models;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.emf.ecore.resource.ResourceSet;

public class Project {
	
	private String projectName;	
	private String sessionId; //change this later to author or login id
	private ResourceSet resourceSet;
	private HashSet<String> referencedResourceSet;	
	private ArrayList<ModelResource> resourceList;
	
	public Project(){
		
	}
	
	public Project(String projectName){
		this.projectName = projectName;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public ResourceSet getResourceSet() {
		return resourceSet;
	}

	public void setResourceSet(ResourceSet projectResourceSet) {
		this.resourceSet = projectResourceSet;
	}

	public HashSet<String> getReferencedResourceSet() {
		return referencedResourceSet;
	}

	public void setReferencedResourceSet(HashSet<String> referencedResourceSet) {
		this.referencedResourceSet = referencedResourceSet;
	}

	public ArrayList<ModelResource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(ArrayList<ModelResource> resourceList) {
		this.resourceList = resourceList;
	}		
}
