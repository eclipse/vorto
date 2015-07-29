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
package org.eclipse.vorto.repository.function;

import org.apache.http.client.fluent.Content;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.repository.ModelContent;
import org.eclipse.vorto.core.model.ModelType;
import org.eclipse.vorto.core.service.IModelTransformerService;

import com.google.common.base.Function;

public class ContentToModelContent<M extends Model> implements
		Function<Content, ModelContent> {

	private IModelTransformerService modelTransformer;
	private Class<M> modelClass;
	private ModelType modelType;

	public ContentToModelContent(Class<M> modelClass, ModelType type,
			IModelTransformerService modelTransformer) {
		this.modelTransformer = modelTransformer;
		this.modelClass = modelClass;
		this.modelType = type;
	}

	public ModelContent apply(Content input) {
		return new ModelContent(input.asBytes(), modelType,
				modelTransformer.xmiToModel(input.asStream(), modelClass), null);
	}

}
