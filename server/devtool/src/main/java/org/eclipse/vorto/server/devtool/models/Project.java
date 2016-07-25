package org.eclipse.vorto.server.devtool.models;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.emf.ecore.resource.ResourceSet;

public class Project {
	
	private String projectName;	
	private String sessionId; //change this later to author or login id
	private ResourceSet resourceSet;
	private HashSet<String> referencedResourceSet;	
	private ArrayList<ProjectResource> resourceList;
	
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

	public ArrayList<ProjectResource> getResourceList() {
		return resourceList;
	}

	public void setResourceList(ArrayList<ProjectResource> resourceList) {
		this.resourceList = resourceList;
	}		
}
