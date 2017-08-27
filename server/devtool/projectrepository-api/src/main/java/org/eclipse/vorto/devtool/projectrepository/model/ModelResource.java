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
package org.eclipse.vorto.devtool.projectrepository.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants;
import org.eclipse.vorto.repository.api.ModelType;

public class ModelResource extends FileResource {

	private String version;
	private String namespace;
	private String modelSubType;
	private ModelType modelType;
	private String description;
	private byte[] content;

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getModelSubType() {
		return modelSubType;
	}

	public void setSubType(String modelSubType) {
		this.modelSubType = modelSubType;
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

	public ModelType getModelType() {
		return modelType;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getFileName() {
		return this.namespace.replace(ProjectRepositoryConstants.DOT, ProjectRepositoryConstants.UNDERSCORE) + "-"
				+ getName() + "-"
				+ this.version.replace(ProjectRepositoryConstants.DOT, ProjectRepositoryConstants.UNDERSCORE);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(this.getClass())) {
			ModelResource projectResource = (ModelResource) obj;
			if (getName().equals(projectResource.getName()) && this.namespace.equals(projectResource.namespace)
					&& this.version.equals(projectResource.version)) {
				return true;
			}
		}
		return false;
	}

	public Map<String, String> getProperties() {
		HashMap<String, String> properties = (HashMap<String, String>) super.getProperties();
		properties.put(ProjectRepositoryConstants.META_PROPERTY_NAMESPACE, this.namespace);
		properties.put(ProjectRepositoryConstants.META_PROPERTY_NAME, getName());
		properties.put(ProjectRepositoryConstants.META_PROPERTY_VERSION, this.version);
		properties.put(ProjectRepositoryConstants.META_PROPERTY_MODEL_TYPE, this.modelType.toString());
		properties.put(ProjectRepositoryConstants.META_PROPERTY_MODEL_SUB_TYPE, this.modelSubType);
		return properties;
	}
}
