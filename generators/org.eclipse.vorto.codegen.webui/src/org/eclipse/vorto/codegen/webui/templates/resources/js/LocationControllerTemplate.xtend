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
 package org.eclipse.vorto.codegen.webui.templates.resources.js

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class LocationControllerTemplate implements IFileTemplate<FunctionblockProperty> {
	
	override getFileName(FunctionblockProperty context) {
		'''location-controller.js'''
	}
	
	override getPath(FunctionblockProperty context) {
		'''«(context.eContainer as InformationModel).name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(FunctionblockProperty locationModelProperty, InvocationContext context) {
		'''
		var locateDevices = angular.module('«(locationModelProperty.eContainer as InformationModel).name.toLowerCase».locate', []);
		
		locateDevices.controller('LocationController', ['$rootScope', '$scope', '$location', '$http','$state',
			function($rootScope, $scope, $location, $http, $state) {
				
				$scope.isLoading = true;
				$scope.things = [];
						
				$scope.showThingsInMap = function() {           
		            $http.get("rest/devices")
						.success(function(data, status) {
							$scope.things = data;
							for (var i = 0; i < $scope.things.length; i++) {
								$scope.things[i] = convertLocation($scope.things[i]);
							}
							$scope.singapore = {
								lat: 1.3580576343735706,
			                    lng: 103.798828125,
								zoom: 2
							};
							$scope.isLoading = false;
						})
						.error(function(data, status, headers, config, statusText) {
							$scope.isLoading = false;
						}
					);
				};
				
				var convertLocation = function(thing) {
					var location = {
						lng : thing.«locationModelProperty.name.toLowerCase».status.longitude === "" ? 103.798828125 : parseFloat(thing.«locationModelProperty.name.toLowerCase».status.longitude),
						lat: thing.«locationModelProperty.name.toLowerCase».status.latitude === "" ? 1.3580576343735706 :parseFloat(thing.«locationModelProperty.name.toLowerCase».status.latitude), 
						message: $scope.renderPopup(thing), 
						icon : $scope.renderMarker(thing)
					};
					
					return location;
				};
								
				$scope.renderPopup = function(thing) {
					return "<p><b>"+thing.name+"</b></p><a href='#/details/"+thing.id+"'>More Info</a><img width='64px' height='64px' src='http://vortorepo.apps.bosch-iot-cloud.com/rest/model/image/"+thing.thingType.namespace+"/"+thing.thingType.name+"/"+thing.thingType.version+"'/>";
				};
				$scope.renderMarker = function(thing) {
					var icon = {
						iconUrl : "img/marker-icon.png"
					};
					return icon;
				};
					
				$scope.showThingsInMap();		
			
				
			}]);
		'''
	}
	
}