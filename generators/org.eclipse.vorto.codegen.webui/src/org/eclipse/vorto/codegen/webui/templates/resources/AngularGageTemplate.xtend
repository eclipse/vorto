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
package org.eclipse.vorto.codegen.webui.templates.resources

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AngularGageTemplate implements IFileTemplate<InformationModel>{
	
	override getFileName(InformationModel context) {
		'''angular-gage.min.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/dist/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''		
		"use strict";angular.module("frapontillo.gage",["frapontillo.gage.directives","frapontillo.gage.controllers"]),angular.module("frapontillo.gage.controllers",[]).controller("justgageCtrl",["$scope","$filter",function(a,b){var c=this;c.getOptionValueOverrides={customSectors:angular.fromJson,levelColors:angular.fromJson,textRenderer:function(a){return a?angular.isFunction(a)?a():a:void 0}},c.getOptionValue=function(a,b){var d=c.getOptionValueOverrides[a];return d?d(b):""===b?void 0:"true"===b?!0:"false"===b?!1:b},c.getOptionsNames=function(c){var d=[],e=c||[];return angular.isDefined(c)&&!angular.isArray(c)&&(e=[c]),angular.forEach(a,function(a,c){"this"!==c&&"$"!==c.substring(0,1)&&0===b("filter")(e,c).length&&d.push(c)}),d},c.getDefinedOptions=function(){var b={},d=c.getOptionsNames();return angular.forEach(d,function(d){"this"!==d&&"$"!==d.substring(0,1)&&a[d]&&(b[d]=c.getOptionValue(d,a[d]))}),b}}]),angular.module("frapontillo.gage.directives",["frapontillo.gage.controllers"]).directive("justgage",function(){return{restrict:"EAC",scope:{title:"@",titleFontColor:"@",value:"@",valueFontColor:"@",width:"@",height:"@",relativeGaugeSize:"@",min:"@",max:"@",valueMinFontSize:"@",titleMinFontSize:"@",labelMinFontSize:"@",minLabelMinFontSize:"@",maxLabelMinFontSize:"@",hideValue:"@",hideMinMax:"@",hideInnerShadow:"@",gaugeWidthScale:"@",gaugeColor:"@",showInnerShadow:"@",shadowOpacity:"@",shadowSize:"@",shadowVerticalOffset:"@",levelColors:"@",customSectors:"@",noGradient:"@",label:"@",labelFontColor:"@",startAnimationTime:"@",startAnimationType:"@",refreshAnimationTime:"@",refreshAnimationType:"@",donut:"@",donutStartAngle:"@",counter:"@",decimals:"@",symbol:"@",formatNumber:"@",humanFriendly:"@",humanFriendlyDecimal:"@",textRenderer:"&"},controller:"justgageCtrl",link:function(a,b,c,d){var e,f=[],g=function(){f.push(a.$watch("value",function(b,c){b!==c&&e.refresh(b,a.max)})),f.push(a.$watch("max",function(b,c){b!==c&&e.refresh(a.value,b)}))},h=function(){var b=d.getOptionsNames(["value","max"]);angular.forEach(b,function(b){f.push(a.$watch(b,function(a,b){a!==b&&i()}))})},i=function(){var a={parentNode:b[0]};if(angular.extend(a,d.getDefinedOptions()),e){var c=e.canvas.canvas;c.parentNode.removeChild(c)}for(;f.length>0;){var i=f.pop();i()}e=new JustGage(a),g(),h()};i()}}});
		'''
	}
}