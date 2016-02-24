/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.perspective.dnd.dropvalidator;

import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.core.api.repository.ModelResource;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.perspective.dnd.IDropValidator;

/**
 * A drop validator that uses the class of the target, and the model type of the
 * ModelResource to determine if drop is valid.
 *
 */
public class TargetClassModelTypeValidator implements IDropValidator {

	private Class<?> targetClass;
	private ModelType modelType;

	public TargetClassModelTypeValidator(Class<?> targetClass, ModelType modelType) {
		this.targetClass = targetClass;
		this.modelType = modelType;
	}

	public boolean allow(IModelProject receivingProject, Object droppedObject) {
		if (droppedObject instanceof ModelResource && targetClass.isInstance(receivingProject)) {
			ModelResource model = (ModelResource) droppedObject;
			return model.getId().getModelType() == modelType;
		}
		return false;
	}

}
