package com.bosch.iotsuite.console.generator.application.templates.resources.js

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ApiControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''api-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var apiController = angular.module('api.controller', []);
		
		apiController.controller('SwaggerController', ['$rootScope', '$scope', '$location', '$http', '$sce',
			function($rootScope, $scope, $location, $http, $sce) {
				
				$scope.isLoading = true;
				$scope.url = $scope.swaggerUrl = 'v2/api-docs';
				$scope.defaultErrorHandler = function(data, status) {
					alert('Error Loading Swagger API!');
				};
				
			}]);
		'''
	}
	
}