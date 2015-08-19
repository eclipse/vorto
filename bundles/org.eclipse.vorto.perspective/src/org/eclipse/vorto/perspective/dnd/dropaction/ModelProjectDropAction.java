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
package org.eclipse.vorto.perspective.dnd.dropaction;

import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.service.ModelProjectServiceFactory;
import org.eclipse.vorto.perspective.dnd.IDropAction;

/**
 * A drop action for dropping an IModelProject to another IModelProject
 *
 */
public class ModelProjectDropAction implements IDropAction {

	private Class<?> droppedObjectClass = null;
	
	public ModelProjectDropAction(Class<?> droppedObjectClass) {
		this.droppedObjectClass = droppedObjectClass;
	}

	@Override
	public boolean performDrop(IModelProject receivingProject,
			Object droppedObject) {
		IModelProject projectToBeDropped = (IModelProject) droppedObject;

		if (droppedObjectClass.isInstance(projectToBeDropped)
				&& !receivingProject.equals(projectToBeDropped)) {
			receivingProject.addReference(projectToBeDropped);
			ModelProjectServiceFactory.getDefault().save(receivingProject);
			return true;
		}
		
		return false;
	}

}
