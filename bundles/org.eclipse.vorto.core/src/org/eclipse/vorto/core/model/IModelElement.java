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
package org.eclipse.vorto.core.model;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.Model;

public interface IModelElement {

	/**
	 * 
	 * @return the id of the model
	 */
	ModelId getId();

	/**
	 * 
	 * @return the model file
	 */
	IFile getModelFile();

	/**
	 * 
	 * @return model instance
	 */
	Model getModel();

	/**
	 * 
	 * @return a description of the model
	 */
	String getDescription();

	/**
	 * 
	 * @return image of the model
	 */
	Image getImage();

	/**
	 * 
	 * @return all references of the model
	 */
	Set<IModelElement> getReferences();

}
