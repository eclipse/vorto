package org.eclipse.vorto.server.devtool.models;

public class ProjectResource {

	private String resourceId;
	private String name;
	private String version;
	private String namespace;

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
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			ProjectResource projectResource = (ProjectResource) obj;
			if (projectResource.getResourceId().equals(this.resourceId)) {
//				if (projectResource.getName().endsWith(this.name)) {
//					return true;
//				}
				return true;
			}
		}
		return false;
	}
}
