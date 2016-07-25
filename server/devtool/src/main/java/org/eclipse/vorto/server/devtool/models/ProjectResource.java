package org.eclipse.vorto.server.devtool.models;

public class ProjectResource {
	
	private String resourceId;
	private String name;
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj.getClass().equals(this.getClass())){
			ProjectResource projectResource = (ProjectResource)obj;
			if(projectResource.getResourceId().equals(this.resourceId)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}	
}
