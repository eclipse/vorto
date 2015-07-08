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

import java.util.List;

/**
 * A lightweight view of a model. Used when the actual content isn't needed.
 * 
 *
 */
public class ModelView {

	private ModelId modelId;
	private String description;
	private List<ModelReference> referenceModels;

	public ModelView(ModelId modelId, String description) {
		this.modelId = modelId;
		this.description = description;
	}

	public ModelId getModelId() {
		return modelId;
	}

	public void setModelId(ModelId modelId) {
		this.modelId = modelId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return "ModelView [modelId=" + modelId + ", description=" + description
				+ "]";
	}

	public List<ModelReference> getReferenceModels() {
		return referenceModels;
	}

	public void setReferenceModels(List<ModelReference> referenceModels) {
		this.referenceModels = referenceModels;
	}
}
