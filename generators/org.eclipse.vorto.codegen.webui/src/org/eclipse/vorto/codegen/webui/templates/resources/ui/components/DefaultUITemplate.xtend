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
package org.eclipse.vorto.codegen.webui.templates.resources.ui.components

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

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