package org.eclipse.vorto.editor.web.persistence

import com.google.inject.Inject
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.HashMap
import java.util.Optional
import javax.transaction.Transactional
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.common.util.WrappedException
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.vorto.model.ModelId
import org.eclipse.vorto.repository.core.FileContent
import org.eclipse.vorto.repository.core.IModelRepositoryFactory
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebDocumentProvider
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider
import org.eclipse.xtext.web.server.model.IXtextWebDocument
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler

class WebEditorResourceHandler implements IServerResourceHandler {
	
	@Inject
	IWebResourceSetProvider resourceSetProvider
	
	@Inject
	IWebDocumentProvider documentProvider
	
	@Inject
	IModelRepositoryFactory repositoryFactory

	def getModelResource(ModelId modelId) {
		return repositoryFactory.getRepositoryByModel(modelId).getFileContent(modelId,Optional.empty).get
	}
	
	def createResource(FileContent modelResource, URI uri, ResourceSet resourceSet){
		val resourceList = resourceSet.getResources();
		var createResource = true;
		for (Resource resource : resourceList) {
			if (resource.getURI().equals(uri)) {
				createResource = false;
			}
		}
		if(createResource){
			val resource = resourceSet.createResource(uri)
			resource.load(new ByteArrayInputStream(modelResource.content), new HashMap)				
		}
	}

	@Transactional
	override get(String resourceId, IServiceContext serviceContext) throws IOException {
		try {
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext)
			val modelResource = getModelResource(ModelId.fromPrettyFormat(resourceId))
			val uri = URI.createURI("dummy:/" + modelResource.fileName)
			createResource(modelResource, uri, resourceSet)
			val xtextResource = resourceSet.getResource(uri, true) as XtextResource
			return documentProvider.get(resourceId, serviceContext) => [
				setInput(xtextResource)
			]
		} catch (WrappedException exception) {
			throw exception.cause
		}
		
//		try {
//			val modelID = ModelId.fromPrettyFormat(resourceId)
////			val modelInfo = this.repositoryFactory.getRepositoryByModel(modelID).getById(modelID)
//			
//			var resourceSet = injector.getInstance(XtextResourceSet)
//			
//			val modelResource = getModelResource(modelID)
//			val uri = URI.createURI("dummy:/" + modelResource.fileName)
//			createResource(modelResource, uri, resourceSet)
//			
////			for (ModelId reference : modelInfo.references) {
////				val modelReference = getModelResource(modelID)
////				val modelReferenceURI = URI.createURI("dummy:/" + reference.prettyFormat + "-" + modelReference.fileName)
////				createResource(modelReference, modelReferenceURI, resourceSet)
////			}
//
//			val xtextResource = resourceSet.getResource(uri, true) as XtextResource
//			return documentProvider.get(resourceId, serviceContext) => [
//				setInput(xtextResource)
//			]
//		} catch (WrappedException exception) {
//			throw exception.cause
//		}
	}

	override put(IXtextWebDocument document, IServiceContext serviceContext) throws IOException {
		//NOOP
	}
}