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
import java.util.UUID
import org.eclipse.xtext.EcoreUtil2

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
	
	def getModelInfo(ModelId modelId) {
		return repositoryFactory.getRepositoryByModel(modelId).getById(modelId)	
	}
	
	def createResource(FileContent modelResource, URI uri, ResourceSet resourceSet){
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().equals(uri)) {
				return resource;
			}
		}
		
		val resource = resourceSet.createResource(uri)
		resource.load(new ByteArrayInputStream(modelResource.content), new HashMap)	
		return resource
	}

	@Transactional
	override get(String resourceId, IServiceContext serviceContext) throws IOException {
		try {
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext)
			val modelResource = getModelResource(ModelId.fromPrettyFormat(resourceId))
			val uri = URI.createURI("dummy:/" + UUID.randomUUID.toString+ modelResource.fileName)
			var resource = createResource(modelResource, uri, resourceSet)
			
			var modelInfo = getModelInfo(ModelId.fromPrettyFormat(resourceId))
			
			for (ModelId reference : modelInfo.references) {
				val modelReference = getModelResource(reference)
				val modelReferenceURI = URI.createURI("dummy:/" + UUID.randomUUID.toString + modelReference.fileName)
				createResource(modelReference, modelReferenceURI, resourceSet)
			}

			val xtextResource = resource as XtextResource
			EcoreUtil2.resolveAll(xtextResource)
			return documentProvider.get(resourceId, serviceContext) => [
				var s = setInput(xtextResource)
				System.out.println(s);
			]
		} catch (WrappedException exception) {
			throw exception.cause
		}
	}

	override put(IXtextWebDocument document, IServiceContext serviceContext) throws IOException {
		//NOOP
	}
}