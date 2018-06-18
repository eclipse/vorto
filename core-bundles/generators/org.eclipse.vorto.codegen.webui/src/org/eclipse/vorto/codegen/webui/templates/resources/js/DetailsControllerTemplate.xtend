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
import org.eclipse.vorto.codegen.webui.templates.resources.ui.UIComponentFactory
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class DetailsControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''details-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var «element.name.toLowerCase»Details = angular.module('«element.name.toLowerCase».details', []);
		
		«element.name.toLowerCase»Details.controller('DetailsController', ['$rootScope', '$scope', '$location', '$http','$state','$stateParams',
			function($rootScope, $scope, $location, $http, $state,$stateParams) {
				
				// Stuff for websockets	in order to automatically retrieve device values without refreshing	
				$scope.websocket = {
					socket      : null,
					stompClient : null
				};
					
				$scope.thing = null;
						
				$scope.getThing = function() {
					$http.get("rest/devices/"+$stateParams.thingId)
						.success(function(data, status) {
						$scope.thing = data;
						$scope.isLoading = false;
						$scope.errorMessage = null;
						
						«FOR fbProperty : element.properties»
						$scope.set«fbProperty.name.toFirstUpper»();
						«ENDFOR»
																							
						$scope.initSockets();
						
						
					})
					.error(function(data, status, headers, config, statusText) {
						$scope.isLoading = false;
						$scope.errorMessage = "Could not load details!";
					});
				};
				
				«FOR fbProperty : element.properties»
					«var template = UIComponentFactory.getByModelId(fbProperty.type,context)»
					«template.renderJavascript(fbProperty,context)»
				«ENDFOR»
		       	
				$scope.reconnect = function() {
					setTimeout($scope.initSockets, 10000);
				};
				
				$scope.subscribeToThingChanges = function() {
					$scope.websocket.stompClient.subscribe("/topic/device/" + $scope.thing.thingId, function(status) {
						$scope.$apply(function() {
							$scope.thing = angular.fromJson(status.body);
							«FOR fbProperty : element.properties»
							$scope.set«fbProperty.name.toFirstUpper»();
							«ENDFOR»
						});
					});
				
					// start subscribing to this thing ID
					$scope.websocket.stompClient.send("/«element.name.toLowerCase»/«element.name.toLowerCase»endpoint/subscribe", {}, $scope.thing.thingId);
				};
				
				$scope.$on("$destroy", function() {
					if ($scope.websocket && $scope.websocket.stompClient) {
						$scope.websocket.stompClient.send("/«element.name.toLowerCase»/«element.name.toLowerCase»endpoint/unsubscribe", {}, $scope.thing.thingId);
					}
			    });
				
				$scope.initSockets = function() {
					$scope.websocket.socket = new SockJS('«element.name.toLowerCase»endpoint');
					$scope.websocket.stompClient = Stomp.over($scope.websocket.socket);
					$scope.websocket.stompClient.connect({}, function() {
						console.log("Connected to websocket. Now subscribing.");
						$scope.subscribeToThingChanges();
					});
					$scope.websocket.stompClient.onclose = $scope.reconnect;
				};
						
				$scope.getThing();	 		
			}]);
		'''
	}
	
}