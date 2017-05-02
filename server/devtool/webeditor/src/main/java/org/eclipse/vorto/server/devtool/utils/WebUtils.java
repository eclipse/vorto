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
package org.eclipse.vorto.server.devtool.utils;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.http.response.LinkReferenceModelRequest;
import org.eclipse.vorto.server.devtool.http.response.LinkReferenceResourceRequest;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.inject.Injector;

@Component
public class WebUtils {
	
	@Autowired
	private Injector injector;
	
	public ResourceSet getResourceSet(HttpServletRequest request){
		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);
		ResourceSet resourceSet = webEditorResourceSetProvider.getResourceSetFromSession(httpServiceContext);
		return resourceSet;
	}
	
	public void validateLinkResourceReferenceRequest(LinkReferenceResourceRequest linkResourceReferenceRequest){
		Objects.requireNonNull(linkResourceReferenceRequest.getReferenceResourceId(), "reference resourceId must not be null");
		Objects.requireNonNull(linkResourceReferenceRequest.getReferenceResourceId(), "target resourceId must not be null");		
	}
	
	public void validateLinkReferenceModelRequest(LinkReferenceModelRequest linkReferenceModelRequest){
		Objects.requireNonNull(linkReferenceModelRequest.getResourceId(), "resourceId must not be null");
		Objects.requireNonNull(linkReferenceModelRequest.getModelName(), "model name must not be null");
		Objects.requireNonNull(linkReferenceModelRequest.getModelNamespace(), "model namespace must not be null");
		Objects.requireNonNull(linkReferenceModelRequest.getModelVersion(), "model version must not be null");		
	}
	
	public void validateSearchExpression(String string){
		Objects.requireNonNull(string, "search expression must not be null");
	}
}
