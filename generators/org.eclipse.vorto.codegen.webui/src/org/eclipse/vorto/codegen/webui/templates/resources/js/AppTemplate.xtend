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
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class AppTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''app.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var iotConsoleMain = angular.module('«element.name.toLowerCase»App', [ 'ui.router','ui.bootstrap', «IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»'swaggerUi',«ENDIF»'leaflet-directive','nvd3', 'frapontillo.gage', 'login.controller',«IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»'api.controller',«ENDIF»'«element.name.toLowerCase».list', '«element.name.toLowerCase».locate', '«element.name.toLowerCase».details']);
		
		iotConsoleMain.config([ '$stateProvider', '$urlRouterProvider', '$httpProvider',
			function($stateProvider, $urlRouterProvider, $httpProvider) {
		
			$urlRouterProvider.otherwise("/");
		
			$stateProvider
		    	.state('browse', {
		    		url: "/",
		    		templateUrl: 'partials/browser.html',
		    		controller: 'BrowserController',
		    		data: {
		        		requireLogin: true
		      		}
		    	})
		    	.state('details', {
		    		url: "/details/:thingId",
		    		templateUrl: 'partials/details.html',
		    		controller: 'DetailsController',
		    		data: {
		        		requireLogin: true
		      		}
		    	})
		    	.state('locate', {
		    		url: "/locate",
		    		templateUrl: 'partials/locator.html',
		    		controller: 'LocationController',
		    		data: {
		        		requireLogin: true
		      		}
		    	})
		    	«IF context.configurationProperties.getOrDefault("swagger","true").equalsIgnoreCase("true")»
		    	.state('api', {
		    	 	url: "/api",
		    	 	templateUrl: 'partials/api.html',
		    	 	controller: 'SwaggerController',
		    	 	data: {
		    	   		requireLogin: true
		    	 	}
		    	 })
		    	«ENDIF»
		    	.state('login', {
		    		url: "/login",
		    		templateUrl: 'partials/login.html',
		    		controller: 'LoginController',
		    		data: {
		        		requireLogin: false
		      		}
		    	});
		
		} ]).run(function($rootScope, $http, $location) {
		
			$rootScope.currentUser = 'undefined';
			$rootScope.currentUserSub = 'undefined';
		
			$rootScope.getUser = function() {
				$http.get('rest/identities/user').success(function(data,status,headers,config) {
					$rootScope.currentUser = data.userId;
					$rootScope.currentUserSub = data.userSub;
					$rootScope.authenticated = true;
				}).error(function(data,status,headers,config) {
					$rootScope.authenticated = false;
					console.log("User not authenticated. Redirecting user to login.");
					$location.path("/login");
				});
			};
		
			$rootScope.getUser();
		
			$rootScope.logout = function() {
				$http.post('logout',{}).success(function() {
					$rootScope.authenticated = false;
					$location.path("/login");
				}).error(function(data) {
					$location.path("/login");
					$rootScope.authenticated = false;
				});
			};
		
			$rootScope.$on('$stateChangeStart', function (event, toState, toParams) {
		    	var requireLogin = toState.data.requireLogin;
		    	if (requireLogin && $rootScope.authenticated == false) {
		    		console.log("redirecting to login");
		      		event.preventDefault();
		      		$location.path("/login");
		   		}
		  	});
		});
		'''
	}
	
}