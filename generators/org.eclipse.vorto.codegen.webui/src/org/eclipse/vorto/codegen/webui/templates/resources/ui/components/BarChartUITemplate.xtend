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

import java.util.List
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class BarChartUITemplate implements IFunctionBlockUITemplate {
	
	private List<String> properties;
	
	new (List<String> properties) {
		this.properties = properties;
	}
	
	override renderJavascript(FunctionblockProperty element, InvocationContext context) {
		'''
		$scope.set«element.name.toFirstUpper» = function() {							
							         
							$scope.«element.name.toLowerCase»Options = {
								chart: {
				       				type: 'discreteBarChart',
				       				height: 250,
				        			margin : {
				            			top: 20,
				            			right: 20,
				           				bottom: 60,
				            			left: 75
				        			},
				        			x: function(d){ return d.label; },
				        			y: function(d){ return d.value; },
				        			forceY: [0, 0], 
				        			showValues: true,
				        			valueFormat: function(d){
				            			return d3.format(',.2f')(d);
				       				},
				       				transitionDuration: 500,
				        			xAxis: {
				        			},
				        			yAxis: { 
				        			}
				    			}
				       		};
				       		
				       		$scope.«element.name.toLowerCase»Data = [{
				   				key: "Cumulative Return",
				    			values: [
				    				«FOR prop : properties SEPARATOR ","»
				    				{ "label" : "«prop»" , "value" : $scope.thing.«element.name.toLowerCase».status.«prop» }
				    				«ENDFOR»
				    			]
							}]; 
				       	};
		'''
	}
	
	override renderHtml(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		<div nvd3 options="«fbProperty.name.toLowerCase»Options" data="«fbProperty.name.toLowerCase»Data"></div>
		'''
	}
	
}
