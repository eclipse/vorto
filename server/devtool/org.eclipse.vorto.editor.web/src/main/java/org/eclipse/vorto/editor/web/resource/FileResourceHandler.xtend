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
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.common.util.WrappedException
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryFileConstants
import org.eclipse.vorto.devtool.projectrepository.file.ProjectRepositoryServiceFS
import org.eclipse.xtext.parser.IEncodingProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebDocumentProvider
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider
import org.eclipse.xtext.web.server.model.IXtextWebDocument
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler

class FileResourceHandler implements IServerResourceHandler {
		
	@Inject 
	private IWebResourceSetProvider resourceSetProvider
	
	@Inject 
	private IWebDocumentProvider documentProvider
	
	@Inject 
	private IEncodingProvider encodingProvider

	private String projectRepositoryPath = "../devtool-project-repository";
	
	private IProjectRepositoryService projectRepositoryService = new ProjectRepositoryServiceFS(projectRepositoryPath);
			
	def getFileURI(String resourceId) {
		val resource = projectRepositoryService.createQuery().property(ProjectRepositoryFileConstants.META_PROPERTY_RESOURCE_ID, resourceId).singleResult();
		return URI.createFileURI(projectRepositoryPath + File.separator +  resource.path);
	}
	
	override get(String resourceId, IServiceContext serviceContext) throws IOException {
		try {
			val uri = getFileURI(resourceId)
			if (uri === null)
				throw new IOException('The requested resource does not exist.')
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext)
			val resource = resourceSet.getResource(uri, true) as XtextResource
			println(resourceSet)
			return documentProvider.get(resourceId, serviceContext) => [
				setInput(resource)
			]
		} catch (WrappedException exception) {
			throw exception.cause
		}
	}
	
	override put(IXtextWebDocument document, IServiceContext serviceContext) throws IOException {
		try {
			val uri = getFileURI(document.resourceId)
			val outputStream = document.resource.resourceSet.URIConverter.createOutputStream(uri)
			val writer = new OutputStreamWriter(outputStream, encodingProvider.getEncoding(uri))
			writer.write(document.text)
			writer.close
		} catch (WrappedException exception) {
			throw exception.cause
		}
	}	
}