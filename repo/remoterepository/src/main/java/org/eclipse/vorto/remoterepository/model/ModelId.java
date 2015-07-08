/*******************************************************************************
 * Copyright (c) 2015 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.remoterepository.model;

/**
 * This class serves as the coordinate of a particular model. 
 * 
 * @author 
 *
 */
public class ModelId {

	private ModelType modelType;
	private String namespace;
	private String version;
	private String name;

	public ModelId(ModelType modelType, String namespace, String version,
			String name) {
		this.modelType = modelType;
		this.namespace = namespace;
		this.version = version;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ModelType getModelType() {
		return modelType;
	}

	public void setModelType(ModelType modelType) {
		this.modelType = modelType;
	}

	@Override
	public String toString() {
		return "ModelID [namespace=" + namespace + ", name=" + name
				+ ", version=" + version + ", modelType=" + modelType + "]";
	}

	public boolean equals(Object obj) {

		if (obj instanceof ModelId) {
			ModelId anotherModelId = (ModelId) obj;
			if (anotherModelId.getName().equalsIgnoreCase(this.getName())
					&& anotherModelId.getNamespace().equalsIgnoreCase(
							this.getNamespace())
					&& anotherModelId.getVersion().equalsIgnoreCase(
							this.getVersion())) {
				return true;
			}
		}
		return false;
	}
}
