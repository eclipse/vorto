package com.bosch.iotsuite.console.generator.application.templates.resources.partials

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