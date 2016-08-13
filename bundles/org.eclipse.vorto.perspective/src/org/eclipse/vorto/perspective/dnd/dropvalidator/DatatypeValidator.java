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

import org.eclipse.vorto.core.api.model.datatype.Entity;
import org.eclipse.vorto.core.api.model.datatype.Enum;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.ui.model.IModelElement;

public class DatatypeValidator extends TargetClassSourceClassValidator {

	public DatatypeValidator(Class<?> targetClass, Class<?> sourceClass) {
		super(targetClass, sourceClass);
	}

	@Override
	public boolean allow(Object receivingModelElement, Object droppedObject) {
		if (receivingModelElement instanceof IModelElement && droppedObject instanceof IModelElement) {
			boolean entityDroppedToEnum = entityDroppedToEnum(((IModelElement)receivingModelElement).getModel(), ((IModelElement) droppedObject).getModel());
			return super.allow(receivingModelElement, droppedObject) && !entityDroppedToEnum;
		} else {
			return false;
		}
	}

	private boolean entityDroppedToEnum(Model targetModel, Model droppedModel) {
		return targetModel instanceof Enum && droppedModel instanceof Entity;
	}
}
