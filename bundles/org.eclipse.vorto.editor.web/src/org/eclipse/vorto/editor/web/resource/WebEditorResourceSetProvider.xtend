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
package org.eclipse.vorto.editor.web.resource

import com.google.inject.Inject
import java.util.HashSet
import java.util.Set
import javax.inject.Provider
import javax.inject.Singleton
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider

@Singleton
class WebEditorResourceSetProvider implements IWebResourceSetProvider {

	@Inject package Provider<ResourceSet> provider

	override ResourceSet get(String resourceId, IServiceContext serviceContext) {
		return getResourceSetFromSession(serviceContext)
	}

	def setSessionResourceSet(IServiceContext serviceContext, ResourceSet resourceSet) {
		serviceContext.getSession().put("set", resourceSet)
	}

	def setSessionRefencedResourceSet(IServiceContext serviceContext, HashSet<String> referencedResourceSet) {
		serviceContext.getSession().put("referenceSet", referencedResourceSet)
	}

	def ResourceSet getResourceSetFromSession(IServiceContext serviceContext) {
		if (serviceContext.getSession().get("set") === null) {
			var ResourceSet resourceSet = provider.get()
			setSessionResourceSet(serviceContext, resourceSet);
			return resourceSet
		} else {
			return serviceContext.getSession().get("set")
		}
	}

	def Set<String> getReferencedResourcesFromSession(IServiceContext serviceContext) {
		if (serviceContext.getSession().get("referenceSet") === null) {
			var HashSet<String> referencedResourceSet = new HashSet()
			serviceContext.getSession().put("referenceSet", referencedResourceSet)
			return referencedResourceSet
		} else {
			return serviceContext.getSession().get("referenceSet")
		}
	}

	def ResourceSet getNewResourceSet() {
		return provider.get();
	}
}
