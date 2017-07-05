/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.core.api.repository;

import java.util.List;

import org.eclipse.vorto.core.api.model.model.ModelId;

/**
 * A lightweight view of the resources in the Repository
 */
public class ModelResource {
	private ModelId id;
	private String description;
	private String displayName;
	private List<ModelId> references;
	private List<ModelId> referencedBy;

	public ModelResource(ModelId id, String description, String displayName,
			List<ModelId> references) {
		this.id = id;
		this.description = description;
		this.displayName = displayName;
		this.references = references;
	}
	
	public ModelResource(ModelId id, String description, String displayName,
			List<ModelId> references, List<ModelId> referencedBy) {
		this(id, description, displayName, references);
		this.referencedBy = referencedBy;
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
	public List<ModelId> getReferences() {
		return references;
	}
	
	/**
	 * 
	 * @return all models that references this model
	 */
	public List<ModelId> getReferencedBy() {
		return referencedBy;
	}

	@Override
	public String toString() {
		return "ModelResource [id=" + id + ", description=" + description
				+ ", displayName=" + displayName + ", references=" + references 
				+ ", referencedBy=" + referencedBy + "]";
	}
}