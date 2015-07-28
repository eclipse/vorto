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
package org.eclipse.vorto.core.api.repository;

import java.util.List;

import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.ModelType;

/**
 *	This class represents a resource that we can get from
 *  the repository. 
 */
public interface IModelContent {
	/**
	 * The actual content in byte array of this resource
	 * @return
	 */
	byte[] getModelContent();

	/**
	 * Returns the EMF metamodel of this resource
	 * 
	 * @return
	 */
	Model getModel();
	
	/**
	 * Returns the type of this resource
	 * @return
	 */
	ModelType getType();

	/**
	 * Returns the dependencies of this resource
	 * @return
	 */
	List<IModelContent> getReferences();
}

