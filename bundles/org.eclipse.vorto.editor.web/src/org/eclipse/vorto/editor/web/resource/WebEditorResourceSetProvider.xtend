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

	def ResourceSet getResourceSetFromSession(IServiceContext serviceContext) {
		if (serviceContext.getSession().get("set") === null) {
			var ResourceSet resourceSet = provider.get()
			serviceContext.getSession().put("set", resourceSet)
			println(resourceSet);
			return resourceSet
		} else {
			println(serviceContext.getSession().get("set"));
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
