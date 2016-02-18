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
package org.eclipse.vorto.core.internal.service;

import org.eclipse.core.resources.IFile;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.model.IModelElement;
import org.eclipse.vorto.core.model.IModelProject;
import org.eclipse.vorto.core.model.SharedDatatypeModelElement;
import org.eclipse.vorto.core.model.SharedFunctionblockModelElement;
import org.eclipse.vorto.core.model.SharedInformationModelElement;
import org.eclipse.vorto.core.service.IModelElementResolver;
import org.eclipse.vorto.core.service.ISharedModelService;

public class DefaultSharedModelService implements ISharedModelService {

	public IModelElementResolver getSharedModelResolver(final IModelProject project) {
		return new IModelElementResolver() {
			public IModelElement resolve(ModelId modelId) {
				return project.getSharedModelReference(modelId);
			}
		};
	}
	
	public IModelElement createSharedModelElement(IModelProject project, IFile modelFile, Model model) {
		if (SharedInformationModelElement.appliesTo(model)){
			return new SharedInformationModelElement(project, modelFile, model);
		} else if (SharedFunctionblockModelElement.appliesTo(model)) {
			return new SharedFunctionblockModelElement(project, modelFile, model);
		} else {
			return new SharedDatatypeModelElement(project, modelFile, model);
		}
	}

}
