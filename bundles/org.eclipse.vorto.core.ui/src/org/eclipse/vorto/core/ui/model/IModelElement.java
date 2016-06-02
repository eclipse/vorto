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
package org.eclipse.vorto.core.ui.model;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Image;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;

public interface IModelElement {

	
	/**
	 * retrieve the wrapping Eclipse {@link IProject}
	 * 
	 * @return IProject project
	 */
	IModelProject getProject();

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
	
	/**
	 * 
	 * @param reference
	 */
	void addModelReference(IModelElement reference);
	
	/**
	 * Adds a model reference to the model. When done, make sure to invoke {@link IModelElement#save()}} to save the model.
	 * @param modelId
	 * @throws ModelNotFoundException if no model can be found in the project with the given modelId
	 * @return the model element that has been added as a reference
	 */
	IModelElement addModelReference(ModelId modelId);
	
	/**
	 * Saves the model element it it has been modified before
	 */
	void save();

}
