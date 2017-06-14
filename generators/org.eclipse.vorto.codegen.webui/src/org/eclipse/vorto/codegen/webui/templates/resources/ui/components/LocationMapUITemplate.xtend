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

class LocationMapUITemplate implements IFunctionBlockUITemplate {
	
	private String longitudeProperty;
	private String latitudeProperty; 
	
	new(String longitudeProperty, String latitudeProperty) {
		this.longitudeProperty = longitudeProperty;
		this.latitudeProperty = latitudeProperty;
	}
	
	override renderJavascript(FunctionblockProperty element, InvocationContext context) {
		'''
		$scope.set«element.name.toFirstUpper» = function() {
					$scope.thingLocation = {
			            	lat: $scope.thing.«element.name».status.«latitudeProperty» === "" ? 1.3580576343735706 : parseFloat($scope.thing.«element.name».status.«latitudeProperty»),
			                lng: $scope.thing.«element.name».status.«longitudeProperty» === "" ? 103.798828125 : parseFloat($scope.thing.«element.name».status.«longitudeProperty»),
			                zoom: 8
			            };
			            $scope.markers = {
			            	m1 : {
			            		lat: $scope.thing.«element.name».status.«latitudeProperty» === "" ? 1.3580576343735706 : parseFloat($scope.thing.«element.name».status.«latitudeProperty»),
			                	lng: $scope.thing.«element.name».status.«longitudeProperty» === "" ? 103.798828125 : parseFloat($scope.thing.«element.name».status.«longitudeProperty»),
			                	icon : { iconUrl : 'img/marker-icon.png'}
			                }
			            }; 
				};
		'''
	}
	
	override renderHtml(FunctionblockProperty fbProperty, InvocationContext ctx) {
		'''
		<leaflet markers="markers" lf-center="thingLocation" width="100%" height="250px"></leaflet>
		'''
	}
	
}
