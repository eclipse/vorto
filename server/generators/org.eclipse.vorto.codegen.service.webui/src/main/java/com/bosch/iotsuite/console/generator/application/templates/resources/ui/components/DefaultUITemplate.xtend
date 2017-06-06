package com.bosch.iotsuite.console.generator.application.templates.resources.ui.components

import com.bosch.iotsuite.console.generator.application.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.codegen.api.InvocationContext

class DefaultUITemplate implements IFunctionBlockUITemplate {
	
	override renderHtml(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		«IF fbProperty.type.functionblock.status != null»
			«FOR statusProperty : fbProperty.type.functionblock.status.properties»
					<dl class="dl-horizontal">
						<div class="row">
							<div class="col-sm-6">
								<label class="control-label input-label" style="margin-left:10px">«statusProperty.name»:</label>
							</div>
							<div class="col-sm-6">
									<input readonly type="text" name="«statusProperty.name»" 
										value={{thing.«fbProperty.name».properties.status.«statusProperty.name»}}
										class="ng-pristine ng-valid col-xs-10" pull-right"/>
							</div>
						</div>
					</dl>
			«ENDFOR»		
		«ENDIF»

		'''
	}
	
	override renderJavascript(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		$scope.set«fbProperty.name.toFirstUpper» = function() {
			
		};
		'''
	}
	
}