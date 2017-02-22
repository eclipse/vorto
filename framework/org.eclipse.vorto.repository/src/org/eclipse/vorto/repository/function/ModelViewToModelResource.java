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
package org.eclipse.vorto.repository.function;

import java.util.List;

import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelIdFactory;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.repository.model.ModelView;

import com.google.common.base.Function;

public class ModelViewToModelResource implements Function<ModelView, ModelResource> {

	public ModelResource apply(ModelView input) {
		ModelId modelId = ModelIdFactory.newInstance(input.getType(), input.getId().getNamespace(), input.getId()
				.getVersion(), input.getId().getName());
		return new ModelResource(modelId, input.getDescription(), input.getDisplayName(), transformReferences(input,
				input.getReferences()), input.getReferencedBy());
	}

	private List<ModelId> transformReferences(ModelView modelView, List<ModelId> references) {
		for (ModelId modelId : references) {
			modelId.setModelType(getReferenceModelType(modelView.getType()));
		}
		return references;
	}

	private ModelType getReferenceModelType(ModelType parentModelType) {
		if (parentModelType == ModelType.InformationModel) {
			return ModelType.Functionblock;
		} else if (parentModelType == ModelType.Functionblock) {
			return ModelType.Datatype;
		}
		return ModelType.Datatype;
	}

}
