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

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelType;

/**
 * 
 * A model project wraps an Eclipse {@link IProject} with model specific
 * metadata
 *
 */
public interface IModelProject {

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
		
	List<IModelElement> getModelElements();
	
	List<IModelElement> getModelElementsByType(ModelType modelType);

	IModelElement getModelElementById(ModelId modelId);
	
	List<MappingModel> getMapping(String targetPlatform);
	
	IModelElement addModelElement(ModelId modelId, InputStream inputStream);

	boolean exists(ModelId modelId);
}
