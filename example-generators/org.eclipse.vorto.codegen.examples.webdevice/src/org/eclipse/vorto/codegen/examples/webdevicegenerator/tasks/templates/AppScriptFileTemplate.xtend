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
package org.eclipse.vorto.codegen.examples.webdevicegenerator.tasks.templates

import org.eclipse.vorto.codegen.api.tasks.ITemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

/**
 * Template for generating application javascript file.
 *
 */
class AppScriptFileTemplate implements ITemplate<InformationModel> {

	override getContent(InformationModel infoModel) {
		return '''
var deviceApp = angular.module('deviceApp', ['ngRoute']);

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
	«FOR functionBlockProperty : infoModel.properties»
	.when('/«functionBlockProperty.name»', {
	    templateUrl : 'pages/modelinfo.html',
	    controller  : '«functionBlockProperty.name»Controller'
	})
		«ENDFOR»
});
 deviceApp.controller('mainController',['$scope', '$http', '$rootScope', function($scope,$http, $rootScope) {
     $http.get('service/informationmodel/instance')
     .success(function(data){
       $scope.infomodelData = data;
     });

     $http.get('service/«infoModel.properties.get(0).name»/instance')
     .success(function(data){
       $scope.modelinfo = data;
     });
     $scope.functionBlockName = '«infoModel.properties.get(0).name»';
     $rootScope.responseMessage = null;
 }]);
«getFunctionBlockControllers(infoModel)»
'''
	}
	
	def getFunctionBlockControllers(InformationModel model) {
		var StringBuffer controllerTextBuffer = new StringBuffer();
		for (var i=1; i < model.properties.length; i++) {
			controllerTextBuffer.append(getControllerFirstPart())
			.append(model.properties.get(i).name)
			.append("Controller")
			.append(getControllerSecondPart())
			.append(model.properties.get(i).name)
			.append(getControllerLastPart(model.properties.get(i).name));
		}
		return controllerTextBuffer.toString
	}
	
	def getControllerLastPart(String fbName) {
		return '''/instance')
   .success(function(data){
     $scope.modelinfo = data;
   });
   $scope.functionBlockName = '«fbName»';
   $rootScope.responseMessage = null;
 }]);
		'''
	}
	
	def getControllerSecondPart() {
		return '''
', ['$scope', '$http', '$rootScope', function($scope,$http, $rootScope) {
   $http.get('service/'''
	}
	
	def getControllerFirstPart() {
		return "deviceApp.controller('"
	}
	

}
