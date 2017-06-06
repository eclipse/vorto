package com.bosch.iotsuite.console.generator.application.templates.resources.ui.components

import com.bosch.iotsuite.console.generator.application.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.codegen.api.InvocationContext

class GaugeUITemplate implements IFunctionBlockUITemplate {
	
	private String symbol;
	private String minValue;
	private String maxValue;
	private String value;
	
	new(String symbol, String minValue, String maxValue, String value) {
		this.symbol = symbol;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.value = value;
	}
	
	override renderHtml(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		<div justgage
			titleFontColor=black
			decimals="2"
			symbol={{thing.«fbProperty.name.toLowerCase».status.«symbol»}} 
			min={{thing.«fbProperty.name.toLowerCase».status.«minValue»}}
			max={{thing.«fbProperty.name.toLowerCase».status.«maxValue»}}
			value={{thing.«fbProperty.name.toLowerCase».status.«value»}}>
		</div>
		'''
	}
	
	override renderJavascript(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		$scope.set«fbProperty.name.toFirstUpper» = function() {
			
		};
		''' // gauge has no javascript elements
	}
	
}