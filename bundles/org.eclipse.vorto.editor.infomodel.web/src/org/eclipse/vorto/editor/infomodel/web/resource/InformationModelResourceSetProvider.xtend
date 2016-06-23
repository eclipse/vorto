package org.eclipse.vorto.editor.infomodel.web.resource

import java.util.HashSet
import java.util.Set
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider
import com.google.inject.Inject
import com.google.inject.Provider
import com.google.inject.Singleton

@Singleton 
class InformationModelResourceSetProvider implements IWebResourceSetProvider {
	
	@Inject package Provider<ResourceSet> provider

	override ResourceSet get(String resourceId, IServiceContext serviceContext) {
		return getResourceSetFromSession(serviceContext)
	}

	def ResourceSet getResourceSetFromSession(IServiceContext serviceContext) {
		if (serviceContext.getSession().get("set") === null) {
			var ResourceSet resourceSet = provider.get()
			serviceContext.getSession().put("set", resourceSet)
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
}
