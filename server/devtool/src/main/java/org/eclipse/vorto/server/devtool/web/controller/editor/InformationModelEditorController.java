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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.http.model.ModelId;
import org.eclipse.vorto.http.model.ModelResource;
import org.eclipse.vorto.server.devtool.service.impl.editor.InformationModelEditorServiceImpl;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/editor/infomodel")
public class InformationModelEditorController implements IEditorController {

	@Autowired
	Injector injector;

	@Autowired
	InformationModelEditorServiceImpl iInformationModelEditorService;

	@ApiOperation(value = "Adds the function block to the resource set")
	@RequestMapping(value = "/link/functionblock/{resourceId}/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void linkEditor(@ApiParam(value = "ResourceId", required = true) final @PathVariable String resourceId,
			@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace,
			@ApiParam(value = "Name", required = true) final @PathVariable String name,
			@ApiParam(value = "Version", required = true) final @PathVariable String version,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request,
			@ApiParam(value = "Response", required = true) final HttpServletResponse response) {

		Objects.requireNonNull(resourceId, "resourceId must not be null");
		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		ModelId modelId = new ModelId(name, namespace, version);

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);
		ResourceSet resourceSet = webEditorResourceSetProvider.getResourceSetFromSession(httpServiceContext);
		HashSet<String> referencedResourceSet = (HashSet<String>) webEditorResourceSetProvider
				.getReferencedResourcesFromSession(httpServiceContext);

		String content = iInformationModelEditorService.linkModelToResource(resourceId, modelId, resourceSet,
				referencedResourceSet);
		try {
			IOUtils.copy(new ByteArrayInputStream(content.getBytes()), response.getOutputStream());
			response.flushBuffer();
		} catch (IOException e) {
			throw new RuntimeException("Error copying file.", e);
		}
	}

	@ApiOperation(value = "")
	@RequestMapping(value = "/search={expression:.*}", method = RequestMethod.GET)
	public List<ModelResource> searchByExpression(
			@ApiParam(value = "Search expression", required = true) @PathVariable String expression) {

		Objects.requireNonNull(expression, "namespace must not be null");
		return iInformationModelEditorService.searchModelByExpression(expression);
	}
}