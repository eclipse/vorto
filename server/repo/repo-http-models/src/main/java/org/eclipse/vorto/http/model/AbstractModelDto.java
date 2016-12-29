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
package org.eclipse.vorto.http.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractModelDto {
	
	protected ModelIdDto id;
	protected ModelTypeDto modelType;
	protected String displayName;
	protected String description;
	
	protected List<ModelIdDto> references = new ArrayList<>();
	
	public AbstractModelDto(ModelIdDto modelId,ModelTypeDto modelType) {
		this.id = modelId;
		this.modelType = modelType;
	}

	public ModelIdDto getId() {
		return id;
	}

	public void setId(ModelIdDto id) {
		this.id = id;
	}

	public ModelTypeDto getModelType() {
		return modelType;
	}

	public void setModelType(ModelTypeDto modelType) {
		this.modelType = modelType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ModelIdDto> getReferences() {
		return references;
	}

	public void setReferences(List<ModelIdDto> references) {
		this.references = references;
	}
	
	
}
