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
import java.io.ByteArrayInputStream
import java.io.IOException
import org.eclipse.emf.common.notify.impl.NotificationChainImpl
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.common.util.WrappedException
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService
import org.eclipse.vorto.devtool.projectrepository.model.FileUploadHandle
import org.eclipse.vorto.devtool.projectrepository.model.FolderResource
import org.eclipse.vorto.devtool.projectrepository.model.ModelResource
import org.eclipse.vorto.devtool.projectrepository.model.ResourceType
import org.eclipse.vorto.devtool.projectrepository.utils.ProjectRepositoryConstants
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebDocumentProvider
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider
import org.eclipse.xtext.web.server.model.IXtextWebDocument
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler

class WedEditorResourceHandler implements IServerResourceHandler {

	@Inject
	private IWebResourceSetProvider resourceSetProvider

	@Inject
	private IWebDocumentProvider documentProvider

	@Inject
	private IProjectRepositoryService repositoryService;

	def getModelResource(String resourceId) {
		val modelResource = repositoryService.createQuery.property(ProjectRepositoryConstants.META_PROPERTY_RESOURCE_ID,
			resourceId).singleResult() as ModelResource;
		return modelResource;
	}

	def createResource(ModelResource modelResource, URI uri, ResourceSet resourceSet){
		val resourceList = resourceSet.getResources();
		var createResource = true;
		for (Resource resource : resourceList) {
			if (resource.getURI().equals(uri)) {
				createResource = false;
			}
		}
		if(createResource){
			val resource = resourceSet.createResource(uri)
			resource.load(new ByteArrayInputStream(modelResource.content), resourceSet.getLoadOptions())				
		}
	}

	override get(String resourceId, IServiceContext serviceContext) throws IOException {
		try {
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext)
			val modelResource = getModelResource(resourceId)
			val uri = URI.createURI(ProjectRepositoryConstants.DUMMY + modelResource.path)
			createResource(modelResource, uri, resourceSet)
			val xtextResource = resourceSet.getResource(uri, true) as XtextResource
			return documentProvider.get(resourceId, serviceContext) => [
				setInput(xtextResource)
			]
		} catch (WrappedException exception) {
			throw exception.cause
		}
	}

	override put(IXtextWebDocument document, IServiceContext serviceContext) throws IOException {
		try {
			val resourceId = document.resourceId
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext)
			if (document.resource.resourceSet === null) {
				document.resource.basicSetResourceSet(resourceSet, new NotificationChainImpl)
			}
			val content = document.text
			val modelResource = getModelResource(resourceId)
			val fileName = modelResource.properties.get(ProjectRepositoryConstants.META_PROPERTY_FILE_NAME)
			val projectName = modelResource.properties.get(ProjectRepositoryConstants.META_PROPERTY_PROJECT_NAME)

			val projectResource = repositoryService.createQuery().path(projectName.replace("\\", "/")).type(
				ResourceType.ProjectResource).singleResult();
			val fileUploadHandle = new FileUploadHandle(projectResource as FolderResource, fileName, content.bytes)
			repositoryService.uploadResource(null, fileUploadHandle)

		} catch (WrappedException exception) {
			throw exception.cause
		}
	}
}
