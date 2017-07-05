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
package org.eclipse.vorto.server.devtool.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.server.devtool.models.LinkReferenceResponse;
import org.eclipse.vorto.server.devtool.models.ModelResource;
import org.eclipse.vorto.server.devtool.utils.DevtoolReferenceLinker;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.vorto.server.devtool.utils.DevtoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class IEditorService {

	@Autowired
	private DevtoolRestClient devtoolRestClient;

	@Autowired
	private DevtoolReferenceLinker devtoolReferenceLinker;

	@Autowired
	private DevtoolUtils devtoolUtils;

	@Autowired
	private IProjectService projectService;

	public abstract List<ModelInfo> searchModelByExpression(String expression);

	public abstract String generateFileContent(ModelResource modelResource);

	public abstract ModelInfo getAndValidateModelInfo(ModelId modelId);

	public abstract void updateVariableNames(String targetResourceId, String referenceResourceId,
			ResourceSet resourceSet);

	public final List<ModelInfo> searchModelByExpressionAndValidate(String expression, List<ModelType> modelTypeList) {
		List<ModelInfo> resourceList = devtoolRestClient.searchByExpression(expression);
		ArrayList<ModelInfo> modelResourceList = new ArrayList<ModelInfo>();
		for (ModelInfo modelResource : resourceList) {
			for (ModelType modelType : modelTypeList) {
				if (modelResource.getType().equals(modelType)) {
					modelResourceList.add(modelResource);
				}
			}
		}
		return modelResourceList;
	}

	public final LinkReferenceResponse linkReferenceToResource(String targetResourceId, ModelId modelId,
			ResourceSet resourceSet) {
		validateArguments(targetResourceId, resourceSet);
		ModelInfo modelInfo = getAndValidateModelInfo(modelId);
		projectService.getReferencedResource(modelInfo);
		String referenceResourceId = devtoolUtils.getReferencedResourceId(modelInfo);
		String content = linkReference(targetResourceId, referenceResourceId, resourceSet);
		LinkReferenceResponse linkReferenceResponse = new LinkReferenceResponse(content, targetResourceId,
				referenceResourceId);
		return linkReferenceResponse;
	}

	public final LinkReferenceResponse linkReferenceToResource(String targetResourceId, String referenceResourceId,
			ResourceSet resourceSet) {
		validateArguments(referenceResourceId, resourceSet);
		validateArguments(targetResourceId, resourceSet);
		String content = linkReference(targetResourceId, referenceResourceId, resourceSet);
		LinkReferenceResponse linkReferenceResponse = new LinkReferenceResponse(content, targetResourceId,
				referenceResourceId);
		return linkReferenceResponse;
	}

	private void validateArguments(String resourceId, ResourceSet resourceSet) {
		if (!devtoolReferenceLinker.containsResource(resourceId, resourceSet)) {
			throw new RuntimeException("No resource with resourceId : " + resourceId);
		}
	}

	private String linkReference(String targetResourceId, String referenceResourceId, ResourceSet resourceSet) {
		devtoolReferenceLinker.linkReferenceToResource(targetResourceId, referenceResourceId, resourceSet);
		updateVariableNames(targetResourceId, referenceResourceId, resourceSet);
		return devtoolUtils.getResourceContentsAsString(targetResourceId, resourceSet);
	}
}
