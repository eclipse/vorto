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