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
import java.io.IOException
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.web.server.IServiceContext
import org.eclipse.xtext.web.server.model.IWebDocumentProvider
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider
import org.eclipse.xtext.web.server.model.IXtextWebDocument
import org.eclipse.xtext.web.server.persistence.IServerResourceHandler
import org.eclipse.emf.common.util.WrappedException

@Deprecated
class HashMapResourceHandler implements IServerResourceHandler {

	@Inject IWebResourceSetProvider resourceSetProvider;

	@Inject IWebDocumentProvider documentProvider;

	override get(String resourceId, IServiceContext serviceContext) throws IOException {
		try {
			val resourceSet = resourceSetProvider.get(resourceId, serviceContext);
			val uri = URI.createURI(resourceId);
			val resource = resourceSet.getResource(uri, true) as XtextResource
			return documentProvider.get(resourceId, serviceContext) => [
				setInput(resource)
			]
		}catch (WrappedException exception){
			throw exception.cause
		}
	}

	override put(IXtextWebDocument document, IServiceContext serviceContext) throws IOException {
		/*
		 * Since the files are being stored in memory here, this has not been implemented
		 */
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}

}
