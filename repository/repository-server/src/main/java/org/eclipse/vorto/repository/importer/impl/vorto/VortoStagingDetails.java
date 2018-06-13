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
package org.eclipse.vorto.repository.importer.impl.vorto;

import java.util.List;

import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;

public class VortoStagingDetails {
	private ModelInfo modelResource;
	private List<ModelId> unresolvedReferences;

	public VortoStagingDetails(ModelInfo model) {
		this.modelResource = model;
	}

	public VortoStagingDetails(ModelInfo model, List<ModelId> missingReferences) {
		this.modelResource = model;
		this.unresolvedReferences = missingReferences;
	}

	public ModelInfo getModelResource() {
		return modelResource;
	}

	public void setModelResource(ModelInfo modelResource) {
		this.modelResource = modelResource;
	}

	public List<ModelId> getUnresolvedReferences() {
		return unresolvedReferences;
	}

	public void setUnresolvedReferences(List<ModelId> unresolvedReferences) {
		this.unresolvedReferences = unresolvedReferences;
	}

}
