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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;

/**
 * 
 * A model project wraps an Eclipse {@link IProject} with model specific
 * metadata
 *
 */
public interface IModelProject extends IModelElement {

	/**
	 * retrieve the wrapping Eclipse {@link IProject}
	 * 
	 * @return IProject project
	 */
	IProject getProject();

	/**
	 * refreshes the project and its content
	 * 
	 * @param monitor
	 *            callback to give feedback to the user while it is refreshing
	 * @throws CoreException
	 */
	void refresh(IProgressMonitor monitor);

	/**
	 * 
	 * @param reference
	 */
	void addReference(IModelElement reference);
	
	/**
	 * Returns a reference to the shared model under the
	 * src/shared_models given by modelId. 
	 */
	IModelElement getSharedModelReference(ModelId modelId);

	/**
	 * 
	 * @param name
	 * @return
	 */
	MappingModel getMapping(String name);

	/**
	 * Saves the actual model project, after it has been modified, e.g. after
	 * {@link IModelProject#addReference(IModelElement)}
	 */
	void save();

}
