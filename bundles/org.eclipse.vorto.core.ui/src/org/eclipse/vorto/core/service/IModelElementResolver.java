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
package org.eclipse.vorto.core.service;

import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.ModelId;

/**
 * An interface for a class that can resolve an IModelElement based on the
 * modelId
 *
 */
public interface IModelElementResolver {
	
	/**
	 * Resolve this modelId into an IModelElement
	 * @param modelId
	 * @return
	 */
	IModelElement resolve(ModelId modelId);
}
