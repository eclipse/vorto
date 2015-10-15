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
package org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.templates

import org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates.AppScriptFileTemplate

import static org.junit.Assert.assertEquals
import org.eclipse.vorto.codegen.examples.tests.webdevicegenerator.TestInforModelFactory
import org.junit.Test

/**
 * Tester to verify the generated app script file contents.
 *
 */
class AppScriptTemplateTest {
	
	@Test
	def void testAppScriptContent() {
		var model = TestInforModelFactory.createInformationModel();

		var result = new AppScriptFileTemplate().getContent(model);
		assertEquals(fetchExpected, result);
	}
	
	private def String fetchExpected() {
		return '''var deviceApp = angular.module('deviceApp', ['ngRoute']);

deviceApp
  .filter( 'titlecase', function() {
				return function( input ) {
          var result = input.replace( /([A-Z])/g, " $1" );
          var finalResult = result.charAt(0).toUpperCase() + result.slice(1);
          return finalResult;
				}
	});

deviceApp.run(['$http', '$rootScope', '$location', function($http, $rootScope,$location) {
		$rootScope.isActiveTab = function(route) {
			return route === $location.path();
		};
         $rootScope.filterConfiguration = function(data) {
           var result = {};
           angular.forEach(data, function(value, key) {
               if (key === 'configData') {
                   result[key] = value;
               }
           });
           return result;
         };
         $rootScope.isObject = function(object) {
         	return (object !=null && object.constructor === {}.constructor) ? true: false;
         };
         
         $rootScope.isBasicFieldOrEnum = function(object, key) {
	         return (key.indexOf("enum_") == 0  || !angular.isObject(object)) ? true : false;
         };
         $rootScope.invokeOperation = function(fbName, operationName) {
         	var response = $http.put('service/'+fbName +'/' + operationName +'/');
         	response.success(function(data, status, headers, config) {
         		$rootScope.responseMessage = fbName + ": " + operationName + " invoked.";
         	});
         	
         	response.error(function(data, status, headers, config) {
         		$rootScope.responseMessage("AJAX failed to get data, status=" + status);
         	});
         }
}]);

deviceApp.config(function($routeProvider) {
  $routeProvider
    .when('/', {
        templateUrl : 'pages/modelinfo.html',
        controller  : 'mainController'
    })
	.when('/fbm1', {
	    templateUrl : 'pages/modelinfo.html',
	    controller  : 'fbm1Controller'
	})
	.when('/fbm2', {
	    templateUrl : 'pages/modelinfo.html',
	    controller  : 'fbm2Controller'
	})
});
 deviceApp.controller('mainController',['$scope', '$http', '$rootScope', function($scope,$http, $rootScope) {
     $http.get('service/informationmodel/instance')
     .success(function(data){
       $scope.infomodelData = data;
     });

     $http.get('service/fbm1/instance')
     .success(function(data){
       $scope.modelinfo = data;
     });
     $scope.functionBlockName = 'fbm1';
     $rootScope.responseMessage = null;
 }]);
deviceApp.controller('fbm2Controller', ['$scope', '$http', '$rootScope', function($scope,$http, $rootScope) {
   $http.get('service/fbm2/instance')
   .success(function(data){
     $scope.modelinfo = data;
   });
   $scope.functionBlockName = 'fbm2';
   $rootScope.responseMessage = null;
 }]);
'''
		
	}
}