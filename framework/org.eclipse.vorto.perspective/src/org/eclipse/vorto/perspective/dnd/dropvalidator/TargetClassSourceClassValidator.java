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

import java.util.Objects;

import org.eclipse.vorto.perspective.dnd.IDropValidator;

/**
 * Drop validator that uses the class of Target and Source to determine if drop
 * is valid
 *
 */
public class TargetClassSourceClassValidator implements IDropValidator {

	private Class<?> targetClass;
	private Class<?> sourceClass;
	
	public TargetClassSourceClassValidator(Class<?> targetClass,
			Class<?> sourceClass) {
		this.targetClass = Objects.requireNonNull(targetClass);
		this.sourceClass = Objects.requireNonNull(sourceClass);
	}

	public boolean allow(Object receivingModelElement, Object droppedObject) {
		return targetClass.isInstance(receivingModelElement) && sourceClass.isInstance(droppedObject);
	}

}
