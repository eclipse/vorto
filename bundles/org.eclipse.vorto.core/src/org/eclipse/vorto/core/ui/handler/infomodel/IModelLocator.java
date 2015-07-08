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
package org.eclipse.vorto.core.ui.handler.infomodel;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

/**
 * 
 * Model Locator loads a specific Model Type for a given {@link IProject}
 * 
 */
public interface IModelLocator {

	/**
	 * Locates and loads a model for the given {@link IProject}
	 * 
	 * @param project
	 *            eclipse project to search in
	 * @param modelType
	 *            type of model to look for, e.g. Information Model, Function
	 *            Block Model etc.
	 * @return Model EMF instance
	 */
	<Model extends EObject> Model locate(IProject project,
			Class<Model> modelType);

	/**
	 * Locates and loads a model for the given {@link IProject} with the
	 * specified prefix
	 * 
	 * @param project
	 *            eclipse project to search in
	 * @param prefix
	 *            prefix of the model file name to look for
	 * @param modelType
	 *            type of model to look for, e.g. Information Model, Function
	 *            Block Model etc.
	 * @return Model EMF instance
	 */
	<Model extends EObject> Model locate(IProject project, String prefix,
			Class<Model> modelType);
}
