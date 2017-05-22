/*******************************************************************************
 * Copyright (c) 2017 Bosch Software Innovations GmbH and others.
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.server.devtool.http.response.LinkReferenceModelRequest;
import org.eclipse.vorto.server.devtool.http.response.LinkReferenceResourceRequest;
import org.eclipse.vorto.server.devtool.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

public abstract class AbstractEditorController {

	@Autowired
	private WebUtils webUtils;

	public abstract String linkReferenceToResource(String resoruceId, ModelId modelId, ResourceSet resourceSet);

	public abstract String linkReferenceToResource(String targetResourceId, String referenceResourceId,
			ResourceSet resourceSet);

	public abstract List<ModelInfo> search(String expression);

	@ApiOperation(value = "Imports the specified model from the repository into the resource")
	@RequestMapping(value = "/link/model", method = RequestMethod.POST)
	public void linkReference(@RequestBody LinkReferenceModelRequest linkReferenceModelRequest,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request,
			@ApiParam(value = "Response", required = true) final HttpServletResponse response) {

		webUtils.validateLinkReferenceModelRequest(linkReferenceModelRequest);
		ModelId modelId = new ModelId(linkReferenceModelRequest.getModelName(),
				linkReferenceModelRequest.getModelNamespace(), linkReferenceModelRequest.getModelVersion());
		ResourceSet resourceSet = webUtils.getResourceSet(request);
		String content = linkReferenceToResource(linkReferenceModelRequest.getResourceId(), modelId, resourceSet);
		try {
			IOUtils.copy(new ByteArrayInputStream(content.getBytes()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

	@ApiOperation(value = "Imports the specified reference resource from the project workspace into the target resource")
	@RequestMapping(value = "/link/resource", method = RequestMethod.POST)
	public void linkReference(@RequestBody LinkReferenceResourceRequest linkReferenceResourceRequest,
			HttpServletRequest request, HttpServletResponse response) {

		webUtils.validateLinkResourceReferenceRequest(linkReferenceResourceRequest);
		ResourceSet resourceSet = webUtils.getResourceSet(request);
		String content = linkReferenceToResource(linkReferenceResourceRequest.getTargetResourceId(),
				linkReferenceResourceRequest.getReferenceResourceId(), resourceSet);
		try {
			IOUtils.copy(new ByteArrayInputStream(content.getBytes()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

	@ApiOperation(value = "Searches the vorto repsository for models matching the expression")
	@RequestMapping(value = "/search={expression:.*}", method = RequestMethod.GET)
	public List<ModelInfo> searchByExpression(
			@ApiParam(value = "Search expression", required = true) @PathVariable String expression) {

		webUtils.validateSearchExpression(expression);
		return search(expression);
	}
}
