package com.bosch.iotsuite.console.generator.application.templates.resources.js

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class LoginControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''login-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var loginController = angular.module('login.controller', []);
		
		loginController.controller('LoginController', ['$rootScope', '$scope', '$location', '$http', '$sce',
			function($rootScope, $scope, $location, $http, $sce) {
				
				$scope.isLoading = false;
				$scope.error = false;
				
				
				$scope.authenticate = function() {
					$http.get('rest/identities/user').success(function(data) {	
						if (data.userId) {
							$rootScope.currentUser = data.userId;
							$rootScope.authenticated = true;
							$location.path("/");
						} else {
							$rootScope.authenticated = false;
							$scope.error = true;
						}
					}).error(function() {
						$rootScope.authenticated = false;
						$scope.error = true;
					});
				}
				
				$scope.login = function() {
				
					$scope.isLoading = true;
					
					$scope.credentials.username = "BCX\\" + $scope.credentials.userId;
			
					$http.post('login', $.param(JSON.parse(JSON.stringify($scope.credentials))), {
						headers : {
							"content-type" : "application/x-www-form-urlencoded"
						}
					}).success(function(data) {
						$scope.authenticate();
					}).error(function(data) {
						$scope.error = true;
						$rootScope.authenticated = false
						$scope.isLoading = false;
					});
				};
				
			}]);
		'''
	}
	
}