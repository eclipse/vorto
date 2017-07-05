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

import org.eclipse.xtext.web.server.generator.DefaultContentTypeProvider
import org.eclipse.xtext.web.server.generator.GeneratorService

class ContentTypeProvider extends DefaultContentTypeProvider {
	
	override getContentType(String fileName) {
		if (fileName == GeneratorService.DEFAULT_ARTIFACT)
			return 'text/html'
		super.getContentType(fileName)
	}	
}