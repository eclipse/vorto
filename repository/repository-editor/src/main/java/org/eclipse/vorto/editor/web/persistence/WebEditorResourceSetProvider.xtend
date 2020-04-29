/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.editor.web.persistence

import com.google.inject.Inject
import javax.inject.Provider
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider

class WebEditorResourceSetProvider implements IWebResourceSetProvider {

	@Inject package Provider<ResourceSet> provider

	override ResourceSet get(String resourceId, IServiceContext serviceContext) {
		return getResourceSetFromSession(serviceContext)
	}

	def setSessionResourceSet(IServiceContext serviceContext, ResourceSet resourceSet) {
		serviceContext.getSession().put("set", resourceSet)
	}

	def ResourceSet getResourceSetFromSession(IServiceContext serviceContext) {
		getNewResourceSet(serviceContext);
	}

	def ResourceSet getNewResourceSet(IServiceContext serviceContext) {
		var ResourceSet resourceSet = provider.get()
		//setSessionResourceSet(serviceContext, resourceSet);
		return resourceSet
	}
}