package com.bosch.iotsuite.console.generator.application.templates.resources.ui.components

import com.bosch.iotsuite.console.generator.application.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty

class BarChartUITemplate implements IFunctionBlockUITemplate {
	
	private String[] properties;
	
	new (String... properties) {
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
