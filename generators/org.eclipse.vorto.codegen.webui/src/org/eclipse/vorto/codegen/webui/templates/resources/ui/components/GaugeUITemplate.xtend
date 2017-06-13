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