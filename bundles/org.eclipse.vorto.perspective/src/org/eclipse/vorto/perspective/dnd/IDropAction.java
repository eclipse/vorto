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
package org.eclipse.vorto.perspective.dnd;

import org.eclipse.vorto.core.model.IModelProject;

/**
 * Action to be done when a resource is dropped on a target
 *
 */
public interface IDropAction {
	
	/**
	 * Actual action to be executed upon drop
	 * @param receivingProject The project receiving the dropped resource
	 * @param droppedObject The dropped resource
	 * @return
	 */
	boolean performDrop(IModelProject receivingProject, Object droppedObject);
}
