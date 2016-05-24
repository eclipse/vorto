/*******************************************************************************
 *  Copyright (c) 2015, 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.perspective.dnd.dropaction;

import org.eclipse.vorto.core.ui.model.IModelElement;
import org.eclipse.vorto.perspective.dnd.IDropAction;

/**
 * A drop action for dropping an IModelProject to another IModelProject
 *
 */
public class AddLocalReferenceDropAction implements IDropAction<IModelElement,IModelElement> {

	private Class<?> droppedObjectClass = null;
	
	public AddLocalReferenceDropAction(Class<?> droppedObjectClass) {
		this.droppedObjectClass = droppedObjectClass;
	}

	@Override
	public IModelElement performDrop(IModelElement receivingModelElement,
			IModelElement modelElementToBeDropped) {

		if (droppedObjectClass.isInstance(modelElementToBeDropped)
				&& !receivingModelElement.equals(modelElementToBeDropped)) {
			receivingModelElement.addModelReference(modelElementToBeDropped);
			receivingModelElement.save();
			return receivingModelElement;
		}
		
		return null;
	}

}
