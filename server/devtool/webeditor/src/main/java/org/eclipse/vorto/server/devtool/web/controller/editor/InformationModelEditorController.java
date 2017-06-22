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
package org.eclipse.vorto.server.devtool.web.controller.editor;

import java.util.List;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.models.LinkReferenceResponse;
import org.eclipse.vorto.server.devtool.service.impl.editor.InformationModelEditorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest/editor/infomodel")
public class InformationModelEditorController extends AbstractEditorController {

	@Autowired
	private InformationModelEditorServiceImpl informationModelEditorServiceImpl;

	@Override
	public LinkReferenceResponse linkReferenceToResource(String resoruceId, ModelId modelId, ResourceSet resourceSet) {
		return informationModelEditorServiceImpl.linkReferenceToResource(resoruceId, modelId, resourceSet);
	}

	@Override
	public List<ModelInfo> search(String expression) {
		return informationModelEditorServiceImpl.searchModelByExpression(expression);
	}

	@Override
	public LinkReferenceResponse linkReferenceToResource(String targetResourceId, String referenceResourceId,
			ResourceSet resourceSet) {
		return informationModelEditorServiceImpl.linkReferenceToResource(targetResourceId,
				referenceResourceId, resourceSet);
	}
}