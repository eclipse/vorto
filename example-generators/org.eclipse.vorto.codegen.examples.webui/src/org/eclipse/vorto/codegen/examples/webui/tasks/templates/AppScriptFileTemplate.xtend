/*******************************************************************************
 *  Copyright (c) 2015 Bosch Software Innovations GmbH and others.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Eclipse Distribution License v1.0 which accompany this distribution.
 *   
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *  The Eclipse Distribution License is available at
 *  http://www.eclipse.org/org/documents/edl-v10.php.
 *   
 *  Contributors:
 *  Bosch Software Innovations GmbH - Please refer to git log
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.webui.tasks.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.examples.webui.tasks.ModuleUtil
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
class AppScriptFileTemplate implements IFileTemplate<InformationModel> {

	override getFileName(InformationModel context) {
		return "app.js";
	}

	override getPath(InformationModel context) {
		return "webdevice.example/src/main/resources/static/script";
	}

	override getContent(InformationModel infoModel,InvocationContext invocationContext) {
		return 
		'''
		var deviceApp = angular.module('deviceApp', ['ngRoute','deviceAppControllers']);
		
		deviceApp
		  .filter( 'titlecase', function() {
						return function( input ) {
		          var result = input.replace( /([A-Z])/g, " $1" );
		          var finalResult = result.charAt(0).toUpperCase() + result.slice(1);
		          return finalResult;
						}
			});
			
		deviceApp.run(['$http', '$rootScope', '$location', function($http, $rootScope,$location) {
		
				$rootScope.infomodel = [];
		     
		     	$rootScope.loadDeviceInfo = function() {
		     		$http.get('rest/deviceInfo').success(function(data){
		       			$rootScope.deviceInfo = data;
		     		});
		     	};
		     
		     	$rootScope.loadDeviceInfo();
				
				$rootScope.isActiveTab = function(route) {
					return route === $location.path();
				};
				
				«var fbWithStatus = ModuleUtil.getFunctionBlocksUsingStatus(infoModel)»
				«IF fbWithStatus.isEmpty == false»
				$rootScope.socket = {
					client: null,
					stomp: null
				};
				
				$rootScope.reconnect = function() {
					setTimeout($rootScope.initSockets, 10000);
				};
				
				$rootScope.initSockets = function() {
					$rootScope.socket.client = new SockJS('fb');
					$rootScope.socket.stomp = Stomp.over($rootScope.socket.client);
					$rootScope.socket.stomp.connect({}, function() {
						«FOR fbm : fbWithStatus»
						$rootScope.socket.stomp.subscribe("/topic/«fbm.name»Status", $rootScope.on«fbm.name»StatusUpdate);
						«ENDFOR»
					});
					$rootScope.socket.client.onclose = $rootScope.reconnect;
				};
				«FOR fbm : fbWithStatus»
				$rootScope.on«fbm.name»StatusUpdate = function(status) {
					$rootScope.$apply(function() {
						$rootScope.«fbm.name»Status = angular.fromJson(status.body);
					});
				};
				«ENDFOR»
				
				$rootScope.initSockets();
				«ENDIF»
				
		}]);
		
		deviceApp.config(function($routeProvider) {
		  $routeProvider
		  	«FOR fbProperty : infoModel.properties»
		  	.when('/«fbProperty.type.name.toLowerCase»', {
		  		templateUrl : 'pages/«fbProperty.type.name.toLowerCase».html',
		  		controller  : '«fbProperty.type.name»Controller'
		  	})
		  	«ENDFOR»
		  	.otherwise({
		  		redirectTo: '/«infoModel.properties.get(0).type.name.toLowerCase»'
		  	});
		});
		'''
	}
}
