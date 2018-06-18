/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
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
package org.eclipse.vorto.codegen.webui.templates.resources.partials

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ApiTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''api.html'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/partials'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		<link href='css/swagger_custom.css' media='screen' rel='stylesheet' type='text/css'/>
		<div class="swagger-section">
		<div id="message-bar" class="swagger-ui-wrap" data-sw-translate>&nbsp;</div>
			<div id="swagger-ui-container" class="swagger-ui-wrap">
			    <h3 ng-show="isLoading">Loading... Please wait...</h3>
			    <div swagger-ui url="swaggerUrl" validator-url="false" loading="isLoading" parser="json" api-explorer="true" trusted-sources="true"
			         error-handler="defaultErrorHandler">
			    </div>
			</div>
		</div>
		'''
	}
	
}
