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
package org.eclipse.vorto.core.api.repository;

import java.util.List;

import org.eclipse.vorto.core.model.ModelId;

/**
 * A lightweight view of the resources in the Repository
 */
public class ModelResource {
	private ModelId id;
	private String description;
	private String displayName;
	private List<ModelResource> references;

	public ModelResource(ModelId id, String description, String displayName,
			List<ModelResource> references) {
		this.id = id;
		this.description = description;
		this.displayName = displayName;
		this.references = references;
	}

	/**
	 * Returns the ID of the resource
	 * 
	 * @return
	 */
	public ModelId getId() {
		return id;
	}

	/**
	 * Returns the description of the resource
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the displayName of the resource
	 * 
	 * @return
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * 
	 * @return all references that the model resources dependents on
	 */
	public List<ModelResource> getReferences() {
		return references;
	}

	@Override
	public String toString() {
		return "ModelResource [id=" + id + ", description=" + description
				+ ", displayName=" + displayName + ", references=" + references
				+ "]";
	}
}