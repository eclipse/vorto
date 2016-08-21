/*******************************************************************************
 * Copyright (c) 2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.server.devtool.service.editor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class IEditorService {

	@Autowired
	DevtoolRestClient devtoolRestClient;
	
	public abstract String linkModelToResource(String infoModelResourceId, ModelId modelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet);

	public abstract List<ModelResource> searchModelByExpression(String expression);
	
	protected final List<ModelResource> searchModelByExpressionAndValidate(String expression,
			org.eclipse.vorto.http.model.ModelType modelType) {
		List<ModelResource> resourceList = devtoolRestClient.searchByExpression(expression);
		ArrayList<ModelResource> modelResourceList = new ArrayList<ModelResource>();
		for (ModelResource modelResource : resourceList) {
			if (modelResource.getModelType().equals(modelType)) {
				modelResourceList.add(modelResource);
			}
		}
		return modelResourceList;
	}
}
