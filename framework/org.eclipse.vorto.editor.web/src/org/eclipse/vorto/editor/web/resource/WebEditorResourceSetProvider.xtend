/**
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
 */
package org.eclipse.vorto.editor.web.resource

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
		if (serviceContext.getSession().get("set") === null) {
			return getNewResourceSet(serviceContext);
		} else {
			return serviceContext.getSession().get("set")
		}
	}

	def ResourceSet getNewResourceSet(IServiceContext serviceContext) {
		var ResourceSet resourceSet = provider.get()
		setSessionResourceSet(serviceContext, resourceSet);
		return resourceSet
	}
}
