package com.bosch.iotsuite.console.generator.application.templates.resources.ui.components

import com.bosch.iotsuite.console.generator.application.templates.resources.ui.IFunctionBlockUITemplate
import org.eclipse.vorto.codegen.api.InvocationContext
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
