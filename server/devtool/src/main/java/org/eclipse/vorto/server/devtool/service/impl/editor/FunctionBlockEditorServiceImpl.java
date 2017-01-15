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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.server.devtool.service.editor.IEditorService;
import org.eclipse.vorto.server.devtool.utils.DevtoolReferenceLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FunctionBlockEditorServiceImpl extends IEditorService {

	@Autowired
	DevtoolReferenceLinker devtoolReferenceLinker;

	public String linkModelToResource(String functionBlockResourceId, ModelId datatypeModelId,
			ResourceSet resourceSet, Set<String> referencedResourceSet) {
		devtoolReferenceLinker.linkDataTypeToFunctionBlock(functionBlockResourceId, datatypeModelId,
				resourceSet, referencedResourceSet);
		Resource functionBlockResource = resourceSet.getResource(URI.createURI(functionBlockResourceId), true);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			functionBlockResource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<ModelInfo> searchModelByExpression(String expression) {
		List<ModelInfo> modelList = searchModelByExpressionAndValidate(expression + " " + ModelType.Datatype.toString() , ModelType.Datatype);
		List<ModelInfo> functionBlockModelList = searchModelByExpressionAndValidate(expression + " " + ModelType.Functionblock.toString(), ModelType.Functionblock);
		modelList.addAll(functionBlockModelList);
		return modelList;
	}
}
