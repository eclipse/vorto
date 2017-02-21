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
package org.eclipse.vorto.repository.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;

public class ModelView {
	private ModelId id = null;
	private ModelType type = null;
	private String description = null;
	private String displayName = null;
	private List<ModelId> references = new ArrayList<>();
	private List<ModelId> referencedBy = new ArrayList<>();

	public ModelId getId() {
		return id;
	}

	public void setId(ModelId id) {
		this.id = id;
	}

	public ModelType getType() {
		return type;
	}

	public void setType(ModelType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<ModelId> getReferences() {
		return references;
	}

	public void setReferences(List<ModelId> references) {
		this.references = references;
	}

	public List<ModelId> getReferencedBy() {
		return referencedBy;
	}
}
