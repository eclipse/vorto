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

import java.util.Collections;

import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.repository.model.ModelView;

import com.google.common.base.Function;

public class ModelViewToModelResource implements
		Function<ModelView, ModelResource> {

	public ModelResource apply(ModelView input) {
		return new ModelResource(input.getModelId(), input.getDescription(),
				input.getModelId().getName(),
				Collections.<ModelResource> emptyList());
	}

}
