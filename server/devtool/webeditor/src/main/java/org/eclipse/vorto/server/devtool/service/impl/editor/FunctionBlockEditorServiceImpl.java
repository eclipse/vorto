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
package org.eclipse.vorto.server.devtool.service.impl.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.devtool.projectrepository.model.ModelResource;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.service.IEditorService;
import org.eclipse.vorto.server.devtool.utils.DevtoolRestClient;
import org.eclipse.vorto.server.devtool.utils.DevtoolUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionBlockEditorServiceImpl extends IEditorService {

	@Autowired
	private DevtoolUtils devtoolUtils;

	@Autowired
	private DevtoolRestClient devtoolRestClient;

	public List<ModelInfo> searchModelByExpression(String expression) {
		ArrayList<org.eclipse.vorto.repository.api.ModelType> modelTypeList = new ArrayList<>();
		modelTypeList.add(org.eclipse.vorto.repository.api.ModelType.Functionblock);
		modelTypeList.add(org.eclipse.vorto.repository.api.ModelType.Datatype);
		List<ModelInfo> modelList = searchModelByExpressionAndValidate(expression, modelTypeList);
		return modelList;
	}

	@Override
	public String generateFileContent(ModelResource modelResource) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("namespace ").append(modelResource.getNamespace()).append("\nversion ")
				.append(modelResource.getVersion()).append("\ndisplayname \"").append(modelResource.getName())
				.append("\"\ndescription \"").append(modelResource.getDescription()).append("\"\ncategory demo")
				.append("\nfunctionblock ").append(modelResource.getName()).append(" {\n")
				.append("\n\tconfiguration {\n\t //Please enter functionblock configuration details.\n\t}\n")
				.append("\n\tstatus {\n\t //Please enter functionblock status details.\n\t}\n")
				.append("\n\tfault {\n\t //Please enter functionblock fault configuration.\n\t}\n")
				.append("\n\toperations {\n\t //Please enter functionblock operations.\n\t}\n").append("}\n");
		return stringBuilder.toString();
	}

	@Override
	public ModelInfo getAndValidateModelInfo(ModelId modelId) {
		ModelInfo modelInfo = devtoolRestClient.getModel(modelId);
		if (!(devtoolUtils.getModelType(modelInfo.getType().toString()) == ModelType.Functionblock
				|| devtoolUtils.getModelType(modelInfo.getType().toString()) == ModelType.Datatype)) {
			throw new RuntimeException("No DataType or FunctionBlock [" + modelId.toString() + "]");
		}
		return modelInfo;
	}
	
	@Override
	public void updateVariableNames(String targetResourceId, String referenceResourceId, ResourceSet resourceSet) {
		return;		
	}
}
