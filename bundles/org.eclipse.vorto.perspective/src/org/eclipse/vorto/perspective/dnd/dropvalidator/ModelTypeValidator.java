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
package org.eclipse.vorto.perspective.dnd.dropvalidator;

import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.perspective.dnd.IDropValidator;

public class ModelTypeValidator implements IDropValidator {

	private ModelType modelType;
	
	public ModelTypeValidator(ModelType modelType) {
		this.modelType = modelType;
	}
	
	@Override
	public boolean allow(Object receivingElement, Object droppedObject) {
		if (receivingElement == null && droppedObject instanceof ModelResource) {
			ModelResource model = (ModelResource) droppedObject;
			return model.getId().getModelType() == modelType;
		}
		return false;
	}

}
