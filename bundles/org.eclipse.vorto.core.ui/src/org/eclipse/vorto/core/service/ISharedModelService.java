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

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.IModelProject;

/**
 * 
 * Services for the creation and resolving of shared model elements
 */
public interface ISharedModelService {

	/**
	 * Returns a shared model element resolver given a project
	 * @param project
	 * @return
	 */
	IModelElementResolver getSharedModelResolver(IModelProject project);
	
	/**
	 * Create a shared model element based on the project, model file, and model
	 * 
	 * @param project
	 * @param modelFile
	 * @param model
	 * @return
	 */
	IModelElement createSharedModelElement(IModelProject project, IFile modelFile, Model model);
}
