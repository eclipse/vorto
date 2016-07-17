package org.eclipse.vorto.server.devtool.models;

import java.util.HashSet;

import org.eclipse.emf.ecore.resource.ResourceSet;

public class Project {
	
	private String projectName;	
	private String sessionId; //change this later to author
	private ResourceSet resourceSet;
	private HashSet<String> referencedResourceSet;
	
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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || !obj.getClass().equals(Project.class)){
			return false;
		}
		Project project = (Project)obj;
		if(this.getProjectName().equals(project.getProjectName())){
			return true;
		}else{
			return false;
		}
	}
	
}
