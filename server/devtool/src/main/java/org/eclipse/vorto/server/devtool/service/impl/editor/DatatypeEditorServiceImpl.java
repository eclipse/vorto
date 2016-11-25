/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.server.devtool.service.impl.editor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.server.devtool.service.editor.IEditorService;
import org.eclipse.vorto.server.devtool.utils.DevtoolReferenceLinker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatatypeEditorServiceImpl extends IEditorService {

	@Autowired
	DevtoolReferenceLinker devtoolReferenceLinker;

	@Override
	public String linkModelToResource(String datatypeResourceId, ModelId datatypeModelId, ResourceSet resourceSet,
			Set<String> referencedResourceSet) {
		devtoolReferenceLinker.linkDataTypeToFunctionBlock(datatypeResourceId, datatypeModelId,
				resourceSet, referencedResourceSet);
		Resource dataTypeResource = resourceSet.getResource(URI.createURI(datatypeResourceId), true);
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			dataTypeResource.save(byteArrayOutputStream, null);
			return byteArrayOutputStream.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<ModelResource> searchModelByExpression(String expression) {
		List<ModelResource> modelList = searchModelByExpressionAndValidate(expression + " " + org.eclipse.vorto.http.model.ModelType.Datatype.toString() , org.eclipse.vorto.http.model.ModelType.Datatype);
		return modelList;
	}
	
}
