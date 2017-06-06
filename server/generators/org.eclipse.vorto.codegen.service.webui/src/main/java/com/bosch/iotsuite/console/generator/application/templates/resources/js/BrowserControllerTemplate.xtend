package com.bosch.iotsuite.console.generator.application.templates.resources.js

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class BrowserControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''browser-controller.js'''
	}
	
	override getPath(InformationModel context) {
		'''«context.name.toLowerCase»-solution/src/main/resources/static/js'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		var listDevice = angular.module('«element.name.toLowerCase».list', []);
		
		listDevice.controller('BrowserController', ['$rootScope', '$scope', '$location', '$http', '$state',
			function($rootScope, $scope, $location, $http, $state) {
				
				$scope.isLoading = true;
				$scope.things = [];
				$scope.thingsMatrix = null;
				$scope.listView = false;
				$scope.currentPage = 1;
				$scope.thingsCount = null;
						
				$scope.getThings = function() {			
					$http.get("rest/devices")
						 .success(function(data, status) {
						 	if (Object.keys(data).length > 0) {
								$scope.things = data;
							}
							$scope.thingsMatrix = $scope.listToMatrix($scope.things, 5);
							$scope.isLoading = false;
						 })
						 .error(function(data, status, headers, config, statusText) {
							console.log("Error: " + JSON.stringify(data));
							$scope.isLoading = false;
						 });
				}	
		
				$scope.getThingsCount = function() {			
					$http.get("rest/devices/count")	
						.success(function(data, status) {
						 	$scope.thingsCount = data;
						 });
				};
										
				$scope.listToMatrix = function(list, n) {
				    var grid = [], i = 0, x = list.length, col, row = -1;
				    for (var i = 0; i < x; i++) {
				        col = i % n;
				        if (col === 0) {
				            grid[++row] = [];
				        }
				        grid[row][col] = list[i];
				    }
			    	return grid;
				};
				
				$scope.normalizeThingId = function(thingId) {
					var newId =  thingId.replace(":","_");
					return newId.split(".").join("_");
				};
								
				$scope.toggleBox = function(thingId) {
					var boxes = document.getElementsByClassName("tiny-box");
					for (i = 0; i < boxes.length; i++) {
		    			boxes[i].style.border = "";
					}
					document.getElementById('tinyBox:' + thingId).style.border = "4px solid #c2e1f5";
				};
				
				$(document).ready(function(){
		    		$('[data-toggle="tooltip"]').tooltip();
				});
				
				$scope.toggleView = function() {
					if($scope.listView) {
						$scope.listView = false;
					} else {
						$scope.listView = true;
					}
				}
				
				$scope.getThings();
				$scope.getThingsCount();
		
			}]);
		'''
	}
	
}